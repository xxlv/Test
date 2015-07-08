var nunjucks  = require('nunjucks');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var flash = require('connect-flash');
var expressSession = require('express-session');
var errorhandler = require('errorhandler');
var passport = require('./passport');
var routes = require('../../routes/index');
var admin = require('../../routes/admin');
var auth = require('../../routes/auth');
var users = require('../../routes/users');
module.exports = function(app, express) {
	nunjucks.configure('views', { autoescape: true, express: app });
	app.set('views', path.join(__dirname, '../../views'));
	app.set('view engine', 'html');
	app.use(logger('dev'));
	app.use(bodyParser.json());
	app.use(bodyParser.urlencoded({
		extended: true,
		uploadDir: '../../public/uploads'
	}));
	app.use(cookieParser());
	app.use(express.static(path.join(__dirname, '../../public')));
	app.use(expressSession({ secret: 'naveen', proxy: true, resave: true, saveUninitialized: true, cookie: { maxAge: 60000 }}));
	app.use(flash());
	app.use(passport.initialize());
	app.use(passport.session());
	app.use(function (req, res, next) {
		res.locals.message = req.flash('message');
		res.locals.error = req.flash('error');
		res.locals.errors = req.flash('errors');
		res.locals.formData = req.flash('formData');
		res.locals.user = req.user;
		next();
	});
	// routes
	app.use('/', routes).use('/auth', auth).use('/admin', admin).use('/users', users).use(function(req, res, next) {
		var err = new Error('Not Found');
		err.status = 404;
		next(err);
	});// error handlers

	app.use(errorhandler({log: errorNotification}));
	function errorNotification(err, str, req) {
		var title = 'Error in ' + req.method + ' ' + req.url;
		console.log(title+"\n"+str);
	}
};
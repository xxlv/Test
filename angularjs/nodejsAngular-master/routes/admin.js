var express = require('express');
var authorization = require('../app/config/authorization');
var passport = require('../app/config/passport');
var admin = require('../app/controllers/admin');
var router = express.Router();

// GET admin page
router.route('/login').get(authorization.requiresAdminNotLogin, admin.getLogin)
	.post(passport.authenticate('admin', { failureRedirect: '/admin/login', failureFlash: 'Invalid username or password.',  }), admin.postLogin);
router.get('/', authorization.requiresAdminLogin, admin.getAdmin);
router.route('/changepwd').get(authorization.requiresAdminLogin, admin.getChangepwd)
	.post(authorization.requiresAdminLogin, admin.postChangepwd);
router.route('/logout').get(admin.getSignout);
module.exports = router;
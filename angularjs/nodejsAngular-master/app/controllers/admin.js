var underscore = require('underscore');
var nodemailer = require('nodemailer');
var validator = require('validator');
var hbs = require('nodemailer-express-handlebars');
var flash = require('connect-flash');
var moment = require('moment');
var crypto = require('crypto');
var config = require("../config/config");
var resetForm = require('../forms/resetPassword');
var db = require('../config/sequelize');

exports.getAdmin = function(req, res, next) {
	res.render('admin/index.html', { title: config.app.name });
};
exports.getLogin = function(req, res, next) {
	res.render('admin/login.html', { title: config.app.name+' - Login' });
};
exports.postLogin = function(req,res) {
	res.redirect("/admin");
};
exports.getChangepwd = function(req, res) {
	res.render('admin/resetPassword.html', { title: 'Reset Password', resetPasswordForm: resetForm.resetPassword });
};
exports.postChangepwd = function(req, res) {
     	var data = req.body;
     db.user.find({ where: { id: req.user.id } }).success(function(user) {
           if (user) {
                user.password = user.encryptPassword(data.password);
                user.save().success(function() {
                     req.flash('message', 'Your Password Successfully Changed');
                     res.redirect('/admin');
                }).error(function(err) {
                     req.flash('message', 'User Not Found');
                     res.redirect('/admin');
                });
           } else {
		      res.redirect('/admin/login');
           }
	}).error(function(err) {
           res.send('Error: token not found');
	});
};
exports.getSignout = function(req, res) {
	req.logout();
	res.redirect('/admin');
};
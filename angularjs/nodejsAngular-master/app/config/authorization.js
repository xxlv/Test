/*Generic require login routing middleware */
var jwt = require('jwt-simple');
var moment = require('moment');
var config = require("./config");

exports.requiresLogin = function(req, res, next) {
      if (!req.isAuthenticated()) {
           res.redirect("/login");
      }else if(req.isAuthenticated() && req.user.role == 'admin') {
           res.redirect("/login");
      }
      next();
};
exports.requiresNotLogin = function(req, res, next) {
      if (req.isAuthenticated() && req.user.role == 'admin') {
           res.redirect("/");
      }
      next();
};
exports.requiresAdminLogin = function(req, res, next) {
      if (!req.isAuthenticated()) {
           res.redirect("/admin/login");
      }else if(req.isAuthenticated() && req.user.role != 'admin') {
           res.redirect("/admin/login");
      }
      next();
};
exports.requiresAdminNotLogin = function(req, res, next) {
      if (req.isAuthenticated() && req.user.role == 'admin') {
           res.redirect("/admin");
      }
      next();
};
/* User authorizations routing middleware */
exports.user = {
      hasAuthorization: function(req, res, next) {
           if (req.profile.id != req.user.id) {
                res.redirect("/");
           }
           next();
      }
};
exports.authorize = {
      isAuthorized: function (req, res, next) {
           var token = req.headers.auth_token;
           if(typeof token != 'undefined') {
                var decode = jwt.decode(token, config.secret_token);
                if(decode.exp <= moment().valueOf()) {
                     res.redirect("/");
                }
           }else{
                res.redirect("/");
           }
           next();
      }
};
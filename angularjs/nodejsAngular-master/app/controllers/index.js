var validator = require('validator');
var hbs = require('nodemailer-express-handlebars');
var flash = require('connect-flash');
var moment = require('moment');
var crypto = require('crypto');
var path = require('path');
var gm = require('gm').subClass({ imageMagick: true });
var fs = require('fs');
var config = require("../config/config");
var db = require('../config/sequelize');
var resetForm = require('../forms/resetPassword');
var form = require('../forms/signup');

exports.getHome = function(req, res, next) {
      res.render('index.html', { title: config.app.name });
};
exports.getLogin = function(req, res, next) {
      res.render('login.html', { title: config.app.name+' - Login' });
};
exports.postLogin = function(req,res) {
      res.redirect("/");
};
exports.getSignup = function(req, res, next) {
      var name = req.query.type;
      res.render('signup.html', { title: config.app.name+' - Signup', type : name, signupForm: form.signup_form });
};
exports.postSignup = function(req,res) {
      var data = req.body;
      form.signup_form.handle(req, {
           success: function (form) {
                if (form.isValid()){
                     db.user.find({where: {email: data.email}}).success(function(user) {
                          if(!user) {
                                var user = db.user.build(data);
                                user.provider = 'local';
                                user.password = user.encryptPassword(data.password);
                                user.save().success(function() {
                                     res.redirect('/login');
                                     // req.login(user, function(err) { if(err) { return next(err); }else{ res.redirect('/login'); } });
                                });
                          }else{
                                res.redirect('/signup');
                          }
                     });
                }
           },
           error: function (form) {
                var errors = {};
                for(var key in form.fields){
                     field = form.fields[key];
                     if(field.required && !field.data) {
                           errors[key] = key+" is required";
                     }else if(field.required && key == "email" && !validator.isEmail(field.data)){
                           errors[key] = "Please enter a valid email address";
                     }else if(field.required && key == "confirmpassword" && !validator.equals(data.password, data.confirmpassword)) {
                           errors[key] = "Password and Confirm password does not match";
                     }
                }
                req.flash("formData", data);
                req.flash('errors', errors);
                res.redirect('back');
           },
           empty: function (form) {
                console.log("empty\n");
           }
      });
      // validator.isEmail(data.email);validator.isAlpha(data.firstname);validator.isAlpha(data.lastname);validator.isAlpha(data.lastname);validator.isLength(data.password);validator.isLength(data.confirm);validator.equals(data.password, data.confirm);
};
exports.getSignout = function(req, res) {
      req.logout();
      res.redirect('/');
};
exports.forgotPassword = function(req, res) {
      res.render('forgotPassword.html', { title: 'Forgot Password', message: req.flash('message') });
};
exports.forgotPasswordEmail = function(req, res) {
      var transporter = config.mail.nodemail;
      transporter.use('compile', hbs(config.mail.options));
      var email = req.body.email;
      db.user.find({ where: { email: email, role: 'user' } }).success(function(user) {
           if (!user) {
                req.flash('error', 'It is not a registered email address');
                res.redirect('back');
           } else {
                token = crypto.randomBytes(20).toString('hex');
                resetPasswordLink = config.app.baseUrl + "resetpasswd/" + token;
                date = moment().add(1, 'day').format("YYYY-MM-DD HH:mm:ss");
                user.forgotPasswordToken = token;
                user.forgotPasswordReqTime = date;
                user.save().success(function(a) {
                     var mailOptions = {
                           from: 'Naveen Kumar <nrnaveen0492@gmail.com>', to: user.email, subject: 'Reset Password Link',
                           template: 'forgotpass', context: { url: config.app.baseUrl, resetLink: resetPasswordLink },
                     };
                     transporter.sendMail(mailOptions, function(error, info){
                           if(error){
                                console.log(error);
                           }else{
                                console.log('Message sent: ' + info.response);
                                res.redirect("/");
                           }
                     });
                });

           }
      }).error(function(err) {
           res.redirect("/");
      });
};
exports.resetPassword = function(req, res) {
      token = req.param("token");
      var data = req.body;
      db.user.find({ where: { forgotPasswordToken: token } }).success(function(user) {  
           if (user) {
                if(moment() <= moment(user.forgotPasswordReqTime)) {
                     res.render('resetPassword.html', { title: 'Reset Password', resetPasswordForm: resetForm.resetPassword });
                } else {
                     user.forgotPasswordToken = null;
                     user.save().success(function() {
                           req.flash('message', 'Token has been Expired');
                           res.redirect('/login');
                     }).error(function(err) {
                           res.redirect('/login');
                     });
                }
           } else {
                req.flash('message', 'Token Not Found');
                res.redirect('/login');
           }
    }).error(function(err) {
           res.send('Error: token not found');
    });
};
exports.updateUserPassword = function(req, res) {
      token = req.param("token");
      var data = req.body;
      db.user.find({ where: { forgotPasswordToken: token } }).success(function(user) {  
           if (user) {
                if(moment() <= moment(user.forgotPasswordReqTime)) {
                     user.password = user.encryptPassword(data.password);
                     user.forgotPasswordToken = null;
                     user.save().success(function() {
                           req.flash('message', 'Your Password Successfully Changed');
                           res.redirect('/login');
                     }).error(function(err) {
                           res.redirect('/login');
                     });
                } else {
                     user.forgotPasswordToken = null;
                     user.save().success(function() {
                           req.flash('message', 'Token has been Expired');
                           res.redirect('/login');
                     }).error(function(err) {
                           res.redirect('/login');
                     });
                }
           } else {
                req.flash('message', 'Token Not Found');
                res.redirect('/login');
           }
      }).error(function(err) {
           res.send('Error: token not found');
      });
};
exports.getChangepwd = function(req, res) {
      res.render('resetPassword.html', { title: 'Reset Password', resetPasswordForm: resetForm.resetPassword });
};
exports.postChangepwd = function(req, res) {
      var data = req.body;
      db.user.find({ where: { id: req.user.id } }).success(function(user) {
           if (user) {
                user.password = user.encryptPassword(data.password);
                user.save().success(function() {
                     req.flash('message', 'Your Password Successfully Changed');
                     res.redirect('/');
                }).error(function(err) {
                     req.flash('message', 'User Not Found');
                     res.redirect('/');
                });
           } else {
                res.redirect('/login');
           }
      }).error(function(err) {
           res.send('Error: token not found');
      });
};




exports.getRedirect = function(req, res) {
      res.redirect("/");
};
exports.getDownload = function(req, res, next) {
      file = "public/report.pdf";
      res.download(file, 'naveen.pdf', function(err){
           if(err) {
                console.log(err);
           }
      });
};
exports.getMail = function(req,res) {
      var transporter = config.mail.nodemail;
      transporter.use('compile', hbs(config.mail.options));
      var mailOptions = {
           from: 'Naveen Kumar <naveen@asareri.com>', to: 'nrnaveen0492@gmail.com', subject: 'Hello',
           template: 'test', context: { name: "Naveen Kumar", },
      };
      transporter.sendMail(mailOptions, function(error, info){
           if(error){
                console.log(error);
           }else{
                console.log('Message sent: ' + info.response);
           }
      });
      res.redirect('/');
};
exports.getFile = function(req, res, next) {
      res.render('file.html', { title: config.app.name+' - File Upload'});
};
exports.postFile = function(req, res, next) {
      var upload = path.join(__dirname, '../../public/uploads/');
      var image = req.files.image;
      originalExtension = path.extname(image.originalFilename);
      originalFilename = path.basename(image.originalFilename, originalExtension);
      var extension = ['.jpg','.jpeg','.png','.JPG'];
      if(extension.indexOf(originalExtension) >= 0) {
           gm(image.path).write( upload + ' original-' + image.originalFilename, function(err) {
                if (err) {
                     console.error(err);
                }
           });
           gm(image.path).resize(240, 240).write( upload + 'resize - ' + image.originalFilename, function(err) {
                if (err) {
                     console.error(err);
                }
           });
      }else{
           fs.rename(image.path, upload + image.originalFilename, function(err) {
                if (err)
                     throw err;
                console.error('renamed complete');
           });
      }
      res.redirect('/');
};
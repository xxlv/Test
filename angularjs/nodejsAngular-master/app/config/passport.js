var passport = require('passport');
var lodash = require('lodash');
var LocalStrategy = require('passport-local').Strategy;
var TwitterStrategy = require('passport-twitter').Strategy;
var FacebookStrategy = require('passport-facebook').Strategy;
var GoogleStrategy = require('passport-google-oauth2').Strategy;
var LinkedinStrategy = require('passport-linkedin').Strategy;
var GitHubStrategy = require('passport-github2').Strategy;
var config = require('./config');
var db = require('./sequelize');

//Serialize sessions
passport.serializeUser(function(user, done) {
      done(null, user.id);
});
passport.deserializeUser(function(id, done) {
      db.user.find({ where: { id: id } }).success(function(user) {
           done(null, user);
      }).error(function(err) {
           done(err, null);
      });
});
//Use local strategy
passport.use(new LocalStrategy({ usernameField: 'email', passwordField: 'password' }, function(email, password, done) {
      db.user.find({ where: { email: email, role: 'user' } }).success(function(user) {
           if (!user) {
                done(null, false, {message: 'Unknown user'});
           }else if (!user.authenticate(password)) {
                done(null, false, {message: 'Invalid password'});
           }else{
                console.log('Login (local) : { id: ' + user.id + ', username: ' + user.firstName + ' }');
                done(null, user);
           }
      }).error(function(err) {
           console.log(err);
           done(err);
      });
}));
passport.use('admin', new LocalStrategy({ usernameField: 'email', passwordField: 'password' }, function(email, password, done) {
      db.user.find({ where: { email: email, role: 'admin' } }).success(function(user) {
           if (!user) {
                done(null, false, {message: 'Unknown user'});
           } else if (!user.authenticate(password)) {
                done(null, false, {message: 'Invalid Password'});
           } else {
                done(null, user);
           }
      }).error(function(err) {
           console.log(err);
           done(err);
      });
}));

// google strategy
passport.use(new GoogleStrategy({
      clientID: config.google.clientID,
      clientSecret: config.google.clientSecret,
      callbackURL: config.google.callbackURL
}, function(token, secretToken, profile, done) {
      db.user.find({ where: { email: profile.email } }).success(function(user) {
           if (!user) {
                var create = db.user.build({});
                create.firstname = profile.name.givenName;
                create.lastname = profile.name.familyName;
                create.email = profile.email;
                create.password = create.encryptPassword(profile.id);
                create.save().success(function(data) {
                     done(null, data);
                });
           }else{
                data = (user.role == 'user') ? user : null;
                done(null, data);
           }
      }).error(function(err) {
           done(err, null);
      });
}));

// facebook strategy
passport.use(new FacebookStrategy({
      clientID: config.facebook.clientID,
      clientSecret: config.facebook.clientSecret,
      callbackURL: config.facebook.callbackURL
},function(accessToken, refreshToken, profile, done) {
      var email = (!profile.emails) ? profile.id + "@fb.com" : profile.emails[0].value;
      db.user.find({ where: { facebookId: profile.id } }).success(function(user) {
           if (!user) {
                db.user.find({ where: { email: email } }).success(function(user) {
                     if (user) {
                          data = (user.role == 'user') ? user : null;
                          done(null, data);
                     }else{
                          var create = db.user.build({});
                          create.firstname = profile.name.givenName;
                          create.lastname = profile.name.familyName;
                          create.email = email;
                          create.password = create.encryptPassword(profile.id);
                          create.facebookId = profile.id;
                          create.save().success(function(data) {
                              done(null, data);
                          });
                     }
                }).error(function(err) {
                      done(err, null);
                });
           }else{
                data = (user.role == 'user') ? user : null;
                done(null, data);
           }
      }).error(function(err) {
           done(err, null);
      });
}));

// Use twitter strategy
passport.use(new TwitterStrategy({
      consumerKey: config.twitter.clientID,
      consumerSecret: config.twitter.clientSecret,
      callbackURL: config.twitter.callbackURL
}, function(token, secretToken, profile, done) {
      var email = profile.username+"@twitter.com";
      var name = profile.username.replace(/[0-9]/g, '');
      db.user.find({ where: { facebookId: profile.id } }).success(function(user) {
           if (!user) {
                var create = db.user.build({});
                create.firstname = name;
                create.lastname = name;
                create.email = email;
                create.password = create.encryptPassword(profile.id);
                create.facebookId = profile.id;
                create.save().success(function(data) {
                     done(null, data);
                });
           }else{
                done(null, user);
           }
      }).error(function(err) {
           done(err, null);
      });
}));

// Use linkedin strategy
passport.use(new LinkedinStrategy({
      consumerKey: config.linkedin.clientID,
      consumerSecret: config.linkedin.clientSecret,
      callbackURL: config.linkedin.callbackURL,
      profileFields: ['id', 'first-name', 'last-name', 'email-address', 'headline']
}, function(token, secretToken, profile, done) {
      var email = (!profile.emails) ? profile.id + "@linkedin.com" : profile.emails[0].value;
      db.user.find({ where: { facebookId: profile.id } }).success(function(user) {
           if (!user) {
                db.user.find({ where: { email: email } }).success(function(user) {
                     if (user) {
                          data = (user.role == 'user') ? user : null;
                          done(null, data);
                     }else{
                          var create = db.user.build({});
                          create.firstname = profile.name.givenName;
                          create.lastname = profile.name.familyName;
                          create.email = email;
                          create.password = create.encryptPassword(profile.id);
                          create.facebookId = profile.id;
                          create.save().success(function(data) {
                              done(null, data);
                          });
                     }
                }).error(function(err) {
                      done(err, null);
                });
           }else{
                data = (user.role == 'user') ? user : null;
                done(null, data);
           }
      }).error(function(err) {
           done(err, null);
      });
}));

// Use github strategy
passport.use(new GitHubStrategy({
      clientID: config.github.clientID,
      clientSecret: config.github.clientSecret,
      callbackURL: config.github.callbackURL,
}, function(accessToken, refreshToken, profile, done) {
      var email = (!profile.emails) ? profile.id + "@github.com" : profile.emails[0].value;
      db.user.find({ where: { facebookId: profile.id } }).success(function(user) {
           if (!user) {
                db.user.find({ where: { email: email } }).success(function(user) {
                     if (user) {
                          data = (user.role == 'user') ? user : null;
                          done(null, data);
                     }else{
                          var create = db.user.build({});
                          create.firstname = profile.username;
                          create.lastname = profile.username;
                          create.email = email;
                          create.password = create.encryptPassword(profile.id);
                          create.facebookId = profile.id;
                          create.save().success(function(data) {
                              done(null, data);
                          });
                     }
                }).error(function(err) {
                      done(err, null);
                });
           }else{
                data = (user.role == 'user') ? user : null;
                done(null, data);
           }
      }).error(function(err) {
           done(err, null);
      });
}));

module.exports = passport;
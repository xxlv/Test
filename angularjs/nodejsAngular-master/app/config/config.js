var nodemailer = require('nodemailer');
var path = require('path');
module.exports = {
      db: {
           name: "naveen",
           password: "root",
           username: "root"
      },
      app: {
           name: "Naveen",
           baseUrl: "http://localhost:8000/"
      },
      mail: {
           nodemail: nodemailer.createTransport({
                service: 'mandrill',
                auth: {
                      user: 'youremail@email.com',
                      pass: 'password'
                }
           }),
           options: {
                viewPath: 'views/emails/',
                extName: '.html'
           },
      },
      root: path.normalize(__dirname + '/../..'),
      google: {
           clientID: "google_client_id",
           clientSecret: "google_client_secret",
           callbackURL: "http://localhost:8000/google/callback"
      },
      facebook: {
           clientID: "facebook_client_id",
           clientSecret: "facebook_client_secret",
           callbackURL: "http://localhost:8000/facebook/callback"
      },
      twitter: {
           clientID: "twitter_consumer_key",
           clientSecret: "twitter_consumer_secret",
           callbackURL: "http://localhost:8000/twitter/callback"
      },
      linkedin: {
           clientID: 'linkedin_client_id',
           clientSecret: 'linkedin_client_secret',
           callbackURL: "http://localhost:8000/linkedin/callback"
      },
      github: {
           clientID: 'github_client_id',
           clientSecret: 'github_client_secret',
           callbackURL: "http://localhost:8000/github/callback"
      },
};
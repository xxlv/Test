var express = require('express');
var app = express();
require('./app/config/express')(app, express);
if (app.get('env') === 'development') {
      app.use(function(err, req, res, next) {
           res.status(err.status || 500);
           res.render('error', {
                message: err.message,
                error: err
           });
      });
}// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
      res.status(err.status || 500);
      res.render('error', {
           message: err.message,
           error: {}
      });
});
module.exports = app;
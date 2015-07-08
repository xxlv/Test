var fs = require('fs');
var path = require('path');
var Sequelize = require('sequelize-mysql').sequelize;
var lodash = require('lodash');
var config = require("./config");
var db = {};

var sequelize = new Sequelize(config.db.name, config.db.username, config.db.password);
fs.readdirSync("app/models").filter(function(file) {
      return (file.indexOf('.') !== 0) && (file !== 'index.js')
}).forEach(function(file) {
      console.log('Load ' + file + ' model file');
      var model = sequelize.import(path.join("../models", file));
      db[model.name] = model;
});
Object.keys(db).forEach(function(modelName) {
      if(db[modelName].options.hasOwnProperty('associate')) {
           db[modelName].options.associate(db);
      }
});
sequelize.sync({force: false}).complete(function(err){
      if(err) {
           console.log("An error occured %j",err);
      }else{
           console.log("Database dropped and synchronized");
      }
});
module.exports = lodash.extend({
      sequelize: sequelize,
      Sequelize: Sequelize
}, db);

var bCrypt = require('bcrypt-nodejs');
module.exports = function(sequelize, DataTypes) {
      var User = sequelize.define('user', {
           firstname: {
                type: DataTypes.STRING,
                allowNull: false,
                validate: {
                     isAlpha: true,
                     len: [3,25]
                },
           },
           lastname: {
                type: DataTypes.STRING,
                allowNull: false,
                validate: {
                     isAlpha: true,
                     len: [1,25]
                },
           },
           email: {
                type: DataTypes.STRING,
                unique: true,
                allowNull: true,
                validate: {
                     isEmail: true,
                     // isUnique: function(value, next) {
                     //       User.find({
                     //            where: {email: value},
                     //            attributes: ['id']
                     //       }).done(function(error, user) {
                     //            if (error)
                     //                 return next(error);
                     //            if (user)
                     //                 return next('Email address already in use!');
                     //       });
                     // }
                }
           },
           password: {
                type: DataTypes.STRING,
                allowNull: false,
           },
           age: {
                type: DataTypes.INTEGER,
                allowNull: true,
                validate: {
                     isNumber: true,
                },
           },
           gender: {
                type: DataTypes.ENUM,
                values: ['male', 'female'],
                defaultValue: 'male',
           },
           forgotPasswordToken: {
                type: DataTypes.STRING,
                allowNull: true
           },
           forgotPasswordReqTime: {
                type: DataTypes.DATE,
                allowNull: true,
                defaultValue: sequelize.NOW
           },
           active: {
                type:DataTypes.STRING,
                defaultValue: '1'
           },
           role: {
                type: DataTypes.STRING,
                defaultValue: 'user',
           },
           facebookId: {
                type: DataTypes.STRING,
                allowNull: null,
           }
      }, {
           instanceMethods: {
                authenticate: function(password){
                     return bCrypt.compareSync(password, this.password);
                },
                encryptPassword: function(password) {
                     if (!password) return '';
                     return bCrypt.hashSync(password, bCrypt.genSaltSync(10), null);
                }
           }
      });
      return User;
};
var forms = require('forms');
var fields = forms.fields;
var validators = forms.validators; 
var resetPassword_form = forms.create({
	password: fields.password({ required: validators.required('You definitely want a password') }),
	confirm:  fields.password({
		required: validators.required('don\'t you know your own password?'),
		validators: [validators.matchField('password')]
	}),
});
module.exports = { resetPassword: resetPassword_form };
var forms = require('forms');
var fields = forms.fields;
var validators = forms.validators;
var reg_form = forms.create({
	firstname: fields.string({ required: true }),
	lastname: fields.string({ required: true }),
	email: fields.string({ required: true }),
	password: fields.password({ required: validators.required('You definitely want a password') }),
	confirmpassword:  fields.password({
		required: validators.required('don\'t you know your own password?'),
		validators: [validators.matchField('password')]
	}),
});
module.exports = { signup_form: reg_form };
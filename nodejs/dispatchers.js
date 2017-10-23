
var unirest = require('unirest');
var validator = require('validator')

exports.isEmailRequestValid = function isEmailRequestValid(emailRequest) {
	//validate to existance and emails
	if (emailRequest.to === undefined || emailRequest.to.length == 0) {
		console.log("missing to field")
		return false;
	}
	for (email of emailRequest.to) {
		if (!validator.isEmail(email)) {
			console.log("invalid email in to field")
			return false;
		}
	}
	//validate bcc/cc emails
	if (emailRequest.cc != undefined) {
		for (email of emailRequest.cc) {
			if (!validator.isEmail(email)) {
				console.log("invalid email in cc field")
				return false;
			}
		}
	}
	
	if (emailRequest.bcc != undefined) {
		for (email of emailRequest.bcc) {
			if (!validator.isEmail(email)) {
				console.log("invalid email in bcc field")
				return false;
			}
		}
	}
	//validate from exist+email and text exist
	if ((emailRequest.from != undefined && validator.isEmail(emailRequest.from) &&
		emailRequest.text != undefined && emailRequest.text != "") == false) {
		console.log("from field or text field do not exist or invalid")
		return false;
	}
	return true;
}

//returns a promise
exports.dispatchSendGrid = function(emailRequest)
{
	return new Promise(function(resolve, reject) {
		//unfortunately my sendGrid account has been suspended, so this doesn't work

		    //build to, cc and bcc arrays
			var toArray = []
			console.log("request : ". Request)
			if (!exports.isEmailRequestValid(emailRequest)) {
				console.log("Request is invalid, see log")
				reject("Email Send Request Invalid (one or more required fields are missing)")
				return
			}
			if (emailRequest.to != undefined)
			{
				for (t of emailRequest.to) {
					toArray.push({
						email:t
					})
				}
			}
			if (!isEmailValid(emailRequest)) {
				reject("No to, from or text fields")
				return
			}
			Request.field("from", emailRequest.from)

			var ccArray = []
			if (emailRequest.cc != undefined)
			{
				for (t of emailRequest.cc) {
				ccArray.push({
						email:t
					})
				}
			}

			var bccArray = []
			if (emailRequest.bcc != undefined)
			{
				for (t of emailRequest.bcc) {
					bccArray.push({
						email:t
					})
				}
			}
			
		    var Request = unirest.post('https://api.sendgrid.com/v3/mail/send').headers({'Authorization':process.env.SENDGRID_API_KEY,
				'Content-Type':'application/json'}).body({
					personalizations: [
					{
						to: toArray
					},
					{
						cc : ccArray 
					},
					{
						bcc : bccArray
					}],
					from : emailRequest.from,
					subject : emailRequest.subject,
					text : emailRequest.text
				})
	})
}

//returns a promise
exports.dispatchMailgun =  function(emailRequest) {
	return new Promise(function(resolve, reject) {
		if (process.env.MAILGUN_API_KEY === undefined || process.env.MAILGUN_API_KEY == "") {
			reject("no API key!")
		}
		var Request = unirest.post('https://api.mailgun.net/v3/sandbox401dbe4a8e9444d38dc1b54b00c5949b.mailgun.org/messages').auth({
			user:"api",
			pass : process.env.MAILGUN_API_KEY

		}).headers({'Content-Type': 'multipart/form-data'})
		if (!exports.isEmailRequestValid(emailRequest)) {
			console.log("Request is invalid, see log")
			reject("Email Send Request Invalid (one or more required fields are missing)")
			return
		}
		console.log("email request validated")
		if (emailRequest.to != undefined)
		{
			for (t of emailRequest.to) {
				Request.field("to", t)
			}
		}
		console.log("to added")
		Request.field("from", emailRequest.from)
		console.log("from added")
		if (emailRequest.cc != undefined)
		{
			for (t of emailRequest.cc) {
			Request.field("cc", t)
			}
		}
		console.log("cc added")
		if (emailRequest.bcc != undefined)
		{
			for (t of emailRequest.bcc) {
				Request.field("bcc", t)
			}
		}
		console.log("bcc added")
		if (emailRequest.subject != undefined) {
			Request.field("subject", emailRequest.subject)
		}
		console.log("subject added")
		if (emailRequest.text != undefined) {
			Request.field("text", emailRequest.text)
		}
		console.log("text added")
		console.log("request", Request)
		Request.end((response) => {
			console.log("ResponseCode:" +  response.code )
			console.log("Response : ", response.body)
			resolve(response)
		})
	})
	
}

var express = require('express');
var bodyParser = require('body-parser')
var app = express();
var dispatcher = require('./dispatchers.js')

var table = "emails";

app.post('/sendMail', bodyParser.json(), function (req, res) {
//handle the request

	var emailRequest = {
		to : req.body.to,
		from : req.body.from,
		subject : req.body.subject,
		cc : req.body.cc,
		bcc : req.body.bcc,
		text : req.body.text
	}
	console.log("email request : ", emailRequest)

	var mailGunPromise = dispatcher.dispatchMailgun(emailRequest)

	mailGunPromise.then(function(response) {
		console.log("response code: ", response.code)

		if (response.code == 200) {
			res.statusCode = 200
			res.send(JSON.stringify({
				returnCode : res.statusCode,
				returnReason : "Request has been fulfilled"
			}))
		}
		else if (response.code == 400) {
			res.statusCode = 400
			res.send(JSON.stringify({
				returnCode : res.statusCode,
				returnReason : "Invalid arguments, probably missing to/from/text fields"
			}))
		} else {
			//something wrong with this provider, try the next one
			console.log("mailgun dispatch failed, ")
			var sendGridPromise = dispatcher.dispatchSendGrid(emailRequest)
			sendGridPromise.then(function(response) {
			console.log("response code: ", response.code)

			if (response.code == 200) {
				res.statusCode = 200
				res.send(JSON.stringify({
					returnCode : res.statusCode,
					returnReason : "Request has been fulfilled"
				}))
			}
			else if (response.code == 400) {
				res.statusCode = 400
				res.send(JSON.stringify({
					returnCode : res.statusCode,
					returnReason : "Invalid arguments, probably missing to/from/text fields"
				}))
			} else {
				//something wrong with this provider, try the next one
				console.log("sendgrid dispatch failed as well - this is where we reinsert the job into some job queue or SQS to be tried later")
				res.statusCode = 201
				res.send(JSON.stringify({
					returnCode : res.statusCode,
					returnReason : "Request received - queued for later sending"
				}))
				
			}

		}, function(err) {
			console.log("error!", err)
			res.statusCode = 400
				res.send(JSON.stringify({
					returnCode : res.statusCode,
					returnReason : "Invalid arguments, probably missing to/from/text fields"
				}))
		})
		}

	}, function(err) {
		console.log("error!", err)
		res.statusCode = 400
			res.send(JSON.stringify({
				returnCode : res.statusCode,
				returnReason : "Invalid arguments, probably missing to/from/text fields"
			}))
	})
   
})

exports.startServer = function (callbackDone) {
	var server = app.listen(8080, function () {
   var host = server.address().address
   var port = server.address().port
   console.log("SiteMinder Email server listening at http://%s:%s", host, port)
   callbackDone()
   
   
})
}




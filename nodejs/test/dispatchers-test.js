var assert = require('assertion')
var unirest = require('unirest');
var dispatcher = require('../dispatchers.js')
describe('Test mailgun dispatcher ', function() {


	it('Rejects promise when no input given', function (done) {
		dispatcher.dispatchMailgun({
			
		}).then(function(ok) {
			console.log("everything went fine")
			assert.fail()

		}, function(err) {
			console.log("got error on invalid input", err)

			done()
		})

	})

	it('Fulfill promise when input is valid', function (done) {
		dispatcher.dispatchMailgun({
			to : ["bla@bla.com"],
			from : "bla@bla.com",
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"
		}).then(function(ok) {
			console.log("everything went fine")
			done()

		}, function(err) {
			console.log("got error on invalid input", err)
		})

	})


})
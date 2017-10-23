var assert = require('assertion')
var unirest = require('unirest');
var server = require('../emailService.js')

//This test doesn't work because of a bug in "unirest" library - it will not accept a "body" function under Mocha for some reason
describe('Test whole service flow ', function() {

before(function(done) {
server.startServer(done)
})

	it('Gets 400 code when no parameters given', function (done) {
		var Request = unirest.post('localhost:8080/sendMail').header({'Content-Type':'application/json'})
		Request.end((response) => {
			console.log("ResponseCode:" +  response.code)
			assert.equal(response.code, 400)
			done()
		})
	})


})
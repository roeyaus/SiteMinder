var dispatcher = require('../dispatchers')
var assert = require('assertion')

describe('Test dispatchers.isEmailRequestValid function ', function() {


	it('fails when all fields are missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('succeeds when all fields are present and valid', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			to : ["bla@bla.com"],
			from : "bla@bla.com",
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, true)
		done()
	
	})
	it('fails when to field  is missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "bla@bla.com",
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when to field is empty', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			to : [],
			from : "bla@bla.com",
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when to field contains an invalid mail', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			to : ["roeyaus@gmail.com", "blabla"],
			from : "bla@bla.com",
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when from field does not exist', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when from field is an empty string', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when text field is missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('succeeds when cc field is missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : 'hi!'

		})
		assert.equal(isValid, true)
		done()
	
	})
	it('succeeds when bcc field is missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			subject : "hi",
			text : 'hi!'

		})
		assert.equal(isValid, true)
		done()
	
	})
	it('succeeds when subject field is missing', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			text : 'hi!'

		})
		assert.equal(isValid, true)
		done()
	
	})
	it('fails when text field is an empty string', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["bla@bla.com"],
			subject : "hi",
			text : ""

		})
		assert.equal(isValid, false)
		done()
	
	})
	it('fails when bcc field contains an invalid email', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["bla@bla.com"],
			bcc : ["blala.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})
		it('fails when cc field contains an invalid email', function(done) {
		var isValid = dispatcher.isEmailRequestValid({
			from : "me@m.com",
			to : ["roeyaus@gmail.com"],
			cc : ["blabla.com"],
			bcc : ["bla@la.com"],
			subject : "hi",
			text : "hi"

		})
		assert.equal(isValid, false)
		done()
	
	})


	
});


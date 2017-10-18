# SiteMinder exercise

This is my submission for SiteMinder's Email server exercise.
It is basically a simple web service built on Springboot MVC which receives "email requests" and forwards them to some 3rd party email service.
If the default 3rd party service returns an error, it tries the rest in order.

Keep is mind I am not and never have been a Java developer an I essentially learned both the language and the IDE/frameworks for this exercise.

My implementation is as follows :

Frameworks/Libraries :

I am using SpringBoot MVC as a webserver
Jersey HTTP Client for my HTTP


*Initialization:*
Application loads and creates the webservice and "Dispatchers" for each 3rd party provider.
It also creates a retry thread and queue. These are in case an email send fails on ALL services, it must be retried until it succeeds.
The retry thread is a reduced implementation of a threadpool - which in a real solution would wait on the queue for emails to re-send.

*Use:*
User calls WebserviceURL/sendEmail with POST and JSON body with the following structure :

{"to":["roeyaus@gmail.com"], "cc": ["roeyaus@gmail.com"], "bcc" : ["roeyaus@gmail.com"], "subject" : "1121", "from":"me@me.com", "text" : "blablabla"}

The service receives the HTTP request and parse the body to a Java class.

The service defers the request to a threadpool using Java mechanisms (NOT IMPLEMENTED)

The service saves the email immediately to DynamoDB  and gets a unique id for it (NOT IMPLEMENTED)

The service tries the first provider, and if receives a response other than 200 or 400 (invalid arguments), it tries the next and so on.

If ALL providers have failed, it saves the email to the retry queue to be retried by the thread.

(Had I implemented DynamoDB integration - the blocking queue would not have been needed.)

*Dataloss:*
Where can dataloss occur?

1) dataloss can occur if the webservice machine dies for any reason, his email is lost unless already delivered.
Obviously saving the email in an in-memory cache is not enough since as mentioned if the machine dies, it dies.
We must write it to a DB for safekeeping until delivery is confirmed

Thus the DB implementation would be as such : (Not Implemented)

When the service loads it checks the DB for unsent emails (probably saved from when the service died last time)

It then allocates several threads to sending these emails.

At the same time it starts receiving new requests - which it writes to DB immediately pending delivery.

When delivery is confirmed (200 status returned from Provider), they are deleted from DB

** For a quick/Dirty/expensive solution I would go with Amazon DynamoDB but it would take me way too long to implement.
** Other DBs that can be used are a MongoDB instance hosted in the cloud or on-premise

2) dataloss can occur if we tried all the services and they all failed.
   Thus the service continues to retry all saved emails in the retry queue if they have not yet been delivered.
   Once an email is sent successfully it is deleted from the retry queue.

3) Dataloss can occured if we delete the emails before we receive confirmation that the emails have gone through.
   Thus we wait for a "200" response from any service before pronouncing the email as "sent" and returning a response.

*Input validation:*

I should validate email addresses before sending to the service - but I didn't get around to it (NOT IMPLEMENTED)

I validate emails multiple times for From/To fields, and return an error to the user if they are missing, or if any other critical fields are missing (if one of the providers said so).

*Error Handling:*

The user gets the following responses:

200 mail was sent successfully through any provider

201 mail is successfully received but NOT YET SENT - deferred for later sending (If all providers are down for example)

400 invalid argument - missing to/from fields or any other error.

500 error - if an unknown error has occured (exception in our application)

*Testing:*

Testing covers input validation, 3rd party provider integration, response codes etc.



*Where it is / How to run:*

Get project from Git : https://github.com/roeyaus/SiteMinder

open in IntelliJ IDEA, import as Maven project, run.

*Where to test : *

I put it on an EC2 instance :

 curl http://ec2-54-213-223-54.us-west-2.compute.amazonaws.com:8080/sendEmail -X POST -d '<JSON GOES HERE>' --header 'Content-Type: application/json' --verbose


Have Fun!

If anything is unclear or needs to be added, don't hesitate to tell me, I'll do my best.



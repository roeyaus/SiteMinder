package MailService;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.sun.jersey.api.client.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmailController implements Runnable{

    List<ProviderDispatcher> _dispatcherList;
    LinkedBlockingQueue<EmailRequest> _emailQueue;
    Thread _retryThread;

    public EmailController() {
        _dispatcherList = new LinkedList<>();
        _dispatcherList.add(new MailGunDispatcher());
        _dispatcherList.add(new SendGridDispatcher());
        _emailQueue = new LinkedBlockingQueue<>();
        //init and start re
        // try thread
        _retryThread = new Thread(this);
        _retryThread.start();
    }


    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity<?> HandleSendMailRequest(@RequestBody EmailRequest emailRequest) {
        System.out.println("Send Mail request received! Handling...");
        if (!emailRequest.isEmailRequestValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailRequestResult(400, "Missing to/from parameters"));
        }
        boolean mailSent = false;
        int currentDispatcher = 0;
        while (!mailSent) {
            try {
                ClientResponse response = _dispatcherList.get(currentDispatcher).Dispatch(
                        emailRequest);

                if (response.getStatus() == 200) {
                    mailSent = true;
                } else if (response.getStatus() == 400) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailRequestResult(400, "Bad arguments/parameters"));
                } else {
                    currentDispatcher++;
                    if (currentDispatcher == _dispatcherList.size()) //no more dispatchers left
                    {
                        try {
                            _emailQueue.put(emailRequest);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ResponseEntity.status(HttpStatus.CREATED).body(new EmailRequestResult(201, "email request has been queued"));
                    }

                }
            }
            catch (IllegalArgumentException exception)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailRequestResult(400, "Bad arguments/parameters"));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(new EmailRequestResult(200, "email request has been fulfilled"));
    }

    //retry thread run function
    @Override
    public void run() {
        //this thread blocks until there are some emails waiting in the retry queue
        while(true) {

            EmailRequest request = null;
            try {
                System.out.println("Retry thread going to sleep until there's something to retry...");
                request = _emailQueue.take();
                System.out.println("Retry thread - got something to retry...");
                ResponseEntity<?> res = HandleSendMailRequest(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //do something with the response entity..... ?
        }
    }
}

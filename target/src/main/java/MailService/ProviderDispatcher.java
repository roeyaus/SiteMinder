package MailService;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.List;

public abstract class ProviderDispatcher {

    public abstract Boolean IsOnline();
    public abstract ClientResponse Dispatch(EmailRequest emailRequest) throws IllegalArgumentException;
}

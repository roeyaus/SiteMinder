package MailService;

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;
import java.util.List;

public class MailGunDispatcher extends ProviderDispatcher {
    @Override
    public Boolean IsOnline() {
        return true; //TODO : add actual check
    }

    @Override
    public ClientResponse Dispatch(EmailRequest emailRequest) throws IllegalArgumentException {
        if (!emailRequest.isEmailRequestValid()){
            throw new IllegalArgumentException("Email Request invalid - Missing To/From fields");
        }
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-d6bb3200b365a615532a261723374ec6"));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/sandbox401dbe4a8e9444d38dc1b54b00c5949b.mailgun.org/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", emailRequest.from);
        StringBuilder sb = new StringBuilder();
        for (String t: emailRequest.to)
        {
            sb.append(t);
            //sb.append(",");

        }

        formData.add("to", sb.toString());
        sb = new StringBuilder();
        for (String t: emailRequest.cc)
        {
            sb.append(t);
           // sb.append(",");
        }
        formData.add("cc", sb.toString());
        sb = new StringBuilder();
        for (String t: emailRequest.bcc)
        {
            sb.append(t);
           // sb.append(",");

        }
        formData.add("bcc", sb.toString());
        formData.add("subject", emailRequest.subject);
        formData.add("text", emailRequest.text);
        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
    }
}

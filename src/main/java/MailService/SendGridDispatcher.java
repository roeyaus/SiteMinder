package MailService;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;
import java.util.List;

public class SendGridDispatcher extends ProviderDispatcher {
    @Override
    public Boolean IsOnline() {
        return true; //TODO : implement actual online check
    }

    /*curl --request POST \
  --url https://api.sendgrid.com/v3/mail/send \
  --header "Authorization: Bearer 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" \
  --header 'Content-Type: application/json' \
  --data '{"personalizations": [{"to": [{"email": "test@example.com"}]}],"from": {"email": "test@example.com"},"subject": "Sending with SendGrid is Fun","content": [{"type": "text/plain", "value": "and easy to do anywhere, even with cURL"}]}'
    */
    @Override
    public ClientResponse Dispatch(EmailRequest emailRequest) throws IllegalArgumentException {
        if (!emailRequest.isEmailRequestValid()){
            throw new IllegalArgumentException("Email Request invalid - Missing To/From fields");
        }
        Client client = Client.create();
        WebResource webResource = client.resource("https://api.sendgrid.com/v3/mail/send");

        return webResource.type(MediaType.APPLICATION_JSON_TYPE).header("Authorization", "Bearer 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")
                .header("Content-Type", "application/json").post(ClientResponse.class,
                        "{'personalizations': [['to': [{'email': 'test@example.com'}]}], 'from': {'email': 'test@example.com'},'subject': 'Sending with SendGrid is Fun','content': [{'type': 'text/plain', 'value': " +
                                "and easy to do anywhere, even with cURL'}]}");
    }
}

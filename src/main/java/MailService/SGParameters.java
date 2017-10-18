package MailService;

public class SGParameters {

    public SGParameters(SGPersonalization personalization, SGFromParameter from, String subject, String content) {
        this.personalization = personalization;
        this.from = from;
        this.subject = subject;
        this.content = content;
    }

    public SGFromParameter getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public SGPersonalization getPersonalization() {
        return personalization;
    }

    SGPersonalization personalization;
    SGFromParameter from;
    String subject;
    String content;

}


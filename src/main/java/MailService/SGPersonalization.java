package MailService;

import java.util.List;

public class SGPersonalization {
    public SGPersonalization(List<SGEmailObject> to, List<SGEmailObject> cc, List<SGEmailObject> bcc) {
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
    }

    List<SGEmailObject> to;
    List<SGEmailObject> cc;
    List<SGEmailObject> bcc;

    public List<SGEmailObject> getTo() {
        return to;
    }

    public List<SGEmailObject> getCc() {
        return cc;
    }

    public List<SGEmailObject> getBcc() {
        return bcc;
    }
}

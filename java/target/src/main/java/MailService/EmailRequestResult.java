package MailService;

public class EmailRequestResult {
    public EmailRequestResult(int _requestReturnCode, String _returnCodeText) {
        this._requestReturnCode = _requestReturnCode;
        this._returnCodeText = _returnCodeText;
    }

    public int get_requestReturnCode() {
        return _requestReturnCode;
    }

    public String get_returnCodeText() {
        return _returnCodeText;
    }

    int    _requestReturnCode;
    String _returnCodeText;
}

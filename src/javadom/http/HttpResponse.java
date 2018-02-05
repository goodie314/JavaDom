package javadom.http;

/**
 * Created by u6062536 on 2/1/2018.
 */
public class HttpResponse {

    private Integer statusCode;
    private String statusMessage;
    private String contentType;
    private String responseBody;

    protected HttpResponse(Integer statusCode, String statusMessage, String contentType, String responseBody) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.contentType = contentType;
        this.responseBody = responseBody;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    @Override
    public String toString() {
        return "Status Code: " + this.statusCode + "\n" +
                "Status Message: " + this.statusMessage + "\n" +
                "Response Body: " + this.responseBody + "\n";
    }
}

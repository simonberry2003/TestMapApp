package stb.com.testmapapp.Mqtt;

import stb.com.testmapapp.util.Preconditions;

public class RequestResponse {
    private String request;
    private String responseTopic;

    public RequestResponse(String request, String responseTopic) {
        this.request = Preconditions.checkNotNull(request);
        this.responseTopic = Preconditions.checkNotNull(responseTopic);
    }

    public String getRequest() {
        return request;
    }

    public String getResponseTopic() {
        return responseTopic;
    }
}

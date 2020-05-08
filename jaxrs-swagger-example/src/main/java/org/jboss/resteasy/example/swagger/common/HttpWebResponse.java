package org.jboss.resteasy.example.swagger.common;

import com.fasterxml.jackson.annotation.JsonProperty;


public class HttpWebResponse<T> {

    @JsonProperty
    private boolean success;

    @JsonProperty
    private T result;

    @JsonProperty
    private String message;

    public HttpWebResponse() {

    }

    public HttpWebResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public HttpWebResponse(boolean success, T result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }


    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}

package com.devsuperior.dscatalog.camel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "route", ignoreUnknownFields = true)
public class ReturnURL {
    private String status;

    public ReturnURL(String status) {
        this.status = status;
    }

    public ReturnURL() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

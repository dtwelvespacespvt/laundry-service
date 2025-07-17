package com.stanzaliving.laundry.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class UpstreamFailureException extends RuntimeException {
    private final String serviceName;
    private final String msg;

    public UpstreamFailureException(String serviceName, String upstreamMessage) {
        super(upstreamMessage);
        this.serviceName = serviceName;
        this.msg = "Upstream service (" + this.serviceName + ") failure.";
    }
}

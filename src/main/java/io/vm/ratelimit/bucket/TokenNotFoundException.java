package io.vm.ratelimit.bucket;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String reqId) {
        super("Token not available for request id: " + reqId);
    }
}

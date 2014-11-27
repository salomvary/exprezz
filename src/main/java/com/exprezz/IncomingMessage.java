package com.exprezz;

/**
 * This has to be a Java class otherwise Scala will generate getters
 * preventing JavaScript from accessing properies like `message.method`.
 */
public class IncomingMessage {
    public final String method;
    public final String url;
    IncomingMessage(String method, String url) {
        this.method = method;
        this.url = url;
    }
}

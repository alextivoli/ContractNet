package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.interaction.Request;

public final class MessageFibonacciNumber implements Request {

    private static final long SerialVersionUID = 1L;

    private final int message;
    public MessageFibonacciNumber(final int message){
        this.message = message;
    }
    public int getMessageFibonacciNumber(){
        return this.message;
    }
}

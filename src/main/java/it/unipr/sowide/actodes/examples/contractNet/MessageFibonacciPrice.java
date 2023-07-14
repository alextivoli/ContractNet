package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.interaction.Request;

public final class MessageFibonacciPrice implements Request {

    private static final long SerialVersionUID = 1L;

    private final int message;
    public MessageFibonacciPrice(final int message){
        this.message = message;
    }
    public int getMessageFibonacciPrice(){
        return this.message;
    }
}

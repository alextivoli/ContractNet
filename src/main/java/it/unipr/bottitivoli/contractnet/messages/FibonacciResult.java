package it.unipr.bottitivoli.contractnet.messages;

import java.math.BigInteger;

import it.unipr.sowide.actodes.interaction.Request;

public final class FibonacciResult implements Request {

    private static final long SerialVersionUID = 1L;

    private final BigInteger message;
    public FibonacciResult(final BigInteger message){
        this.message = message;
    }
    public BigInteger getMessageFibonacciNumber(){
        return this.message;
    }
}

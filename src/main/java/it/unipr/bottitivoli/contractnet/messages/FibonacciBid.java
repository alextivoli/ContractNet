package it.unipr.bottitivoli.contractnet.messages;

import it.unipr.sowide.actodes.interaction.Request;

public final class FibonacciBid implements Request {

    private static final long SerialVersionUID = 1L;

    private final int message;
    public FibonacciBid(final int message){
        this.message = message;
    }
    public int getFibonacciBid(){
        return this.message;
    }
}

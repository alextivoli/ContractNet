package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.interaction.Request;

public final class MessageAcknowledgement implements Request {

    private static final long SerialVersionUID = 1L;

    private final boolean message;
    public MessageAcknowledgement(final boolean message){
        this.message = message;
    }
    public boolean getMessageAcknowledgement(){
        return this.message;
    }
}

package it.unipr.sowide.actodes.examples.contractNet.messages;

import it.unipr.sowide.actodes.interaction.Request;

public final class AcknowledgmentMessage implements Request {

    private static final long SerialVersionUID = 1L;

    private final boolean message;
    public AcknowledgmentMessage(final boolean message){
        this.message = message;
    }
    public boolean getMessageAcknowledgement(){
        return this.message;
    }
}

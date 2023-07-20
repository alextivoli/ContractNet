package it.unipr.bottitivoli.contractnet.messages;

import it.unipr.sowide.actodes.interaction.Request;

public final class TaskAnnouncement implements Request {

    private static final long SerialVersionUID = 1L;

    private final int message;
    public TaskAnnouncement(final int message){
        this.message = message;
    }
    public int getMessageFibonacciNumber(){
        return this.message;
    }
}

package it.unipr.bottitivoli.contractnet.messages;

import java.util.ArrayList;

import it.unipr.sowide.actodes.interaction.Request;

public final class ReportMessage implements Request {

    private static final long SerialVersionUID = 1L;

    private final ArrayList<Integer> bids;

    public ReportMessage(final ArrayList<Integer> bids){
        this.bids = bids;
    }
    public ArrayList<Integer> getAllBids(){
        return this.bids;
    }
}

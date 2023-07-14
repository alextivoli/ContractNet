package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;

import java.util.HashMap;

/**
 *
 * The {@code Worker} class defines a behavior that waits for messages
 * from a {@code Master} actor until it receives a {@code KILL} message.
 *
 * When it happens it kills itself.
 *
 * @see Master
 *
**/

public final class Worker extends Behavior
{

  private static final MessagePattern FIBONACCINUMBERPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));

  private static final MessagePattern FIBONACCIPROPOSALACCEPTED = MessagePattern.contentPattern(new IsInstance(MessageAcknowledgement.class));

//  private static final MessagePattern FIBONACCIPROPOSAL = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));
//
//  private static final MessagePattern FIBONACCINUMBERPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));
  private static final long serialVersionUID = 1L;

  private final boolean isStorageEnable;

  private HashMap<Integer, Integer> fibonacciStorage = new HashMap<Integer, Integer>();

  private int greatherFibonacciStored = 0;

  public Worker(final boolean isStorageEnable){
    this.isStorageEnable = isStorageEnable;
  }

  private int getFibonacciPrice(int fibonacciNumber){
    if(!isStorageEnable){
      return fibonacciNumber;
    }

    if(fibonacciNumber <= this.greatherFibonacciStored){
      return 0;
    }else{
      return fibonacciNumber - fibonacciStorage.get(this.greatherFibonacciStored);
    }
  }



  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {

    MessageHandler h = (m) -> {
      System.out.print("W :: SONO IL WORKER \n");

      return null;
    };

    MessageHandler fibonacciNumberReceived = (m) -> {
      MessageFibonacciNumber n = (MessageFibonacciNumber) m.getContent();
      System.out.print("W :: RICEVUTO MESSAGGIO : " + n.getMessageFibonacciNumber() +" \n");

      int price = this.getFibonacciPrice(n.getMessageFibonacciNumber());

      MessageFibonacciPrice messageFibonacciPrice = new MessageFibonacciPrice(price);
      send(m.getSender(), messageFibonacciPrice);
      return null;
    };

    c.define(FIBONACCINUMBERPATTERN, fibonacciNumberReceived);
//    c.define(FIBONACCIPROPOSALACCEPTED,...);
    c.define(KILL,DESTROYER);
    c.define(START, h);

  }
}

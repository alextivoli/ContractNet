package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;

import java.util.HashMap;
import java.util.Random;

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

  private int kBid = 0;

  private boolean bidAccepted = false;

  private int fibonacciNumber = 0;

  private Reference masterReference;

  public Worker(final boolean isStorageEnable){
    this.isStorageEnable = isStorageEnable;
    // Create a new Random object.
    Random random = new Random();
    // Generate a random number between MIN and MAX.
    this.kBid = random.nextInt(10) + 1;
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

  private int computeFibonacci(int n){
      if (n <= 1) {
        return n;
      }

      if(this.isStorageEnable){
        if(!this.fibonacciStorage.containsKey(n)){
          int  fibonacci = computeFibonacci(n - 1) + computeFibonacci(n - 2);
          this.fibonacciStorage.put(n, fibonacci);
        }

        return this.fibonacciStorage.get(n);
      }
      return computeFibonacci(n - 1) + computeFibonacci(n - 2);
  }



  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {

    MessageHandler h = (m) -> {
      System.out.print("W :: I'M WORKER \n");

      return null;
    };

    MessageHandler taskTimeout = (m) -> {

      if(this.bidAccepted){
        System.out.print("W :: ACCETTATA \n");
        this.kBid = 10;
        send(this.masterReference, new MessageFibonacciNumber(computeFibonacci(this.fibonacciNumber)));
      }else {
        this.kBid--;
      }

      this.bidAccepted = false;
      return null;
    };

    MessageHandler masterAccepted = (m) -> {

      this.bidAccepted = true;
      return null;
    };

    MessageHandler fibonacciNumberReceived = (m) -> {
      this.masterReference = m.getSender();
      MessageFibonacciNumber n = (MessageFibonacciNumber) m.getContent();
      System.out.print("W :: RICEVUTO MESSAGGIO : " + n.getMessageFibonacciNumber() +" \n");
      this.fibonacciNumber = n.getMessageFibonacciNumber();
      // Create a new Random object.
      Random random = new Random();
      // Generate a random number between MIN and MAX.
      int isAvailable = random.nextInt(2) + 1;

      if(isAvailable == 1){
        int price = this.getFibonacciPrice(n.getMessageFibonacciNumber());
        System.out.print("W :: ACCETTO E INVIO PROPOSTA \n");
        MessageFibonacciPrice messageFibonacciPrice = new MessageFibonacciPrice(price + this.kBid);
        future(m.getSender(), messageFibonacciPrice, 3000, taskTimeout);
      }else{
        System.out.print("W :: RIFIUTO \n");
        MessageFibonacciPrice messageFibonacciPrice = new MessageFibonacciPrice(-1);
        send(m.getSender(), messageFibonacciPrice);
      }

      return null;
    };

    c.define(FIBONACCINUMBERPATTERN, fibonacciNumberReceived);
    c.define(FIBONACCIPROPOSALACCEPTED,masterAccepted);
    c.define(KILL,DESTROYER);
    c.define(START, h);

  }
}

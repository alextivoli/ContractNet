package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;

import java.sql.Timestamp;
import java.util.Random;

/**
 *
 * The {@code Master} class defines a behavior that cyclically selects
 * randomly a {@code Worker} actor until the number of selected actor
 * becomes equal to the number of workers. Moreover, after the selection
 * of the worker, the master cyclically sends it a message and waits
 * for the answer until the number of sent messages becomes equal to
 * the a predefined value.
 *
 * @see Worker
 *
**/

public final class Master extends Behavior
{
  private static final long serialVersionUID = 1L;


  private Reference[] references;

  private Reference acceptedWorker;

  private int minPriceReceived = 101;


  private static final MessagePattern FIBONACCIPRICEPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciPrice.class));

  private static final int MIN = 2;

  private static final int MAX = 100;

  /**
   * Class constructor.
   *
   * @param w  the number of workers.
   * @param m  the number of messages.
   *
  **/
  public Master(Reference[] references)
  {
    this.references = references;
  }


  public int RandomFibonacci(){

    // Create a new Random object.
    Random random = new Random();

    // Generate a random number between MIN and MAX.
    return random.nextInt(MAX) + MIN;
  }

  private void chooseWorker(int price, Reference ref){
    if(price >= this.minPriceReceived){
      return;
    }

    this.minPriceReceived = price;
    this.acceptedWorker = ref;
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c) {

    MessageHandler start = (m) -> {
      System.out.print("M :: I'M MASTER \n");
      MessageFibonacciNumber messageFibonacciNumber = new MessageFibonacciNumber(RandomFibonacci());
      for(int i = 0; i < references.length; i++){
        send(references[i], messageFibonacciNumber);
      }
      return null;
    };

    MessageHandler priceReceived = (m) -> {
      MessageFibonacciPrice n = (MessageFibonacciPrice) m.getContent();
      System.out.print("M :: PRICE RECEIVED : " + n.getMessageFibonacciPrice() +" \n");
      chooseWorker(n.getMessageFibonacciPrice(), m.getSender());



      return null;
    };





    c.define(KILL,DESTROYER);
    c.define(START, start);
    c.define(FIBONACCIPRICEPATTERN, priceReceived);




  }

}

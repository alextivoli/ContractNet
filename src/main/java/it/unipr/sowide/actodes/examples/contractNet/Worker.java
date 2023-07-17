package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.registry.Reference;

import java.math.BigInteger;
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

  private static final MessagePattern FIBONACCINUMBERPATTERN = MessagePattern.contentPattern(new IsInstance(TaskAnnouncement.class));

  private static final MessagePattern FIBONACCIPROPOSALACCEPTED = MessagePattern.contentPattern(new IsInstance(MessageAcknowledgement.class));

//  private static final MessagePattern FIBONACCIPROPOSAL = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));
//
//  private static final MessagePattern FIBONACCINUMBERPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));
  private static final long serialVersionUID = 1L;

  private final boolean isStorageEnable;

  private HashMap<Integer, BigInteger> fibonacciStorage = new HashMap<Integer, BigInteger>();

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
    this.fibonacciStorage.put(0,BigInteger.ZERO);
  }

  private int getFibonacciPrice(int fibonacciNumber){
    if(!isStorageEnable){
      return fibonacciNumber;
    }

    if(fibonacciNumber <= this.greatherFibonacciStored){
      return 0;
    }else{
      System.out.print(fibonacciStorage.get(this.greatherFibonacciStored));
      return fibonacciNumber - fibonacciStorage.size() + 1;
    }
  }

  private BigInteger computeFibonacci(BigInteger n){
      if (n.intValue() <= 1) {
        return n;
      }

      if(this.isStorageEnable){
        if(!this.fibonacciStorage.containsKey(n.intValue())){
          BigInteger fibonacci = computeFibonacci(n.subtract(BigInteger.ONE)).add(computeFibonacci(n.subtract(BigInteger.TWO)));
          this.fibonacciStorage.put(n.intValue(), fibonacci);
          this.greatherFibonacciStored = n.intValue();
        }

        return this.fibonacciStorage.get(n.intValue());
      }
      return computeFibonacci(n.subtract(BigInteger.ONE)).add(computeFibonacci(n.subtract(BigInteger.TWO)));
  }



  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c)
  {

    MessageHandler h = (m) -> {
      System.out.print("W :: Worker online \n");

      return null;
    };

    MessageHandler taskTimeout = (m) -> {

      if(this.bidAccepted){
        System.out.print("W :: \uD83E\uDD11 Bid accepted by Master \n");
        this.kBid = 10;
        send(this.masterReference, new FibonacciResult(computeFibonacci(BigInteger.valueOf(this.fibonacciNumber))));
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
      TaskAnnouncement n = (TaskAnnouncement) m.getContent();
      System.out.print("W :: Received fibonacci number to compute: " + n.getMessageFibonacciNumber() +" \n");
      this.fibonacciNumber = n.getMessageFibonacciNumber();
      // Create a new Random object.
      Random random = new Random();
      // Generate a random number between MIN and MAX.
      int isAvailable = random.nextInt(2) + 1;

      if(isAvailable == 1){
        int price = this.getFibonacciPrice(n.getMessageFibonacciNumber());
        System.out.print("W :: I'm available, sending price offer: " + price + this.kBid + "\n");
        MessageFibonacciPrice messageFibonacciPrice = new MessageFibonacciPrice(price + this.kBid); //RIMETTI KBID
        future(m.getSender(), messageFibonacciPrice, 3000, taskTimeout);
      }else{
        System.out.print("W :: I'm not available, sending -1 \n");
        MessageFibonacciPrice messageFibonacciPrice = new MessageFibonacciPrice(-1);
        send(m.getSender(), messageFibonacciPrice);
      }

      return null;
    };

    		
		MessageHandler killHandler = (m) -> {
		      send(m.getSender(), Done.DONE);
          System.out.println("W :: \uD83D\uDC80 Received kill message");
		      return Shutdown.SHUTDOWN;
		    };

		c.define(KILL, killHandler);

    c.define(FIBONACCINUMBERPATTERN, fibonacciNumberReceived);
    c.define(FIBONACCIPROPOSALACCEPTED,masterAccepted);
    c.define(START, h);

  }
}

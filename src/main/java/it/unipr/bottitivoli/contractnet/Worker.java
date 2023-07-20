package it.unipr.bottitivoli.contractnet;


import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.bottitivoli.contractnet.messages.FibonacciResult;
import it.unipr.bottitivoli.contractnet.messages.AcknowledgmentMessage;
import it.unipr.bottitivoli.contractnet.messages.FibonacciBid;
import it.unipr.bottitivoli.contractnet.messages.ReportMessage;
import it.unipr.bottitivoli.contractnet.messages.TaskAnnouncement;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The {@code Worker} class defines a behavior for the Worker
 * The worker receives task announcement from the master and make offers to the master
 * If the master accepts the offer, the worker compute the fibonacci values of the number received and store it if the storage is enable
 * The worker then send the fibonacci value to the master and wait for another value to compute
 * After all the worker sends a report to the master with all of the accepted offers 
 * @author Filippo Botti, Alex Tivoli
 *
**/

public final class Worker extends Behavior
{

  private static final MessagePattern FIBONACCITASKPATTERN = MessagePattern.contentPattern(new IsInstance(TaskAnnouncement.class));

  private static final MessagePattern BIDACCEPTED = MessagePattern.contentPattern(new IsInstance(AcknowledgmentMessage.class));

  private static final long serialVersionUID = 1L;

  private final boolean isStorageEnable;

  private HashMap<Integer, BigInteger> fibonacciStorage = new HashMap<Integer, BigInteger>();

  private int greatherFibonacciStored = 0;

  private ArrayList<Integer> bidsAcceptedByMaster = new ArrayList<Integer>();

  private int kBid = 0;

  private int bid;

  private boolean isCurrentBidAccepted = false;

  private int fibonacciNumber = 0;

  private Reference masterReference;

  /**
   * Constructor for the Worker class.
   *
   * @param isStorageEnable A boolean indicating whether storage is enabled in the worker node.
   *                        If true, storage is enabled; if false, storage is disabled.
   */
  public Worker(final boolean isStorageEnable){
    this.isStorageEnable = isStorageEnable;
    Random random = new Random();
    this.kBid = random.nextInt(5);
    this.fibonacciStorage.put(0,BigInteger.ZERO);
    this.fibonacciStorage.put(1,BigInteger.ONE);
  }

  /**
   * Get the Fibonacci price for a given Fibonacci number.
   *
   * @param fibonacciNumber The Fibonacci number for which the price needs to be calculated.
   * @return The Fibonacci price for the given Fibonacci number.
   *         If storage is disabled, the Fibonacci number itself is returned as the price.
   *         If storage is enabled, the Fibonacci price is calculated based on the stored values.
   *         If the given Fibonacci number is less than or equal to the greatest Fibonacci number stored,
   *         the price is 0 (indicating that it is already stored).
   *         Otherwise, the price is calculated as the difference between the given Fibonacci number and
   *         the number of Fibonacci values stored plus 1.
   */
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

  /**
   * Compute the Fibonacci number for a given positive integer n using recursion.
   *
   * @param n The positive integer for which the Fibonacci number needs to be computed.
   * @return The Fibonacci number for the given positive integer n.
   */
  // private BigInteger computeFibonacci(BigInteger n){
  //     if (n.intValue() <= 1) {
  //       return n;
  //     }
  //     if(this.isStorageEnable){
  //       if(!this.fibonacciStorage.containsKey(n.intValue())){
  //         BigInteger fibonacci = computeFibonacci(n.subtract(BigInteger.ONE)).add(computeFibonacci(n.subtract(BigInteger.TWO)));
  //         this.fibonacciStorage.put(n.intValue(), fibonacci);
  //         this.greatherFibonacciStored = n.intValue();
  //       }
  //       return this.fibonacciStorage.get(n.intValue());
  //     }
  //     return computeFibonacci(n.subtract(BigInteger.ONE)).add(computeFibonacci(n.subtract(BigInteger.TWO)));
  // }
  private BigInteger computeFibonacci(BigInteger n) {
    if (n.intValue() <= 1) {
        return n;
    }

    if (isStorageEnable) {
        for (int i = 2; i <= n.intValue(); i++) {
            if (!fibonacciStorage.containsKey(i)) {
                BigInteger fibonacci = fibonacciStorage.get(i - 1).add(fibonacciStorage.get(i - 2));
                fibonacciStorage.put(i, fibonacci);
            }
        }
        return fibonacciStorage.get(n.intValue());
    }

    BigInteger prevFib = BigInteger.ZERO;
    BigInteger currentFib = BigInteger.ONE;

    for (int i = 2; i <= n.intValue(); i++) {
        BigInteger nextFib = prevFib.add(currentFib);
        prevFib = currentFib;
        currentFib = nextFib;
    }

    return currentFib;
}

/** {@inheritDoc} **/
@Override
public void cases(final CaseFactory c) {
    
    /*
     * This block of code will be executed when the START message is received.
     */
    MessageHandler h = (m) -> {
        System.out.print("W :: Worker online \n");
        return null;
    };

    /*
     * This block of code will be executed when a taskTimeout message is received.
     * Check if the current bid was accepted by the master.
     * If the bid is accepted, add the current bid to the bidsAcceptedByMaster list, reset the kBid to 0 and
     * send the FibonacciResult message to the master with the computed Fibonacci number.
     * If the bid is not accepted, decrement the kBid by 1.
     */
    MessageHandler taskTimeout = (m) -> {
        if (this.isCurrentBidAccepted) {
            System.out.print("W :: \uD83E\uDD11 Bid accepted by Master \n");
            this.bidsAcceptedByMaster.add(this.bid);
            this.kBid = 0;
            send(this.masterReference, new FibonacciResult(computeFibonacci(BigInteger.valueOf(this.fibonacciNumber))));
        } else {
            this.kBid--;
        }
        this.isCurrentBidAccepted = false;

        return null;
    };

    /*
     * This block of code will be executed when a masterAccepted message is received.
     * Set the isCurrentBidAccepted flag to true, indicating that the current bid is accepted.
     */
    MessageHandler masterAccepted = (m) -> {
        this.isCurrentBidAccepted = true;

        return null;
    };

    /*
     * This block of code will be executed when a message containing a TaskAnnouncement object is received.
     * Extract the Fibonacci number from the TaskAnnouncement message.
     * Generate a random value (1 or 2) to simulate worker availability.
     * If the worker is available, calculate the bid using getFibonacciPrice method and kBid to add fariness and random to the bid.
     * Ensure the bid is not negative (minimum bid is 5).
     * Send the FibonacciBid message to the master and set a timeout for the taskTimeout message.
     */
    MessageHandler fibonacciNumberReceived = (m) -> {

        this.masterReference = m.getSender();
        TaskAnnouncement n = (TaskAnnouncement) m.getContent();
        System.out.print("W :: Received fibonacci number to compute: " + n.getMessageFibonacciNumber() +" \n");
        this.fibonacciNumber = n.getMessageFibonacciNumber();
        Random random = new Random();
        int isAvailable = random.nextInt(2) + 1;

        if (isAvailable == 1) {
            this.bid = this.getFibonacciPrice(n.getMessageFibonacciNumber()) + this.kBid * 5;
            if (this.bid < 0) {
                this.bid = 5;
            }
            System.out.print("W :: I'm available, sending price offer: " + bid + "\n");
            FibonacciBid FibonacciBid = new FibonacciBid(this.bid);
            future(m.getSender(), FibonacciBid, 3000, taskTimeout);
        } else {
            System.out.print("W :: I'm not available, sending -1 \n");
            FibonacciBid FibonacciBid = new FibonacciBid(-1);
            send(m.getSender(), FibonacciBid);
        }

        return null;
    };

    /*
     * This block of code will be executed when a KILL message is received.
     * Send a report with all of the accepted bid to the worker
     */
    MessageHandler killHandler = (m) -> {

        System.out.println("W :: \uD83D\uDC80 Received kill message, sending my trend to the master");
        send(m.getSender(), new ReportMessage(this.bidsAcceptedByMaster));
        return Shutdown.SHUTDOWN;
    };
    MessageHandler error = (m) -> {

        System.out.println("W :: \uD83D\uDC80 \uD83D\uDC80 \uD83D\uDC80 Received error message, sending my trend to the master. " + m.getContent());
        return null;
        // send(m.getSender(), new ReportMessage(this.bidsAcceptedByMaster));
        // return Shutdown.SHUTDOWN;
    };

    c.define(TIMEOUT, error);
    c.define(KILL, killHandler);
    c.define(FIBONACCITASKPATTERN, fibonacciNumberReceived);
    c.define(BIDACCEPTED, masterAccepted);
    c.define(START, h);
}
}

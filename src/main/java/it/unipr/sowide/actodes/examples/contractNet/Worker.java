package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.examples.contractNet.messages.FibonacciResult;
import it.unipr.sowide.actodes.examples.contractNet.messages.AcknowledgmentMessage;
import it.unipr.sowide.actodes.examples.contractNet.messages.FibonacciBid;
import it.unipr.sowide.actodes.examples.contractNet.messages.ReportMessage;
import it.unipr.sowide.actodes.examples.contractNet.messages.TaskAnnouncement;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.registry.Reference;

import java.math.BigInteger;
import java.util.ArrayList;
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
   * Compute the Fibonacci number for a given positive integer n using recursion and dynamic programming.
   *
   * @param n The positive integer for which the Fibonacci number needs to be computed.
   * @return The Fibonacci number for the given positive integer n.
   *         If the input n is less than or equal to 1, n itself is returned.
   *         If storage is enabled, the Fibonacci number is computed using dynamic programming.
   *         Otherwise, the Fibonacci number is computed using simple recursion.
   */
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



  /**
 * Define message handlers for different message types using lambda expressions.
 *
 * @param c A CaseFactory used to define the message handlers for various message patterns.
 *          This allows handling different types of messages received in the context of a distributed system.
 *          The message handlers are defined as lambda expressions for START, FIBONACCITASKPATTERN,
 *          BIDACCEPTED, and KILL message patterns.
 */
/** {@inheritDoc} **/
@Override
public void cases(final CaseFactory c) {

    MessageHandler h = (m) -> {
        // This block of code will be executed when the START message is received.
        System.out.print("W :: Worker online \n");
        return null;
    };

    MessageHandler taskTimeout = (m) -> {
        // This block of code will be executed when a taskTimeout message is received.

        // Check if the current bid is accepted by the master.
        if (this.isCurrentBidAccepted) {
            // If the bid is accepted, print a message indicating the acceptance by the master.
            System.out.print("W :: \uD83E\uDD11 Bid accepted by Master \n");

            // Add the current bid to the bidsAcceptedByMaster list.
            this.bidsAcceptedByMaster.add(this.bid);

            // Reset the kBid to 0.
            this.kBid = 0;

            // Send the FibonacciResult message to the master with the computed Fibonacci number.
            send(this.masterReference, new FibonacciResult(computeFibonacci(BigInteger.valueOf(this.fibonacciNumber))));
        } else {
            // If the bid is not accepted, decrement the kBid by 1.
            this.kBid--;
        }

        // Reset the flag isCurrentBidAccepted to false for the next bid.
        this.isCurrentBidAccepted = false;

        return null;
    };

    MessageHandler masterAccepted = (m) -> {
        // This block of code will be executed when a masterAccepted message is received.

        // Set the isCurrentBidAccepted flag to true, indicating that the current bid is accepted.
        this.isCurrentBidAccepted = true;

        return null;
    };

    MessageHandler fibonacciNumberReceived = (m) -> {
        // This block of code will be executed when a message containing a TaskAnnouncement object is received.

        // Get the sender of the message (presumably the master) and store it in the masterReference variable.
        this.masterReference = m.getSender();

        // Extract the Fibonacci number from the TaskAnnouncement message.
        TaskAnnouncement n = (TaskAnnouncement) m.getContent();
        System.out.print("W :: Received fibonacci number to compute: " + n.getMessageFibonacciNumber() +" \n");
        this.fibonacciNumber = n.getMessageFibonacciNumber();

        // Generate a random value (1 or 2) to simulate worker availability.
        Random random = new Random();
        int isAvailable = random.nextInt(2) + 1;

        // Check if the worker is available (isAvailable = 1).
        if (isAvailable == 1) {
            // If the worker is available, calculate the bid using getFibonacciPrice method and kBid.
            this.bid = this.getFibonacciPrice(n.getMessageFibonacciNumber()) + this.kBid * 5;

            // Ensure the bid is not negative (minimum bid is 0).
            if (this.bid < 0) {
                this.bid = 0;
            }

            // Print a message indicating the worker is available and sending the price offer.
            System.out.print("W :: I'm available, sending price offer: " + bid + "\n");

            // Create a FibonacciBid message with the bid value.
            FibonacciBid FibonacciBid = new FibonacciBid(this.bid);

            // Send the FibonacciBid message to the master and set a timeout for the taskTimeout message.
            future(m.getSender(), FibonacciBid, 3000, taskTimeout);
        } else {
            // If the worker is not available (isAvailable = 2), send a bid of -1 to indicate unavailability.
            System.out.print("W :: I'm not available, sending -1 \n");
            FibonacciBid FibonacciBid = new FibonacciBid(-1);
            send(m.getSender(), FibonacciBid);
        }

        return null;
    };

    MessageHandler killHandler = (m) -> {
        // This block of code will be executed when a KILL message is received.

        // Print a message indicating the receipt of a KILL message and send the trend to the master.
        System.out.println("W :: \uD83D\uDC80 Received kill message, sending my trend to the master");
        send(m.getSender(), new ReportMessage(this.bidsAcceptedByMaster));

        return Shutdown.SHUTDOWN;
    };

    // Define message handlers for different message patterns.
    c.define(KILL, killHandler);
    c.define(FIBONACCITASKPATTERN, fibonacciNumberReceived);
    c.define(BIDACCEPTED, masterAccepted);
    c.define(START, h);
}
}

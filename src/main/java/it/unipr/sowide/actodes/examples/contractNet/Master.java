package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.registry.Reference;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private int minPriceReceived = 9999;
  private int bidCounter = 0;
  private HashMap<Reference, Integer> receivedBids = new HashMap<Reference, Integer>();
  private static final MessagePattern FIBONACCIPRICEPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciPrice.class));
  private static final MessagePattern FIBONACCIRECEIVEDNUMBER = MessagePattern.contentPattern(new IsInstance(MessageFibonacciNumber.class));
  private static final int MIN = 2;
  private static final int MAX = 25;
  private int totalAcceptedWorkers = 0;
  private int totalCycle = 0;
  private List<Integer> prices = new ArrayList<Integer>();
  private int totalDoubleChoose = 0;
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

  private void chooseWorker(){
    if(this.receivedBids.size() == 0){
      startFibonacci();
    }else{
      this.totalCycle++;
      this.receivedBids.forEach( (ref, key) -> {
        if(key == this.minPriceReceived){
          this.totalAcceptedWorkers ++ ;
          this.prices.add(this.minPriceReceived);
          send(ref, new MessageAcknowledgement(true) );
        }
      });

      if(totalAcceptedWorkers > 1){
        this.totalDoubleChoose++;
      }
    }
  }

  public void startFibonacci(){

    this.receivedBids.clear();
    this.bidCounter = 0;
    this.minPriceReceived = 9999;
    this.totalAcceptedWorkers = 0;
    MessageFibonacciNumber messageFibonacciNumber = new MessageFibonacciNumber(RandomFibonacci());
    for(int i = 0; i < references.length; i++){
      send(references[i], messageFibonacciNumber);
    }
  }

  public int sum(List<Integer> numbers) {
    int sum = 0;
    for (int num : numbers) {
      sum += num;
    }
    return sum;
  }

  public void finish(){
    int totalcost = sum(this.prices);
    System.out.print("M :: TOTAL COST: " + totalcost + " MEAN COST: " + totalcost/50 + " NUMBER DOUBLE CHOICE: " + this.totalDoubleChoose + " \n");

    XYChart chart = new XYChartBuilder().width(800).height(600).title("Int List Plot").xAxisTitle("Index")
            .yAxisTitle("Value").build();         // Add the data series to the chart
    chart.addSeries("Integers", null, this.prices);         // Show the chart
    new SwingWrapper<>(chart).displayChart();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c) {


    MessageHandler start = (m) -> {
      System.out.print("M :: I'M MASTER \n");
      startFibonacci();
      return null;
    };

    MessageHandler priceReceived = (m) -> {
      MessageFibonacciPrice n = (MessageFibonacciPrice) m.getContent();
      this.bidCounter++;
      if(n.getMessageFibonacciPrice() == -1){
        System.out.print("M :: WORKER NON DISPONIBILE \n");
      }else{
        System.out.print("M :: PRICE RECEIVED : " + n.getMessageFibonacciPrice() +" \n");
        this.receivedBids.put(m.getSender(),n.getMessageFibonacciPrice());

        if(n.getMessageFibonacciPrice() < this.minPriceReceived){
          this.minPriceReceived = n.getMessageFibonacciPrice();
        }
      }

      if(bidCounter == references.length){
        System.out.print("M :: "+ receivedBids +" \n");
        chooseWorker();
      }
      return null;
    };

    MessageHandler fibonacciNumberCalculatedReceived = (m) -> {
      MessageFibonacciNumber n = (MessageFibonacciNumber) m.getContent();
      this.totalAcceptedWorkers --;
      System.out.print("M :: NUMBER CALCULATED RECEIVED : " + n.getMessageFibonacciNumber() +" \n");
      if(this.totalAcceptedWorkers == 0){
        System.out.print("M :: " + this.totalCycle + "\n");
        if(this.totalCycle == 5){
          System.out.print("UCCIDO TUTTO");
            for (Reference reference : this.references) {
              send(reference, KILL);
            }
          finish();
          return Shutdown.SHUTDOWN;
        }
        startFibonacci();
      }

      return null;
    };


    c.define(KILL,DESTROYER);
    c.define(START, start);
    c.define(FIBONACCIPRICEPATTERN, priceReceived);
    c.define(FIBONACCIRECEIVEDNUMBER,fibonacciNumberCalculatedReceived );

  }

}

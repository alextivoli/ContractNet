package it.unipr.sowide.actodes.examples.contractNet;

import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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
  private HashMap<Reference, ArrayList<Integer>> reports = new HashMap<Reference, ArrayList<Integer>>();
  private static final MessagePattern FIBONACCIPRICEPATTERN = MessagePattern.contentPattern(new IsInstance(MessageFibonacciPrice.class));
  private static final MessagePattern FIBONACCIRECEIVEDNUMBER = MessagePattern.contentPattern(new IsInstance(FibonacciResult.class));
  private static final MessagePattern REPORTMESSAGE = MessagePattern.contentPattern(new IsInstance(ReportMessage.class));
  private static final int MIN = 2;
  private static final int MAX = 100;
  private int totalAcceptedWorkers = 0;
  private int totalCycle = 0;
  private List<Integer> prices = new ArrayList<Integer>();
  private int totalDoubleChoose = 0;
  private int reportCounter = 0;
  private final boolean isStorageEnable;
  /**
   * Class constructor.
   *
   * @param w  the number of workers.
   * @param m  the number of messages.
   *
  **/
  public Master(Reference[] references, final boolean isStorageEnable)
  {
    this.references = references;
    this.isStorageEnable = isStorageEnable;
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
    }
    else {
      this.totalCycle++;
      this.receivedBids.forEach( (ref, key) -> {
        if(key == this.minPriceReceived){
          this.totalAcceptedWorkers++;
          this.prices.add(this.minPriceReceived);
          send(ref, new MessageAcknowledgement(true));
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
    TaskAnnouncement messageFibonacciNumber = new TaskAnnouncement(RandomFibonacci());
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

  public void writeReportToFile(HashMap<Reference, ArrayList<Integer>> hashMap) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("./results/output.txt"))) {
        writer.write("REPORT\nNumber of workers: " + this.references.length + " Storage: " + this.isStorageEnable + "\n");
        for (Reference key : hashMap.keySet()) {
            List<Integer> values = hashMap.get(key);
            float mean = (float)sum(values)/values.size();
            StringBuilder row = new StringBuilder(key + " Mean: " + mean + " bids: " + values.toString());
            writer.write(row.toString());
            writer.newLine();
        }
        int totalcost = sum(this.prices);
        writer.write("Master :: Total price: " + totalcost + " Average transaction price: " + (float) totalcost/50 + " Simultaneous transaction count: " + this.totalDoubleChoose + " \n");
   
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public void finish(){
    writeReportToFile(this.reports);
     XYChart chart = new XYChartBuilder().width(800).height(600).title("Price trend").xAxisTitle("Task")
            .yAxisTitle("Price").build();         // Add the data series to the chart
    chart.addSeries("Integers", null, this.prices);         // Show the chart
    new SwingWrapper<>(chart).displayChart();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c) {

    MessageHandler start = (m) -> {
      System.out.print("M :: Master online\n");
      startFibonacci();
      return null;
    };

    MessageHandler priceReceived = (m) -> {
      MessageFibonacciPrice n = (MessageFibonacciPrice) m.getContent();
      this.bidCounter++;
      if(n.getMessageFibonacciPrice() == -1){
        System.out.print("M :: \uD83D\uDEAB Worker with id: " + m.getSender() + " not available. \n");
      }else{
        System.out.print("M :: \uD83D\uDCB5 Worker with id: " + m.getSender() + " wants: " + n.getMessageFibonacciPrice() +" \n");
        this.receivedBids.put(m.getSender(),n.getMessageFibonacciPrice());
        if(n.getMessageFibonacciPrice() < this.minPriceReceived){
          this.minPriceReceived = n.getMessageFibonacciPrice();
        }
      }
      if(bidCounter == references.length){
        chooseWorker();
      }
      return null;
    };

    MessageHandler fibonacciNumberCalculatedReceived = (m) -> {
      FibonacciResult n = (FibonacciResult) m.getContent();
      this.totalAcceptedWorkers --;
      System.out.print("M :: \u2705 Result received: " + n.getMessageFibonacciNumber() +" \n");
      if(this.totalAcceptedWorkers == 0){
        if(this.totalCycle == 2){
            for (Reference reference : this.references) {
              System.out.print("M :: Sending kill message to: "+ reference + "\n");
              send(reference, Kill.KILL);
            }
          return null;
        }
        else {
          startFibonacci();
        }
      }

      return null;
    };

    MessageHandler killHandler = (m) -> {
        send(m.getSender(), Done.DONE);
        System.out.println("M :: \uD83D\uDC80 Received kill message");
        return Shutdown.SHUTDOWN;
      };

    MessageHandler reportMessageHandler = (m) -> {
        this.reportCounter++;
        ArrayList<Integer> receivedReport = ((ReportMessage)m.getContent()).getAllBids();
        System.out.println("M :: Received report bids from: " + m.getSender() + " " + receivedReport);
        this.reports.put(m.getSender(), receivedReport);
        if(this.reportCounter == this.references.length){
          System.out.println("M :: Received all reports, start writing on file");
          finish();
          send(getParent(), Kill.KILL); 
        }
        return null;
      };

		c.define(KILL, killHandler);
    c.define(REPORTMESSAGE, reportMessageHandler);
    c.define(START, start);
    c.define(FIBONACCIPRICEPATTERN, priceReceived);
    c.define(FIBONACCIRECEIVEDNUMBER,fibonacciNumberCalculatedReceived );

  }

}

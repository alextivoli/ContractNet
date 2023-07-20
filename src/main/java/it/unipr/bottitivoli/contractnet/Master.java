package it.unipr.bottitivoli.contractnet;


import it.unipr.sowide.actodes.actor.*;
import it.unipr.sowide.actodes.actor.Shutdown;
import it.unipr.bottitivoli.contractnet.messages.FibonacciResult;
import it.unipr.bottitivoli.contractnet.messages.AcknowledgmentMessage;
import it.unipr.bottitivoli.contractnet.messages.FibonacciBid;
import it.unipr.bottitivoli.contractnet.messages.ReportMessage;
import it.unipr.bottitivoli.contractnet.messages.TaskAnnouncement;
import it.unipr.sowide.actodes.filtering.constraint.IsInstance;
import it.unipr.sowide.actodes.interaction.Done;
import it.unipr.sowide.actodes.interaction.Kill;
import it.unipr.sowide.actodes.registry.Reference;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The {@code Master} class defines a behavior for the Master
 * The master sends task announcements with a random number to compute its correspondive fibonacci value
 * The master choose which worker (or workers) select to compute fibonacci based on the bids that it receive from the workers
 * After all the master make a report with all of the bids accepted and the price trend
 *
 * @author Filippo Botti, Alex Tivoli
 *
**/

public final class Master extends Behavior
{
  private static final long serialVersionUID = 1L;
  private Reference[] workers;
  private int minPriceReceived = 1000;
  private int bidsReceivedCounter = 0;
  private HashMap<Reference, Integer> validBidsReceived = new HashMap<Reference, Integer>();
  private HashMap<Reference, ArrayList<Integer>> reports = new HashMap<Reference, ArrayList<Integer>>();
  private static final MessagePattern FIBONACCIBIDPATTERN = MessagePattern.contentPattern(new IsInstance(FibonacciBid.class));
  private static final MessagePattern FIBONACCIRESULTPATTERN = MessagePattern.contentPattern(new IsInstance(FibonacciResult.class));
  private static final MessagePattern REPORTMESSAGEPATTERN = MessagePattern.contentPattern(new IsInstance(ReportMessage.class));
  private static final int MIN = 2;
  private static final int MAX = 100;
  private int totalAcceptedWorkers = 0;
  private int totalCycle = 0;
  private List<Integer> prices = new ArrayList<Integer>();
  private int totalDoubleChoose = 0;
  private int reportCounter = 0;
  private final boolean isStorageEnable;
  private long startTime;
  private long endTime;

  /**
   * Constructor for the Master class.
   *
   * @param workers An array of worker references representing the workers
   *                Each worker reference is an identifier for a worker node
   * @param isStorageEnable A boolean indicating whether storage is enabled in the system.
   *                        If true, storage is enabled; if false, storage is disabled.
   */
  public Master(Reference[] workers, final boolean isStorageEnable)
  {
    this.workers = workers;
    this.isStorageEnable = isStorageEnable;
  }

  /**
   * Generate a random Fibonacci number.
   *
   * @return A random Fibonacci number.
   */
  public int RandomFibonacci(){
    Random random = new Random();
    return random.nextInt((MAX-MIN)+1) + MIN;
  }

  /**
   * Choose the workers to accept based on the received bids.
   * If no valid bids were received, restart the Fibonacci sequence.
   * Otherwise, iterate through the valid bids and accept the workers with the minimum price received.
   * If multiple workers have the minimum price, accept all of them and record the occurrence of double choose.
   */
  private void chooseWorker(){
    if(this.validBidsReceived.size() == 0){
      startFibonacci();
    }
    else {
      this.totalCycle++;
      this.validBidsReceived.forEach( (ref, key) -> {
        if(key == this.minPriceReceived){
          this.totalAcceptedWorkers++;
          this.prices.add(this.minPriceReceived);
          send(ref, new AcknowledgmentMessage(true));
        }
      });

      if(totalAcceptedWorkers > 1){
        this.totalDoubleChoose++;
      }
    }
  }

  /**
   * Start the Fibonacci sequence by sending a TaskAnnouncement message with a random Fibonacci number
   * to all workers in the 'workers' array. Reset relevant variables to their initial values for the
   * new cycle of worker selection.
   */
  public void startFibonacci(){
    this.validBidsReceived.clear();
    this.bidsReceivedCounter = 0;
    this.minPriceReceived = 1000;
    this.totalAcceptedWorkers = 0;
    TaskAnnouncement messageFibonacciNumber = new TaskAnnouncement(RandomFibonacci());
    for(int i = 0; i < workers.length; i++){
      send(workers[i], messageFibonacciNumber);
    }
  }

  /**
   * Calculates the sum of all the numbers in the given list.
   *
   * @param numbers A List of integers for which the sum needs to be calculated.
   * @return The sum of all the numbers in the list.
   */
  public int sum(List<Integer> numbers) {
    int sum = 0;
    for (int num : numbers) {
      sum += num;
    }
    return sum;
  }

  /**
   * Writes the report to a file and saves an XYChart as an image.
   *
   * @param hashMap A HashMap containing Reference keys and ArrayList<Integer> values.
   *               It represents the bid values that were accepted by the master for every workers.
   * @param chart   An XYChart that represents the price trend for the master.
   *               
   */
  public void writeReportToFile(HashMap<Reference, ArrayList<Integer>> hashMap, XYChart chart) {
    String filePath = "./results/output.txt";
    File file = new File(filePath);
    if (!file.exists()) {
        try {
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
        double executionTime = (this.endTime-this.startTime)/ 1000.0;
        writer.write("REPORT\nNumber of workers: " + this.workers.length + " Storage: " + this.isStorageEnable + "\nExecution Time: " + executionTime + " seconds\n");
        for (Reference key : hashMap.keySet()) {
            List<Integer> values = hashMap.get(key);
            float mean = (float)sum(values)/values.size();
            StringBuilder row = new StringBuilder(key + " Mean: " + mean + " bids: " + values.toString());
            writer.write(row.toString());
            writer.newLine();
        }
        int totalcost = sum(this.prices);
        writer.write("Master :: Total price: " + totalcost + " Average transaction price: " + (float) totalcost/50 + " Simultaneous transaction count: " + this.totalDoubleChoose + " \n");
        String chartName = "./results/" + String.valueOf(this.workers.length) + this.isStorageEnable;
        BitmapEncoder.saveBitmap(chart, chartName, BitmapFormat.PNG);
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  /**
   * saveReports the process by recording the end time, creating an XYChart to visualize the price trend,
   * and generating a report with the provided HashMap of worker bids. The generated report includes
   * information about the execution time and worker bids. The XYChart visualizes
   * the price trend over tasks. The report is saved to a file, and the chart is saved as an image.
   */
  public void saveReports(){
    this.endTime = System.currentTimeMillis();
     XYChart chart = new XYChartBuilder().width(800).height(600).title("Price trend").xAxisTitle("Task")
            .yAxisTitle("Price").build();         
    chart.addSeries("Integers", null, this.prices);        
    writeReportToFile(this.reports, chart);

    //new SwingWrapper<>(chart).displayChart();
  }

  /** {@inheritDoc} **/
  @Override
  public void cases(final CaseFactory c) {

    /*
     * This block of code will be executed when the START message is received.
     * Record the current time as the start time for execution.
     * Call the startFibonacci() method to start the system.
     */
    MessageHandler start = (m) -> {
      System.out.print("M :: Master online\n");
      this.startTime = System.currentTimeMillis();
      startFibonacci();

      return null;
    };

    /*
     * This block of code will be executed when a message containing a FibonacciBid object is received.
     * Get the FibonacciBid object from the message content
     * Check if the FibonacciBid value is -1, indicating that the worker is not available.
     * Store the valid FibonacciBid value received from the worker in the validBidsReceived HashMap.
     * Update the minimum FibonacciBid value if the received value is lower than the current minimum.
     * Check if all workers have sent their bids
     *  If all bids are received, proceed to choose the workers.
     */
    MessageHandler priceReceived = (m) -> {
      FibonacciBid n = (FibonacciBid) m.getContent();
      this.bidsReceivedCounter++;

      if (n.getFibonacciBid() == -1) {
          System.out.print("M :: \uD83D\uDEAB Worker with id: " + m.getSender() + " not available. \n");
      } else {
          System.out.print("M :: \uD83D\uDCB5 Worker with id: " + m.getSender() + " wants: " + n.getFibonacciBid() + " \n");
          this.validBidsReceived.put(m.getSender(), n.getFibonacciBid());
          if (n.getFibonacciBid() < this.minPriceReceived) {
              this.minPriceReceived = n.getFibonacciBid();
          }
      }

      if (bidsReceivedCounter == workers.length) {
          chooseWorker();
      }

      return null;
    };

    /*
     * This block of code will be executed when a message containing a FibonacciResult object is received.
     * Get the FibonacciResult object from the message content.
     * Check if all expected workers have sent their results (totalAcceptedWorkers equals 0), if true
     * and the totalCycle is 50, it means the process has completed the simulation and
     * the master can send the Kill message to all workers to terminate their operations.
     */
    MessageHandler fibonacciNumberCalculatedReceived = (m) -> {
      FibonacciResult n = (FibonacciResult) m.getContent();
      this.totalAcceptedWorkers--;
      System.out.print("M :: \u2705 Result received: " + n.getMessageFibonacciNumber() + " \n");

      if (this.totalAcceptedWorkers == 0) {
          if (this.totalCycle == 50) {
              for (Reference reference : this.workers) {
                  System.out.print("M :: Sending kill message to: " + reference + "\n");
                  send(reference, Kill.KILL);
              }
              return null;
          } else {
              startFibonacci();
          }
      }

      return null;
    };

    /*
     * This block of code will be executed when a KILL message is received.
     * Send a Done.DONE message to the sender of the KILL message to acknowledge the receipt.
     * Terminate the execution
     */
    MessageHandler killHandler = (m) -> {
      send(m.getSender(), Done.DONE);
      System.out.println("M :: \uD83D\uDC80 Received kill message");
      return Shutdown.SHUTDOWN;
    };

    /*
     * This block of code will be executed when a message containing a ReportMessage object is received.
     * Get the ReportMessage object from the message content and extract the received report bids.
     * Store the received report bids in the reports HashMap associated with the sender.
     * Check if all workers have sent their reports (reportCounter equals the number of workers).
     * If all reports are received, start writing the reports to a file and finish the process.
     * Then send a kill message to the Initiator
     */
    MessageHandler reportMessageHandler = (m) -> {
      this.reportCounter++;
      ArrayList<Integer> receivedReport = ((ReportMessage) m.getContent()).getAllBids();
      System.out.println("M :: Received report bids from: " + m.getSender() + " " + receivedReport);
      this.reports.put(m.getSender(), receivedReport);

      if (this.reportCounter == this.workers.length) {
          System.out.println("M :: Received all reports, start writing on file");
          saveReports();
          send(getParent(), Kill.KILL);
      }

      return null;
    };

		c.define(KILL, killHandler);
    c.define(REPORTMESSAGEPATTERN, reportMessageHandler);
    c.define(START, start);
    c.define(FIBONACCIBIDPATTERN, priceReceived);
    c.define(FIBONACCIRESULTPATTERN,fibonacciNumberCalculatedReceived );

  }

}

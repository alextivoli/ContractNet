# Contract Net Protocol Implementation with ActoDes Framework

## Overview

This project showcases an implementation of the Contract Net Protocol using the ActoDes framework. The Contract Net Protocol is a communication protocol commonly used in multi-agent systems to allocate tasks among autonomous agents. It was originally proposed by Reid G. Smith in 1980.

## Purpose

The main purpose of this project is to demonstrate how the Contract Net Protocol can be applied to share activities efficiently among multiple agents in a distributed system. The protocol involves a manager (master) who initiates task announcements, and various service providers (workers) who respond with bids or indicate their unavailability. The manager then assigns the task to one or more workers based on the received bids.

## Task Description

For this implementation, the specific task delegated by the manager to the workers is the calculation of numbers belonging to the Fibonacci sequence. The project explores two different execution scenarios:

1. Execution without memory: In this scenario, the system does not store the results of the Fibonacci sequence calculated in previous tasks by the workers.

2. Execution with memory: In this case, the system saves the results, allowing for optimization in subsequent tasks and reducing overall computation costs.

## ActoDes Framework

The ActoDes framework is utilized to facilitate the implementation of the Contract Net Protocol. ActoDes is a powerful software framework for building multi-agent systems and enables efficient communication and coordination among agents.

## How to Use

To run this project and observe the Contract Net Protocol in action, follow these steps:

1. Clone the repository
2. Launch Initiator.class without any arguments
3. Specify the number of workers and if the storage is enable via input when the system asks
4. Read the reports producted by the system in the results directory

If you're in trouble with these info you can find the installation guide's in the pdf.


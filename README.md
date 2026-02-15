# FV-SEC001 - Software Engineer Challenge — Ad Performance Aggregator

## Introductions:
This project is a console application(CLI) written in Java that processes a large CSV dataset(~1GB) containing 
advertising performance records.
The application aggregates campaign performance metrics and generates analytical reports including:
- Top 10 campaigns with highest CTR
- Top 10 campaigns with lowest CPA
The solution is designed for performance efficiency and low memory usage, suitable for very large datasets.

## Setup instructions:
Requirements:
- Java JDK 17+
- Command Line / PowerShell / Terminal

Verify Java installation:
- java -version
- javac -version

## How to run the program
Pre-process:
1. create new folder src/results 
2. copy template file ad_data.csv and paste in folder /src
In terminal:
1. cd src
2. javac Main.java CampaignStats.java
3. java Main --input ad_data.csv --output results
4. Two result files existing in folder src/results

## Libraries used
No external libraries required.
Pure Java Standard Library (java.io, java.nio, java.util).

## Processing time for the 1GB file
The application:
- Reads csv using streaming(BufferedReader)
- Aggregates data using HashMap
- Avoids loading full dataset into memory
- Computes CTR and CPA after aggregation
- Uses PriorityQueue to track Top 10 results

## AI tool used: ChatGPT

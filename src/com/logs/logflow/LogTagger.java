package com.logs.logflow;

import java.io.*;
import java.util.*;

public class LogTagger {

    private static final String UNTAGGED = "Untagged";
    
    public static void main(String[] args) throws IOException {
        String flowLogFile = "flowlog.txt"; // The flow log file
        String lookupTableFile = "lookup.csv"; // The lookup table file
        String outputFile = "output.txt"; // Output file
        
        // Load the lookup table
        Map<String, String> lookupTable = loadLookupTable(lookupTableFile);
        
        // Process the flow log and output the results
        processFlowLog(flowLogFile, lookupTable, outputFile);
    }
    
    private static Map<String, String> loadLookupTable(String lookupTableFile) throws IOException {
        Map<String, String> lookupTable = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(lookupTableFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming CSV format: dstport,protocol,tag
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0].trim() + parts[1].trim().toLowerCase();
                    String tag = parts[2].trim();
                    lookupTable.put(key, tag);
                }
            }
        }
        return lookupTable;
    }
    
    private static void processFlowLog(String flowLogFile, Map<String, String> lookupTable, String outputFile) throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(flowLogFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming the flow log format is well-defined and fields are space-separated
                String[] parts = line.split("\\s+");
                if (parts.length > 7) {
                    String dstPort = parts[5]; // Extract dstPort
                    String protocol = parts[7].equals("6") ? "tcp" : "udp"; // For simplicity, assume 6 = tcp, others = udp
                    
                    // Create key for lookup
                    String lookupKey = dstPort + protocol;
                    
                    // Check if there is a match in the lookup table
                    String tag = lookupTable.getOrDefault(lookupKey, UNTAGGED);
                    
                    // Increment the tag count
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    
                    // Increment the port/protocol combination count
                    String portProtocolKey = dstPort + "," + protocol;
                    portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
                }
            }
        }
        
        // Output the results to both console and output file
        outputResults(tagCounts, portProtocolCounts, outputFile);
    }
    
    private static void outputResults(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts, String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            // Output tag counts to both console and file
            System.out.println("Tag Counts:");
            writer.println("Tag Counts:");
            System.out.println("Tag,Count");
            writer.println("Tag,Count");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                String result = entry.getKey() + "," + entry.getValue();
                System.out.println(result);
                writer.println(result);
            }
            
            // Output port/protocol combination counts to both console and file
            System.out.println("\nPort/Protocol Combination Counts:");
            writer.println("\nPort/Protocol Combination Counts:");
            System.out.println("Port,Protocol,Count");
            writer.println("Port,Protocol,Count");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                String result = entry.getKey() + "," + entry.getValue();
                System.out.println(result);
                writer.println(result);
            }
        }
    }
}

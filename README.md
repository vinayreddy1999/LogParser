
# Flow Log Tagging Program

## Overview

This Java program reads flow log data, processes each row to map it to a tag based on a lookup table, and generates an output file with the following:
- Count of matches for each tag.
- Count of matches for each port/protocol combination.

The lookup table is stored in a CSV file, where each row specifies a `dstport`, `protocol`, and `tag`. The program uses this information to match the flow log entries.

The output is written to both the console and an output file (`output.txt`).

## Prerequisites

- **Java 8 or above**: Ensure that Java is installed and properly configured in your environment. You can download Java [here](https://www.oracle.com/java/technologies/javase-downloads.html).

## Setup Instructions

### Input Files
1. **flowlog.txt**: This file contains the flow log data. An example of the content is as follows:
    ```
    2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
    2 123456789012 eni-4d3c2b1a 192.168.1.100 203.0.113.101 23 49154 6 15 12000 1620140761 1620140821 REJECT OK
    2 123456789012 eni-5e6f7g8h 192.168.1.101 198.51.100.3 25 49155 6 10 8000 1620140761 1620140821 ACCEPT OK
    ```
   
2. **lookup.csv**: This file contains the lookup table for tags. Each line should be in the format `dstport,protocol,tag`. Example content:
    ```
    25,tcp,sv_P1
    23,tcp,sv_P1
    443,tcp,sv_P2
    110,tcp,email
    ```

Place these files in the same directory as your Java program.

### Compilation and Execution

1. **Compile the Program**: Open a terminal or command prompt in the directory containing `LogTagger.java` and run:
    ```bash
    javac LogTagger.java
    ```
    This will compile the Java program.

2. **Run the Program**: Run the following command to execute the program:
    ```bash
    java LogTagger
    ```
    The program will:
    - Read the flow log from `flowlog.txt`.
    - Read the lookup table from `lookup.csv`.
    - Match the flow logs to their respective tags.
    - Output results to the console and also write them to an output file named `output.txt`.

### Output

The program will generate two sections of results:
1. **Tag Counts**: The count of matches for each tag.
2. **Port/Protocol Combination Counts**: The count of matches for each port/protocol combination.

Example of console and file output:
```
Tag Counts:
Tag,Count
sv_P1,2
sv_P2,1
email,3
Untagged,5

Port/Protocol Combination Counts:
Port,Protocol,Count
443,tcp,1
23,tcp,1
25,tcp,1
110,tcp,1
```


## License

This project is licensed under the MIT License.

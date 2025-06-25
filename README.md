Prerequisites:

Ensure the following tools are installed on your system (Mac/Linux) using terminal commands:

1. Java Installation

sudo apt update # For Linux
sudo apt install default-jdk -y # For Linux

brew install openjdk # For Mac

Verify installation:

java -version


2. Maven Installation

sudo apt install maven -y # For Linux

brew install maven # For Mac

Verify installation:

mvn -version


Clone the Code Repository

Use the following command to clone the repository:

git clone https://github.com/Padmanaban2906/ChronicCouriers.git

git checkout feature-chronos

Navigate to the cloned directory


Input Requirements:

The input for the process is provided via two JSON files(Packages and Riders). The files should include below data's:

Examples of Packages.json:
[
  {
    "id": 1001,
    "priority": "STANDARD",
    "location" : "Chennai",
    "deadline": 1700000000000,
    "orderTime": 1555550000000,
    "isFragile": true
  },
]

Example of Riders.json
[
  {
    "id" : 1,
    "location" : "Chennai",
    "reliabilityRating" : 4.8
  }
]

How to run the application:

mvn clean compile

mvn exec:java -Dexec.args="path/to/packages.json path/to/riders.json"

Make sure the first argument should be packages.json then next is riders.json

Example of output:

mvn exec:java -Dexec.args="/Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Packages.json /Users/mnvspd/ChronicCouriers/ChronosCouriers/src/main/java/org/example/Riders.json"

[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------< org.example:ChronosCouriers >---------------------
[INFO] Building ChronosCouriers 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.0.0:java (default-cli) @ ChronosCouriers ---
2025-06-25 20:31:39.236 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Rider added: 1
2025-06-25 20:31:39.237 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Rider added: 2
2025-06-25 20:31:39.237 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Rider added: 3
2025-06-25 20:31:39.237 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Rider added: 4
2025-06-25 20:31:39.238 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package placed: 1001
2025-06-25 20:31:39.238 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package placed: 1002
2025-06-25 20:31:39.238 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package placed: 1003
2025-06-25 20:31:39.238 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package placed: 1004
2025-06-25 20:31:39.238 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package placed: 1005
2025-06-25 20:31:39.239 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1005 assigned to Rider 1
2025-06-25 20:31:39.239 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1004 assigned to Rider 3
2025-06-25 20:31:39.239 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Riders are not available at this time for the package 1001
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1002 assigned to Rider 2
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1003 assigned to Rider 4
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1005 has been delivered successfully by the Rider 1
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1004 has been delivered successfully by the Rider 3
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1002 has been delivered successfully by the Rider 2
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1003 has been delivered successfully by the Rider 4
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1001 assigned to Rider 1
2025-06-25 20:31:39.240 [org.example.ChronosCouriers.main()] INFO  org.example.service.DispatchCenter - Package 1001 has been delivered successfully by the Rider 1


Explanation:

1. Main Class (ChronosCouriers):
   - Entry point of the application
   - Reads JSON files for packages and riders
   - Initializes DispatchCenter and starts delivery process

2. DispatchCenter Class:
   - Core logic handler
   - Key data structures:
     * HashMap<Integer, Rider> riders
     * HashMap<Integer, Package> packages
     * LinkedHashMap<Integer, Package> riderMappingDet
     * PriorityQueue<Package> packageQueue
     * List<Package> delivered
   - Main methods:
     * addRiderDetails(): Adds new riders
     * setPackages(): Queues packages
     * assignPackageToRider(): Matches packages to riders
     * deliveryCompletionStatus(): Updates delivery status
     * availabilityChk(): Checks for available riders

3. PackageComparator Class:
   - Implements Comparator<Package>
   - Prioritizes packages based on:
     1. Express vs Standard priority
     2. Deadline
     3. Order time

4. Key Workflow:
   a. Load rider and package data
   b. Queue packages by priority
   c. Match riders to packages based on:
      - Location
      - Availability
      - Reliability rating
   d. Update package and rider statuses
   e. Repeat until all packages delivered or no suitable riders

5. Features:
   - Priority-based package handling
   - Location-based rider assignment
   - Status tracking for riders and packages
   - Logging of operations
   - Exception handling for data issues



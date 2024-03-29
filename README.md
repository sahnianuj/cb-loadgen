# What it is
A simple java application that can connect to SSL or non-SSL Couchbase Server endpoint to generate some workload.

# How to load test

Under bin directory there is a self-executing jar file called **cbloadgen.jar**. You just need that jar file to load test your couchbase server.

In order to run the jar you need few input arguments. Here is a list of those argument:

```
Options:

     -h <s>          The cluster address (default: localhost)
     -u <s>          Cluster Admin or RBAC username (default: Administrator)
     -p <s>          Cluster Admin or RBAC password (default: password)
     -b <s>          Couchbase bucket (default: default)
     -e <s>          Couchbase cluster SSL enabled or not (default: false)
     -ks <s>          Keystore full path
     -kp <s>         Keystore password used during creating the store
     
     -t <n>          Number of threads you would like to spawn (default: 1)
     -d <n>          Number of documents you would like to load into the bucket (default: 100) 
 ```    
 

## Without SSL setup

You can simply cd to bin directory first

```
$cd bin

$ java -jar cbloadgen.jar -t 10 -d 100000 -h localhost -u Administrator -p password -b default

or 

$ java -jar cbloadgen.jar -t 10 -d 100000

Connection created.
..........................
*****************************************************************
Time elapsed: 27223 ms
Average Throughput: 3673 ops/sec
Average Latency: 0.27 ms
*****************************************************************


```

## With SSL setup

```
$cd bin

$ java -jar cbloadgen.jar -t 10 -d 100000 -h localhost -u Administrator \
-p password -b default -e true -ks ~/.keystore -kp password

or 

$ java -jar cbloadgen.jar -t 10 -d 100000  -e true -ks ~/.keystore \
-kp password

Connection created.
..........................
*****************************************************************
Time elapsed: 27223 ms
Average Throughput: 3673 ops/sec
Average Latency: 0.27 ms
*****************************************************************
```

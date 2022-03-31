# Author
Name: José Sousa

# DS_Project

The assignment is constituted by three different parts:
+ Part 1 is to implement a network that shares a token ring. Each node taking a turn having the token, and once not needed anymore, passing it along
+ Part 2 is to have a network where each node contains a dictionary. In this network it is possible to share one's dictionary with other nodes.
+ Part 3 is to implement a chat application, where the main objective is to synchronize the messages.

# Organization

The project contains three main folders:
+ **src** folder where the source code is contained
+ **bin** folder where the binaries are contained
+ **log** folder where the execution logs are maintained

Our project contains three main packages:
+ **ds/trabalho/parte1** contains the first part of the assignment
+ **ds/trabalho/parte2** contains the second part of the assignment
+ **ds/trabalho/parte3** contains the third part of the assignment

This project also contains a makefile that will be responsible for compiling and executing the programs. Some of the folders are created during the program execution

# Compile

To compile all project parts, part(1|2|3), the user needs to run the makefile, contained in the DS folder.
To compile, run the following command in DS fodler

```
make
```

To clean the folders of the binaries and log files you can do
```
make clean
```

# Executing the program
## Part1
To execute the first part, the programmer will need to enter into 5 different nodes or create 5 different network interfaces, since the code requires different ips. 
Once the ip set is known, the user needs to change the variable PART_1_MACHINES in the makefile with the ip set to be used. This variable is an array and the order is important, for the correct execution of the program. 
The first ip in the array should be the first machine, and so on...

Example where l115 is the first machine, l116 is the second machine etc...

```
PART_1_MACHINES := l115,l116,l117,l118,l119
```

Once the ip set is done, the user has two options:
+ run the verbose mode, where important information, but not relevant to what was asked, are shown by executing on each corresponding node 
``` 
make tr<machineNumber>Test 
```

+ run with the expected output, where only what was asked is shown, by executing on each corresponding node 
```
make tr<machineNumber>
```
Make file run example: 

+ In node 1, with ip l115, run ```make tr1Test``` 
+ In node 2, with ip l116, run ```make tr2Test```

Finally if not to user sastifaction, its always possible to run manually. Inside the bin folder, you can run ``` java ds.trabalho.part1/TokenRing ```and see the usage i.e. the required options.
Once the program is running, a shell will start, and its possible to run the command register by typing ``` register(<machineName>) ``` to manually connect to another node

You may also want to filter the output by using ``` egrep ``` there are three different types of information available
+ basic info ```[INFO]```
+ erro info ```[ERROR]```
+ expected output ```[STDOUT]```

Manual example:
```
java ds.trabalho.parte1/TokenRing --id 1 --listenPort 5000 | egrep \\[STDOUT\\] 2> stderr.log
```

## Part2
Much like the last section, the user needs to create an ip set with 6 different ips and change the variable PART_2_MACHINES in the makefile

Example where l115 is the first machine, l116 is the second machine etc...
```
PART_2_MACHINES := l115,l116,l117,l118,l119,l120
```

Once the ip set is done, the user has two options:
+ run verbose mode, by executing on each corresponding node 
```
make dic<machineNumber>Test
```
+ normal mode, by executing on each corresponding node 
```
make dic<machineNumber>
```

Example: In node 1, with ip l115, run ```make dic1Tes```, in node 2, with ip l116, run ```make dic2Test``` etc..

Finally if not to user satisfaction, it's always possible to run manually. Inside the bin folder, you can run ``` java ds.trabalho.part2/RedeP2P``` and see the usage i.e. the required options needed
Once the program is running, a shell will start, and its possible to run the command register by typing ``` register(<machineName>) ``` to manually connect to another node

You may also want to filter the output by using ``` egrep ``` there are three different types of information available
+ basic info ```[INFO]```
+ erro info ```[ERROR]```
+ expected output ```[STDOUT]```

## Part3
Much like the previous sections, the user needs to create an ip set with 4 different ips and change the variable PART_3_MACHINES in the makefile


Once the ip set is done, the user has two options:
+ run verbose mode, by executing on each corresponding node 
```
make rtom<machineNumber>Test 
```
+ normal mode, by executing on each corresponding node 
```
make rtom<machineNumber>
```
I assume that the person reading this tutorial is busy, so i made a test to prove synchronization. When an ip set is given, every node running this program will broadcast a message with its epoch time. Do not be mistaken, this program does not use timestamps to synchronize the chat message, but it's useful to see the order of the messages. Here is what I recommend. Run the program in normal mode, then compare the messages received.

Finally if not to user satisfaction, it's always possible to run manually. Inside the bin folder, you can run ``` java ds.trabalho.part3/RTOM``` and see the usage i.e. the required options needed
Once the program is running, a shell will start, and its possible to run the command register by typing ``` register(<machineName>) ``` to manually connect to another node

You may also want to filter the output by using ``` egrep ``` there are three different types of information available
+ basic info ```[INFO]```
+ erro info ```[ERROR]```
+ expected output ```[STDOUT]```


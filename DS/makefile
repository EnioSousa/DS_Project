SRC := src/
BIN := bin/
LOG := log/

PART_1_MACHINES := l116,l117,l118,l119,l120
PART_2_MACHINES := l116,l117,l118,l119,l120,l115
PART_3_MACHINES := l116,l117,l118,l119

PORT := 5000

PACKAGE := ds/trabalho/parte
PARTS := 1 2 3

.PHONY: all checkDir compile run

all: compile

compile: checkDir
	mkdir -p $(BIN) $(LOG)
	for num in $(PARTS); do \
		echo $(PACKAGE)$$num; \
		mkdir -p $(LOG)part$$num; \
		javac -d $(BIN) $(SRC)$(PACKAGE)$$num/*; \
	done

tr1:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 1 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m1.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m1.log
tr2:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 2 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m2.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m2.log
tr3:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 3 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m3.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m3.log
tr4:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 4 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m4.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m4.log
tr5:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 5 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m5.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m5.log

tr1Test:
	cd $(BIN); java $(PACKAGE)1/TokenRing  --id 1 --ipSet $(PART_1_MACHINES) \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m1.log  2> ../$(LOG)part2/stderr.m1.log
tr2Test:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 2 --ipSet $(PART_1_MACHINES)  \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m2.log  2> ../$(LOG)part2/stderr.m2.log
tr3Test:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 3 --ipSet $(PART_1_MACHINES)  \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m3.log  2> ../$(LOG)part2/stderr.m3.log
tr4Test:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 4 --ipSet $(PART_1_MACHINES)  \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m4.log  2> ../$(LOG)part2/stderr.m4.log
tr5Test:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 5 --ipSet $(PART_1_MACHINES)  \ 
	--listenPort $(PORT)| tee ../$(LOG)part2/stdout.m5.log  2> ../$(LOG)part2/stderr.m5.log
	



dic1:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 1 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m1.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m1.log
dic2:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 2 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m2.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m2.log
dic3:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 3 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m3.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m3.log
dic4:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 4 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m4.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m4.log
dic5:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 5 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m5.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m5.log
dic6:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 6 --listenPort $(PORT)  \
	 | tee ../$(LOG)part2/stdout.m6.log | egrep \\[STDOUT\\] 2> ../$(LOG)part2/stderr.m6.log


dic1Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 1 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m1.log  2> ../$(LOG)part2/stderr.m1.log
dic2Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 2 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m2.log  2> ../$(LOG)part2/stderr.m2.log
dic3Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 3 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m3.log  2> ../$(LOG)part2/stderr.m3.log
dic4Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 4 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m4.log  2> ../$(LOG)part2/stderr.m4.log
dic5Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 5 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m5.log  2> ../$(LOG)part2/stderr.m5.log
dic6Test:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 6 --listenPort $(PORT) --ipSet $(PART_2_MACHINES) \
	 | tee ../$(LOG)part2/stdout.m6.log  2> ../$(LOG)part2/stderr.m6.log

# Scripts for part 3 of assignment
rtom1:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 1 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 | tee ../$(LOG)part3/stdout.m1.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m1.log
rtom2:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 2 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 | tee ../$(LOG)part3/stdout.m2.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m2.log
rtom3:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 3 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 | tee ../$(LOG)part3/stdout.m3.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m3.log
rtom4:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 4 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 | tee ../$(LOG)part3/stdout.m4.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m4.log


rtom1Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 1 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 | tee ../$(LOG)part3/stdout.m1.log 2> tee ../$(LOG)part3/stderr.m1.log
rtom2Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 2 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 | tee ../$(LOG)part3/stdout.m2.log 2> tee ../$(LOG)part3/stderr.m2.log
rtom3Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 3 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 | tee ../$(LOG)part3/stdout.m3.log 2> tee ../$(LOG)part3/stderr.m3.log
rtom4Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 4 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 | tee ../$(LOG)part3/stdout.m4.log 2> tee ../$(LOG)part3/stderr.m4.log 

checkDir:
	mkdir -p $(BIN)

clean:
	rm -r $(BIN)
	rm -r $(LOG)

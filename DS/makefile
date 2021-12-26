SRC := ./src/
BIN := ./bin/
LOG := ./log/

PART_3_MACHINES := l116,l117,l118,l119

PORT := $(PORT)

PACKAGE := ds/trabalho/parte
PARTS := 1 2 3

.PHONY: all checkDir compile run

all: compile

compile: checkDir
	mkdir -p $(BIN) $(LOG)
	for num in $(PARTS); do \
		echo $(PACKAGE)$$num; \
		mkdir -p ../$(LOG)part$$num; \
		javac -d $(BIN) $(SRC)$(PACKAGE)$$num/*; \
	done

tr1:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 1 --ip l117 --port $(PORT) --listenPort $(PORT)
tr2:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 2 --ip l118 --port $(PORT) --listenPort $(PORT)
tr3:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 3 --ip l119 --port $(PORT) --listenPort $(PORT)
tr4:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 4 --ip l120 --port $(PORT) --listenPort $(PORT)
tr5:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 5 --ip l116 --port $(PORT) --listenPort $(PORT)

dic1:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 1 --listenPort $(PORT)
dic2:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 2 --listenPort $(PORT)
dic3:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 3 --listenPort $(PORT)
dic4:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 4 --listenPort $(PORT)
dic5:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 5 --listenPort $(PORT)
dic6:
	cd $(BIN); java $(PACKAGE)2/RedeP2P --id 6 --listenPort $(PORT)

# Scripts for part 3 of assignment
rtom1:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 1 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 1> tee ../$(LOG)part3/stdout.m1.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m1.log
rtom2:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 2 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 1> tee ../$(LOG)part3/stdout.m2.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m2.log
rtom3:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 3 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 1> tee ../$(LOG)part3/stdout.m3.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m3.log
rtom4:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 4 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 1> tee ../$(LOG)part3/stdout.m4.log | egrep \\[STDOUT\\] 2> ../$(LOG)part3/stderr.m4.log


rtom1Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 1 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 1> tee ../$(LOG)part3/stdout.m1.log 2> tee ../$(LOG)part3/stderr.m1.log
rtom2Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 2 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 1> tee ../$(LOG)part3/stdout.m2.log 2> tee ../$(LOG)part3/stderr.m2.log
rtom3Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 3 --listenPort $(PORT) --ipSet $(PART_3_MACHINES)  \
	 1> tee ../$(LOG)part3/stdout.m3.log 2> tee ../$(LOG)part3/stderr.m3.log
rtom4Test:
	cd $(BIN); java $(PACKAGE)3/RTOM --id 4 --listenPort $(PORT) --ipSet $(PART_3_MACHINES) \
	 1> tee ../$(LOG)part3/stdout.m4.log 2> tee ../$(LOG)part3/stderr.m4.log 

checkDir:
	mkdir -p $(BIN)

clean:
	rm -r $(BIN)
	rm -r $(LOG)

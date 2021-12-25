SRC := ./src/
BIN := ./bin/

PACKAGE := ds/trabalho/parte
PARTS := 1 2 3

.PHONY: all checkDir compile run

all: compile

compile: checkDir
	mkdir -p $(BIN)
	for num in $(PARTS); do \
		echo $(PACKAGE)$$num; \
		javac -d $(BIN) $(SRC)$(PACKAGE)$$num/*; \
	done

tr0:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 1 --ip l117 --port 5000 --listenPort 5000
tr1:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 2 --ip l118 --port 5000 --listenPort 5000
tr2:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 3 --ip l119 --port 5000 --listenPort 5000
tr3:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 4 --ip l120 --port 5000 --listenPort 5000
tr4:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 5 --ip l116 --port 5000 --listenPort 5000

dic0:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 1 --listenPort 5000
dic1:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 2 --listenPort 5000
dic2:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 3 --listenPort 5000
dic3:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 4 --listenPort 5000
dic4:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 5 --listenPort 5000
dic5:
	cd $(BIN); java $(PACKAGE)3/RedeP2P --id 6 --listenPort 5000

checkDir:
	mkdir -p $(BIN)

clean:
	rm -r $(BIN)

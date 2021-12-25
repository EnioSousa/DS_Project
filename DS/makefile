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
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 0 --ip l117 --port 5000 --listenPort 5000
tr1:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 1 --ip l118 --port 5000 --listenPort 5000
tr2:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 2 --ip l119 --port 5000 --listenPort 5000
tr3:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 3 --ip l120 --port 5000 --listenPort 5000
tr4:
	cd $(BIN); java $(PACKAGE)1/TokenRing --id 4 --ip l116 --port 5000 --listenPort 5000

checkDir:
	mkdir -p $(BIN)

clean:
	rm -r $(BIN)

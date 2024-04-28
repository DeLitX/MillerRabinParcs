all: run

clean:
	rm -f out/*.jar

out/MillerRabinTask.jar: out/parcs.jar src/MillerRabinTask.java
	@javac -cp out/parcs.jar --release 11 src/MillerRabinTask.java
	@jar cf out/MillerRabinTask.jar -C src MillerRabinTask.class
	@rm -f src/MillerRabinTask.class

out/MillerRabinHost.jar: out/parcs.jar src/MillerRabinHost.java
	@javac -cp out/parcs.jar --release 11 src/MillerRabinHost.java
	@jar cf out/MillerRabinHost.jar -C src MillerRabinHost.class
	@rm -f src/MillerRabinHost.class

build: out/MillerRabinTask.jar out/MillerRabinHost.jar

run: out/MillerRabinHost.jar out/MillerRabinTask.jar
	@cd out && java -cp "MillerRabinHost.jar:parcs.jar" MillerRabinHost

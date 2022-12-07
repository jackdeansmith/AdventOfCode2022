all: day5.jar

day5.jar : day5.kt
	kotlinc day5.kt -include-runtime -d day5.jar

run: day5.jar
	java -jar day5.jar input.txt

stacks: day5.jar
	java -jar day5.jar stackparse.txt

test: day5.jar
	java -jar day5.jar testinput.txt

clean:
	rm -f day5.jar
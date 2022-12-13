all: day10.jar

day10.jar : day10.kt
	kotlinc day10.kt -include-runtime -d day10.jar

run: day10.jar
	java -jar day10.jar input.txt

test: day10.jar
	java -jar day10.jar testinput.txt

clean:
	rm -f day10.jar
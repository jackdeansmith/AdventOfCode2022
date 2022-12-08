all: day9.jar

day9.jar : day9.kt
	kotlinc day9.kt -include-runtime -d day9.jar

run: day9.jar
	java -jar day9.jar input.txt

test: day9.jar
	java -jar day9.jar testinput.txt

clean:
	rm -f day9.jar
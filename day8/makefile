all: day8.jar

day8.jar : day8.kt
	kotlinc day8.kt -include-runtime -d day8.jar

run: day8.jar
	java -jar day8.jar input.txt

test: day8.jar
	java -jar day8.jar testinput.txt

clean:
	rm -f day8.jar
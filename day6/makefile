all: day6.jar

day6.jar : day6.kt
	kotlinc day6.kt -include-runtime -d day6.jar

run: day6.jar
	java -jar day6.jar input.txt

test: day6.jar
	java -jar day6.jar testinput.txt

clean:
	rm -f day6.jar
all: day3.jar

day3.jar : day3.kt
	kotlinc day3.kt -include-runtime -d day3.jar

run: day3.jar
	java -jar day3.jar input.txt

clean:
	rm -f day3.jar
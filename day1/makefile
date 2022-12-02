all: day1.jar

day1.jar : day1.kt
	kotlinc day1.kt -include-runtime -d day1.jar

run: day1.jar
	java -jar day1.jar input.txt

clean:
	rm -f day1.jar
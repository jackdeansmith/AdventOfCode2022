all: day2.jar

day2.jar : day2.kt
	kotlinc day2.kt -include-runtime -d day2.jar

run: day2.jar
	java -jar day2.jar input.txt

clean:
	rm -f day2.jar
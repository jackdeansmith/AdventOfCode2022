all: day4.jar

day4.jar : day4.kt
	kotlinc day4.kt -include-runtime -d day4.jar

run: day4.jar
	java -jar day4.jar input.txt

clean:
	rm -f day4.jar
all: day7.jar

day7.jar : day7.kt
	kotlinc day7.kt -include-runtime -d day7.jar

run: day7.jar
	java -jar day7.jar input.txt

test: day7.jar
	java -jar day7.jar testinput.txt

clean:
	rm -f day7.jar
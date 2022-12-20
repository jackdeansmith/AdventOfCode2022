all: solution.jar

solution.jar : solution.kt
	kotlinc solution.kt -include-runtime -d solution.jar

run: solution.jar
	java -jar solution.jar input.txt

test: solution.jar
	java -jar solution.jar testinput.txt

clean:
	rm -f solution.jar
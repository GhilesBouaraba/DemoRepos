javac src/main/bakery/*.java src/main/util/*.java src/main/javafx.java -d ./bin/ --module-path ./lib --add-modules=javafx.controls
java --class-path ./bin/ javafx


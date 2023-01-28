# fruit-store

This project simulates the management of a small fruit store.

Tecnologies used:
* Spring Boot 2
* Maven
* Java 11

The project reads the list of fruits and their prices and the purchase list in the files <b>fruit.txt</b> and <b>purchase.txt</b> respectively, located in the <b>src/main/resources</b> folder inside the project.

The project can be compiled with the maven tool by executing the command:
* mvn paclage

In this way, a .jar file is generated in the target directory, which can be executed by opening a terminal in that path and executing the following command:
* java -jar fruit-store-0.0.1-SNAPSHOT.jar

The project can also be run directly with maven with the following command:
* mvn spring-boot:run

When executed, the project prints in the console the result of calculating the total price of the order defined in purchase.txt, according to the prices configured in fruit.txt and applying the offers defined in the code.

package com.asaitec.fruitstore;

import com.asaitec.fruitstore.service.FruitService;
import com.asaitec.fruitstore.service.LoadDataComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@Log4j2
@SpringBootApplication
@RequiredArgsConstructor
public class FruitStoreApplication implements CommandLineRunner {

    private final FruitService fruitService;

    public static void main(String[] args) {
        log.info("STARTING THE FRUIT STORE APPLICATION");
        SpringApplication.run(FruitStoreApplication.class, args);
        log.info("FRUIT STORE APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");

        fruitService.processPurchase();
    }

}

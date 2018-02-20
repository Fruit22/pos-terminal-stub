package ru.bmstu.posterminalstub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class PosTerminalStubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosTerminalStubApplication.class, args);
    }
}


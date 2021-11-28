package com.notayessir;

import com.notayessir.common.spring.registrar.EnableMars;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableMars
@SpringBootApplication
public class ConsumerApplication {



    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}

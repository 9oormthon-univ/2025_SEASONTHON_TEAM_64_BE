package org.goormthon.seasonthon.nocheongmaru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NocheongmaruApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NocheongmaruApplication.class, args);
    }
    
}

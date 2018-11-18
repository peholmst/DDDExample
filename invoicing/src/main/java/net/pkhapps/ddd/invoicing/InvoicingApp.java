package net.pkhapps.ddd.invoicing;

import net.pkhapps.ddd.shared.SharedConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@Import(SharedConfiguration.class)
public class InvoicingApp {

    public static void main(String[] args) {
        SpringApplication.run(InvoicingApp.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}

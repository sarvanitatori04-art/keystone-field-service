package com.zidio.keystone;

import com.zidio.keystone.model.WorkOrder;
import com.zidio.keystone.repository.WorkOrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class KeystoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeystoneApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(WorkOrderRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new WorkOrder("WO-101", "HVAC Compressor Failure - Floor 4", "HIGH", "NEW", LocalDateTime.now().plusHours(3)));
                repo.save(new WorkOrder("WO-102", "Emergency Plumbing Leak in Main Restroom", "CRITICAL", "ASSIGNED", LocalDateTime.now().plusHours(1)));
                repo.save(new WorkOrder("WO-103", "Replace Main Circuit Breaker Panel", "MEDIUM", "IN_PROGRESS", LocalDateTime.now().plusHours(8)));
                repo.save(new WorkOrder("WO-104", "Quarterly Chiller Routine Service", "LOW", "COMPLETED", LocalDateTime.now().plusDays(2)));
            }
        };
    }
}
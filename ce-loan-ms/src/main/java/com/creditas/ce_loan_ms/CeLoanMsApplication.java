package com.creditas.ce_loan_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CeLoanMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CeLoanMsApplication.class, args);
    }

}

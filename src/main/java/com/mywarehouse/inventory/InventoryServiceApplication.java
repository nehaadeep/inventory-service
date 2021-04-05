package com.mywarehouse.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @description: Inventory service main application
 * @author: Suresh Vannale
 */

@SpringBootApplication
public class InventoryServiceApplication extends SpringBootServletInitializer {

    @Generated
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1000000);
        filter.setIncludeHeaders(false);
        filter.setIncludeClientInfo(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}

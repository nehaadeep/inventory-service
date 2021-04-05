package com.mywarehouse.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: Swagger Auto configuration
 * @author: Suresh Vannale
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Collections.singletonList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.mywarehouse.inventory"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo()).produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Warehouse service")
                .description("Inventory management system where you can buy products")
                .contact(new Contact("Suresh", "www.mywarehouse.com", "hello@hello.com"))
                .license("MyWarehouse 1.0.0")
                .licenseUrl("www.mywarehouse.se")
                .version("1.0.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}

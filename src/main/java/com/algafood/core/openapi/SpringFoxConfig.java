package com.algafood.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    // gera um json que irá gerar a documentação
    // GET hhtp://localhost:8080/v3/api-docs
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.any())
            .build();
    }

    // para visualizar a documentação da api
    // http://localhost:8080/swagger-ui/index.html
}

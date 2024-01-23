package com.algafood;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TesteApiCadastroCozinhaTestsIT {

    @LocalServerPort
    private int port;
    
    @Test
    void retornaStatus200_QuandoConsultaCozinha() {

        RestAssured
            .enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured
            .given()
                .basePath("/cozinhas")
                .port(port)
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(200);
    }
}

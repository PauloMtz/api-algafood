package com.algafood;

import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TesteApiCadastroCozinhaTestsIT {

    @LocalServerPort
    private int port;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {

        RestAssured
            .enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        flyway.migrate();
    }
    
    @Test
    void retornaStatus200_QuandoConsultaCozinha() {

        RestAssured
            .given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(200);
    }

    @Test
    void retornaRegistrosEspecificos_QuandoConsultaCozinha() {

        RestAssured
            .given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .body("", Matchers.hasSize(4))
                .body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
    }

    @Test
    void retornaStatus201_QuandoCadastraCozinha() {

        RestAssured
            .given()
                .body("{ \"nome\": \"Chinesa\" }")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .post()
            .then()
                .statusCode(HttpStatus.CREATED.value());
    }
}

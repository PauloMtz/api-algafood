package com.algafood;

import static org.hamcrest.Matchers.equalTo;

//import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;
import com.algafood.util.DatabaseCleaner;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

// arquivo dentro da pasta resources
@TestPropertySource("/application-tests.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TesteApiCadastroCozinhaTestsIT {

    @LocalServerPort
    private int port;

    /*@Autowired
    private Flyway flyway;*/

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @BeforeEach
    public void setUp() {

        RestAssured
            .enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        //flyway.migrate();
        databaseCleaner.clearTables();
        prepararDados();
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
                .body("", Matchers.hasSize(2))
                .body("nome", Matchers.hasItems("Americana", "Tailandesa"));
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

    @Test
    void retornaStatusCorreto_QuandoConsultaCozinhaExistente() {

        RestAssured
            .given()
                .pathParam("cozinhaId", 2)
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId}")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Americana"));
    }

    @Test
    void retornaStatus404_QuandoConsultaCozinhaInexistente() {

        RestAssured
            .given()
                .pathParam("cozinhaId", 20)
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId}")
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Americana");
		cozinhaRepository.save(cozinha2);
	}
}

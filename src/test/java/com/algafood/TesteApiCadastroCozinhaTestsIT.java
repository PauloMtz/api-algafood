package com.algafood;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

//import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;
import com.algafood.util.DatabaseCleaner;
import com.algafood.util.ResourceUtils;

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

    private static final int COZINHA_ID_INEXISTENTE = 100;

    @LocalServerPort
    private int port;

    /*@Autowired
    private Flyway flyway;*/

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;

    @BeforeEach
    public void setUp() {

        RestAssured
            .enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/cozinha-chinesa.json");

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
    public void retornaQuantidadeDeCozinhas_QuandoConsultaCozinhas() {

        RestAssured
            .given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .body("", hasSize(quantidadeCozinhasCadastradas));
    }

    @Test
    void retornaStatus201_QuandoCadastraCozinha() {

        RestAssured
            .given()
                .body(jsonCorretoCozinhaChinesa)
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
                .pathParam("cozinhaId", cozinhaAmericana.getId())
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId}")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(cozinhaAmericana.getNome()));
    }

    @Test
    void retornaStatus404_QuandoConsultaCozinhaInexistente() {

        RestAssured
            .given()
                .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId}")
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);

        cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);
        
        quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
}

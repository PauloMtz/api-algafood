package com.algafood.api.controller;

import java.nio.file.Path;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.model.inputDto.ProdutoUploadInputDto;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/imagem")
public class ProdutoUploadController {
    
    // http://localhost:8080/restaurantes/6/produtos/10/imagem
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void atualizaImagem(@PathVariable Long restauranteId,
        @PathVariable Long produtoId, @Valid ProdutoUploadInputDto produtoUploadInputDto) {

        var nomeArquivo = UUID.randomUUID().toString()
            + "_" + produtoUploadInputDto.getImagem().getOriginalFilename();

        var arquivoImagem = 
            Path.of("C:/Users/paulo/Downloads/desenv/api-algafood-imagens", nomeArquivo);

        System.out.println("\n>>> Nome inicial da imagem: "+ produtoUploadInputDto.getDescricao());
        System.out.println(">>> Nome da imagem depois do upload: "+ nomeArquivo);
        System.out.println(">>> Local da imagem: " + arquivoImagem + "\n");

        try {
            produtoUploadInputDto.getImagem().transferTo(arquivoImagem);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

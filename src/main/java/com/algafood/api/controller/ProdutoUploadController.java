package com.algafood.api.controller;

import java.io.IOException;

//import java.nio.file.Path;
//import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algafood.api.assembler.FotoProdutoDtoAssembler;
import com.algafood.api.model.dto.ProdutoFotoDto;
import com.algafood.api.model.inputDto.ProdutoUploadInputDto;
import com.algafood.domain.model.FotoProduto;
import com.algafood.domain.model.Produto;
import com.algafood.domain.service.ProdutoService;
import com.algafood.domain.service.ProdutoUploadService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/imagem")
public class ProdutoUploadController {

    @Autowired
    private ProdutoUploadService produtoUploadService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FotoProdutoDtoAssembler assembler;
    
    // http://localhost:8080/restaurantes/6/produtos/10/imagem
    /*@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    }*/

    // http://localhost:8080/restaurantes/6/produtos/10/imagem
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProdutoFotoDto atualizaImagem(@PathVariable Long restauranteId,
        @PathVariable Long produtoId, @Valid ProdutoUploadInputDto produtoUploadInputDto) throws IOException {

        Produto produto = produtoService.buscar(restauranteId, produtoId);
        MultipartFile arquivoFoto = produtoUploadInputDto.getImagem();
        
        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(produtoUploadInputDto.getDescricao());
        fotoProduto.setContentType(arquivoFoto.getContentType());
        fotoProduto.setTamanho(arquivoFoto.getSize());
        fotoProduto.setNomeArquivo(arquivoFoto.getOriginalFilename());

        FotoProduto foto = produtoUploadService.salvar(fotoProduto, arquivoFoto.getInputStream());

        return assembler.convertToDto(foto);
    }

    @GetMapping
    public ProdutoFotoDto buscar(@PathVariable Long restauranteId, 
        @PathVariable Long produtoId) {

        FotoProduto fotoProduto = produtoUploadService.buscar(restauranteId, produtoId);
        
        return assembler.convertToDto(fotoProduto);
    }
}

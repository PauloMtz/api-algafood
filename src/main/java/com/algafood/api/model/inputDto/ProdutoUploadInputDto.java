package com.algafood.api.model.inputDto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUploadInputDto {
    
    private MultipartFile imagem;
    private String descricao;
}

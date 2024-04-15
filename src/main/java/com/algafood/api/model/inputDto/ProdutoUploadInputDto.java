package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.algafood.core.validation.AnotacaoCriadaFileSize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUploadInputDto {
    
    @NotNull
    @AnotacaoCriadaFileSize(tamanhoMaximo = "5MB")
    private MultipartFile imagem;

    @NotBlank
    private String descricao;
}

package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.algafood.core.validation.AnotacaoCriadaFileSize;
import com.algafood.core.validation.FileContentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUploadInputDto {
    
    @NotNull
    @AnotacaoCriadaFileSize(tamanhoMaximo = "5MB")
    @FileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    private MultipartFile imagem;

    @NotBlank
    private String descricao;
}

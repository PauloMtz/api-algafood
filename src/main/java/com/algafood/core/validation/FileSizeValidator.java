package com.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<
    AnotacaoCriadaFileSize, MultipartFile> {

    private DataSize maxSize;

    @Override
    public void initialize(AnotacaoCriadaFileSize constraintAnnotation) {
        this.maxSize = DataSize.parse(constraintAnnotation.tamanhoMaximo());
    }
    
    @Override
    public boolean isValid(MultipartFile value, 
        ConstraintValidatorContext context) {

        // se o arquivo == null, ou seja, não foi enviado
        // ou, o tamanho é menor ou igual ao tamanho máximo
        return value == null || value.getSize() <= this.maxSize.toBytes();
    }
    
}

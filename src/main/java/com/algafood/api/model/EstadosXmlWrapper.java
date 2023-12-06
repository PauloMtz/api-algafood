package com.algafood.api.model;

import java.util.List;

import com.algafood.domain.model.Estado;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.NonNull;

@JacksonXmlRootElement(localName = "Estados")
@Data
public class EstadosXmlWrapper {
    
    @JacksonXmlProperty(localName = "estado")
    // remove um componente que empacota outros
    @JacksonXmlElementWrapper(useWrapping = false)
    @NonNull
    private List<Estado> estados;
}

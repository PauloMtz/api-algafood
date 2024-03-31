package com.algafood.core.squiggly;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

@Configuration
public class SquigglyConfig {
    
    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyFiler(ObjectMapper mapper) {

        Squiggly.init(mapper, new RequestSquigglyContextProvider());

        // se quiser trocar o nome padrão 'fields' para 'campos', substituir construtor acima
        //Squiggly.init(mapper, new RequestSquigglyContextProvider("campos", null));
        // http://localhost:8080/pedidos?campos=codigo,valorTotal

        // esse filtro fica disponível para todos os recursos
        // mas, se quiser restringir a apenas algumas URLs
        var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*");

        var registroFiltro = new FilterRegistrationBean<SquigglyRequestFilter>();
        registroFiltro.setFilter(new SquigglyRequestFilter());
        registroFiltro.setOrder(1);
        // restrição a apenas algumas URLs
        registroFiltro.setUrlPatterns(urlPatterns);

        return registroFiltro;
    }

    // Exemplos de pesquisa:
    // http://localhost:8080/pedidos?fields=codigo,valorTotal,status,sub*,cliente
    // http://localhost:8080/pedidos?fields=codigo,valorTotal,status,sub*,cliente.id
    // http://localhost:8080/pedidos?fields=codigo,valorTotal,status,sub*,cliente[id,nome]
    // http://localhost:8080/pedidos?fields=codigo,valorTotal,status,sub*,cliente[-id]
    // http://localhost:8080/pedidos?fields=-codigo,-restaurante,-cliente,-dataCriacao,-status
    // ...
    // se não aceitar os colchetes, retire-os e os substitua por %5B e %5D [abertura e fechamento]
    // ou, implemente a classe TomcatCustomizer para forçar a aceitação
    // nesse código, aceitou com e sem ajustes indicados
    // http://localhost:8080/pedidos?fields=codigo,valorTotal,status,sub*,cliente%5Bid,nome%5D

    // além disso, acrescentar essa biblioteca no arquivo pom.xml
    /*
        <dependency>
			<groupId>com.github.bohnman</groupId>
			<artifactId>squiggly-filter-jackson</artifactId>
			<version>1.3.18</version>
		</dependency>
    */
}

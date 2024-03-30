package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.model.Pedido;

@Service
public class StatusPedidoService {

    @Autowired
    private PedidoService pedidoService;
    
    @Transactional
    public void confirmar(String codigoPedido) {
        
        Pedido pedido = pedidoService.buscar(codigoPedido);
        pedido.confirmarPedido();
    }

    @Transactional
    public void entregar(String codigoPedido) {

        Pedido pedido = pedidoService.buscar(codigoPedido);
        pedido.entregarPedido();
    }

    @Transactional
    public void cancelar(String codigoPedido) {
        
        Pedido pedido = pedidoService.buscar(codigoPedido);
        pedido.cancelarPedido();
    }
}

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
    public void confirmar(Long pedidoId) {
        
        Pedido pedido = pedidoService.buscar(pedidoId);
        pedido.confirmarPedido();
    }

    @Transactional
    public void entregar(Long pedidoId) {

        Pedido pedido = pedidoService.buscar(pedidoId);
        pedido.entregarPedido();
    }

    @Transactional
    public void cancelar(Long pedidoId) {
        
        Pedido pedido = pedidoService.buscar(pedidoId);
        pedido.cancelarPedido();
    }
}

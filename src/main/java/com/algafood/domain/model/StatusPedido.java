package com.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
    
    CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO),// atual e anterior
	ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO, CONFIRMADO);

	private String descricao;
	private List<StatusPedido> statusAnteriores;

	// o construtor passa a receber um var args (uma lista)
	StatusPedido(String descricao, StatusPedido... statusAnteriores) {
		this.descricao = descricao;
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}

	public String getDescricao() {
		return this.descricao;
	}

	public boolean naoPodeAlterarStatus(StatusPedido novoStatus) {
		// se o novo status não estiver na lista (var args), chama esse método
		return !novoStatus.statusAnteriores.contains(this);
	}
}

package com.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {
    
    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
	private BigDecimal subtotal;

    @Column(nullable = false)
	private BigDecimal taxaFrete;

	@Column(nullable = false)
	private BigDecimal valorTotal;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime  dataCriacao;

	@CreationTimestamp
	@Column(columnDefinition = "datetime")
	private OffsetDateTime  dataConfirmacao;

	@CreationTimestamp
	@Column(columnDefinition = "datetime")
	private OffsetDateTime  dataCancelamento;

	@CreationTimestamp
	@Column(columnDefinition = "datetime")
	private OffsetDateTime  dataEntrega;

    @Embedded
	private Endereco enderecoEntrega;

	@Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;

    @ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;
	
	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens = new ArrayList<>();

	public void calcularValorTotal() {
		this.subtotal = getItens().stream()
			.map(item -> item.getPrecoTotal())
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void definirFrete() {
		setTaxaFrete(getRestaurante().getTaxaFrete());
	}
	
	public void atribuirPedidoAosItens() {
		getItens().forEach(item -> item.setPedido(this));
	}
}

package com.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
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
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;

import com.algafood.domain.exception.NegocioException;

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
	private String codigo;

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
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();

	public void calcularValorTotal() {

		getItens().forEach(ItemPedido::calcularPrecoTotal);

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

	// os status serão alterados aqui
	public void confirmarPedido() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
	}

	public void entregarPedido() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}

	public void cancelarPedido() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
	}

	// altera o status
	private void setStatus(StatusPedido novoStatus) {

		// verifica se pode alterar
		// chama o método de StatusPedido e verifica se tem o status na lista
		if (getStatus().naoPodeAlterarStatus(novoStatus)) {
			throw new NegocioException(String.format(
                "Status do pedido %s não pode ser alterado de %s para %s",
                getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
		}

		// se puder, altera
		this.status = novoStatus;
	}

	// a instrução pré-persist vai dizer para o Spring para executar
	// esse método gerarCodigo() antes de persistir uma entidade
	// é um método de callback da JPA
	@PrePersist
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
}

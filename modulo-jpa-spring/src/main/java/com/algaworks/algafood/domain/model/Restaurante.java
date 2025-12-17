package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
	/*
	 	por padrao tudo que termina com to one por padrao e eager -> busca ansiosamente(vai dar varios selects ao mesmo tempo)

		tudo que termina com to many tem por padrao e lazy -> a busca so e feita quando e chamada(vai dar varios selects quando for chamado ao mesmo tempo)
		
		O recomendado é deixar tudo como LAZY e usar FETCH JOIN(só da um select) para otimizar as consultas. para evitar o erro do N+1
	 */

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	//@JsonIgnoreProperties("hibernateLazyInitializer") hibernateLazyInitializer -> É um objeto interno do Hibernate que controla o lazy loading.
	@ManyToOne(fetch = FetchType.LAZY)//fetch -> muda estrategia de fetch(carregamento)
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;
	
	@JsonIgnore
	@Embedded//para vincular uma classe embeddlabe
	private Endereco endereco;
	
	@JsonIgnore
	@CreationTimestamp//quando o obj for criada e passado a data da hr para o atributo
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	@UpdateTimestamp//toda vez que obj for alterado ira passar uma nova data+hora automatica
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataAtualizacao;
	
	@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "restaurante_forma_pagamento",//nomei o nome  da tabela que será criada
			joinColumns  = @JoinColumn(name = "restaurante_id"),//nomeia a referencia do restaurnate na nova tabela
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id")//nomeia a referencia do  FormaPagamento na nova tabela
			)
	private List<FormaPagamento> formaPagamentos = new ArrayList<>();
}

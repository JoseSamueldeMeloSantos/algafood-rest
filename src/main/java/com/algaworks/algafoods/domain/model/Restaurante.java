package com.algaworks.algafoods.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 30)
	private String nome;
	
	@Column(name = " taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@ManyToOne//define a relação
	@JoinColumn(name = "cozinha_id", nullable = false)//para definir o nome da coluna da cozinha(parecida com o @Column)
	private Cozinha cozinha;
	
	@ManyToOne
	@JoinColumn(name = "forma_pagamento_id")
	private FormaPagamento formaPagamento;
}

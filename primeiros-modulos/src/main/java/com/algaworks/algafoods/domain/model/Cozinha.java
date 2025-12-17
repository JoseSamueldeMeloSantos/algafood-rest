package com.algaworks.algafoods.domain.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@JsonRootName("cozinha")//define o nome root do obj  na resposta json
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)//manda criar o equals e hashcode apenas com os atributos q vc incluir
@Entity
@Table(name = "tab_cozinhas")//se não colocar nada o nome da tabela sera o nome da classe
public class Cozinha {
	
		@EqualsAndHashCode.Include//inclui o atributo
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)//identity deixa o banco responsavel pela geracao do id
		private Long id;

		//@JsonProperty("titulo")//muda o nome da propriedade no json
		@JsonIgnore//não manda a propriedade para o json
		@Column(name = "nom_cozinha", length = 30)
		private String nome;
}

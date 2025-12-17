package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Restaurante> find(String nome, 
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		// CriteriaBuilder → fábrica para criar componentes da Criteria API
		var builder = manager.getCriteriaBuilder();
		
		// CriteriaQuery → representa a consulta (qual entidade será retornada)
		// Aqui informamos que o resultado será Restaurante.class
		var criteria = builder.createQuery(Restaurante.class);
		
		// Root → representa a entidade raiz da consulta (FROM Restaurante)
		var root = criteria.from(Restaurante.class);

		// Lista de Predicates → armazena condições para o WHERE
		var predicates = new ArrayList<Predicate>();
		
		// Se o nome tiver texto, cria um predicado LIKE (busca por parte do nome)
		if (StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		
		// Se taxaFreteInicial for informada:
		// cria condição taxaFrete >= taxaFreteInicial
		if (taxaFreteInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		
		// Se taxaFreteFinal for informada:
		// cria condição taxaFrete <= taxaFreteFinal
		if (taxaFreteFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
		// Aplica todos os predicados no WHERE
		// (funciona como um AND entre todas as condições)
		criteria.where(predicates.toArray(new Predicate[0]));
		
		// Cria a query a partir da CriteriaQuery
		var query = manager.createQuery(criteria);
		
		// Executa e retorna o resultado
		return query.getResultList();
	}
	
}
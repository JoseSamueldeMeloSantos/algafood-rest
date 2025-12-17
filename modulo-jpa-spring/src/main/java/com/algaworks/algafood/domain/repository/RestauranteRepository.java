package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, JpaSpecificationExecutor<Restaurante>, RestauranteRepositoryQueries {

	/*
find, query, read, get, search = acha pelo parametro

Containing = faz um LIKE '%valor%' no banco.
Ou seja: contém o texto informado.
Exemplo: findByNomeContaining("bar") → nome LIKE '%bar%'
	
Between = verifica se um valor está ENTRE dois limites.
Exemplo: findByTaxaFreteBetween(5, 10) → taxa >= 5 AND taxa <= 10

And = junta duas condições ao mesmo tempo.
Exemplo: findByNomeContainingAndCozinhaId → (nome LIKE ...) AND (cozinha_id = ...)

First / Top = limita o número de resultados retornados.
First = retorna só o primeiro
Top2 = retorna os dois primeiros
Top10 = os dez primeiros etc.

CountBy = retorna a QUANTIDADE de registros que atendem ao filtro.
Não retorna lista, só um número.


	 */
	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
	
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);
	
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	int countByCozinhaId(Long cozinha);
	
	@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")//para fazer consulta personalizada via jpql
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);//o @param e usando para especificar o atributo que e usado no jpql quando os nomes sao diferentes

	// Usando JOIN FETCH para evitar o problema do N+1:
	// - join fetch r.cozinha: carrega a cozinha junto com o restaurante (ManyToOne sempre existe)
	// - left join fetch r.formasPagamento: carrega as formas de pagamento sem excluir restaurantes sem formas de pagamento
	// Resultado: tudo é carregado em uma única query, evitando várias consultas extras do Hibernate.

		// Errata: se um restaurante não tiver nenhuma forma de pagamento associada a ele,
		// esse restaurante não será retornado usando JOIN FETCH r.formasPagamento.
		// Para resolver isso, temos que usar LEFT JOIN FETCH r.formasPagamento
		//	@Query("from Restaurante r join fetch r.cozinha join fetch r.formasPagamento")
		@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
		List<Restaurante> findAll();

		
		/*
		 Sem fetch:

			SELECT * FROM restaurante;
			
			SELECT * FROM cozinha WHERE id = ?
			SELECT * FROM forma_pagamento WHERE restaurante_id = ?
			SELECT * FROM forma_pagamento WHERE restaurante_id = ?
			SELECT * FROM forma_pagamento WHERE restaurante_id = ?
			...
			
			
		Com fetch join:
			
			SELECT * 
			FROM restaurante r
			JOIN cozinha c
			LEFT JOIN forma_pagamento fp
		 */
		
}

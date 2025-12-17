package com.algaworks.algafoods.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.algaworks.algafoods.domain.model.Cozinha;

@Component
public class CadastroCozinha {
	
	@PersistenceContext
	private EntityManager manager;

	public List<Cozinha> listar() {
	
		TypedQuery<Cozinha> query =  manager.createQuery("from cozinha", Cozinha.class);//faz uma consulta entre objs
		
		return query.getResultList();
	}
	
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);//para buscar uma classe pelo id
	}
	
	
	@Transactional//usado quando e feito uma modificação direta no banco de dados
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);//para adicionar um obj ou atualizar(caso o id seja o mesmo de algum ja existente)
	}
	
	@Transactional
	public void remover(Cozinha cozinha) {
		cozinha = buscar(cozinha.getId());
		manager.remove(cozinha);//obj é precisso tá no estado managed para remover
	}
}

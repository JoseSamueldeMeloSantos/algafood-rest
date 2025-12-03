package com.algaworks.algafoods.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.algaworks.algafoods.domain.model.Cozinha;
import com.algaworks.algafoods.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Cozinha> listar() {
		TypedQuery<Cozinha> query =  manager.createQuery("from cozinha", Cozinha.class);
		
		return query.getResultList();
	}

	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}

	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}

	@Transactional
	@Override
	public void remover(Long cozinhaId) {
		Cozinha cozinha = buscar(cozinhaId);
		
		if (cozinha == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		manager.remove(cozinha);
	}

}

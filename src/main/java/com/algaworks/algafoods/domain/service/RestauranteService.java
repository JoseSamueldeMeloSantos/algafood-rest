package com.algaworks.algafoods.domain.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.algaworks.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoods.domain.exception.ParametroInvalidoException;
import com.algaworks.algafoods.domain.model.Cozinha;
import com.algaworks.algafoods.domain.model.Restaurante;
import com.algaworks.algafoods.domain.repository.CozinhaRepository;
import com.algaworks.algafoods.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository repository;
	@Autowired
	private CozinhaRepository cozinhaRepository;

	public List<Restaurante> listar() {
		return repository.listar();
	}
	
	public Restaurante buscar(Long restauranteId) {
		Restaurante restaurante = repository.buscar(restauranteId);
		
		if (restaurante != null) {
			return restaurante;
		}
		
		throw new EntidadeNaoEncontradaException("Imagem não existe no banco");
	}
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId); // usa apenas o id da cozinha para cadastrar
		
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
				String.format("Não existe cadastro de cozinha com código %d", cozinhaId));
		}
		
		restaurante.setCozinha(cozinha);
		
		return repository.salvar(restaurante);
	}

	public Restaurante atualizar(Long restauranteId, Restaurante restaurante) {
		
		Cozinha cozinha = cozinhaRepository.buscar(restaurante.getCozinha().getId());
		
		if (cozinha ==  null) throw new ParametroInvalidoException("Cozinha invalida");
		
		restaurante.setCozinha(cozinha);
		
		Restaurante restauranteAtual = repository.buscar(restauranteId);
		
		if (restauranteAtual == null) throw new EntidadeNaoEncontradaException("Restaurante não presente no banco");
		
		BeanUtils.copyProperties(restaurante, restauranteAtual,"id");
		
		repository.salvar(restauranteAtual);
		
		return restauranteAtual;
	}

}

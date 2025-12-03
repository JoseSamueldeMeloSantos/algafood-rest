package com.algaworks.algafoods.domain.repository;

import java.util.List;

import com.algaworks.algafoods.domain.model.Estado;

public interface EstadoRepository {

	List<Estado> listar();
	Estado buscar(Long id);
	Estado salvar(Estado estado);
	void remover(Long estadoId);
	
}

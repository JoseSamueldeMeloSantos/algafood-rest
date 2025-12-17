package com.algaworks.algafoods.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoods.domain.exception.ParametroInvalidoException;
import com.algaworks.algafoods.domain.model.Cozinha;
import com.algaworks.algafoods.domain.model.Restaurante;
import com.algaworks.algafoods.domain.repository.RestauranteRepository;
import com.algaworks.algafoods.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	
	@Autowired
	private RestauranteService service;
	@Autowired
	private RestauranteRepository repository;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<Restaurante> listar() {
		return service.listar();
	}
	
	@GetMapping("/{restauranteId")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		try {
			Restaurante restaurante = service.buscar(restauranteId);
			return ResponseEntity.ok(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = service.salvar(restaurante);
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(
			@PathVariable Long restauranteId,
			@RequestBody Restaurante restaurante) {

		try {
			Restaurante restauranteSalvo = service.atualizar(restauranteId, restaurante);
			
			return ResponseEntity.ok(restauranteSalvo);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		} catch (ParametroInvalidoException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	
	/*
	 *  Map<String, Object> campos  e igual no json
	 *   
	 *  {
	 *  	"string": objeto
	 *  }
	 */
	@PatchMapping
	public ResponseEntity<?> atualizarParcial(
			@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {
		
		Restaurante restauranteAtual = repository.buscar(restauranteId);
		
		if (restauranteAtual == null) ResponseEntity.notFound().build();
		
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual) ;
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino) {
		ObjectMapper mapper = new ObjectMapper();
		Restaurante restauranteOrigem = mapper.convertValue(camposOrigem, Restaurante.class);//converte o obj json em obj java
		
		camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);//busca a referencia da prorpiedade com o nome passado pela variavel (nomePropriedade) da classe Determinada (tudo e retornado em json)
			field.setAccessible(true);//deixar as propriedades private acessivel
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);//busca a prorpiedade do obj com a referencia json o field
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor);//usa a referencia da propriedade field na classe restauranteDestino para definir um novo valorProrpiedade(estou usando a propriedade convertidade de json para obj)
		});
	}
	
}

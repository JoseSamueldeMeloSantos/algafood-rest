package com.algaworks.algafoods.api.controller;



import java.util.List;
import com.algaworks.algafoods.infrastructure.repository.CozinhaRepositoryImpl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafoods.api.controller.model.CozinhaXmlWrapper;
import com.algaworks.algafoods.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoods.domain.model.Cozinha;
import com.algaworks.algafoods.domain.repository.CozinhaRepository;
import com.algaworks.algafoods.domain.service.CozinhaService;

//@Controller
//@ResponseBody//indica que a resposta dos metodos deve ir como resposta do corpo http

@RestController//união dos dois de cima
@RequestMapping("/cozinhas")//mapeia controller
public class CozinhaController {

    //private final CozinhaRepositoryImpl cozinhaRepositoryImpl;

	@Autowired
	private CozinhaRepository repository;
	
	@Autowired
	private CozinhaService service;
	
//	@Autowired
//	CozinhaController(CozinhaRepositoryImpl cozinhaRepositoryImpl) {
//	    this.cozinhaRepositoryImpl = cozinhaRepositoryImpl;
//	}
	
	@GetMapping
	public List<Cozinha> listar() {
		return repository.listar();
	}
	
	//para listar com a tag nomeada da forma certa
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhaXmlWrapper listarXml() {
		return new CozinhaXmlWrapper(repository.listar());
	}
	
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {//quando o nome da variavel e o caminho do mapping e o mesmo nao precissa especificar
		Cozinha cozinha = repository.buscar(cozinhaId);
		//return ResponseEntity.status(HttpStatus.OK).body(cozinha);
		//return ResponseEntity.ok(cozinha).ok(cozinha);//atalho para o de cima
		
		//para retornar um response entity com header
		
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.LOCATION, "http://api,algafood.local:8080/cozinhas");
//		
//		return ResponseEntity
//				.status(HttpStatus.FOUND)
//				.headers(headers)
//				.build();
		
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}
		
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//build e para quando nao for retornar corpo
		return ResponseEntity.notFound().build();//atalho do de cima
	}
	
	@ResponseStatus(HttpStatus.CREATED)//definindo response status
	@PostMapping
	public void adicionar(@RequestBody Cozinha cozinha) {
		service.adicionar(cozinha);;
	}
	
	@PutMapping("/{cozinhaId}")
	public  ResponseEntity<Cozinha> atualizar(
			@PathVariable Long cozinhaId,
			@RequestBody Cozinha cozinha) {
		
		Cozinha cozinhaAtual = repository.buscar(cozinhaId);
		
		if (cozinhaAtual != null) {
			
			//pega os atributos do primeiro salva no segundo e ignora o atributo do terceiro
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			
			repository.salvar(cozinhaAtual);
			
			return ResponseEntity.ok(cozinhaAtual);
		}
			return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
		try {
			service.excluir(cozinhaId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	
	}
	
//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
//		
//		try {
//			Cozinha cozinha = repository.buscar(cozinhaId);
//			
//			if(cozinha != null ) {
//				repository.remover(cozinha.getId());
//				
//				return ResponseEntity.noContent().build();
//			}
//			
//			return ResponseEntity.notFound().build();
//		} catch (DataIntegrityViolationException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
		
}
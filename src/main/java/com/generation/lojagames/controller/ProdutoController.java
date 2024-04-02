package com.generation.lojagames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojagames.model.Produto;
import com.generation.lojagames.repository.CategoriaRepository;
import com.generation.lojagames.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
    private CategoriaRepository categoriaRepository;
	
	
	// MOSTRAR TODOS OS PRODUTOS
	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	// METODO BUSCAR POR ID
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	// BUSCAR POR NOME DO PRODUTO
		@GetMapping("/nome/{nome}")
		public ResponseEntity<List<Produto>> getByTitle(@PathVariable String nome){
			return ResponseEntity.ok(produtoRepository.findALLByNomeContainingIgnoreCase(nome));
		}

		
		// MÉTODO POSTAR POSTAGEM
		@PostMapping
		public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
			if(categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(produtoRepository.save(produto));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
		}
		
		// MÉTODO ATUALIZAR POSTAGEM
		
		@PutMapping
		public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
			if(produtoRepository.existsById(produto.getId())) {
				if(categoriaRepository.existsById(produto.getCategoria().getId()))
					return ResponseEntity.status(HttpStatus.OK)
							.body(produtoRepository.save(produto));
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
		// MÉTODO DELETAR POSTAGEM
		
		@ResponseStatus(HttpStatus.NO_CONTENT)
		@DeleteMapping("/{id}")
		public ResponseEntity<String> delete(@PathVariable Long id) {
			Optional<Produto> produto = produtoRepository.findById(id);
			
			if(produto.isEmpty())
				//throw new ResponseStatusException(HttpStatus.NOT_FOUND)//
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado um produto com o id: " + id);
			
			produtoRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Produto com o id: " + id + " deletada com sucesso!");

		}
		
	
	
	
	
	
	
	
	
	
}

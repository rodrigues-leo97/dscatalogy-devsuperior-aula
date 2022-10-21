package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping(value = "/products")
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) { //ResponseEntity vai encapsular uma resposta HTTP
		//PARAMETRO -> PAGE, SIZE SORT: só mudar no postman a forma como faz a chamada HTTP na url, ex: (/products?page=0&size=12&sort=name,asc) o asc é de ascendente

		//@RequestParam(value = "page", defaultValue = "0") Integer page,
		//@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
		//@RequestParam(value = "direction", defaultValue = "DESC") String direction,
		//@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
		//@RequestParam -> o parâmetro é opcional, ou seja, não é obrigatório passar
		//RETIRANDO TODOS OS PARAMETROS PASSADOS NA MÃO E COLOCANDO O OBJETO PAGEABLE NO PARAMETRO

		//PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<ProductDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list); //ok() retorna quando uma requisição é do código 200(deu certo)
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) { 
			ProductDTO ProductDTO = service.findById(id);
			return ResponseEntity.ok().body(ProductDTO); 
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) { //@Valid para validar os @DECORATORS que estão na classe do ProductDTO
			dto = service.insert(dto); //inserir esse dto no banco de dados
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(dto.getId())
					.toUri();
			return ResponseEntity.created(uri).body(dto);
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
			dto = service.update(id, dto); //inserir esse dto no banco de dados

			return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
			service.delete(id); //inserir esse dto no banco de dados

			//resposta 204 - deu certo e no corpo está vazio
			return ResponseEntity.noContent().build(); //resposta não precisa ter corpo
		
	}

}

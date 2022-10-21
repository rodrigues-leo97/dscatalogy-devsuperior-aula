package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) { //ResponseEntity vai encapsular uma resposta HTTP
		Page<UserDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list); //ok() retorna quando uma requisição é do código 200(deu certo)
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) { 
			UserDTO UserDTO = service.findById(id);
			return ResponseEntity.ok().body(UserDTO); 
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
			UserDTO newDto = service.insert(dto); //pois ele recebe o UserInsertDTO mas ele retorna o UserDTO, pois tem toda a questão do tratamento da senha
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(newDto.getId())
					.toUri();
			return ResponseEntity.created(uri).body(newDto);
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
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

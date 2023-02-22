package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.*;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.BadRequestException;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; //injetando o BCrypt que criei com @Bean na classe AppConfig

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        //User User = new User();
        Page<User> list = repository.findAll(pageable);
        return list
                .map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> entityUser = repository.findById(id);

        //User User = entityUser.get();
        User entity = entityUser.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO insert(UserInsertDTO dto) throws BadRequestException { //na hora de inserir eu passo o UserInsertDTO, pois nessa classe temos a SENHA e ela herda os dados da UserDTO, ou seja, tenho acesso a tudo
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); //criptografa a senha
        entity = repository.save(entity); //por padrão retorna uma referencia para a entidade salva

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);

            // entity.setName(dto.getName());
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (javax.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("Id " + id +" not found");
        }

    }

    public void delete(Long id) { //não usa o transactional, pq vou capturar uma exception lá do banco de dados e com transactional não é possível capturar

        //para caso tente deletar um id que não existe
        try {
            repository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }

    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        // só não irá setar o ID
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        entity.setLastName(dto.getLastName());

        entity.getRoles().clear(); //para limpar a entidade
        for(RoleDTO roleDTO : dto.getRoles()) { //se chegou as categorias no UserDTO eu percorro a lista para instanciar uma categoria
            Role role  = roleRepository.getOne(roleDTO.getId()); //getOne para não acessar o BD, apenas salva em memória e posterior adiciona quando for necessário
            entity.getRoles().add(role);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //irá fazer a busca por EMAIL

        User user = repository.findByEmail(username);
        if (user == null) {
            logger.error("User not found: " + username);
            throw new UsernameNotFoundException("E-mail not found");
        }
        logger.info("User found: " +  username);
        return user;
    }
}

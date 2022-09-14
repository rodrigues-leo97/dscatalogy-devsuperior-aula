package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //timeStamp sem timezone
	private Instant createdAt; //armazena instante em que o registro foi criado
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") 
	private Instant updateAt;

	@ManyToMany(mappedBy = "categories") //não preciso escrever o código do manyToMany tudo novamente aqui, já está na classe product, eu só preciso MAPEAR com o MAPPEDBY usando como base o atributo java da classe PRODUCT
	private Set<Product> products = new HashSet<>();

	//Constructor
	public Category() {
		
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}
	
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updateAt = Instant.now();
	}

	public Set<Product> getProducts() { //não precisa colocar o SETTER dele nesse caso, pois se trata de uma classe onde será apenas mostrado o relacionamento na tabela auxiliar
		return products;
	}

	//Equals and Hashcode
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}

	
}

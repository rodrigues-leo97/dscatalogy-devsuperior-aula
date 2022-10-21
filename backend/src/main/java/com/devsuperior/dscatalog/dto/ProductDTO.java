package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProductDTO  implements Serializable{
	private static final long serialVersionUID = 1L;

	//OBS: os @ nesse caso são feitos aqui pq são eles que passam pelas validações das requisições primeiro
	//além disso colocar anotações nas controllers/resources para identificar que essas validações tem que ser cobradas em determinados métodos

	private Long id;

	@Size(min = 5, max = 60, message = "Nome deve ter entre 5 e 60 caracteres") //para controle de tamanho, min e max e caso de erro exibe a mensagem
	@NotBlank(message = "Favor preencher o nome, campo requerido") //blank válida até se o user digitou espaços em branco, diferente do empty
	private String name;
	private String description;

	@Positive(message = "O preço deve ser um valor positivo")
	private Double price;
	private String imgUri;

	@PastOrPresent(message = "A data do produto não pode ser futura")
	private Instant date;
	
	//posso cadastrar mais de uma categoria, então preparo a aplicação pra receber uma lista
	private List<CategoryDTO> categories = new ArrayList<>(); //coleção
	
	public ProductDTO() {
		
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUri, Instant date) {
		//OBS: não se coloca COLEÇÃO EM CONSTRUTOR, por isso não colocamos a Lista aqui dentro
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUri = imgUri;
		this.date = date;
	}
	
	//Criamos mais um construtor para que ele aceite os dados vindo da entidade
	public ProductDTO(Product entity) {
		//OBS: não se coloca COLEÇÃO EM CONSTRUTOR, por isso não colocamos a Lista aqui dentro
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUri = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	//construtor que recebe CATEGORIAS
	public ProductDTO(Product entity, Set<Category> categories) { //para saber que quando eu chamar esse construtor, quero instanciar o DTO colocando os elementos na lista criada(this.categories)
		this(entity); //chama o construtor acima que recebe somente a entidade
		
		//chama uma função de auta-ordem e pego os elementos que estão dentro da minha lista no argumento e os acrescento na lista criada no inicio da classe
		categories.forEach(x -> this.categories.add(new CategoryDTO(x)));
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}
	
	
	
}

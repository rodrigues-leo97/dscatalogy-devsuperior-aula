# dscatalogy-devsuperior-aula

Curso Dev Superior

# DIAGRAMA

- Product -> 1 produto pode ter 1 ou mais categorias(Product ----- 1..* Category)

- Um Product tem que ser cadastrado tendo pelo menos 1 Category

- Category -> 1 categoria pode ter zero ou muitos Product (Category ---- * Product)

- Quando não tem nada e só o *, significa que o mínimo é zero	

	- Uma categoria pode ser cadastrada sem ter a necessidade de informar um produto

- No diagrama temos a seta direcionada (--->): Somente dentro da class1 (class 1 ---> class2) teremos referência para a class2

- dentro da class2 não teremos uma referência para a class1
    
	- User --- 1..* -> Role: Um USER tem 1 ou muitos papéis

	AULA (01-06)
  
  # Dependencias a baixar
  
  -> spring initializr

  ->	com.devsuperior

  -> java version: 11

  -> dependencies: Spring Web, Spring Data JPA, h2, PostgreSql Driver)

		
	# RESOURCE
  
 - Começa criando a classe Category por ser uma classe independente

- após criar atributos, constructor, getters and setter
    
- criar o (hashcode), método padrão que todo obj Java pode ter
      
- serve para comparar se o objeto é igual a um outro

- porém a comparação não é 100%, mas ganha em perfomance

- EQUALS: serve para suprir a deficiência do hashcode, pois é 100% preciso, porém, demora bem mais o processamento

- public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

- Implementar o Serializable, serve para converter em Bytes
  
		
	AULA (01-09)
  
- Resourcer: implementa o controlador REST

- Os recursos da aplicação são implementados aqui

- é por onde um aplicativo ou aplicação web vai usar para acessar a API e obter o resultado da busca

- @RestController -> para dizer que é um controlador REST

- @RequestMapping(value = "/categories") -> para informar a rota inicial

- ResponseEntity: é um objeto que vai encapsular uma resposta HTTP

- ele é do tipo GENERIC

- List: é uma interface, ou seja, vc não pode instanciar ela

- tem que instanciar uma classe que implementa a interface, ex:

List<Category> list = new ArrayList<>(); -> neste caso é o ArrayList
	
![image](https://user-images.githubusercontent.com/71105466/168932645-f09df67d-dac9-4790-83fc-69717a15c77e.png)
	
- O Return neste caso tem que ser do OBJ ResponseEntity, e uso o OK() para dizer que a requisição foi realizada com sucesso (request 200)
	
- Posso retornar também o corpo (body(list)) da minha requisição
	
# Resultado POSTMAN
	
  ![image](https://user-images.githubusercontent.com/71105466/168933352-185e27e9-8db6-4188-a844-30dda62861fa.png)


	


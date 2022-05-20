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
	
	
# CRUD
	
- Create
	
- Retrieve
	
- Update
	
- Delete
	

# INTEGRANDO COM BANCO H2
	
- Colar no POM.XML as dependências abaixo
	
```
	
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>

<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<scope>runtime</scope>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
	
 ```
	
# DECORADORES

- @Entity -> transforma a classe em uma entidade, para saber que tem conexão com a base de dados

- @Table -> para especificar a tabela da base de dados que irá se conectar
	
- @Id -> para dizer que o atributo será o identificador da classe

- @GeneratedValue(strategy = GenerationType.IDENTITY) -> para dizer que esse Id terá que ser um autoincremento
	
# REPOSITORIES (ACESSO Á DADOS)
	
![image](https://user-images.githubusercontent.com/71105466/168936044-f7d11696-7688-4d04-b7ba-e7466a9b15d0.png)

	
- Terei a camada do CONTROLADOR, que terá acesso a camada de DTO, que irá conversar com a camada de SERVIÇO

- A camada de SERVIÇO irá conversar com a camada de ACESSO A DADOS
	
- OBS: o SERVIÇO e ACESSO A DADOS tem acesso a camada de ENTIDADES, conforme mostra a imagem ácima
	
- DTO: objetos auxiliares para tráfegar os dados
	
## COMEÇANDO PELA CAMADA DE ACESSO A DADOS
	
- com.devsuperior.dscatalog.repositories.CategoryRepository
	
- Será uma INTERFACE
	
- ela da um EXTENDS de JpaRepository<T, ID>
	
![image](https://user-images.githubusercontent.com/71105466/168937448-1d77b9aa-a714-4f89-9cf9-a7c0154b7f4e.png)

- Só com o JPA já da para usar todas as operações de banco relacional para Postgre, Oracle, h2, SQL e etc...
	

# CAMADA DE SERVIÇO
	
- com.devsuperior.dscatalog.services.CategoryService
	
- Camada que irá chamar alguma funcionalidade da camada de ÁCESSO A DADOS

- @Service -> serve para dizer que é uma camada de SERVIÇO, um componente que vai participar do sistema de injeção de dependências automatizado so SPRING

- Ou seja, quem vai gerenciar as instâncias das dependências dos OBJETOS do tipo da classe será o SPRING
	
- Para que eu consiga ter acesso a camada de ACESSO A DADOS eu tenho que ter uma instância da classe Repository desejada
	
![image](https://user-images.githubusercontent.com/71105466/168937533-ae17df01-a1d9-4172-8f88-257684628afc.png)
	
- @AUTOWIRED: Serve para injetar AUTOMATICAMENTE uma instância gerenciada pelo SPRING
	
- Após isso ir na classe CAtegoryResource (que é o controlador)
	
- Tirar a lista mocada de lá, instanciar a classe de Serviço 
	
![image](https://user-images.githubusercontent.com/71105466/168938376-117e851e-15e1-4134-982c-7e2b249dd0a1.png)
	
# APPLICATION.PROPERTIES
	
```
spring.profiles.active=test

spring.jpa.open-in-view=false
	
```

- spring.jpa.open-in-view=false: para garantir que ao chegar na camada CONTROLLER as transações do banco de dados sejam encerradas e apenas mantenha os dados necessários que foi pedido na requisição e enviado a CONTROLLER
	
# application-test.properties
	
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console	
	
```	
	
- O Apllication.properties tem as configurações do meu projeto, porém, configs para todos os perfis
- PERFIL: usamos perfils diferentes, teste(feature/h2), homologação(release/hml) e produção(master...)
	
# TRANSACTIONAL	
	
![image](https://user-images.githubusercontent.com/71105466/169423980-e6bafc4d-d4c1-4912-835d-4a6dab228175.png)


- Serve para garantir a integridade da transação
- O próprio FrameWork envolve o método ao identificar que tem o @Transactional e garante o envolvimento em uma transação com o Banco de Dados (ou faz tudo ou não faz nada)
	
- No caso como é uma transação somente de LEITURA, para verificar a lista se coloca o readOnly = true, para ficar somente como leitura
- Evita que faça o locking/travar o banco de dados, pois é só leitura
	
![image](https://user-images.githubusercontent.com/71105466/169424212-5c15f2c2-48ef-4e3f-bdbf-9f8fd59661e6.png)

Resumo: Quando se tem o 'spring.jpa.open-in-view=false' setado pra false e usamos o @Transactional garantimos que tudo seja resolvido na camada de Serviço, e APENAS envia os dados necessários para a requisição pedida para a CONTROLLER, conforme imagem abaixo:
	
![image](https://user-images.githubusercontent.com/71105466/169424432-b0bd040f-f2d2-4adb-bcb0-367356a44aaf.png)



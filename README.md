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

# DTO
	
- Data Transfer Object
- O Controlador não vai ter acesso direto a camada de serviço, ele vai receber os dados por meio do DTO, como mostra a imagem abaixo: 
	
![image](https://user-images.githubusercontent.com/71105466/169431543-589949bb-23f1-4215-a330-f93d35915ed0.png)
	
- É um objeto somente para carregar os dados
	
- Podemos controlar quais dados iremos mandar para a CONTROLLER, ou seja, menos informações passando de um lado para o outro
	
- A arquitetura não tem haver com segurança, porém, se vc usar o DTO para não transmitir dados SENSÍVEIS como senhas e etc.. isso automaticamente passa a ser uma medida de segurança da sua aplicação.

- Criando a classe DTO

import com.devsuperior.dscatalog.entities.Category.CategoryDTO;
	
![image](https://user-images.githubusercontent.com/71105466/169432487-c88141c3-d8f6-476f-8337-b6e7a3f5fb07.png)

- O Construtor com a ENTITY como argumento está ai para que povoe pra mim o CategoryDTO simplesmente ao passar a entidade como argumento
	
- O método findAll() na CategoryService não irá mais retornar o Category, mas sim o CategoryDTO
	
- Para isso precisa mudar o tipo de retorno da função e converter os dados, e tem 3 formas de fazer:
	
![image](https://user-images.githubusercontent.com/71105466/169433600-10f9e4a1-0d25-4697-bab1-0ce677071b17.png)
	
![image](https://user-images.githubusercontent.com/71105466/169434276-50efe2f2-f8ce-4484-b43a-50049cf50110.png)
	
![image](https://user-images.githubusercontent.com/71105466/169434316-3977d685-fa03-43a6-aaf3-dff52b550481.png)
	
# EXPRESSÂO LAMBDA EXPLICADA
## findAll
	
List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	
- stream() -> serve para converter a coleção normal(no caso a lista) para uma stream.
	
- Recurso do Java 8 em diante, que permite vc trabalhar com funções de alta ordem, lambda. Recurso de PROGRAMAÇÃO FUNCIONAL
	
- Possibilidade de fazer transformações na coleção
	
- map() -> transforma cada elemento original em alguma outra coisa: map(x -> new CategoryDTO(x)) --> é igual ao map do JAVASCRIPT

- Também da para usar MAP, FILTER E REDUCE
	
- map(x -> new CategoryDTO(x)) -> para cada elemento X(retorno do meu objeto) eu vou levar ele para outro elemento, no caso, new CategoryDTO e aplicando o retorno dele(X)
	
- o resultado desse retorno será uma STREAM, portanto, preciso converter de volta para o tipo LIST que é o valor da minha variável
	
- Aplico então o collect(Collectors.toList()); --> se encarrega de transformar novamente para algo do tipo LIST
	
#findById
	
- Criando o método para chamar por id
	
- Começamos pela controller, o método é bem parecido com o anterior, porém, não irá retornar uma lista, mas sim um único elemento

![image](https://user-images.githubusercontent.com/71105466/170164173-d1044044-c9c4-4681-b7d0-67a42911dd13.png)

- O decorador @GetMapping(value = "/{id}") funciona basicamente como um binding, estou informando com as { } que o id será adquirido dinamicamente
	
- Criando o método eu uso o decorador @PathVariable, para que ele saiba que o {id} da URL será passado como parâmetro da minha função
	
- Crio na classe SERVICE o método findById

![image](https://user-images.githubusercontent.com/71105466/170164487-66a000e7-e291-417e-9937-07564d87377c.png)

- Eu crio uma variável do tipo OPTIONAL, por sinal facilita em algumas buscas e faz com que não precisemos trabalhar com null
	
- Acesso a repository e uso o próprio método da JPA findById 
	
- .get() é usado para que eu consiga adquirir somente o OBJETO que está dentro do OPTIONAL, pois minha função é do tipo CategoryDTO
	
- Se não usar o get() irá dar erro na hora de fazer o retorno da função pois não podemos retornar o tipo OPTIONAL, mas sim o tipo CategoryDTO
	
- instancio no retorno um CategoryDTO passando o a váriavel que armazena o retorno do método findById e já convertendo pra DTO
	
	
# TRATAMENTO DE EXCEÇÃO
	
## EntityNotFound
	
- criar um subpacote em services.exceptions.EntityNotFound.java
	
- a classe irá extender de RunTimeException ou de Exception

- RunTimeException: Neste caso a exceção pode OU NÃO ser tratada
	
- Exception: quando se usa esse no extends estou informando que uma exceção tem que ser OBRIGATORIAMENTE tratada
	
![image](https://user-images.githubusercontent.com/71105466/170604594-2ff9255e-eab2-4a99-86be-c0ddead52b88.png)
	
	
- Na classe SERVICE iremos alterar o método findByID, para que seja tratada a exceção que pode vir nele
	
- No caso a exceção será para quando a pessoa digitar um id que não existe, ai precisaremos tratar
	
- orElseThrow(): permite definir uma chamada de exceção, no caso uma expressão lambda, pois não irá receber argumento nenhum
	
- Se o Category não existir ele entra na EXCEPTION
	
- OBS: a diferença entre parametro e argumento

- PARAM: é quando vc está criando o método e informando oq ele irá receber de PARÂMETRO
	
- ARGUMENTO: é quando vc está chamando o método que já FOI criado e passando os valores que ele irá receber, neste caso se chama argumento
	
![image](https://user-images.githubusercontent.com/71105466/170604982-1cab8e12-035a-4eb8-af67-39cb16b74619.png)
	
- uso o orElseThrow e instancio uma nova EXCEPTION, no caso, a classe criada anteriormente EntityNotFound, conforme mostra a imagem acima
	
- Desta forma ele lança a EXCEPTION na API, mas ainda não aparece na tela o retorno dela
	
- O próximo passo agora é não permitir que estoure o ERRO 500 no retorno da request no postman, e sim um mecanismo que capture essa exceção e retorne uma resposta de objeto 404(not found)
	
# StandardError
	
- resources.exceptions.StandardError.java
	
- Criar um objeto java que contenha exatamente a estrutura da imagem abaixo: 
	
![image](https://user-images.githubusercontent.com/71105466/170605448-eaa2ccd9-ffa1-4b53-ba8b-bf3dc77bc4d7.png)

- Esse por padrão é oq retorna do postman ao fazer a requisição e não encontrar o ID desejado
	
- Então para isso irei montar um objeto contendo a mesma estrutura(TimeStamp, status, error, message e path), para tratar o erro e configura-lo da forma que eu desejar
	
![image](https://user-images.githubusercontent.com/71105466/170605697-bd807b76-9cf6-4650-aba7-1b87e4acae56.png)

- Cria também os getters e setters


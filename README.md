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

- Agora eu poderia ir na controller e tratar minha EXCEPTION, pois a classe CategoryService já está identificando que pode estourar uma EXCEÇÃO e agora eu tenho que tratar ela na CONTROLLER
	
- Poderia criar um TRY, porém, eu teria que ficar fazendo um TRY toda vez que eu criar um método no controlador que for que te tratar uma EXCEPTION
	
- Para isso poderemos criar uma classe especial do Spring: ControllerAdvice, para substituir a necessidade do TRY CATCH
	
# ResourceExceptionHandler
	
- resources.exceptions.ResourceExceptionHandler.java
	
- Ele irá manipular exceções a NÍVEL DA CAMADA DE RESOURCE
	
![image](https://user-images.githubusercontent.com/71105466/170606141-c10aac15-cedd-4fdd-9fbb-faa04ad0b68e.png)
	
- @ControllerAdvice: isso que permite que essa classe INTERCEPTE alguma exceção que aconteça na CONTROLLER
	
- @ExceptionHandler(EntityNotFoundException.class): tem esse decorador com esse ARGUMENTO para que ele saiba o tipo de EXCEPTION que ele irá interceptar
	
- Ou seja, se em algum dos métodos das minhas Controllers que tiver alguma Exception que estoura na EntityNotFound, automaticamente ele será enviado para cá e tratada pelo INTERCEPTADOR
	
OBS: verificar o import do EntityNotFound, pois existe uma mesma classe com esse nome na Framework

- Ele irá tratar essa exceção
	
- Iremos declarar um método PÚBLICO
	
![image](https://user-images.githubusercontent.com/71105466/170606245-8bd2b0ae-c0f7-4d8f-9916-aa848c9fa15d.png)
	
- O GENERIC dele vai ser a classe <StandardError>, ou seja, esse método vai ser uma resposta de request onde o conteúdo da resposta vai ser um objeto do tipo StandarError
	
- Por isso criamos a nossa classe StandardError, pois nela contém a estrutura do meu erro que vai estourar na EXCEPTION, e uso ela pra ser o objeto de retorno da ControllerAdvice
	
- No primeiro parâmetro iremos passar a classe da nossa EXCEPTION personalizada que criamos EntityNotFound
	
- HttpServletRequest: Esse objeto que vai ter as informações da minha requisição
	
- Instancio um objeto StandarError e vou setando nele oq eu desejo para tratar a EXCEPTION
	
![image](https://user-images.githubusercontent.com/71105466/170607148-f22d15f9-1232-494b-90f3-d8bf74298f22.png)

- err.setTimestamp(Instant.now()); //para pegar o instante ATUAL da request
	
- err.setStatus(HttpStatus.NOT_FOUND.value()); //Código 404
	
err.setError("Resource not found"); //setando manualmente uma mensagem a gosto
	
err.setMessage(e.getMessage()); //pegando a mensagem passada no método findById para quando o erro estourar
	
err.setPath(request.getRequestURI()); //pega o caminho da requisição feita. EX: "/categories/6"
	
- return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); //permite que customize o status que irá retornar, e no caso quero o erro 404 que será o HttpStatus.NOT_FOUND e no corpo o OBJETO que setei os valores acima
	

# POST
	
- Criando um método POST 

- Criando na controller um novo método
	
- neste caso teremos que receber um OBJETO como parâmetro, pois o usuário pode passar uma ou mais informações para acrescentar algo na aplicação
	
- @RequestBody para identificar que o OBJETO que a aplicação vai receber vai vir pelo corpo da requisição
	
- OBS: neste caso como é algo que estou enviando para a API o código correto Http para ela não é 200, mas sim 201(Recurso criado)
	
![image](https://user-images.githubusercontent.com/71105466/170612191-559e6baa-88c4-4c90-a7fb-10c4f554a295.png)

- Dessa forma para tratar quando se está criando um novo recurso e para exibir o 201(created) como retorno
	
![image](https://user-images.githubusercontent.com/71105466/170612357-8da066e8-05b6-4c70-adc0-3fb69a370510.png)
	
	
# DELETE
	
![image](https://user-images.githubusercontent.com/71105466/171527881-d4637b11-fc36-486d-b9af-e1f52e9138b5.png)

- O método delete só irá receber o id da URL
	
- O retorno dele não terá corpo, pois irá apenas deletar, e será o código 204
	
- Por não ter corpo o MÉTODO dele poderá ser VOID
	
- Se usa para quando não há corpo o noContent().build();

![image](https://user-images.githubusercontent.com/71105466/171528065-77baca63-31f8-4549-9c5a-0705078e6e61.png)

- Temos uma particularidade neste caso, pois teremos dois CATCH

- Isso se dá devido ao fato que além da pessoa poder digitar um ID que não existe ela também poderá deletar algo que não pode ser deletado
	
- Por exemplo, uma Categoria tem Produtos cadastrados, se apagar uma categoria com produtos cadastrados esses Produtos irão ficar sem Category
	
- E isso não pode acontecer, então temos ai uma VIOLAÇÃO DA INTEGRIDADE DOS DADOS
	
- Criamos também uma outra EXCEPTION personalizada 
	
- services.exceptions.DataBaseException.java
	
- Também iremos INTERCEPTAR essa exceção quando ela for lançada
	
![image](https://user-images.githubusercontent.com/71105466/171528411-9374a0b5-52b3-48c9-99ae-7ba7c93abe91.png)
	
# DADOS DE AUDITORIA
	
- Armazenando dados de acordo com o TIMEZONE

- Criando os atributos na classe Category.java
	
![image](https://user-images.githubusercontent.com/71105466/172970784-c6a47134-a63e-4145-b5fd-673f612ccadb.png)
	
- @Colum(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") -> TIMESTAMP sem timezone, ou seja, não especificando um timezone
	
![image](https://user-images.githubusercontent.com/71105466/172971118-b18a4af9-700a-4999-bc93-f1ddd3da28cd.png)

	
- Não precisa do método SET, para que não seja disponível e nem possível alterar esses dados pela aplicação
	
- @PreUpdate -> Para que cada vez que eu mande salvar uma categoria ele armazene o instante atual, da mesma forma que quando eu ATUALIZAR ele fará a mesma coisa
	
- OBS: Não precisa passar isso para a DTO, pq não é um tipo de dado que quero que o USUÁRIO que está acessando a aplicação tenha acesso, então por isso não tem sentido colocar na camada de DTO isso, fica somente na camada da MODEL
	
![image](https://user-images.githubusercontent.com/71105466/172971440-03d3c44a-7b3b-4727-8892-7fe5364f61c9.png)

- Atualizei os dados a exibirem na base de dados
	
# PAGINAÇÃO
	
```
@RequestParam(value = "page", defaultValue = "0") Integer page,
@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
@RequestParam(value = "orderBy", defaultValue = "moment") String orderBy,
@RequestParam(value = "direction", defaultValue = "DESC") String direction)
	
```
	
- diferente do PathVariable, pois o mesmo é um parâmetro obrigatório
	
- Toda vez que for um parâmetro opcional se usa o @RequestParam
	
![image](https://user-images.githubusercontent.com/71105466/172972636-87286298-21fb-49e0-9c35-00c7e0668392.png)
	
@RequestParam(value = "page", defaultValue = "0") Integer page, //se não informar esse parâmetro irá começar pela página ZERO
@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage, // Quantidade de linhas/itens pra trazer na página, no caso 12
@RequestParam(value = "orderBy", defaultValue = "name") String orderBy, // ORDENAR POR NOME
@RequestParam(value = "direction", defaultValue = "DESC") String direction) //Em ordem descendente






# AUTENTICAÇÃO JWT

### PARTE 1

- Implementar algumas interfaces:

- UserDetails e UserDetailsService

- Implementando UserDetails:

	- Implementar na classe User.java o UserDetails

		- No getUsername() substituir o return pelo email

			- Iremos usar o email como base para autenticar
			
- O método <? extends GrantedAuthority> getAuthorities()

	- Retorna uma coleção do tipo GrantedAuthority
		
		- Na classe User.java o User user tem um relacionamento com a classe de ROLE
			
			- Irei percorrer cada elemento do tipo ROLE convertendo para o elemento do tipo GrantedAuthority
			
![image](https://user-images.githubusercontent.com/71105466/201779826-42386d4d-708e-4492-bb3d-093ae63f65c3.png)

- Todo os outros métodos basta colocar o return como TRUE, mas caso queira evoluir a regra da aplicação basta manipular os métodos para o desejado


### PARTE 2

- Na classe de serviço UserService.java

	- Implementar UserDetailsService
	
- Irá implementar o método loadUserByUsername() throws UserNameNotFoundException

	- é um método para realizar a busca por username, neste caso dessa aplicação por EMAIL
	
	- realizar a busca na REPOSITORY do User buscando pelo email
	
	- Essa classe tem que retornar uma EXCEPTION caso não encontre o email
	
![image](https://user-images.githubusercontent.com/71105466/201780882-7fb8ad29-05e9-4511-bc7a-06d9bbe74889.png)


### PARTE 3

- CLASSE DE CONFIGURAÇÃO DE SEGURANÇA WEB (WebSecurityConfigurerAdapter)

	- Neste caso será a classe WebSecurityConfig.java já criada na nossa aplicação
	
	- Ela foi criada quando foi feito a criação da classe ROLE e USER na aplicação, já preparando para esse contexto
	
	- Na classe terá o método configure(WebSecurity web) que foi criado para liberar todas as rotas, ele precisará ser alterado
	
	- Iremos acrescentar no antMatchers o /actuator
		- Outra lib que o SringCloud usa para passar nas requisições
		- Continuamos liberando todos, porém, passando também por essa rota pra liberar
		
- Realizar uma SOBRECARGA do método configure()

	- Irá servir para configurar o algoritmo que está sendo usado para encriptar a senha(BCryptPasswordEncoder neste caso)
	
	- Configurar também quem é o UserDetailService, ou seja, a classe que usamos pra implementar essa interface
	
	- OBS: injetar o BCrypt e o UserDetailService
	
	
	
- Configurar o método AuthenticationManager authenticationManager()

	- Injetar a annotation @Bean
	
	- Para que ele seja também um componente disponível no sistema, pq iremos usar futuramente no AuthorizationService
	
	
![image](https://user-images.githubusercontent.com/71105466/201782439-d9286e75-78f8-4070-bd7c-a5f68ee3a2ca.png)


# SPRING CLOUD OAUTH2

### PARTE 4

- Classe de configuração para Authorization server

	- AuthorizationServerConfigurerAdapter
	
		- Criar no pacote com.devsuperior.dscatalog.config.AuthorizationServerConfig
		
		- Transformar ela numa classe de configuração (@Configuration) para que possa usa-la como um componente através do @Bean indicando o gerenciamento das suas dependências
		
		- Injetar como na imagem abaixo: 
		
		![image](https://user-images.githubusercontent.com/71105466/202334775-16f6f88a-56df-4b54-b60f-2770c40bea17.png)

		- Após isso criar três métodos de configurações que virão do EXTENDS da classe AuthorizationServerConfigurerAdapter, como na imagem abaixo:
		
		![image](https://user-images.githubusercontent.com/71105466/202334884-3c4be09b-94c2-4b97-b2ef-dc18292f1558.png)
		
		
		
		
# EXTERNALIZANDO CONFIGURAÇÕES

- Iremos externalizar algumas propriedades pois estão em hardcode dentro do código

- Para isso iremos no APPLICATION.PROPERTIE, deixando assim:

![image](https://user-images.githubusercontent.com/71105466/202606467-7e6505b7-a02f-4075-8f51-69a48c016417.png)

- Iremos deixar uma referência a uma variável de ambiente, usando o operador de coalescência

	- CLIENT_ID:dscatalog => a variável de ambiente é o CLIENT_ID e irá guardar um valor no contexto do sistema, ou seja, se o ambiente em que a aplicação estiver não dizer qual será o valor da variável ela automaticamente irá assumir o valor que vem após o : (neste caso será o dscatalog)
	
- Dentro da classe que estava em hardcode eu crio uma variável e coloco a anotação do @Value em cima da variável, fazendo um apontamente para a variável de ambiente do application.propertie, como na imagem abaixo: 

	![image](https://user-images.githubusercontent.com/71105466/202606950-7e8f3ce3-702c-4dd2-9178-9a22d995dd51.png)
	
	
# ACRESCENTANDO INFORMAÇÃO AO TOKEN

- Por padrão no token vai o username e as roles dele, então, podemos acrescentar mais informações caso precisemos

	- Para isso iremos usar um COMPONENTE chamado TokenEnhencer
	
	- Não será um SERVICE, por isso será criado outro pacote pra ele
	
		- dscatalog.components.JwtTokenEnhancer
	
	- Irá implementar o TOKEN ENHANCER na classe
	
	- Colocar anotação @Component e implementar a interface
	
### OBJETIVO DO TOKEN ENHANCER

- Ele irá entrar no CICLO DE VIDA do token que será gerado e irá acrescentar as informações, veja o exemplo da implementação do método da interface na imagem abaixo:

![image](https://user-images.githubusercontent.com/71105466/202607510-dd435069-e3e1-40d2-ae35-28abb771e5eb.png)

- Para fazer a busca na repository ele irá usar o objeto já que vem no argumento da classe(oAuth2Authentication), pois ele já tem as informações do token, ai com ele conseguimos realizar a busca na REPOSITORY

- E para inserirmos as informações no token de fato após usar o MAP<> para informar o que queremos fazer usamos o oAuth2AccessToken, pois é ele que de fato irá entrar no ciclo de vida e acrescentar as informações ao token com base no que foi setado no código nas linhas ácima, neste caso o ID e PRIMEIRO NOME

- Após a implementação desse método será necessário alterar a classe AuthorizationServerConfig

### ALTERANDO A CLASSE AuthorizationServerConfig

- Iremos injetar o JwtTokenEnhancer

![image](https://user-images.githubusercontent.com/71105466/202607923-eec0c666-c34b-4d2a-b824-080d7d486201.png)

	- Após isso iremos alterar o método  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	
	![image](https://user-images.githubusercontent.com/71105466/202608146-6e78778d-ead2-4221-9ced-b0eb166451ad.png)

	- Isso fará a implementação dele conforme esperado dentro do ciclo de vida do token










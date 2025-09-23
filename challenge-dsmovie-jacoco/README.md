## DSMovie with Jacoco
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/ohyescris/qa-tests-check/blob/main/LICENSE)

# Sobre o projeto

É uma aplicação backend construída durante as aulas do módulo **Java Spring Expert**, curso organizado pela [DevSuperior](https://devsuperior.com "Site da DevSuperior").

A aplicação consiste em API REST para manipulação de filmes e avaliações com conexão com banco de dados H2 utilizando Spring Boot. Como se trata de um 
projeto de aprendizado, os dados são importados por um seed e depois são armazenados no banco de dados, este seed pode ser visualizado pelo H2 Console. Para 
visualização das respostas para os endpoints, é possível utilizar o Postman. Para a saída dos dados serão apresentados apenas DTOs, estes os mesmos construindos utilizando
ORM e respeitando o padrão REST.

## Testes de Unidade

Uma breve explicação de cada serviço será dada, entretanto, o objetivo deste projeto é apresentar os testes de código e não os serviços em si.
Como a API conta com um sistema de autenticação, uma classe utilitária chamada TokenUtil, no pacote **com.devsuperior.dsmovie.tests**, foi criada com o intuito de simular
esta autenticação, viabilizando os testes de integração sem a necessidade de estar gerando o token manualmente e passando diretamente por linha de código.

### Movies

Nesta seção é possível fazer busca paginada ou por ID, inserir, atualizar e deletar novos filmes.

![SERVICES-COVERAGE](https://github.com/ohyescris/assets/blob/main/images/qa/dsmovie-jacoco/services_coverage.png)

Várias componentes do resultado são testadas e com o resultado obtido, assertions são feitas. Podemos destacar neste exemplo o resultado obtido para a busca paginada.

```java
@Test
public void findAllShouldReturnPagedMovieDTO() {
  
  Pageable pageable = PageRequest.of(0, 12);
  
  Page<MovieDTO> result = service.findAll(movieTitle, pageable);
  
  assertNotNull(result);
  assertEquals(result.iterator().next().getTitle(), movieTitle);
}
```

Já neste caso por exemplo, a Assertion é feita com base no lançamento da exceção.

```java
@Test
public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
  
  assertThrows(ResourceNotFoundException.class, () -> {
    MovieDTO result = service.findById(nonExistingId);
  });
}
```

### Score

Nesta seção é possível apenas salvar novas avaliações sobre filmes.

No teste a seguir a Assertion é feita no resultado verificando se o retorno é o esperado.

```java
@Test
public void saveScoreShouldReturnMovieDTO() {
      
  MovieDTO result = service.saveScore(scoreDTO);
  
  assertNotNull(result);
  assertEquals(result.getTitle(), movieDTO.getTitle());
}
```

Já neste trecho verifica-se o lançamento da exceção caso a condição ocorra.

```java
@Test
public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
  
  scoreDTO = new ScoreDTO(nonExistingId, scorePoints);
  
  assertThrows(ResourceNotFoundException.class, () -> {
    MovieDTO result = service.saveScore(scoreDTO);
  });
}
```

### Users

Nesta seção é possível buscar o usuário logado/autenticado e também pelo username/email.

No teste a seguir é esperado que o usuário seja retornado corretamente, e assertions são feitas com base nessa suposição.

```java
@Test
public void authenticatedShouldReturnUserEntityWhenUserExists() {

  when(userUtil.getLoggedUsername()).thenReturn(clientUsername);

  UserEntity result = service.authenticated();

  assertNotNull(result);
  assertEquals(result.getUsername(), clientUsername);
}
```

Já neste teste, espera-se que uma exceção seja lançada, considerando o caso de um username inexistente ou inválido.

```java
@Test
public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

  assertThrows(UsernameNotFoundException.class, () -> {
    UserDetails result = service.loadUserByUsername(nonExistingUsername);
  });
}
```

# Tecnologias utilizadas back end
- Java
- Spring Boot
- JPA / H2
- Maven
- JUnit

# Como executar o projeto

Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/ohyescris/qa-tests-check.git

# executar o projeto
./mvnw spring-boot:run
```

# Autor

Cristiano da Silva Araújo

https://www.linkedin.com/in/cristiano-araújo-8172191a3



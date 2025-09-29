## DSMovie REST Assured Challenge
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/ohyescris/qa-tests-check/blob/main/LICENSE)

# Sobre o projeto

É uma aplicação backend construída durante as aulas do módulo **Java Spring Expert**, curso organizado pela [DevSuperior](https://devsuperior.com "Site da DevSuperior").

A aplicação consiste em API REST para manipulação de filmes e avaliações com conexão com banco de dados H2 utilizando Spring Boot. Como se trata de um 
projeto de aprendizado, os dados são importados por um seed e depois são armazenados no banco de dados, este seed pode ser visualizado pelo H2 Console. Para 
visualização das respostas para os endpoints, é possível utilizar o Postman. Para a saída dos dados serão apresentados apenas DTOs, estes os mesmos construindos utilizando
ORM e respeitando o padrão REST.

Podemos destacar a divisão dos projetos seguindo um padrão para colocar o projeto base de referência separado do projeto dos testes, melhorando assim a organização, possível
identificação de falhas, divisão de recursos e tarefas.

## Testes de Unidade

Uma breve explicação de cada serviço será dada, entretanto, o objetivo deste projeto é apresentar os testes de código e não os serviços em si.
Como a API conta com um sistema de autenticação, uma classe utilitária chamada TokenUtil, no pacote **com.devsuperior.dsmovie.tests**, foi criada com o intuito de simular
esta autenticação, viabilizando os testes de integração sem a necessidade de estar gerando o token manualmente e passando diretamente por linha de código.

Na imagem a seguir podemos ver a prova de que os testes foram um sucesso para ambos os controladores.

![CONTROLLERS-TESTS](https://github.com/ohyescris/assets/blob/main/images/qa/dsmovie-rest-assured/scorecontroller_moviecontroller_ra.png)

### Movies

Nesta seção é possível fazer busca paginada ou por ID, inserir, atualizar e deletar novos filmes.

Várias componentes do resultado são testadas e com o resultado obtido, assertions são feitas. Podemos destacar nestes dois testes exemplo o resultado obtido para a busca paginada.

```java
@Test
public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
  
  given()
    .get("/movies")
  .then()
    .statusCode(200)
    .body("content.id", hasItems(1, 20))
    .body("content.title", hasItems("The Witcher", "Harry Potter e a Pedra Filosofal"));
}

@Test
public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
  
  given()
    .get("/movies?title={movieTitle}", movieTitle)
  .then()
    .statusCode(200)
    .body("content.id[0]",is(2))
    .body("content.title[0]", equalTo("Venom: Tempo de Carnificina"))
    .body("content.score[0]", is(3.3F))
    .body("content.count[0]", is(3))
    .body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"));
}
```

Já neste caso por exemplo, o teste é feito se o código específico será retornado para a condição de id inexistente.

```java
@Test
public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
  
  given()
    .get("/movies/{id}", nonExistingId)
  .then()
    .statusCode(404)
    .body("error", equalTo("Recurso não encontrado"));
}
```

### Score

Nesta seção é possível apenas salvar novas avaliações sobre filmes.

No teste a seguir verificamos algumas componentes como o código e título retornados para confirmar o retorno esperado no teste.

```java
@Test
public void saveScoreShouldReturnOkWhenAdminLoggedAndValidData() throws Exception {
  
  JSONObject newProduct = new JSONObject(putScoreInstance);
  
  given()
    .header("Content-type", "application/json")
    .header("Authorization", "Bearer " + adminToken)
    .body(newProduct)
    .contentType(ContentType.JSON)
    .accept(ContentType.JSON)
  .when()
    .put("/scores")
  .then()
    .statusCode(200)
    .body("id", is(1))
    .body("title", equalTo("The Witcher"));
}
```

Também é possível fazer testes para códigos específicos de erros, como este teste que verifica se o código 401 referente a uma operação não autorizada
está sendo retornado.

```java
@Test
public void saveScoreShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
  
  JSONObject newProduct = new JSONObject(putScoreInstance);
  
  given()
    .header("Content-type", "application/json")
    .header("Authorization", "Bearer " + invalidToken)
    .body(newProduct)
    .contentType(ContentType.JSON)
    .accept(ContentType.JSON)
  .when()
    .put("/scores")
  .then()
    .statusCode(401);
}
```

# Tecnologias utilizadas back end
- Java
- Spring Boot
- JPA / H2
- Maven
- REST Assured

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



## Movieflix Challenge
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/ohyescris/qa-tests-check/blob/main/LICENSE)

# Sobre o projeto

É uma aplicação backend construída durante as aulas do módulo **Java Spring Expert**, curso organizado pela [DevSuperior](https://devsuperior.com "Site da DevSuperior").

A aplicação consiste em API REST para manipulação de usuários, filmes, gêneros e usuários com conexão com banco de dados H2 utilizando Spring Boot. Como se trata de um 
projeto de aprendizado, os dados são importados por um seed e depois são armazenados no banco de dados, este seed pode ser visualizado pelo H2 Console. Para 
visualização das respostas para os endpoints, é possível utilizar o Postman. Para a saída dos dados serão apresentados apenas DTOs, estes os mesmos construindos utilizando
ORM e respeitando o padrão REST.

## IT (Integration Tests)

Uma breve explicação de cada controlador será dada, entretanto, o objetivo deste projeto é apresentar os testes de código e não os controladores em si.
Como a API conta com um sistema de autenticação, uma classe utilitária chamada TokenUtil, no pacote **com.devsuperior.movieflix.tests**, foi criada com o intuito de simular
esta autenticação, viabilizando os testes de integração sem a necessidade de estar gerando o token manualmente e passando diretamente por linha de código.

### Movies

Na seção de filmes, é possível buscar de diversas formas: por ID, com as críticas e uma busca mais refinada por título.

![MOVIE-CONTROLLER-IT](https://github.com/ohyescris/assets/blob/main/images/qa/movieflix/moviecontrollerit.png)

Várias componentes do resultado são testadas e este mesmo resultado JSON é obtido usando o mockMvc para testes mais realistas. A imagem a seguir apresenta um trecho do código
que tem como objetivo testas se o JSON da resposta realmente está retornando o que foi solicitado.

![MOVIE-CONTROLLER-IT-BY-ID-VISITOR-AUTHENTICATED](https://github.com/ohyescris/assets/blob/main/images/qa/movieflix/moviecontrollerit_visitor_authenticated.png)

Já para a busca paginada, também temos uma imagem do trecho de código a seguir representando um exemplo de teste para as componentes do JSON de resposta de uma busca paginada
mais refinada.

![MOVIE-CONTROLLER-IT-BY-GENRE-VISITOR-AUTHENTICATED](https://github.com/ohyescris/assets/blob/main/images/qa/movieflix/moviecontrollerit_get_by_genre_visitor_authenticated.png)

Por último também podemos destacar um exemplo de testes de segurança, quando o ID não existir ou usuário não for autorizado.

![MOVIE-CONTROLLER-IT-BY-ID-UNAUTHORIZED](https://github.com/ohyescris/assets/blob/main/images/qa/movieflix/moviecontrollerit_unauthorized.png)

# Tecnologias utilizadas back end
- Java
- Spring Boot
- JPA / H2
- Maven
- MockMVC

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



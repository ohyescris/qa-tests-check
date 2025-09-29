package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class MovieControllerRA {
	
	private String adminUsername, adminPassword;
	private String clientUsername, clientPassword;
	private String adminToken, clientToken, invalidToken;
	private Long existingId, nonExistingId;
	
	private String movieTitle, testTitle;
	
	private Map<String, Object> postMovieInstance;
	
	@BeforeEach
	public void setUp() throws Exception {
		baseURI = "http://localhost:8080";
		
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		
		invalidToken = clientToken + "xpto";
		
		existingId = 1L;
		nonExistingId = 50L;
		
		movieTitle = "Venom";
		
		testTitle = "Test Movie";
		
		postMovieInstance = new HashMap<>();
		
		postMovieInstance.put("title", testTitle);
		postMovieInstance.put("score", 0.0F);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
	}
	
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
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		
		given()
			.get("/movies/{id}", existingId)
		.then()
			.statusCode(200)
			.body("id",is(1))
			.body("title", equalTo("The Witcher"))
			.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		
		given()
			.get("/movies/{id}", nonExistingId)
		.then()
			.statusCode(404)
			.body("error", equalTo("Recurso não encontrado"));
	}
	
	@Test
	public void insertShouldReturnMovieCreatedWhenAdminLogged() {
		
		JSONObject newProduct = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(201)
			.body("title", equalTo(testTitle));
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {	
		
		postMovieInstance.put("title", "");
		JSONObject newProduct = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(422)
			.body("errors.fieldName", hasItem("title"));
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		
		JSONObject newProduct = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(403);
	}
		
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		JSONObject newProduct = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newProduct)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(401);
	}
}

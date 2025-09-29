package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {
	
	private String adminUsername, adminPassword;
	private String clientUsername, clientPassword;
	private String adminToken, clientToken, invalidToken;
	private Long existingMovieId, nonExistingMovieId;
	
	private Map<String, Object> putScoreInstance;
	
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
		
		existingMovieId = 1L;
		nonExistingMovieId = 50L;
		
		putScoreInstance = new HashMap<>();
		
		putScoreInstance.put("movieId", existingMovieId);
		putScoreInstance.put("score", 4.0F);
	}
	
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
	
	@Test
	public void saveScoreShouldReturnOkWhenClientLoggedAndValidData() throws Exception {
		
		JSONObject newProduct = new JSONObject(putScoreInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
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
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		
		putScoreInstance.put("movieId", nonExistingMovieId);
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
			.statusCode(404)
			.body("error", equalTo("Recurso não encontrado"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		
		putScoreInstance.put("movieId", null);
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
			.statusCode(422)
			.body("error", equalTo("Dados inválidos"))
			.body("errors.fieldName[0]", equalTo("movieId"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		
		putScoreInstance.put("score", -5.0F);
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
			.statusCode(422)
			.body("error", equalTo("Dados inválidos"))
			.body("errors.fieldName[0]", equalTo("score"));
	}
}

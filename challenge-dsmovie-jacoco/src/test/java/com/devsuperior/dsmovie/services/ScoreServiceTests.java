package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ScoreRepository repository;
	
	@Mock
	private MovieRepository movieRepository;
	
	private Long existingId, nonExistingId, dependentId;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	private String movieTitle;
	private UserEntity user;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;
	private Double scorePoints;
	private Set<ScoreEntity> scoreList;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		scorePoints = 4.5;
		
		movie = MovieFactory.createMovieEntity();
		scoreList = new HashSet<>();
		ScoreEntity existingScore1 = new ScoreEntity();
	    existingScore1.setValue(3.0);
	    existingScore1.setMovie(movie);
	    
	    ScoreEntity existingScore2 = new ScoreEntity();
	    existingScore2.setValue(4.0);
	    existingScore2.setMovie(movie);
	    
	    scoreList.add(existingScore1);
	    scoreList.add(existingScore2);
	    
	    movie.getScores().addAll(scoreList);
		movieDTO = new MovieDTO(movie);
		movieTitle = "Test Movie";
		
		user = UserFactory.createUserEntity();
		score = ScoreFactory.createScoreEntity();
		scoreDTO = new ScoreDTO(score);
		
		when(movieRepository.findById(existingId)).thenReturn(Optional.of(movie));
		when(repository.saveAndFlush((ScoreEntity) any())).thenReturn(score);
		when(movieRepository.save((MovieEntity) any())).thenReturn(movie);
		when(userService.authenticated()).thenReturn(user);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
				
		MovieDTO result = service.saveScore(scoreDTO);
		
		assertNotNull(result);
		assertEquals(result.getTitle(), movieDTO.getTitle());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		
		scoreDTO = new ScoreDTO(nonExistingId, scorePoints);
		
		assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = service.saveScore(scoreDTO);
		});
	}
}

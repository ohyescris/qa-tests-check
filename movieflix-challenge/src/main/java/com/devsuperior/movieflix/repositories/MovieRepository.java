package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	@Query(nativeQuery = true, value = """
			SELECT * FROM (
		    SELECT DISTINCT tb_movie.id, tb_movie.title, tb_movie.sub_title, tb_movie.movie_year, tb_movie.img_url
		    FROM tb_movie
		    INNER JOIN tb_genre ON tb_genre.id = tb_movie.genre_id
		    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
			) AS tb_result
			""",
			countQuery = """
			SELECT COUNT(*) FROM (
			    SELECT DISTINCT tb_movie.id, tb_movie.title, tb_movie.sub_title, tb_movie.movie_year, tb_movie.img_url
		    FROM tb_movie
		    INNER JOIN tb_genre ON tb_genre.id = tb_movie.genre_id
		    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
			) AS tb_result
			""")
	Page<MovieProjection> searchMovies(Long genreId, Pageable pageable);
	
	@Query("""
			SELECT obj
			FROM Movie obj
			JOIN FETCH obj.genre
			WHERE obj.id IN :movieIds
			""")
	List<Movie> searchMoviesWithGenres(List<Long> movieIds);
}

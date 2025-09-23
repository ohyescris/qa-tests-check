package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil userUtil;

	private String clientUsername, nonExistingUsername;
	private List<UserDetailsProjection> clientDetails;

	private UserEntity user;

	@BeforeEach
	void setUp() throws Exception {
		clientUsername = "maria@gmail.com";
		nonExistingUsername = "user@gmail.com";

		user = UserFactory.createUserEntity();

		clientDetails = UserDetailsFactory.createCustomClientUser(clientUsername);

		when(repository.searchUserAndRolesByUsername(clientUsername)).thenReturn(clientDetails);
		when(repository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());

		when(repository.findByUsername((String) any())).thenReturn(Optional.of(user));
		when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {

		when(userUtil.getLoggedUsername()).thenReturn(clientUsername);

		UserEntity result = service.authenticated();

		assertNotNull(result);
		assertEquals(result.getUsername(), clientUsername);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		when(userUtil.getLoggedUsername()).thenReturn(nonExistingUsername);

		assertThrows(UsernameNotFoundException.class, () -> {
			UserEntity result = service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {

		UserDetails result = service.loadUserByUsername(clientUsername);

		assertNotNull(result);
		assertEquals(result.getUsername(), clientUsername);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		assertThrows(UsernameNotFoundException.class, () -> {
			UserDetails result = service.loadUserByUsername(nonExistingUsername);
		});
	}
}

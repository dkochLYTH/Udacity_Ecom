package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
	
	private UserController userController;
	
	private UserRepository userRepo = Mockito.mock(UserRepository.class);
	
	private CartRepository cartRepo = Mockito.mock(CartRepository.class);
	
	private BCryptPasswordEncoder encoder=Mockito.mock(BCryptPasswordEncoder.class);
	
	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.injectObject(userController, "userRepository", userRepo);
		TestUtils.injectObject(userController, "cartRepository", cartRepo);
		TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
	}
	
	@Test
	public void createUserHappypath() {
		Mockito.when(encoder.encode("testPW123")).thenReturn("thisIsHashed");
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("test");
		r.setPassword("testPW123");
		r.setConfirmPassword("testPW123");
		
		final ResponseEntity<User> response = userController.createUser(r);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
		assertEquals("test", u.getUsername());
		assertEquals("thisIsHashed", u.getPassword());
		
	}
	
	@Test
	public void testFindUserbyName() {
		String uname = "utest";
		String pw= "testPW123";
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername(uname);
		u1.setPassword(pw);
		
		Mockito.when(userRepo.findByUsername(uname)).thenReturn(u1);
		
		ResponseEntity<User> response = userController.findByUserName(uname);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(1, u.getId());
		assertEquals("utest", u.getUsername());
//		assertEquals("thisIsHashed", u.getPassword());
		
	}
	
	@Test
	public void testFindUserbyID() {
		String uname = "utest";
		String pw= "testPW123";
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername(uname);
		u1.setPassword(pw);
		
		Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(u1));
		
		ResponseEntity<User> response = userController.findById(1L);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(1, u.getId());
		assertEquals("utest", u.getUsername());
//		assertEquals("thisIsHashed", u.getPassword());
		
	}
	@Test
	public void testUsernotFound() {
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("user");
		
		Mockito.when(userRepo.findByUsername("user")).thenReturn(null);
		ResponseEntity<User> response = userController.findByUserName(u1.getUsername());
		assertEquals(404, response.getStatusCodeValue());
	}
	
	
}

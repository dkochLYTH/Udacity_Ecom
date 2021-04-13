package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.h2.command.ddl.CreateUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

	@InjectMocks
	private CartController cartController;

	@Mock
	private UserRepository userRepo;

	@Mock
	private CartRepository cartRepo;
	
	@Mock
	private ItemRepository itemRepo;

	@Before
	public void setUp() {
//		cartController = new CartController();
//		TestUtils.injectObject(cartController, "userRepository", userRepo);
//		TestUtils.injectObject(cartController, "cartRepository", cartRepo);
//		TestUtils.injectObject(cartController, "itemRepository", itemRepo);	
	}
	
	@Test
	public void testAddtoCart() {
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setItems(new ArrayList<Item>());
//		cart.setUser(u1);
		cart.setTotal(new BigDecimal(0));
		
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("utest1");
		u1.setPassword("testPW123");
		u1.setCart(cart);
		Item item = new Item();
		item.setId(1L);
		item.setName("TestItem");
		item.setDescription("TestDescription");
		item.setPrice(new BigDecimal(200.99));
			
		
		Mockito.when(userRepo.findByUsername("utest1")).thenReturn(u1);
		Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
		Mockito.when(cartRepo.save(cart)).thenReturn(null);
		      
		ModifyCartRequest request = new ModifyCartRequest();		
		request.setItemId(1L);
		request.setQuantity(3);
		request.setUsername(u1.getUsername());
		
		
		ResponseEntity<Cart> response = cartController.addTocart(request);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		Cart c = response.getBody();
		assertNotNull(c);
		assertEquals(request.getQuantity(), c.getItems().size());
		assertEquals(item.getPrice().multiply(new BigDecimal(request.getQuantity())), c.getTotal() );
//		assertEquals("thisIsHashed", u.getPassword());

	}
	@Test
	public void testRemovefromCart() {
		Cart cart = new Cart();
		cart.setId(1L);
//		cart.setItems(new ArrayList<Item>());
//		cart.setUser(u1);
//		cart.setTotal(new BigDecimal(0));
		
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("utest1");
		u1.setPassword("testPW123");
		u1.setCart(cart);
		
		Item item = new Item();
		item.setId(1L);
		item.setName("TestItem");
		item.setDescription("TestDescription");
		item.setPrice(new BigDecimal(200.99));
			
		cart.setUser(u1);
		cart.addItem(item);
		cart.addItem(item);
		cart.addItem(item);
		
		Mockito.when(userRepo.findByUsername("utest1")).thenReturn(u1);
		Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
		Mockito.when(cartRepo.save(cart)).thenReturn(null);
		
		ModifyCartRequest request = new ModifyCartRequest();		
		request.setItemId(1L);
		request.setQuantity(2);
		request.setUsername(u1.getUsername());
		
		
		ResponseEntity<Cart> response = cartController.removeFromcart(request);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		Cart c = response.getBody();
		assertNotNull(c);
		assertEquals("utest1", c.getUser().getUsername());
		assertEquals(1, c.getItems().size());
		assertEquals(item.getPrice(), c.getTotal() );
		
	}
	@Test
	public void testUsernotFound() {
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("user");
		
		ModifyCartRequest request = new ModifyCartRequest();		
		request.setItemId(1L);
		request.setQuantity(2);
		request.setUsername(u1.getUsername());
		
		Mockito.when(userRepo.findByUsername("user")).thenReturn(null);
		
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertEquals(404, response.getStatusCodeValue());
		
		ResponseEntity<Cart> response2 = cartController.removeFromcart(request);
		assertEquals(404, response2.getStatusCodeValue());
	}
	@Test
	public void testItemnotFound() {
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("user");
		
		Item item = new Item();
		item.setId(1L);
		
		ModifyCartRequest request = new ModifyCartRequest();		
//		request.setItemId(1L);
		request.setQuantity(2);
		request.setUsername(u1.getUsername());
		
		Mockito.when(userRepo.findByUsername("user")).thenReturn(u1);
//		Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.empty());
		
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertEquals(404, response.getStatusCodeValue());
		
		ResponseEntity<Cart> response2 = cartController.removeFromcart(request);
		assertEquals(404, response2.getStatusCodeValue());
	}
	
	
}

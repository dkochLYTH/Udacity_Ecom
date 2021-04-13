package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {
	
	@InjectMocks
	private OrderController orderController;

	@Mock
	private UserRepository userRepo= Mockito.mock(UserRepository.class);

	@Mock
	private OrderRepository orderRepo= Mockito.mock(OrderRepository.class);

	@Before
	public void setUp() {
		orderController = new OrderController();
		TestUtils.injectObject(orderController, "userRepository", userRepo);
		TestUtils.injectObject(orderController, "orderRepository", orderRepo);
//		TestUtils.injectObject(cartController, "itemRepository", itemRepo);	
	}
	
	@Test
	public void testSubmitOrder() {
		
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
			
		cart.setUser(u1);
		cart.addItem(item);
		cart.addItem(item);
		cart.addItem(item);
		
		Mockito.when(userRepo.findByUsername("utest1")).thenReturn(u1);
		
		ResponseEntity<UserOrder> response = orderController.submit(u1.getUsername());
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		UserOrder order = response.getBody();
		
		assertNotNull(order);
		assertEquals("utest1", order.getUser().getUsername());
		assertEquals(3, order.getItems().size());
		assertEquals(item.getPrice().multiply(new BigDecimal(cart.getItems().size())), order.getTotal() );
		
	}
	
	@Test
	public void testGetOrderforUsers() {
		
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
			
		cart.setUser(u1);
		cart.addItem(item);
		cart.addItem(item);
		cart.addItem(item);
		
		
		UserOrder order = new UserOrder();
		order.setId(1L);
		order.setUser(u1);
		order.setItems(cart.getItems());
		order.setTotal(cart.getTotal());
		
		List<UserOrder> orders = new ArrayList<UserOrder>();
		orders.add(order);
		
		Mockito.when(userRepo.findByUsername("utest1")).thenReturn(u1);
		Mockito.when(orderRepo.findByUser(u1)).thenReturn(orders);
		
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("utest1");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		UserOrder o = response.getBody().get(0);
		assertNotNull(order);
		assertEquals("utest1", order.getUser().getUsername());
		assertEquals(3, order.getItems().size());
		assertEquals(item.getPrice().multiply(new BigDecimal(cart.getItems().size())), order.getTotal() );
	
	}
	
	@Test
	public void testUsernotFound() {
		User u1 = new User();
		u1.setId(1L);
		u1.setUsername("user");
		
		Mockito.when(userRepo.findByUsername("user")).thenReturn(null);
		ResponseEntity<UserOrder> response = orderController.submit("user");
		assertEquals(404, response.getStatusCodeValue());
		ResponseEntity<List<UserOrder>> response2 = orderController.getOrdersForUser("user");
		assertEquals(404, response2.getStatusCodeValue());
		
		
	}
	
}

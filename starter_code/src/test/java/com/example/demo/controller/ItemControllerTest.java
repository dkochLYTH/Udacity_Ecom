package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

	@InjectMocks
	private ItemController itemController;

	@Mock
	private ItemRepository itemRepo = Mockito.mock(ItemRepository.class);

//	@Mock
//	private OrderRepository orderRepo= Mockito.mock(OrderRepository.class);

	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObject(itemController, "itemRepository", itemRepo);
//		TestUtils.injectObject(orderController, "orderRepository", orderRepo);
//		TestUtils.injectObject(cartController, "itemRepository", itemRepo);	
	}

	public Item createItem(String name) {
		Item item = new Item();
		item.setId(1L);
		item.setName(name);
		item.setDescription("TestDescription");
		item.setPrice(new BigDecimal(200.99));

		return item;
	}

	@Test
	public void testGetItems() {
		Item item1 = createItem("TestItem");
		Item item2 = createItem("Item2 ");
		List<Item> items = new ArrayList<Item>();
		items.add(item1);
		items.add(item2);
		Mockito.when(itemRepo.findAll()).thenReturn(items);

		ResponseEntity<List<Item>> response = itemController.getItems();
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());

		assertEquals(items.size(), response.getBody().size());
	}

	@Test
	public void testGetItembyId() {
		Item item1 = createItem("TestItem");

		Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
		ResponseEntity<Item> response = itemController.getItemById(1L);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Item item = response.getBody();
		assertEquals(item1.getId(), item.getId());
		assertEquals(item1.getDescription(), item.getDescription());
		assertEquals(item1.getName(), item.getName());
	}

	@Test
	public void testGetItemsbyName() {
		Item item1 = createItem("TestItem");
		Item item2 = createItem("TestItem");
		List<Item> items = new ArrayList<Item>();
		items.add(item1);
		items.add(item2);
		Mockito.when(itemRepo.findByName(item1.getName())).thenReturn(items);
		ResponseEntity<List<Item>> response = itemController.getItemsByName(item1.getName());

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Item item = response.getBody().get(0);
		assertEquals(item1.getId(), item.getId());
		assertEquals(item1.getDescription(), item.getDescription());
		assertEquals(item1.getName(), item.getName());
	}

}

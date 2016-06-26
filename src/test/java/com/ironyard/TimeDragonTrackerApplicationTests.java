package com.ironyard;

import com.ironyard.services.DragonRepository;
import com.ironyard.services.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeDragonTrackerApplication.class)
@WebAppConfiguration
public class TimeDragonTrackerApplicationTests
{

	@Autowired
	UserRepository users;

	@Autowired
	DragonRepository dragons;

	@Autowired
	WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
	public void before()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	}


	@Test
	public void testLogin() throws Exception
	{
		String username = "WhoGoesThere";
		String password = "logThis";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.param("username", username)
						.param("password",password));
		Assert.assertTrue(users.count() == 1);

	}

	@Test
	public void testCreateDragon() throws Exception
	{
		testLogin();

		mockMvc.perform(
				MockMvcRequestBuilders.post("/create-dragon")
						.param("name", "OhNoMisterBill")
						.param("color", "Aquamarine")
						.param("type", "Draka")
						.param("birthdate", LocalDateTime.now().toString())
						.param("age", "8675309")
						.sessionAttr("username", "WhoGoesThere")
		);
		System.out.println("Num of Dragons: " + dragons.count());
		Assert.assertTrue(dragons.count() == 1);

	}


	@Test
	public void testUpdateDragon() throws Exception
	{
		testLogin();

		mockMvc.perform(
				MockMvcRequestBuilders.post("/update-dragon")
						.param("id","1")
						.param("name", "bobbyBatitsta")
						.param("color", "Lavender")
						.param("type", "Serpentine")
						.param("birthdate", LocalDateTime.now().toString())
						.param("age", "12345678")
						.sessionAttr("username", "WhoGoesThere")
		);

		Assert.assertTrue(dragons.count() == 1);
		Assert.assertTrue(dragons.findById(1).getName().equals("bobbyBatitsta"));

	}

	@Test
	public void testDeleteDragon() throws Exception
	{
		testLogin();

		mockMvc.perform(
				MockMvcRequestBuilders.post("/delete-dragon")
						.param("id", "1")
						.sessionAttr("username", "WhoGoesThere")
		);

		Assert.assertTrue(dragons.count() == 0);
	}


}

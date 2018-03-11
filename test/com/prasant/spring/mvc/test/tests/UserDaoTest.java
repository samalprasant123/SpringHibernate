package com.prasant.spring.mvc.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.prasant.spring.mvc.dao.UserDAO;
import com.prasant.spring.mvc.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { 
		"classpath:com/prasant/spring/mvc/config/dao-context.xml", 
		"classpath:com/prasant/spring/mvc/test/config/dataSource.xml",
		"classpath:com/prasant/spring/mvc/config/security-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	public UserDAO userDao;
	
	@Before
	public void init() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("DELETE FROM offer");
		jdbcTemplate.execute("DELETE FROM users");
	}
	
	@Test
	public void testCreateUser() {
		User user = new User("grout", "grout@email.com", "password", true, "ROLE_USER", "Gayatree Rout");
		assertTrue("User creation successfull", userDao.create(user));
		List<User> users = userDao.getUsers();
		assertEquals("1 users currently", 1, users.size());
		assertTrue("User grout exists", userDao.getUser("grout"));
		assertEquals("Created user and 	retrieved User should be identical", user, users.get(0));
	}

}

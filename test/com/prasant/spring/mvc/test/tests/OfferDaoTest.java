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

import com.prasant.spring.mvc.dao.OfferDAO;
import com.prasant.spring.mvc.dao.UserDAO;
import com.prasant.spring.mvc.model.Offer;
import com.prasant.spring.mvc.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { 
		"classpath:com/prasant/spring/mvc/config/dao-context.xml", 
		"classpath:com/prasant/spring/mvc/test/config/dataSource.xml",
		"classpath:com/prasant/spring/mvc/config/security-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OfferDaoTest {
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	public OfferDAO offerDao;
	
	@Autowired
	public UserDAO userDao;
	
	@Before
	public void init() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//jdbcTemplate.execute("DELETE FROM offer");
		//jdbcTemplate.execute("DELETE FROM users");
	}
	
	@Test
	public void testOffer() {
		User user = new User("grout", "grout@cat.com", true, "ROLE_USER", "Gayatree Rout");
		//assertTrue("User creation successfull", userDao.create(user));
		Offer offer = new Offer(user, "I write awesome contents.");
		//assertTrue("Ofer creation successful", offerDao.create(offer));
		List<Offer> offers = offerDao.getOffersByUserName("grout");
		assertEquals("Retrieved offer should match created offer", offer, offers.get(0));
	}
}

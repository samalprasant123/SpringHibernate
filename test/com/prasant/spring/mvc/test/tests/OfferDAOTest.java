package com.prasant.spring.mvc.test.tests;

import static org.junit.Assert.assertEquals;

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
public class OfferDAOTest {
	
	@Autowired
	public DataSource dataSource;
	
	@Autowired
	public OfferDAO offerDao;
	
	@Autowired
	public UserDAO userDao;
	
	private User user1 = new User("grout", "grout@email.com", true, "ROLE_USER", "Gayatree Rout");
	
	private Offer offer1 = new Offer(user1, "I write awesome contents.");
	
	private User user2 = new User("testuser", "testuser@email.com", "password", true, "ROLE_USER", "Test User");
	
	private Offer offer2 = new Offer(user2, "This is a test offer.");
	
	@Before
	public void init() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("DELETE FROM offer");
	}
	
	/*@Test
	public void testOffer() {
		
		Offer offer = new Offer(user1, "I write awesome contents.");
		//assertTrue("Ofer creation successful", offerDao.create(offer));
		List<Offer> offers = offerDao.getOffersByUserName("grout");
		assertEquals("Retrieved offer should match created offer", offer, offers.get(0));
	}*/
	
	@Test
	public void create() {
		offerDao.create(offer1);
	}
	
	@Test
	public void getOffers() {
		offerDao.create(offer1);
		List<Offer> offers1 = offerDao.getOffers();
		assertEquals("There should be one offer", 1, offers1.size());
		
		userDao.create(user2);
		offerDao.create(offer2);
		List<Offer> offers2 = offerDao.getOffers();
		assertEquals("There should be one offer", 2, offers2.size());
	}
}

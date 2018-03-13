package com.prasant.spring.mvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prasant.spring.mvc.model.Offer;
import com.prasant.spring.mvc.model.User;

@Component("offerDao")
public class OfferDAO {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Offer> getOffers() {
		List<Offer> offers = null;
		try {			
			String sql = "SELECT * FROM offer, users WHERE offer.username = users.username AND users.enabled = true";
			offers = jdbcTemplate.query(sql, new OfferRowMapper());
		} catch(CannotGetJdbcConnectionException e) {
			System.out.println("Cannot connect to database " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return offers;
	}
	
	public Offer getOfferById(int id) {
		Offer offer = null;
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);
		String sql = "SELECT * FROM offer, users WHERE offer.username = users.username AND id = :id";
		offer = jdbcTemplate.queryForObject(sql, paramMap, new RowMapper<Offer>() {
			@Override
			public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User(rs.getString("username"), 
						rs.getString("email"), 
						rs.getBoolean("enabled"), 
						rs.getString("authority"), 
						rs.getString("name"));
				Offer offer = new Offer(rs.getInt("id"), user, rs.getString("text"));
				return offer;
			}
		});
		return offer;
	}
	
	public List<Offer> getOffersByUserName(String username) {
		List<Offer> offers = null;
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("username", username);
		String sql = "SELECT * FROM offer, users WHERE offer.username = users.username AND users.enabled = true AND offer.username = :username";
		offers = jdbcTemplate.query(sql, paramMap, new OfferRowMapper());
		return offers;
	}
	
	public boolean delete(int id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		String sql = "DELETE FROM offer WHERE id = :id";
		return jdbcTemplate.update(sql, paramMap) == 1;		
	}
	
	public boolean create(Offer offer) {
		BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(offer);
		String sql = "INSERT INTO offer(username, text) VALUES(:username, :text)";
		return jdbcTemplate.update(sql, paramMap) == 1;
	}
	
	public boolean update(Offer offer) {
		BeanPropertySqlParameterSource paramMap =  new BeanPropertySqlParameterSource(offer);
		String sql = "UPDATE offer SET 	text = :text WHERE id = :id";
		return jdbcTemplate.update(sql, paramMap) == 1;
	}
	
	public int[] createOffers(List<Offer> offers) {
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(offers.toArray());
		String sql = "INSERT INTO offer(username, text) VALUES(:username, :text)";
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	
	@Transactional
	public void testTransactional(Offer o1, Offer o2) {
		boolean create = false, update = false;
		BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(o1);
		String sql = "INSERT INTO offer(name, email, text) VALUES(:name, :email, :text)";
		create = jdbcTemplate.update(sql, paramMap) == 1;
		if (create)
			System.out.println("Creation Successfull");
		BeanPropertySqlParameterSource paramMapU =  new BeanPropertySqlParameterSource(o2);
		String sqlU = "UPDATE offer SET name = :name, email = :email, text = :text WHERE id = :id";
		update = jdbcTemplate.update(sqlU, paramMapU) == 1;
		if (update)
			System.out.println("Update Successfull");
	}

}

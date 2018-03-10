package com.prasant.spring.mvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.prasant.spring.mvc.model.Offer;
import com.prasant.spring.mvc.model.User;

public class OfferRowMapper implements RowMapper<Offer> {

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

}

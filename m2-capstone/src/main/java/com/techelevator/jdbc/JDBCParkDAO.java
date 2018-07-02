package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import model.Park;
import model.ParkDAO;



public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		ArrayList<Park> allParks = new ArrayList<>();
		String sqlListAllParks =  "SELECT * " +
									"FROM park "
								  + "ORDER BY name ASC ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllParks);
		while(results.next()) {
			Park name = mapRowToPark(results);
			allParks.add(name);
		}
		return allParks;
	}
	
	@Override
	public List<Park> searchParkName(String inputParkName) {
		ArrayList<Park> searchPark = new ArrayList<>();
		String sqlListSearchPark = "SELECT * "
								+ "FROM park "
								+ "WHERE name = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListSearchPark, inputParkName);
		while(results.next()) {
			Park name = mapRowToPark(results);
			searchPark.add(name);
		}
		return searchPark;
	}
	
	private Park mapRowToPark(SqlRowSet results) {
		Park park;
		park = new Park();
		park.setId(results.getLong("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		if (results.getDate("establish_date") != null) {
			park.setEstablishedDate(results.getDate("establish_date").toLocalDate());
		}
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		return park;
	}


	
}

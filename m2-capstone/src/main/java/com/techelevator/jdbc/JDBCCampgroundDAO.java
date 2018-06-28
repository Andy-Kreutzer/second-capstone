package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Campground;
import com.techelevator.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> getAllCampgrounds(String campgroundChoice) {
		ArrayList<Campground> allCampgrounds = new ArrayList<>();
		String sqlListAllCampgrounds =  "SELECT campground_id, campground.park_id, campground.name, open_from_mm, open_to_mm, daily_fee " +
									"FROM campground "
									+"JOIN park ON campground.park_id = park.park_id "
									+ "WHERE park.name = '"	+ campgroundChoice + "'";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllCampgrounds);
		System.out.println("Campgrounds: ");
		while(results.next()) {
			Campground name = mapRowToCampground(results);
			allCampgrounds.add(name);
		}
		return allCampgrounds;
	}

	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground;
		campground = new Campground();
		campground.setCampground_id(results.getLong("campground_id"));
		campground.setPark_id(results.getLong("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpen_from_mm(results.getString("open_from_mm"));
		campground.setOpen_to_mm(results.getString("open_to_mm"));
		campground.setDaily_fee(results.getDouble("daily_fee"));
		return campground;
	}
}

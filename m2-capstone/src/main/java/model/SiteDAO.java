package model;

import java.util.List;

public interface SiteDAO {
	
	public List<Site> getAllSites();
	
	public List<Site> getSitesByCampgroundIdAndDates(long campgroundId, String arrival, String departure, int arrivalMonth, int departureMonth);
	
}

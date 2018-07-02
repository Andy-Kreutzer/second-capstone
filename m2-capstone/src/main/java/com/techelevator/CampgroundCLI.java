package com.techelevator;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.jdbc.JDBCCampgroundDAO;
import com.techelevator.jdbc.JDBCParkDAO;
import com.techelevator.jdbc.JDBCReservationDAO;
import com.techelevator.jdbc.JDBCSiteDAO;

import model.Campground;
import model.CampgroundDAO;
import model.Park;
import model.ParkDAO;
import model.Reservation;
import model.ReservationDAO;
import model.Site;
import model.SiteDAO;
import view.Menu;


public class CampgroundCLI {
	
	private static final String MAIN_MENU_OPTION_PARKS = "View Parks";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, 
																	 MAIN_MENU_OPTION_EXIT};
	
	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	
	private static final String PARK_ACADIA = "Acadia";
	private static final String PARK_ARCHES = "Arches";
	private static final String PARK_CUYAHOGA = "Cuyahoga Valley";
	private static final String[] PARK_OPTIONS = new String[] {PARK_ACADIA, PARK_ARCHES, PARK_CUYAHOGA, MENU_OPTION_RETURN_TO_MAIN};
	private static final String linebreak = "\n"; // or "\r\n";
	
	private static final String CAMPGROUND_COMMAND = "View Campgrounds";
	private static final String CAMPGROUND_RESERVATION = "Search for Reservation";
	private static final String CAMPGROUND_RETURN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_OPTIONS = new String[] {CAMPGROUND_COMMAND, CAMPGROUND_RESERVATION, CAMPGROUND_RETURN, MENU_OPTION_RETURN_TO_MAIN};
	
	private static final String BOOK_COMMAND = "Search for Available Reservations";
	private static final String[] BOOK_OPTIONS = new String[] {BOOK_COMMAND, CAMPGROUND_RETURN};
	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;
	
	public static void main(String[] args) {
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		//dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
	}
	
	public void run() {
		
		while(true) {
			printHeading("National Park Service");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(choice.equals(MAIN_MENU_OPTION_PARKS)) {
				handleSelectAllParks();
			} else if(choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}
	
	private void handleSelectAllParks() {
		List<Park> allParks = parkDAO.getAllParks();
		if(allParks.size() > 0) {
			printHeading("Select a Park for Further Details");
			String choice = (String)menu.getChoiceFromOptions(PARK_OPTIONS);
			if(choice.equals(PARK_ACADIA)) {
				handleAcadia(choice);
			} else if(choice.equals(PARK_ARCHES)) {
				handleArches(choice);
			} else if(choice.equals(PARK_CUYAHOGA)) {
				handleCuyahoga(choice);
			}
		}
	}

	private void handleAcadia(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
		System.out.println("Select a Command");
		String campgroundChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
		if(campgroundChoice.equals(CAMPGROUND_COMMAND)) {
			handleCampgrounds(choice);
		} else if(campgroundChoice.equals(CAMPGROUND_RESERVATION)) {
			handleReservations();
		} 
		else if(campgroundChoice.equals(CAMPGROUND_RETURN)) {
			handleSelectAllParks();
		}
	}	

	

	private void handleArches(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
		System.out.println("Select a Command");
		String campgroundChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
		if(campgroundChoice.equals(CAMPGROUND_COMMAND)) {
			handleCampgrounds(choice);
		} else if(campgroundChoice.equals(CAMPGROUND_RESERVATION)) {
			handleReservations();
		} 
		else if(campgroundChoice.equals(CAMPGROUND_RETURN)) {
			handleSelectAllParks();
		}
	}	
	
	
	private void handleCuyahoga(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
		System.out.println("Select a Command");
		String campgroundChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
		if(campgroundChoice.equals(CAMPGROUND_COMMAND)) {
			handleCampgrounds(choice);
		} else if(campgroundChoice.equals(CAMPGROUND_RESERVATION)) {
			handleReservations();
		} 
		else if(campgroundChoice.equals(CAMPGROUND_RETURN)) {
			handleSelectAllParks();
		}
	}
	
	private void handleCampgrounds(String campgroundChoice) {
		printHeading("All Campgrounds");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(campgroundChoice);
		listCampgrounds(allCampgrounds);
		String campYesNo = (String)menu.getChoiceFromOptions(BOOK_OPTIONS);
		if(campYesNo.equals(BOOK_COMMAND)) {
			handleBooking(campgroundChoice);
		} 
		else if(campYesNo.equals(CAMPGROUND_RETURN)) {
			handleSelectAllParks();
		}
	}
	
	private void handleBooking(String campgroundChoice) {
		printHeading("Search for Campground Reservation");
	    List<Site> allSites = siteDAO.getAllSites();
	    
	    String campgroundNum = getUserInput("Which campground (enter 0 to cancel)?");	    
	    Long campgroundNumLong = Long.parseLong(campgroundNum);  
	    String arrivalDate = null;
	    String departureDate = null;
	    
	    while(campgroundNumLong != 0) {
	    	arrivalDate = getUserInput("What is the arrival date? (enter as YYYY/MM/DD)");
		    departureDate = getUserInput("What is the departure date? (enter as YYYY/MM/DD)");
		    int arrivalMonth = Integer.parseInt(arrivalDate.substring(5, 7));
		    int departureMonth = Integer.parseInt(departureDate.substring(5, 7));
			
		    if(allSites.size() > 0) {
				List<Site> availableSites = siteDAO.getSitesByCampgroundIdAndDates(campgroundNumLong, arrivalDate, departureDate, arrivalMonth, departureMonth);
				listSites(availableSites);
				campgroundNumLong = (long) 0;
				
				if(availableSites.size() > 0) {
					String siteNumToReserve = getUserInput("Which site should be reserved (enter 0 to cancel)?");	    
				    int siteNumInt = Integer.parseInt(siteNumToReserve);
				    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				    LocalDate localArrivalDate = LocalDate.parse(arrivalDate, formatter);
				    LocalDate localDepartureDate = LocalDate.parse(departureDate, formatter);
				   
				    while(siteNumInt != 0) {
				    	String nameToReserve = getUserInput("What name should the reservation be under?");
				    	reservationDAO.setReservation(siteNumInt, nameToReserve, localArrivalDate, localDepartureDate, LocalDate.now());
				    	List<Reservation> reservationId = reservationDAO.getReservationId(nameToReserve);
				    	listReservationId(reservationId);
				    	siteNumInt = 0;
				    	}
					} 
				}
		    }
	    
	    
	   
	}

	private void handleReservations() {
		// TODO Auto-generated method stub
		
	}
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private void listParkInfo(List<Park> parks) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DecimalFormat df = new DecimalFormat("#,###,###");
		System.out.println();
		if(parks.size() > 0) {
			for(Park park : parks) {
				System.out.println(park.getName().toString() + " National Park");
				System.out.printf("Location: \t " + park.getLocation().toString() + "\n");
				System.out.printf("Established: \t " + park.getEstablishedDate().format(formatter).toString() + "\n");
				System.out.printf("Area: \t \t " + df.format(park.getArea()) + " " + "sq km" + "\n");
				System.out.printf("Annual Visitors: " + df.format(park.getVisitors()) + "\n");
				System.out.println();
				System.out.printf(wrap(park.getDescription(), 100));
				System.out.println();
				System.out.println();
			}
		}
	}
	
	
	private void listReservationId(List<Reservation> id) {
		System.out.println(); 
		if(id.size() > 0) {
			for (Reservation i : id) {
				System.out.println("The reservation has been made and the confirmation id is: " + i.getReservation_id());
			}
		}
	}
	
	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.println();
		if(campgrounds.size() > 0) {
			System.out.printf("%-5s %-35s %-10s %-10s %-15s\n", " ", "Name", "Open", "Close", "Daily Fee");
			for(Campground camp : campgrounds) {	
				System.out.printf("%-5s %-35s %-10s %-10s $%-15.2f\n", "#" + camp.getCampground_id(), camp.getName().toString(), formatMonth(Integer.parseInt(camp.getOpen_from_mm())), formatMonth(Integer.parseInt(camp.getOpen_to_mm())), camp.getDaily_fee());
			}
		} else {
			System.out.println("\n*** No Results ***");
		}
	}
	
	private void listSites(List<Site> sites) {
		System.out.println();
		if(sites.size() > 0) {
			System.out.printf("%-15s %-10s %-10s %-15s %-15s %-15s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
			for(Site site : sites) {
				System.out.printf("%-15s %-10s %-10s %-15s %-15s $%-15.2f\n", site.getSite_number(), site.getMax_occupancy(), translate(site.isAccessible()), rvTranslate(site.getMax_rv_length()), translate(site.isUtilities()), site.getDailyFee());
			}
		} else {
			System.out.println("\nReservation could not be fulfilled due to an invalid date range! You will be directed to the main menu to try again.");
		}
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
	public static String translate(boolean trueOrFalse) {
		return trueOrFalse ? "Yes" : "No";
	}
	
	public static String translateUtility(boolean trueOrFalse) {
		return trueOrFalse ? "Yes" : "N/A";
	}
	
	public static String rvTranslate(int length) {
		if(length == 0) {
			return "N/A";
		}
		else {
			return Integer.toString(length);
		}
	}
	
	public String formatMonth(int month) {	
		return new DateFormatSymbols().getMonths()[month -1];
	}
	
	public static String wrap(String string, int lineLength) {
		StringBuilder b = new StringBuilder();
		for (String line : string.split(Pattern.quote(linebreak))) {
		b.append(wrapLine(line, lineLength));
		}
		return b.toString();
		}
	
	private static String wrapLine(String line, int lineLength) {
	    if (line.length() == 0) return linebreak;
	    if (line.length() <= lineLength) return line + linebreak;
	    String[] words = line.split(" ");
	    StringBuilder allLines = new StringBuilder();
	    StringBuilder trimmedLine = new StringBuilder();
	    for (String word : words) {
	        if (trimmedLine.length() + 1 + word.length() <= lineLength) {
	            trimmedLine.append(word).append(" ");
	        } else {
	            allLines.append(trimmedLine).append(linebreak);
	            trimmedLine = new StringBuilder();
	            trimmedLine.append(word).append(" ");
	        }
	    }
	    if (trimmedLine.length() > 0) {
	        allLines.append(trimmedLine);
	    }
	    allLines.append(linebreak);
	    return allLines.toString();
	}
	
	
}

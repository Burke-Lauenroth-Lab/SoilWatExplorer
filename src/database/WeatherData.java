package database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

public class WeatherData {
	
	private Path weatherDatabase;
	private Connection connection = null;
	
	public class YearData {
		public int id;
		public int year;
		public int days;
		public double[] Tmax;
		public double[] Tmin;
		public double[] ppt;
	}
	
	private List<MapMarkerDot> sites = new ArrayList<MapMarkerDot>();
	private List<YearData> weatherData = new ArrayList<YearData>();
	
	public WeatherData(Path weatherDB) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.weatherDatabase = weatherDB;
		this.connect();
	}
	
	public WeatherData(String weatherDB) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.weatherDatabase = Paths.get(weatherDB);
		this.connect();
	}

	public WeatherData() {
		Path weatherDB = Paths.get("/media/ryan/Storage/Users/Ryan_Murphy/My_Documents/Work/dbWeatherData.sqlite");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.weatherDatabase = weatherDB;
		this.connect();
	}

	private void connect() {
		try
	    {
	      // create a database connection
	      connection = DriverManager.getConnection("jdbc:sqlite:"+weatherDatabase.toString());
	    }
	    catch(SQLException e)
	    {
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	}
	
	public List<YearData> getData(int Site_id, int startYear, int endYear) {
		weatherData.clear();
		for(int i=startYear; i<= endYear; i++) {
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30);  // set timeout to 30 sec.
				YearData data = new YearData();
				
				ResultSet rs = statement.executeQuery("SELECT COUNT(Site_id) FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+" AND Year="+String.valueOf(i)+";");
				rs.next();
				data.days = rs.getInt(1);
				rs = statement.executeQuery("SELECT Tmax_C, Tmin_C, PPT_cm FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+" AND Year="+String.valueOf(i)+" ORDER BY DOY;");
				data.id = Site_id;
				data.year = i;
				data.ppt = new double[data.days];
				data.Tmax = new double[data.days];
				data.Tmin = new double[data.days];
				int j=0;
				while(rs.next()) {
					data.Tmax[j] = rs.getDouble(1);
					data.Tmin[j] = rs.getDouble(2);
					data.ppt[j] = rs.getDouble(3);
					j++;
				}
				weatherData.add(data);
			} catch(SQLException e) {
		    	System.err.println(e.getMessage());
		    }
		}
		return weatherData;
	}
	
	public List<MapMarkerDot> getSites(double Lat_min, double Lat_max, double Long_min, double Long_max, double min_MAT_C, double max_MAT_C, double min_MAP_cm, double max_MAP_cm) {
		this.sites.clear();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String minlat = String.format("%.3f", Lat_min);
			String maxlat = String.format("%.3f", Lat_max);
			String minlong = String.format("%.3f", Long_min);
			String maxlong = String.format("%.3f", Long_max);
			String minMAT = String.format("%.5f", min_MAT_C);
			String maxMAT = String.format("%.5f", max_MAT_C);
			String minMAP = String.format("%.5f", min_MAP_cm);
			String maxMAP = String.format("%.5f", max_MAP_cm);
			String sqlAmbient = "SELECT Site_id FROM climateambient WHERE MAP_cm BETWEEN "+minMAP+" AND "+maxMAP+" AND MAT_C BETWEEN "+minMAT+" AND "+maxMAT;
			String sql = "SELECT * FROM sites WHERE";
			if((Lat_max - Lat_min) > 0) {
				sql+=" Latitude BETWEEN "+minlat+" AND "+maxlat+" AND";
			}
			if((Long_max - Long_min) > 0) {
				sql+=" Longitude BETWEEN "+minlong+" AND "+maxlong+" AND";
			}
			sql+=" Site_id IN("+sqlAmbient+");";
			
	
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				Coordinate n = new Coordinate(rs.getDouble("Latitude"), rs.getDouble("Longitude"));
				sites.add(new MapMarkerDot(rs.getInt("Site_id"), n));
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return this.sites;
	}
	
	public double getMaxMeanAnnualTemp() {
		double maxMAT_c = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(MAT_C) AS MAT_C FROM climateambient;");
			while(rs.next()) {
				maxMAT_c = rs.getDouble("MAT_C");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return maxMAT_c;
	}
	
	public double getMinMeanAnnualTemp() {
		double minMAT_c = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(MAT_C) AS MAT_C FROM climateambient;");
			while(rs.next()) {
				minMAT_c = rs.getDouble("MAT_C");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return minMAT_c;
	}
	
	public double getMaxMeanAnnualPPT() {
		double maxMAP_cm = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(MAP_cm) AS MAP_cm FROM climateambient;");
			while(rs.next()) {
				maxMAP_cm = rs.getDouble("MAP_cm");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return maxMAP_cm;
	}
	
	public double getMinMeanAnnualPPT() {
		double minMAP_cm = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(MAP_cm) AS MAP_cm FROM climateambient;");
			while(rs.next()) {
				minMAP_cm = rs.getDouble("MAP_cm");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return minMAP_cm;
	}
	
	//Latitude and Longitude
	public double getMinLatitude() {
		double MinLatitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(Latitude) AS Latitude FROM sites;");
			while(rs.next()) {
				MinLatitude = rs.getDouble("Latitude");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return MinLatitude;
	}
	public double getMaxLatitude() {
		double MaxLatitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(Latitude) AS Latitude FROM sites;");
			while(rs.next()) {
				MaxLatitude = rs.getDouble("Latitude");
			}
		} catch(SQLException e) {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
		return MaxLatitude;
	}
	public double getMinLongitude() {
		double MinLongitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(Longitude) AS Longitude FROM sites;");
			while(rs.next()) {
				MinLongitude = rs.getDouble("Longitude");
			}
		} catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		return MinLongitude;
	}
	public double getMaxLongitude() {
		double MaxLongitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(Longitude) AS Longitude FROM sites;");
			while(rs.next()) {
				MaxLongitude = rs.getDouble("Longitude");
			}
		} catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		return MaxLongitude;
	}
	public int getNumberSites() {
		int nSites = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS nSites FROM sites;");
			while(rs.next()) {
				nSites = rs.getInt("nSites");
			}
		} catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		return nSites;
	}
	public int getMinYear(int Site_id) {
		int year = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(Year) AS year FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+" ORDER BY Year;");
			while(rs.next()) {
				year = rs.getInt("year");
			}
		} catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		return year;
	}
	public int getMaxYear(int Site_id) {
		int year = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(Year) AS year FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+" ORDER BY Year;");
			while(rs.next()) {
				year = rs.getInt("year");
			}
		} catch(SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		return year;
	}
	public double[] getMeanMonthlyTemp(int Site_id) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "MMTemp_C_m1,MMTemp_C_m2,MMTemp_C_m3,MMTemp_C_m4,MMTemp_C_m5,MMTemp_C_m6,MMTemp_C_m7,MMTemp_C_m8,MMTemp_C_m9,MMTemp_C_m10,MMTemp_C_m11,MMTemp_C_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climateambient WHERE Site_id="+String.valueOf(Site_id)+";");
			while(rs.next()) {
				temp[0] = rs.getDouble(1);
				temp[1] = rs.getDouble(2);
				temp[2] = rs.getDouble(3);
				temp[3] = rs.getDouble(4);
				temp[4] = rs.getDouble(5);
				temp[5] = rs.getDouble(6);
				temp[6] = rs.getDouble(7);
				temp[7] = rs.getDouble(8);
				temp[8] = rs.getDouble(9);
				temp[9] = rs.getDouble(10);
				temp[10] = rs.getDouble(11);
				temp[11] = rs.getDouble(12);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double[] getMeanMonthlyPPT(int Site_id) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "MMPPT_C_m1,MMPPT_C_m2,MMPPT_C_m3,MMPPT_C_m4,MMPPT_C_m5,MMPPT_C_m6,MMPPT_C_m7,MMPPT_C_m8,MMPPT_C_m9,MMPPT_C_m10,MMPPT_C_m11,MMPPT_C_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climateambient WHERE Site_id="+String.valueOf(Site_id)+";");
			while(rs.next()) {
				temp[0] = rs.getDouble(1);
				temp[1] = rs.getDouble(2);
				temp[2] = rs.getDouble(3);
				temp[3] = rs.getDouble(4);
				temp[4] = rs.getDouble(5);
				temp[5] = rs.getDouble(6);
				temp[6] = rs.getDouble(7);
				temp[7] = rs.getDouble(8);
				temp[8] = rs.getDouble(9);
				temp[9] = rs.getDouble(10);
				temp[10] = rs.getDouble(11);
				temp[11] = rs.getDouble(12);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double getMAT(int Site_id) {
		double temp=0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAT_C FROM climateambient WHERE Site_id="+String.valueOf(Site_id)+";");
			while(rs.next()) {
				temp = rs.getDouble(1);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double getMAP(int Site_id) {
		double temp=0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAP_cm FROM climateambient WHERE Site_id="+String.valueOf(Site_id)+";");
			while(rs.next()) {
				temp = rs.getDouble(1);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return temp;
	}
}

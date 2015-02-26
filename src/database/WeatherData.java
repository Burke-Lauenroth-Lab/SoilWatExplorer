package database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import soilwat.SW_WEATHER_HISTORY;
import soilwat.SW_WEATHER_HISTORY.WeatherException;

public class WeatherData {

	private String climateColumns = "MAT_C,MAP_cm," +
			"MMTemp_C_m1,MMTemp_C_m2,MMTemp_C_m3,MMTemp_C_m4,MMTemp_C_m5,MMTemp_C_m6,MMTemp_C_m7,MMTemp_C_m8,MMTemp_C_m9,MMTemp_C_m10,MMTemp_C_m11,MMTemp_C_m12," +
			"MMPPT_C_m1,MMPPT_C_m2,MMPPT_C_m3,MMPPT_C_m4,MMPPT_C_m5,MMPPT_C_m6,MMPPT_C_m7,MMPPT_C_m8,MMPPT_C_m9,MMPPT_C_m10,MMPPT_C_m11,MMPPT_C_m12," +
			"ClimatePerturbations_PrcpMultiplier_m1,ClimatePerturbations_PrcpMultiplier_m2,ClimatePerturbations_PrcpMultiplier_m3,ClimatePerturbations_PrcpMultiplier_m4,ClimatePerturbations_PrcpMultiplier_m5,ClimatePerturbations_PrcpMultiplier_m6,ClimatePerturbations_PrcpMultiplier_m7,ClimatePerturbations_PrcpMultiplier_m8,ClimatePerturbations_PrcpMultiplier_m9,ClimatePerturbations_PrcpMultiplier_m10,ClimatePerturbations_PrcpMultiplier_m11,ClimatePerturbations_PrcpMultiplier_m12," +
			"ClimatePerturbations_TmaxAddand_m1,ClimatePerturbations_TmaxAddand_m2,ClimatePerturbations_TmaxAddand_m3,ClimatePerturbations_TmaxAddand_m4,ClimatePerturbations_TmaxAddand_m5,ClimatePerturbations_TmaxAddand_m6,ClimatePerturbations_TmaxAddand_m7,ClimatePerturbations_TmaxAddand_m8,ClimatePerturbations_TmaxAddand_m9,ClimatePerturbations_TmaxAddand_m10,ClimatePerturbations_TmaxAddand_m11,ClimatePerturbations_TmaxAddand_m12," +
			"ClimatePerturbations_TminAddand_m1,ClimatePerturbations_TminAddand_m2,ClimatePerturbations_TminAddand_m3,ClimatePerturbations_TminAddand_m4,ClimatePerturbations_TminAddand_m5,ClimatePerturbations_TminAddand_m6,ClimatePerturbations_TminAddand_m7,ClimatePerturbations_TminAddand_m8,ClimatePerturbations_TminAddand_m9,ClimatePerturbations_TminAddand_m10,ClimatePerturbations_TminAddand_m11,ClimatePerturbations_TminAddand_m12";
	private String insertClimateTableSQL = "INSERT INTO climate"
			+ "(Site_id, Scenario, StartYear, EndYear, "+climateColumns+") VALUES"
			+ "("+new String(new char[65]).replace("\0", "?,")+"?)";
	
	private Path weatherDatabase;
	private Connection connection = null;
	private Connection connection2 = null;
	private PreparedStatement insertData;
	private PreparedStatement insertClimate;
	private PreparedStatement insertSites;
	private PreparedStatement insertScenarios;
	//private soilwat.SW_WEATHER_HISTORY hist;
	
	public class YearData {
		NumberFormat dFormatTwo = new DecimalFormat("#.##");
		
		public int id;
		public int scenario;
		public int year;
		public int days;
		public double[] Tmax;
		public double[] Tmin;
		public double[] ppt;
		
		public String toString() {
			String out = new String();
			for(int i=0; i<days; i++) {
				out += dFormatTwo.format(Tmax[i]) + "," + dFormatTwo.format(Tmin[i]) + "," + dFormatTwo.format(ppt[i]) + "\n";
			}
			out+=";";
			return out;
		}
	}
	
	private List<MapMarkerDot> sites = new ArrayList<MapMarkerDot>();
	private List<YearData> weatherData = new ArrayList<YearData>();
	
	public WeatherData(Path weatherDB) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
		this.weatherDatabase = weatherDB;
		this.connect();
	}
	
	public WeatherData(String weatherDB) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
		this.weatherDatabase = Paths.get(weatherDB);
		this.connect();
	}

	public WeatherData() {
		Path weatherDB = Paths.get("/media/ryan/Storage/WeatherData/dbWeatherData_GTD.sqlite");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.toString());
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
	    	JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
	}
	
	public Path getDatabasePath() {
		return this.weatherDatabase;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public byte[] compresseString(String str) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DeflaterOutputStream gzip = new DeflaterOutputStream(out);
		try {
			out.reset();
			gzip.write(str.getBytes(StandardCharsets.US_ASCII));
			gzip.finish();
			gzip.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
		
		return out.toByteArray();
	}
	
	public String uncompressBytes(byte[] data) {
		//write some initial stuff to the buffer and then add data
		ByteArrayOutputStream in = new ByteArrayOutputStream();
		DeflaterOutputStream dgzip = new DeflaterOutputStream(in);
		try {
			dgzip.flush();
			in.write(data);
			in.flush();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.toString());
			e1.printStackTrace();
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InflaterOutputStream gzip = new InflaterOutputStream(out);
		try {
			gzip.write(in.toByteArray());
			gzip.finish();
			gzip.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
		return new String(out.toByteArray());
	}
	
	public void createTableWeatherData(Connection con) {
		String weatherData = "CREATE TABLE \"WeatherData\" (\"Site_id\" INT NOT NULL, \"Scenario\" INT NOT NULL, \"StartYear\" INT NOT NULL, \"EndYear\" INT NOT NULL, \"data\" BLOB, PRIMARY KEY (\"Site_id\", \"Scenario\"));";
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate(weatherData);
			statement.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	        System.err.println(e.getMessage());
	    }
	}
	
	public void createTableVersion(Connection con) {
		String version = "CREATE TABLE \"Version\" (\"Version\" integer);";
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate(version);
			statement.executeUpdate("INSERT INTO Version (Version) VALUES (1);");
			statement.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	        System.err.println(e.getMessage());
	    }
	}
	
	public void createTableSites(Connection con) {
		String sites = "CREATE TABLE \"Sites\" (\"Site_id\" integer PRIMARY KEY, \"Latitude\" REAL, \"Longitude\" REAL, \"Label\" TEXT);";
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate(sites);
			statement.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	        System.err.println(e.getMessage());
	    }
	}
	
	public void createTableScenarios(Connection con) {
		String scenarios = "CREATE TABLE \"Scenarios\" (\"id\" integer PRIMARY KEY, \"Scenario\" TEXT);";
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate(scenarios);
			statement.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	        System.err.println(e.getMessage());
	    }
	}
	
	public boolean createTableClimate(Connection con) {
		String columns = "MAT_C REAL,MAP_cm REAL," +
				"MMTemp_C_m1 REAL,MMTemp_C_m2 REAL,MMTemp_C_m3 REAL,MMTemp_C_m4 REAL,MMTemp_C_m5 REAL,MMTemp_C_m6 REAL,MMTemp_C_m7 REAL,MMTemp_C_m8 REAL,MMTemp_C_m9 REAL,MMTemp_C_m10 REAL,MMTemp_C_m11 REAL,MMTemp_C_m12 REAL," +
				"MMPPT_C_m1 REAL,MMPPT_C_m2 REAL,MMPPT_C_m3 REAL,MMPPT_C_m4 REAL,MMPPT_C_m5 REAL,MMPPT_C_m6 REAL,MMPPT_C_m7 REAL,MMPPT_C_m8 REAL,MMPPT_C_m9 REAL,MMPPT_C_m10 REAL,MMPPT_C_m11 REAL,MMPPT_C_m12 REAL," +
				"ClimatePerturbations_PrcpMultiplier_m1 REAL,ClimatePerturbations_PrcpMultiplier_m2 REAL,ClimatePerturbations_PrcpMultiplier_m3 REAL,ClimatePerturbations_PrcpMultiplier_m4 REAL,ClimatePerturbations_PrcpMultiplier_m5 REAL,ClimatePerturbations_PrcpMultiplier_m6 REAL,ClimatePerturbations_PrcpMultiplier_m7 REAL,ClimatePerturbations_PrcpMultiplier_m8 REAL,ClimatePerturbations_PrcpMultiplier_m9 REAL,ClimatePerturbations_PrcpMultiplier_m10 REAL,ClimatePerturbations_PrcpMultiplier_m11 REAL,ClimatePerturbations_PrcpMultiplier_m12 REAL," +
				"ClimatePerturbations_TmaxAddand_m1 REAL,ClimatePerturbations_TmaxAddand_m2 REAL,ClimatePerturbations_TmaxAddand_m3 REAL,ClimatePerturbations_TmaxAddand_m4 REAL,ClimatePerturbations_TmaxAddand_m5 REAL,ClimatePerturbations_TmaxAddand_m6 REAL,ClimatePerturbations_TmaxAddand_m7 REAL,ClimatePerturbations_TmaxAddand_m8 REAL,ClimatePerturbations_TmaxAddand_m9 REAL,ClimatePerturbations_TmaxAddand_m10 REAL,ClimatePerturbations_TmaxAddand_m11 REAL,ClimatePerturbations_TmaxAddand_m12 REAL," +
				"ClimatePerturbations_TminAddand_m1 REAL,ClimatePerturbations_TminAddand_m2 REAL,ClimatePerturbations_TminAddand_m3 REAL,ClimatePerturbations_TminAddand_m4 REAL,ClimatePerturbations_TminAddand_m5 REAL,ClimatePerturbations_TminAddand_m6 REAL,ClimatePerturbations_TminAddand_m7 REAL,ClimatePerturbations_TminAddand_m8 REAL,ClimatePerturbations_TminAddand_m9 REAL,ClimatePerturbations_TminAddand_m10 REAL,ClimatePerturbations_TminAddand_m11 REAL,ClimatePerturbations_TminAddand_m12 REAL,";
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);
			
			statement.executeUpdate("CREATE TABLE climate (Site_id INTEGER, Scenario INTEGER, StartYear INTEGER, EndYear INTEGER, "+columns+" PRIMARY KEY (Site_id, Scenario));");
			statement.close();
			return true;
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public void insertSiteRow(int Site_id, double Latitude, double Longitude, String Label) {
		try {
			insertSites.setInt(1, Site_id);
			insertSites.setDouble(2, Latitude);
			insertSites.setDouble(3, Longitude);
			insertSites.setString(4, Label);
			insertSites.executeUpdate();
			insertSites.clearParameters();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
	}
	
	public void insertScenarioRow(int id, String Scenario) {
		try {
			insertScenarios.setInt(1, id);
			insertScenarios.setString(2, Scenario);
			insertScenarios.executeUpdate();
			insertScenarios.clearParameters();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
	}
	
	public void insertClimateRow(int Site_id, int Scenario, int StartYear, int EndYear, double MAP_cm, double MAT_C, double[] MMTemp, double[] MMPPT, double[] CP_PPT, double[] CP_TempMax, double[] CP_TempMin) {
		try {
			insertClimate.setInt(1, Site_id);
			insertClimate.setInt(2, Scenario);
			insertClimate.setInt(3, StartYear);
			insertClimate.setInt(4, EndYear);
			insertClimate.setDouble(5, MAP_cm);
			insertClimate.setDouble(6, MAT_C);
			for(int i=0; i<12; i++) //7,8,9,10,11,12,13,14,15,16,17,18
				insertClimate.setDouble(i+7, MMTemp[i]);
			for(int i=0; i<12; i++) //19,20,21,22,23,24,25,26,27,28,29,30
				insertClimate.setDouble(i+19, MMPPT[i]);
			for(int i=0; i<12; i++) //31,32,33,34,35,36,37,38,39,40,41,42
				insertClimate.setDouble(i+31, CP_PPT[i]);
			for(int i=0; i<12; i++) //43,44,45,46,47,48,49,50,51,52,53,54
				insertClimate.setDouble(i+43, CP_TempMax[i]);
			for(int i=0; i<12; i++) //55,56,57,58,59,60,61,62,63,64,65,66
				insertClimate.setDouble(i+55, CP_TempMin[i]);
			insertClimate.executeUpdate();
			insertClimate.clearParameters();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
	}
	
	public void insertWeatherDataRow(int Site_id, int Scenario, List<YearData> data) {
		int id = data.get(0).id;
		int scenario = data.get(0).scenario;
		int startYear = data.get(0).year;
		int endYear = data.get(data.size()-1).year;
		String compress = new String();
		for(int i=0; i<data.size(); i++) {
			compress += data.get(i).toString();
		}
		try {
			insertData.setInt(1, id);
			insertData.setInt(2, scenario);
			insertData.setInt(3, startYear);
			insertData.setInt(4, endYear);
			insertData.setBytes(5, compresseString(compress));
			insertData.executeUpdate();
			insertData.clearParameters();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
	}
	
	public int getVersion() {
		int version = -1;
		
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
			while(rs.next()) {
				if(rs.getString("name").toLowerCase().equals("version")) {
					try {
						Statement statement2 = connection.createStatement();
						statement2.setQueryTimeout(30);
						ResultSet rs2 = statement2.executeQuery("SELECT Version FROM Version;");
						rs2.next();
						version = rs2.getInt("Version");
						rs2.close();
					} catch(SQLException e) {
				        System.err.println(e.getMessage());
				    }
					break;
				}
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	        System.err.println(e.getMessage());
	    }
		//No version table exists or a problem happend
		if(version == -1) {
			//check the weatherdata table
			int columns = 0;
			try {
				Statement statement2 = connection.createStatement();
				statement2.setQueryTimeout(30);
				ResultSet rs2 = statement2.executeQuery("PRAGMA table_info(WeatherData);");
				while(rs2.next()) {
					columns++;
					if(rs2.getString("name").toLowerCase().equals("startyear")) {
						version = 1;
						break;
					}
				}
				if(columns == 3)
					version = 0;
				rs2.close();
			} catch(SQLException e) {
				JOptionPane.showMessageDialog(null, e.toString());
		        System.err.println(e.getMessage());
		    }
		}

		return version;
	}
	
	public List<YearData> getDataNew(Connection con, int Site_id, int Scenario, int startYear, int endYear) {
		weatherData.clear();
		String allData = new String();
		int StartYear = 0;
		int EndYear = 0;
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			ResultSet rs;

			rs = statement.executeQuery("SELECT StartYear, EndYear, data FROM weatherdata WHERE Site_id="
							+ String.valueOf(Site_id) + " AND Scenario="
							+ String.valueOf(Scenario) + ";");
			rs.next();
			StartYear = rs.getInt(1);
			EndYear = rs.getInt(2);
			byte[] vals = rs.getBytes(3);
			rs.close();
			allData = uncompressBytes(vals);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
			return null;
		}
		
		String[] yearlyData = allData.split(";");
		int year = StartYear;
		
		for(int i=0; i<yearlyData.length; i++) {
			if(year >= startYear && year <= endYear) {
				String[] dailyData = yearlyData[i].split("\n");
				int days = dailyData.length;
				YearData data = new YearData();
				data.id = Site_id; data.scenario = Scenario; data.year = year; data.days=days;
				data.Tmax = new double[days];
				data.Tmin = new double[days];
				data.ppt = new double[days];
				for(int j=0; j<dailyData.length; j++) {
					String[] values = dailyData[j].split(",");
					data.Tmax[j] = Double.parseDouble(values[0]);
					data.Tmin[j] = Double.parseDouble(values[1]);
					data.ppt[j] = Double.parseDouble(values[2]);
				}
				weatherData.add(data);
			}
			year++;
			if(year > EndYear)
				break;
		}
		return weatherData;
	}
	
	public List<YearData> getDataOld(int Site_id, int Scenario, int startYear, int endYear) throws Exception {
		weatherData.clear();
		ByteBuffer serialized = ByteBuffer.wrap(new byte[1]);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			ResultSet rs;

			rs = statement
					.executeQuery("SELECT data FROM weatherdata WHERE Site_id="
							+ String.valueOf(Site_id) + " AND Scenario="
							+ String.valueOf(Scenario) + ";");
			rs.next();
			byte[] vals = rs.getBytes(1);
			rs.close();

			InflaterInputStream gzis = new InflaterInputStream(
					new ByteArrayInputStream(vals));

			byte[] buffer = new byte[1024];
			ByteArrayOutputStream serializedRObjects = new ByteArrayOutputStream(
					381766);

			int len;
			while ((len = gzis.read(buffer)) > 0) {
				serializedRObjects.write(buffer, 0, len);
			}
			serialized = ByteBuffer.wrap(serializedRObjects.toByteArray());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}

		Serialize ds = new Serialize();
		ds.unserialize(serialized);

		if(ds.problem == false) {
			for (int i = 0; i < ds.data.list.size(); i += 3) {
				int year = ds.data.list.get(i + 2).nVals[0];
				if (year >= startYear && year <= endYear) {
					int days = ds.data.list.get(i + 1).nVals[0];
					YearData data = new YearData();
					data.id = Site_id;
					data.scenario = Scenario;
					data.year = year;
					data.days = days;
					data.Tmax = Arrays.copyOfRange(ds.data.list.get(i).dVals, data.days, data.days * 2);
					data.Tmin = Arrays.copyOfRange(ds.data.list.get(i).dVals, data.days * 2, data.days * 3);
					data.ppt = Arrays.copyOfRange(ds.data.list.get(i).dVals, data.days * 3, data.days * 4);
					weatherData.add(data);
				}
			}
		} else {
			throw new Exception(ds.emessage);
		}
		return weatherData;
	}
	
	public List<MapMarkerDot> getSites(int Scenario, double Lat_min, double Lat_max, double Long_min, double Long_max, double min_MAT_C, double max_MAT_C, double min_MAP_cm, double max_MAP_cm) {
		this.sites.clear();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String minlat = String.format("%.3f", Lat_min);
			String maxlat = String.format("%.3f", Lat_max);
			String minlong = String.format("%.3f", Long_min);
			String maxlong = String.format("%.3f", Long_max);
			String minMAT = String.format("%.6f", min_MAT_C);
			String maxMAT = String.format("%.6f", max_MAT_C);
			String minMAP = String.format("%.6f", min_MAP_cm);
			String maxMAP = String.format("%.6f", max_MAP_cm);
			String sqlAmbient = "SELECT Site_id FROM climate WHERE Scenario="+Integer.toString(Scenario)+" AND MAP_cm BETWEEN "+minMAP+" AND "+maxMAP+" AND MAT_C BETWEEN "+minMAT+" AND "+maxMAT;
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return this.sites;
	}
	
	public double getMaxMeanAnnualTemp(int Scenario) {
		double maxMAT_c = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(MAT_C) AS MAT_C FROM climate WHERE Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				maxMAT_c = rs.getDouble("MAT_C");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return maxMAT_c;
	}
	
	public double getMinMeanAnnualTemp(int Scenario) {
		double minMAT_c = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(MAT_C) AS MAT_C FROM climate WHERE Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				minMAT_c = rs.getDouble("MAT_C");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return minMAT_c;
	}
	
	public double getMaxMeanAnnualPPT(int Scenario) {
		double maxMAP_cm = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAX(MAP_cm) AS MAP_cm FROM climate WHERE Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				maxMAP_cm = rs.getDouble("MAP_cm");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return maxMAP_cm;
	}
	
	public double getMinMeanAnnualPPT(int Scenario) {
		double minMAP_cm = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MIN(MAP_cm) AS MAP_cm FROM climate WHERE Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				minMAP_cm = rs.getDouble("MAP_cm");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return MaxLongitude;
	}
	public double getLatitude(int site_id) {
		double Latitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Latitude AS Latitude FROM sites WHERE Site_id = "+String.valueOf(site_id)+";");
			while(rs.next()) {
				Latitude = rs.getDouble("Latitude");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return Latitude;
	}
	public double getLongitude(int site_id) {
		double Longitude = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Longitude AS Longitude FROM sites WHERE Site_id = "+String.valueOf(site_id)+";");
			while(rs.next()) {
				Longitude = rs.getDouble("Longitude");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return Longitude;
	}
	//end of lat and Long
	public String getLabel(int site_id) {
		String label = "";
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Label FROM sites WHERE Site_id = "+String.valueOf(site_id)+";");
			while(rs.next()) {
				label = rs.getString("Label");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return label;
	}
	
	public int getNumberClimate() {
		int nRows = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS nSites FROM climate;");
			while(rs.next()) {
				nRows = rs.getInt("nSites");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nRows;
	}
	
	public int getNumberWeatherData() {
		int nRows = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS nSites FROM weatherdata;");
			while(rs.next()) {
				nRows = rs.getInt("nSites");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nRows;
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nSites;
	}
	public int[] getSiteIds() {
		int nSites = getNumberSites();
		int[] ids = new int[nSites];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Site_id FROM sites ORDER BY Site_id;");
			nSites=0;
			while(rs.next()) {
				ids[nSites] = rs.getInt("Site_id");
				nSites++;
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return ids;
	}
	public int getNumberScenarios() {
		int nScenarios = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS nScenarios FROM scenarios;");
			while(rs.next()) {
				nScenarios = rs.getInt("nScenarios");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nScenarios;
	}
	
	public String[] getScenarioNames() {
		int nScenarios = getNumberScenarios();
		String[] names = new String[nScenarios];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Scenario FROM scenarios ORDER BY id;");
			nScenarios=0;
			while(rs.next()) {
				names[nScenarios] = rs.getString("Scenario");
				nScenarios++;
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return names;
	}
	
	public int getTotalDataRows() {
		int nRows = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS nRows FROM weatherdata;");
			while(rs.next()) {
				nRows = rs.getInt("nRows");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nRows;
	}
	
	public int getDataRows(int Site_id) {
		int nRows = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT COUNT(Site_id) AS nRows FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+";");
			while(rs.next()) {
				nRows = rs.getInt("nRows");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return nRows;
	}
	
	public int[] getDataScenarioIds(int Site_id) {
		int nRows = getDataRows(Site_id);
		int[] ids = new int[nRows];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT Scenario FROM weatherdata WHERE Site_id="+String.valueOf(Site_id)+" ORDER BY Scenario;");
			nRows=0;
			while(rs.next()) {
				ids[nRows] = rs.getInt("Site_id");
				nRows++;
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return ids;
	}
	
	public int getMinYear(int Site_id, int Scenario) {
		int year = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT StartYear AS year FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				year = rs.getInt("year");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return year;
	}
	public int getMaxYear(int Site_id, int Scenario) {
		int year = 0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT EndYear AS year FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				year = rs.getInt("year");
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
	    	System.err.println(e.getMessage());
	    }
		return year;
	}
	public double[] getMeanMonthlyTemp(int Site_id, int Scenario) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "MMTemp_C_m1,MMTemp_C_m2,MMTemp_C_m3,MMTemp_C_m4,MMTemp_C_m5,MMTemp_C_m6,MMTemp_C_m7,MMTemp_C_m8,MMTemp_C_m9,MMTemp_C_m10,MMTemp_C_m11,MMTemp_C_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double[] getMeanMonthlyPPT(int Site_id, int Scenario) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "MMPPT_C_m1,MMPPT_C_m2,MMPPT_C_m3,MMPPT_C_m4,MMPPT_C_m5,MMPPT_C_m6,MMPPT_C_m7,MMPPT_C_m8,MMPPT_C_m9,MMPPT_C_m10,MMPPT_C_m11,MMPPT_C_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double[] getCP_PrcpMult(int Site_id, int Scenario) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "ClimatePerturbations_PrcpMultiplier_m1,ClimatePerturbations_PrcpMultiplier_m2,ClimatePerturbations_PrcpMultiplier_m3,ClimatePerturbations_PrcpMultiplier_m4,ClimatePerturbations_PrcpMultiplier_m5,ClimatePerturbations_PrcpMultiplier_m6,ClimatePerturbations_PrcpMultiplier_m7,ClimatePerturbations_PrcpMultiplier_m8,ClimatePerturbations_PrcpMultiplier_m9,ClimatePerturbations_PrcpMultiplier_m10,ClimatePerturbations_PrcpMultiplier_m11,ClimatePerturbations_PrcpMultiplier_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double[] getCP_TmaxAdd(int Site_id, int Scenario) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "ClimatePerturbations_TmaxAddand_m1,ClimatePerturbations_TmaxAddand_m2,ClimatePerturbations_TmaxAddand_m3,ClimatePerturbations_TmaxAddand_m4,ClimatePerturbations_TmaxAddand_m5,ClimatePerturbations_TmaxAddand_m6,ClimatePerturbations_TmaxAddand_m7,ClimatePerturbations_TmaxAddand_m8,ClimatePerturbations_TmaxAddand_m9,ClimatePerturbations_TmaxAddand_m10,ClimatePerturbations_TmaxAddand_m11,ClimatePerturbations_TmaxAddand_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double[] getCP_TminAdd(int Site_id, int Scenario) {
		double temp[] = new double[12];
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String columns = "ClimatePerturbations_TminAddand_m1,ClimatePerturbations_TminAddand_m2,ClimatePerturbations_TminAddand_m3,ClimatePerturbations_TminAddand_m4,ClimatePerturbations_TminAddand_m5,ClimatePerturbations_TminAddand_m6,ClimatePerturbations_TminAddand_m7,ClimatePerturbations_TminAddand_m8,ClimatePerturbations_TminAddand_m9,ClimatePerturbations_TminAddand_m10,ClimatePerturbations_TminAddand_m11,ClimatePerturbations_TminAddand_m12";
			ResultSet rs = statement.executeQuery("SELECT "+columns+" FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
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
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	public double getMAT(int Site_id, int Scenario) {
		double temp=0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAT_C FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				temp = rs.getDouble(1);
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	
	public double getMAP(int Site_id, int Scenario) {
		double temp=0;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT MAP_cm FROM climate WHERE Site_id="+String.valueOf(Site_id)+" AND Scenario="+String.valueOf(Scenario)+";");
			while(rs.next()) {
				temp = rs.getDouble(1);
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return temp;
	}
	
	public boolean checkClimateTable() {
		boolean climateTableExists = false;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			String Tables = "";
			ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
			while(rs.next()) {
				Tables = rs.getString("name");
				if(Tables.toLowerCase().contains("climate"))
					climateTableExists = true;
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return climateTableExists;
	}
	
	public boolean checkAllClimateTableRows() {
		boolean climateTableAllValuesExist = false;
		if(!checkClimateTable())
			return false;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			int totalDataRows = getTotalDataRows();
			int totalClimateRows = 0;
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS COUNT FROM climate;");
			while(rs.next()) {
				totalClimateRows = rs.getInt("COUNT");
				if(totalClimateRows < totalDataRows)
					climateTableAllValuesExist = false;
				if(totalClimateRows > totalDataRows) {
					System.err.println("climate contains extra records.");
				}
				if(totalClimateRows == totalDataRows)
					climateTableAllValuesExist = true;
			}
			rs.close();
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
		return climateTableAllValuesExist;
	}
	
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
	}
	
	public void createNewDatabase(ProgressBar.Task t, JTextArea taskOutput, JLabel timeLeft) {
		try {
			//Create this table if it doesn't exist
			if(!checkClimateTable()) {
				createTableClimate(connection);
			}
			
			File newdb = File.createTempFile("temp", ".sqlite3", this.weatherDatabase.getParent().toFile());
			connection2 = DriverManager.getConnection("jdbc:sqlite:"+newdb.toString());
			Statement statement = connection2.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("PRAGMA synchronous = OFF");
			statement.executeUpdate("PRAGMA journal_mode = MEMORY");
			statement.close();
			createTableVersion(connection2);
			createTableSites(connection2);
			createTableScenarios(connection2);
			createTableWeatherData(connection2);
			createTableClimate(connection2);
			insertSites = connection2.prepareStatement("INSERT INTO Sites(Site_id,Latitude,Longitude,Label) VALUES(?,?,?,?);");
			insertScenarios = connection2.prepareStatement("INSERT INTO Scenarios(id,Scenario) VALUES (?,?);");
			insertData = connection2.prepareStatement("INSERT INTO weatherdata(Site_id,Scenario,StartYear,EndYear,data) VALUES(?,?,?,?,?);");
			insertClimate = connection2.prepareStatement(insertClimateTableSQL);
			
			
			//total work
			int total = getNumberWeatherData();
			int done = 0;
			ExecutionTimer timer = new ExecutionTimer();
			double timeSum = 0;
			double average = 0;
			double remaining = 0;
			String units = "";
			
			statement = connection.createStatement();
			
			//Copy Sites
			ResultSet rs = statement.executeQuery("SELECT * FROM Sites;");
			while(rs.next()) {
				insertSiteRow(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4));
				if(Thread.interrupted()) {
					return;
				}
			}
			rs.close();
			taskOutput.append("Table Sites Copied\n");
			
			//Copy Scenarios
			rs = statement.executeQuery("SELECT * FROM Scenarios;");
			while(rs.next()) {
				insertScenarioRow(rs.getInt(1), rs.getString(2));
				
				if(Thread.interrupted()) {
					return;
				}
			}
			rs.close();
			taskOutput.append("Table Scenarios Copied\n");
			
			//Copy Climate
			rs = statement.executeQuery("SELECT * FROM climate;");
			while(rs.next()) {
				double[] MMTemp = new double[] {rs.getDouble(7),rs.getDouble(8),rs.getDouble(9),rs.getDouble(10),rs.getDouble(11),rs.getDouble(12),rs.getDouble(13),rs.getDouble(14),rs.getDouble(15),rs.getDouble(16),rs.getDouble(17),rs.getDouble(18)};
				double[] MMPPT = new double[] {rs.getDouble(19),rs.getDouble(20),rs.getDouble(21),rs.getDouble(22),rs.getDouble(23),rs.getDouble(24),rs.getDouble(25),rs.getDouble(26),rs.getDouble(27),rs.getDouble(28),rs.getDouble(29),rs.getDouble(30)};
				double[] CP_PPT = new double[] {rs.getDouble(31),rs.getDouble(32),rs.getDouble(33),rs.getDouble(34),rs.getDouble(35),rs.getDouble(36),rs.getDouble(37),rs.getDouble(38),rs.getDouble(39),rs.getDouble(40),rs.getDouble(41),rs.getDouble(42)};
				double[] CP_TempMax = new double[] {rs.getDouble(43),rs.getDouble(44),rs.getDouble(45),rs.getDouble(46),rs.getDouble(47),rs.getDouble(48),rs.getDouble(49),rs.getDouble(50),rs.getDouble(51),rs.getDouble(52),rs.getDouble(53),rs.getDouble(54)};
				double[] CP_TempMin = new double[] {rs.getDouble(55),rs.getDouble(56),rs.getDouble(57),rs.getDouble(58),rs.getDouble(59),rs.getDouble(60),rs.getDouble(61),rs.getDouble(62),rs.getDouble(63),rs.getDouble(64),rs.getDouble(65),rs.getDouble(66)};
				insertClimateRow(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), MMTemp, MMPPT, CP_PPT, CP_TempMax, CP_TempMin);
				
				if(Thread.interrupted()) {
					return;
				}
			}
			rs.close();
			taskOutput.append("Table climate Copied\n");
			
			//Copy Data
			rs = statement.executeQuery("SELECT Site_id, Scenario FROM weatherdata;");
			List<WeatherData.YearData> data = this.weatherData;
			timer.reset();
			while(rs.next()) {
				int Site_id = rs.getInt(1);
				int Scenario = rs.getInt(2);
				
				try {
					data = getDataOld(Site_id, Scenario, 0, 5000);
				} catch(Exception e) {
					taskOutput.append("Site:"+Integer.toString(Site_id) + " Scenario:"+Integer.toString(Scenario)+"\n");
					taskOutput.append(e.toString() + "\n");
				}
				if(data.isEmpty()) {
					
				} else {
					insertWeatherDataRow(Site_id, Scenario, data);
				}
				
				done++;
				
				int percent = (int)(((double)(done)/total)*100);
				t.updateProgress(percent);
				timer.end();
				timeSum += (double)timer.duration()/1000.0;
				timer.reset();
				
				if(percent % 2 == 0) {
					average = timeSum/(done);
					remaining = (average)*(total-done);
					units = "seconds";
					if(remaining > 3600) {
						remaining = remaining/(60*60);
						units = "hour(s)";
					} else if (remaining > 60) {
						remaining = remaining/(60);
						units = "minute(s)";
					}
					timeLeft.setText(String.format("%.1f", remaining) + " " + units);
				}
				if(Thread.interrupted()) {
					return;
				}
			}
			this.connection.close();
			this.connection2.close();
			
			Object[] options = {"Yes","No"};
			int choice = JOptionPane.showOptionDialog(null, "Delete old database?", "Weather Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(choice == 0) {
				Files.delete(weatherDatabase);
				newdb.renameTo(weatherDatabase.toFile());
			} else {
				String fileName = weatherDatabase.getFileName().toString();
				String pathName = weatherDatabase.getParent()+"/"+fileName+".bk";
				weatherDatabase.toFile().renameTo(new File(pathName));
				newdb.renameTo(weatherDatabase.toFile());
			}
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
	}
	
	public void generateClimateTable(ProgressBar.Task t, JTextArea taskOutput, JLabel timeLeft) {
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("PRAGMA synchronous = OFF");
			statement.executeUpdate("PRAGMA journal_mode = MEMORY");
			statement.close();
			
			if(!checkClimateTable()) {
				createTableClimate(connection);
				insertClimate = connection.prepareStatement(insertClimateTableSQL);
			}
			insertClimate = connection.prepareStatement(insertClimateTableSQL);
			
			statement = connection.createStatement();
			statement.setQueryTimeout(45);
			
			ResultSet rs = statement.executeQuery("SELECT weatherdata.Site_id AS Site_id, weatherdata.Scenario AS Scenario FROM weatherdata LEFT JOIN climate ON (weatherdata.Site_id=climate.Site_id) AND (weatherdata.Scenario=climate.Scenario) WHERE climate.Site_id IS NULL AND climate.Scenario IS NULL ORDER BY Site_id,Scenario;");

			int nScenarios = getNumberScenarios();
			int nSites = getNumberSites();
			double totalWork = (double)nSites * nScenarios;
			
			List<soilwat.SW_WEATHER_HISTORY> scenarioWeatherData = new ArrayList<soilwat.SW_WEATHER_HISTORY>(nScenarios);
			for (int i = 0; i < nScenarios; i++)
				scenarioWeatherData.add(i, null);

			boolean first = true;
			int lastSite = -1;
			int Site_id = 0;
			int Scenario = 0;
			int StartYear, EndYear;

			double[] MMTemp_avg=null, MMTemp_min=null, MMTemp_max=null, MMPPT=null, Current_MMPPT=null, Current_MMTemp_max=null, Current_MMTemp_min=null;
			double MAP_cm=0, MAT_C=0;

			double[] PrcpMultiplier = new double[12];
			double[] TmaxAddand = new double[12];
			double[] TminAddand = new double[12];

			soilwat.SW_WEATHER_HISTORY hist;
					
			List<Integer> Site_ids = new ArrayList<Integer>();
			List<Integer> Scenarios = new ArrayList<Integer>();
			
			while(rs.next()) {
				Site_ids.add(rs.getInt("Site_id"));
				Scenarios.add(rs.getInt("Scenario"));
			}
			rs.close();
			
			ExecutionTimer timer = new ExecutionTimer();
			
			double timeSum = 0;
			
			for(int k=0; k<Site_ids.size(); k++) {
				Site_id = Site_ids.get(k);
				Scenario = Scenarios.get(k);

				if (first) {
					lastSite = Site_id;
					first = false;
				}
				if (lastSite != Site_id) {
					t.updateProgress((int)(((double)(Site_id*nScenarios+Scenario)/totalWork)*100));
					timer.end();
					timeSum += (double)timer.duration()/1000.0;
					if((k+1) % (nScenarios*100+1) == 0)
						taskOutput.append("Site: "+Integer.toString(lastSite)+" complete in "+timer.duration()+"\n");
					
					timer.reset();
					if((k+1) % (nScenarios*10+1) == 0) {
						double average = timeSum/(k+1);
						double remaining = (average)*(Site_ids.size()-(k+1));
						String units = "seconds";
						if(remaining > 3600) {
							remaining = remaining/(60*60);
							units = "hour(s)";
						} else if (remaining > 60) {
							remaining = remaining/(60);
							units = "minutes(s)";
						}
						timeLeft.setText(String.format("%.1f", remaining) + " " + units);
					}
					//System.out.println("Site: " + Integer.toString(lastSite) + " is done.");
					if(Thread.interrupted()) {
						return;
					}
					lastSite = Site_id;
					for (int i = 0; i < nScenarios; i++)
						scenarioWeatherData.set(i, null);
				}

				hist = new SW_WEATHER_HISTORY();
				for (YearData year : this.getDataNew(connection, Site_id, Scenario, 0, 5000)) {
					hist.add_year(year.year, year.ppt, year.Tmax, year.Tmin);
				}
				scenarioWeatherData.set(Scenario - 1, hist);

				StartYear = hist.getStartYear();
				EndYear = hist.getEndYear();
				try {
					MMTemp_avg = hist.meanMonthlyTempC(StartYear, EndYear);
					MMTemp_min = hist.meanMonthlyTempC_Min(StartYear, EndYear);
					MMTemp_max = hist.meanMonthlyTempC_Max(StartYear, EndYear);
					MMPPT = hist.meanMonthlyPPTcm(StartYear, EndYear);
					MAP_cm = hist.MAP_cm(StartYear, EndYear);
					MAT_C = hist.MAT_C(StartYear, EndYear);
				} catch(WeatherException e) {
					System.err.println(e.getMessage());
					continue;
				}

				if (scenarioWeatherData.get(0) == null) { // Make sure we have
															// current scenario,
															// should have
					hist = new SW_WEATHER_HISTORY();
					for (YearData year : this.getDataNew(connection, Site_id, 1, 0, 5000)) {
						hist.add_year(year.year, year.ppt, year.Tmax, year.Tmin);
					}
					scenarioWeatherData.set(0, hist);
				}

				if (Scenario == 1) {
					for (int i = 0; i < 12; i++) {
						PrcpMultiplier[i] = 1;
						TmaxAddand[i] = TminAddand[i] = 0;
					}
				} else {
					try {
						Current_MMPPT = scenarioWeatherData.get(0).meanMonthlyPPTcm(StartYear, EndYear);
						Current_MMTemp_max = scenarioWeatherData.get(0).meanMonthlyTempC_Max(StartYear, EndYear);
						Current_MMTemp_min = scenarioWeatherData.get(0).meanMonthlyTempC_Min(StartYear, EndYear);
					} catch(WeatherException e) {
						System.err.println(e.getMessage());
						continue;
					}
					for (int i = 0; i < 12; i++) {
						PrcpMultiplier[i] = MMPPT[i] / Current_MMPPT[i];
						TmaxAddand[i] = MMTemp_max[i] - Current_MMTemp_max[i];
						TminAddand[i] = MMTemp_min[i] - Current_MMTemp_min[i];
					}
				}
				insertClimateRow(Site_id, Scenario, StartYear, EndYear, MAP_cm,
						MAT_C, MMTemp_avg, MMPPT, PrcpMultiplier, TmaxAddand,
						TminAddand);
			}
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.err.println(e.getMessage());
		}
	}
	
	private class ExecutionTimer {
		private long start;
		private long end;

		public ExecutionTimer() {
			reset();
		}

		public void end() {
			end = System.currentTimeMillis();
		}

		public long duration() {
			return (end - start);
		}

		public void reset() {
			start = 0;
			end = 0;
			start = System.currentTimeMillis();
		}
	}
}

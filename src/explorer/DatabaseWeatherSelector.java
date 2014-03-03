package explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import charts.Map;
import charts.Weather;
import database.WeatherData;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import org.openstreetmap.gui.jmapviewer.SiteEvent;

import javax.swing.JComboBox;

import java.awt.Font;

public class DatabaseWeatherSelector extends JFrame implements ActionListener, SiteEvent {
	private static final long serialVersionUID = 1L;
	
	protected SiteEvent siteEvent;
	
	public void addSiteEventListener(SiteEvent e) {
		siteEvent = e;
	}
	
	private Map map = new Map("Database Sites");
	private WeatherData data;
	private soilwat.SW_WEATHER_HISTORY hist;
	
	private double map_min;
	private double map_max;
	private double mat_min;
	private double mat_max;
	private double longitude_max;
	private double longitude_min;
	private double latitude_max;
	private double latitude_min;
	
	private JFormattedTextField formattedTextField_lat_min;
	private JFormattedTextField formattedTextField_lat_max;
	private JFormattedTextField formattedTextField_long_min;
	private JFormattedTextField formattedTextField_long_max;
	private JFormattedTextField formattedTextField_mat_min;
	private JFormattedTextField formattedTextField_mat_max;
	private JFormattedTextField formattedTextField_map_min;
	private JFormattedTextField formattedTextField_map_max;
	private JButton btn_ClearMap;
	private JButton btn_Update;
	private JLabel lbl_totalSites;
	private JLabel lbl_totalDisplayed;
	private JPanel panel_control_map;
	private JButton btn_load;
	private JComboBox<Integer> comboBox_startYear;
	private JComboBox<Integer> comboBox_endYear;
	private JPanel graphs;
	private Weather plots;
	private JPanel panel_overall;
	private JLabel lbl_MAT_C;
	private JLabel lbl_MAP_cm;
	private int Site_id;
	
	public DatabaseWeatherSelector(Path weatherDB, soilwat.SW_WEATHER_HISTORY hist) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 513);
		plots = new Weather();
		data = new WeatherData(weatherDB);
		map_min = data.getMinMeanAnnualPPT();
		map_max = data.getMaxMeanAnnualPPT();
		mat_min = data.getMinMeanAnnualTemp();
		mat_max = data.getMaxMeanAnnualTemp();
		latitude_max = data.getMaxLatitude();
		latitude_min = data.getMinLatitude();
		longitude_max = data.getMaxLongitude();
		longitude_min = data.getMinLongitude();
		this.hist = hist;
		map.onSetMapMarkers_db(data.getSites(latitude_min, latitude_max, longitude_min, longitude_max, mat_min, mat_max, map_min, map_max));
		map.addSiteListener(this);
		
		Format format = NumberFormat.getNumberInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		panel_overall = new JPanel();
		panel_overall.setLayout(new BoxLayout(panel_overall, BoxLayout.PAGE_AXIS));
		
		setContentPane(panel_overall);
		
		panel_control_map = new JPanel();
		panel_overall.add(panel_control_map);
		
		JPanel panel_siteControl = new JPanel();
		panel_control_map.add(panel_siteControl);
		panel_siteControl.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblMinimum = new JLabel("Minimum");
		panel_siteControl.add(lblMinimum, "3, 1");
		
		JLabel lblMax = new JLabel("Maximum");
		panel_siteControl.add(lblMax, "5, 1");
		
		JLabel lblLatitude = new JLabel("Latitude");
		panel_siteControl.add(lblLatitude, "1, 3, right, default");
		
		formattedTextField_lat_min = new JFormattedTextField(format);
		formattedTextField_lat_min.setValue(new Double(latitude_min));
		panel_siteControl.add(formattedTextField_lat_min, "3, 3, fill, default");
		
		formattedTextField_lat_max = new JFormattedTextField(format);
		formattedTextField_lat_max.setValue(new Double(latitude_max));
		panel_siteControl.add(formattedTextField_lat_max, "5, 3, fill, default");
		
		JLabel lblLongitude = new JLabel("Longitude");
		panel_siteControl.add(lblLongitude, "1, 5, right, default");
		
		formattedTextField_long_min = new JFormattedTextField();
		formattedTextField_long_min.setValue(new Double(longitude_min));
		panel_siteControl.add(formattedTextField_long_min, "3, 5, fill, default");
		
		formattedTextField_long_max = new JFormattedTextField(format);
		formattedTextField_long_max.setValue(new Double(longitude_max));
		panel_siteControl.add(formattedTextField_long_max, "5, 5, fill, default");
		
		JLabel lblMAT = new JLabel("MAT Celsius");
		panel_siteControl.add(lblMAT, "1, 7, right, default");
		
		formattedTextField_mat_min = new JFormattedTextField(format);
		formattedTextField_mat_min.setValue(new Double(mat_min));
		panel_siteControl.add(formattedTextField_mat_min, "3, 7, fill, default");
		
		formattedTextField_mat_max = new JFormattedTextField(format);
		formattedTextField_mat_max.setValue(new Double(mat_max));
		panel_siteControl.add(formattedTextField_mat_max, "5, 7, fill, default");
		
		JLabel lblMAP = new JLabel("MAP cm");
		panel_siteControl.add(lblMAP, "1, 9, right, default");
		
		formattedTextField_map_min = new JFormattedTextField(format);
		formattedTextField_map_min.setValue(new Double(map_min));
		panel_siteControl.add(formattedTextField_map_min, "3, 9, fill, default");
		
		formattedTextField_map_max = new JFormattedTextField(format);
		formattedTextField_map_max.setValue(new Double(map_max));
		panel_siteControl.add(formattedTextField_map_max, "5, 9, fill, default");
		
		btn_ClearMap = new JButton("Clear Map");
		btn_ClearMap.addActionListener(this);
		panel_siteControl.add(btn_ClearMap, "3, 11");
		
		btn_Update = new JButton("Update");
		btn_Update.addActionListener(this);
		panel_siteControl.add(btn_Update, "5, 11");
		
		lbl_totalSites = new JLabel("Sites: ");
		lbl_totalSites.setText("Sites: "+String.valueOf(data.getNumberSites()));
		panel_siteControl.add(lbl_totalSites, "3, 13");
		
		lbl_totalDisplayed = new JLabel("Displayed:");
		lbl_totalDisplayed.setText("Displayed: "+String.valueOf(map.getNumberMarkers()));
		panel_siteControl.add(lbl_totalDisplayed, "5, 13");
		
		btn_load = new JButton("Load Site");
		btn_load.setEnabled(false);
		btn_load.addActionListener(this);
		
		comboBox_startYear = new JComboBox<Integer>();
		comboBox_startYear.setEnabled(false);
		panel_siteControl.add(comboBox_startYear, "1, 15, fill, default");
		
		comboBox_endYear = new JComboBox<Integer>();
		comboBox_endYear.setEnabled(false);
		
		panel_siteControl.add(comboBox_endYear, "3, 15, fill, default");
		panel_siteControl.add(btn_load, "5, 15");
		
		lbl_MAT_C = new JLabel("");
		lbl_MAT_C.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_siteControl.add(lbl_MAT_C, "1, 16");
		
		lbl_MAP_cm = new JLabel("");
		lbl_MAP_cm.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_siteControl.add(lbl_MAP_cm, "3, 16");
		
		panel_control_map.add(map.get_MapPanel());
	}
	public void onGetValues() {
		latitude_min = ((Number)formattedTextField_lat_min.getValue()).doubleValue();
		latitude_max = ((Number)formattedTextField_lat_max.getValue()).doubleValue();
		longitude_min = ((Number)formattedTextField_long_min.getValue()).doubleValue();
		longitude_max = ((Number)formattedTextField_long_max.getValue()).doubleValue();
		mat_min = ((Number)formattedTextField_mat_min.getValue()).doubleValue();
		mat_max = ((Number)formattedTextField_mat_max.getValue()).doubleValue();
		map_min = ((Number)formattedTextField_map_min.getValue()).doubleValue();
		map_max = ((Number)formattedTextField_map_max.getValue()).doubleValue();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == btn_ClearMap) {
			map.removeAllMarkers();
			this.lbl_totalDisplayed.setText("Displayed: "+String.valueOf(map.getNumberMarkers()));
		}
		if(src == btn_Update) {
			map.removeAllMarkers();
			onGetValues();
			map.onSetMapMarkers_db(data.getSites(latitude_min, latitude_max, longitude_min, longitude_max, mat_min, mat_max, map_min, map_max));
			this.lbl_totalDisplayed.setText("Displayed: "+String.valueOf(map.getNumberMarkers()));
		}
		if(src == btn_load) {
			hist.removeAll();
			int startYear = ((Number)comboBox_startYear.getSelectedItem()).intValue();
			int endYear = ((Number)comboBox_endYear.getSelectedItem()).intValue();
			List<database.WeatherData.YearData> temp = data.getData(this.Site_id, startYear, endYear);
			for (database.WeatherData.YearData year : temp) {
				hist.add_year(year.year, year.ppt, year.Tmax, year.Tmin);
			}
			siteEvent.siteSelected(Site_id);
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	@Override
	public void siteSelected(int Site) {
		this.Site_id = Site;
		this.btn_load.setEnabled(true);
		this.comboBox_startYear.setEnabled(true);
		this.comboBox_endYear.setEnabled(true);
		int start = data.getMaxYear(Site);
		int end = data.getMinYear(Site);
		Integer[] model = new Integer[end-start+1];
		for(int i=start; i <= end; i++) {
			model[i-start] = i;
		}
		this.comboBox_startYear.setModel(new DefaultComboBoxModel<Integer>(model));
		this.comboBox_startYear.setSelectedIndex(0);
		this.comboBox_endYear.setModel(new DefaultComboBoxModel<Integer>(model));
		this.comboBox_endYear.setSelectedIndex(model.length - 1);
		
		double[] temp = data.getMeanMonthlyTemp(Site);
		double[] ppt = data.getMeanMonthlyPPT(Site);
		
		if(graphs == null) {
			graphs = plots.create_DatabasePanel(temp, ppt, Site);
			this.panel_overall.add(graphs);
		} else {
			this.panel_overall.remove(graphs);
			graphs = plots.create_DatabasePanel(temp, ppt, Site);
			this.panel_overall.add(graphs);
			this.panel_overall.revalidate();
			this.pack();
		}
		lbl_MAP_cm.setText("MAP cm: "+String.format("%.4f",data.getMAP(Site)));
		lbl_MAT_C.setText("MAT Celsius: "+String.format("%.4f",data.getMAT(Site)));
	}
}

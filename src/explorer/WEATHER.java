package explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import charts.Map;
import charts.Weather;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import database.Converter;
import database.ProgressBar;
import database.WeatherData;

import javax.swing.JComboBox;

import org.openstreetmap.gui.jmapviewer.SiteEvent;

public class WEATHER implements ListSelectionListener, ActionListener, SiteEvent, Converter {
	
	private static class DirectoriesFilter implements Filter<Path> {
	    @Override
	    public boolean accept(Path entry) throws IOException {
	        return Files.isDirectory(entry);
	    }
	}
	
	private soilwat.SW_WEATHER.WEATHER_INPUT_DATA weatherSetupIn;
	private soilwat.SW_WEATHER_HISTORY weatherHist;
	
	JList<String> list_historyYears;
	
	private JTable table_weather_monthlyScalingParam;
	private JTable table_weather_History;
	
	private JCheckBox chckbx_weather_snowAccumulation;
	private JFormattedTextField formattedTextField_pct_snowdrift;
	private JFormattedTextField formattedTextField_pct_snowRunoff;
	private JCheckBox chckbx_weather_useMarkov;
	private JFormattedTextField formattedTextField_days_in_runavg;
	private JFormattedTextField formattedTextField_insertYear;
	private List<String> weatherFolders = new ArrayList<String>();
	private Path lookupWeatherFolder_Path = Paths.get("");
	private Path lookupWeatherFolder_FolderPath = Paths.get("");
	private Path folderPath = Paths.get("");
	private JButton btnMap;
	private JButton btn_lookupWeatherFolder;
	private JComboBox<Integer> comboBox_lookup_start;
	private JComboBox<Integer> comboBox_lookup_stop;
	private JButton btn_lookup_load;
	private JButton btnNewButton_folder_select;
	private JComboBox<Integer> comboBox_folder_start;
	private JComboBox<Integer> comboBox_folder_end;
	private JButton btn_folder_load;
	private String weatherPrefix;
	
	JButton btn_weather_historyAdd;
	JButton btn_weather_historyRemove;
	
	private int list_index=-1;
	
	public WEATHER(soilwat.SW_WEATHER.WEATHER_INPUT_DATA weatherSetupIn, soilwat.SW_WEATHER_HISTORY weatherHist, soilwat.SW_FILES.FILES_INPUT_DATA data) {
		this.weatherHist = weatherHist;
		this.weatherSetupIn = weatherSetupIn;
		weatherPrefix = data.WeatherPathAndPrefix;
	}
	
	public void onGetValues() {
		weatherSetupIn.use_snow = chckbx_weather_snowAccumulation.isSelected();
		weatherSetupIn.pct_snowdrift = ((Number)formattedTextField_pct_snowdrift.getValue()).doubleValue();
		weatherSetupIn.pct_snowRunoff = ((Number)formattedTextField_pct_snowRunoff.getValue()).doubleValue();
		weatherSetupIn.use_markov = chckbx_weather_useMarkov.isSelected();
		weatherSetupIn.days_in_runavg = ((Number)formattedTextField_days_in_runavg.getValue()).intValue();
		weatherSetupIn.yr.setFirst(weatherHist.getStartYear());
		
		for(int i=0; i<12; i++) {
			weatherSetupIn.scale_precip[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 1)).doubleValue();
			weatherSetupIn.scale_temp_max[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 2)).doubleValue();
			weatherSetupIn.scale_temp_min[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 3)).doubleValue();
			weatherSetupIn.scale_skyCover[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 4)).doubleValue();
			weatherSetupIn.scale_wind[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 5)).doubleValue();
			weatherSetupIn.scale_rH[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 6)).doubleValue();
			weatherSetupIn.scale_transmissivity[i] = ((Number)table_weather_monthlyScalingParam.getValueAt(i, 7)).doubleValue();
		}
		//Save Current Year
		if(list_historyYears.getModel().getSize() != 0) {
			int year = Integer.parseInt(list_historyYears.getSelectedValue());
			onGet_HIST(year);
		}
	}
	
	public void onSetValues() {
		chckbx_weather_snowAccumulation.setSelected(weatherSetupIn.use_snow);
		formattedTextField_pct_snowdrift.setValue(weatherSetupIn.pct_snowdrift);
		formattedTextField_pct_snowRunoff.setValue(weatherSetupIn.pct_snowRunoff);
		chckbx_weather_useMarkov.setSelected(weatherSetupIn.use_markov);
		if(weatherSetupIn.use_markov) {
			//TODO show tab
		} else {
			//TODO hide tab
		}
		formattedTextField_days_in_runavg.setValue(weatherSetupIn.days_in_runavg);
		
		for(int i=0; i<12; i++) {
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_precip[i], i, 1);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_temp_max[i], i, 2);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_temp_min[i], i, 3);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_skyCover[i], i, 4);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_wind[i], i, 5);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_rH[i], i, 6);
			table_weather_monthlyScalingParam.setValueAt(weatherSetupIn.scale_transmissivity[i], i, 7);
		}
		
		onReset_list();
	}
	
	public void onReset_list() {
		this.list_index = -1;
		list_historyYears.removeAll();
		list_historyYears.setListData(weatherHist.getHistYearsString());
		list_historyYears.setSelectedIndex(0);
		
		Integer year = null;
		if(this.weatherHist.data())
			year = new Integer(Collections.max(this.weatherHist.getHistYearsInteger())+1);
		else
			year = new Integer(1950);
		formattedTextField_insertYear.setValue(year);
		
		if(list_historyYears.getModel().getSize() == 0 && list_historyYears.getSelectedIndex() == -1) {
			table_weather_History.setModel(getModel(365));
			table_weather_History.getColumnModel().getColumn(0).setResizable(false);
			table_weather_History.getColumnModel().getColumn(0).setPreferredWidth(35);
			table_weather_History.getColumnModel().getColumn(0).setCellRenderer(new IntegerColumnRenderer());
			table_weather_History.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
			table_weather_History.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
			table_weather_History.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
			for(int i=0; i<365; i++) {
				table_weather_History.setValueAt(i+1, i, 0);
				table_weather_History.setValueAt(0.0, i, 1);
				table_weather_History.setValueAt(0.0, i, 2);
				table_weather_History.setValueAt(0.0, i, 3);
			}
		}
	}
	
	private void onGet_HIST(int year) {
		if(list_index != -1) {
			this.weatherHist.setCurrentYear(year);
			int rows = this.weatherHist.getDays();
			
			for(int i=0; i<rows; i++) {
				double temp_max = ((Number)table_weather_History.getValueAt(i, 1)).doubleValue();
				double temp_min = ((Number)table_weather_History.getValueAt(i, 2)).doubleValue();
				double ppt = ((Number)table_weather_History.getValueAt(i, 3)).doubleValue();
				
				this.weatherHist.set_day(i, ppt, temp_max, temp_min);
			}
			this.weatherHist.onCalcData();
		}
	}
	private void onSet_HIST() {
		weatherHist.setCurrentYear(Integer.parseInt(list_historyYears.getSelectedValue()));
		table_weather_History.setModel(getModel(weatherHist.getDays()));
		table_weather_History.getColumnModel().getColumn(0).setResizable(false);
		table_weather_History.getColumnModel().getColumn(0).setPreferredWidth(35);
		table_weather_History.getColumnModel().getColumn(0).setCellRenderer(new IntegerColumnRenderer());
		table_weather_History.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table_weather_History.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table_weather_History.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		for(int i=0; i<weatherHist.getDays(); i++) {
			table_weather_History.setValueAt(i+1, i, 0);
			table_weather_History.setValueAt(weatherHist.get_temp_max(i), i, 1);
			table_weather_History.setValueAt(weatherHist.get_temp_min(i), i, 2);
			table_weather_History.setValueAt(weatherHist.get_ppt(i), i, 3);
		}
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel onGetPanel_weather() {
		Format format_int = NumberFormat.getIntegerInstance();
		((NumberFormat)format_int).setGroupingUsed(false);
		
		Format format_double = NumberFormat.getNumberInstance();
		((NumberFormat)format_double).setGroupingUsed(false);
		
		JPanel panel_weather = new JPanel();
		panel_weather.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_weather_left = new JPanel();
		panel_weather.add(panel_weather_left, BorderLayout.WEST);
		panel_weather_left.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_weather_setup = new JPanel();
		panel_weather_left.add(panel_weather_setup, BorderLayout.NORTH);
		panel_weather_setup.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lbl_weatherSetup = new JLabel("Weather Setup Parameters");
		panel_weather_setup.add(lbl_weatherSetup, "1, 1, 2, 1");
		
		chckbx_weather_snowAccumulation = new JCheckBox("");
		panel_weather_setup.add(chckbx_weather_snowAccumulation, "1, 3, right, default");
		
		JLabel lbl_weather_snowAccum = new JLabel("Allow Snow Accumulation");
		lbl_weather_snowAccum.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_setup.add(lbl_weather_snowAccum, "2, 3");
		
		formattedTextField_pct_snowdrift = new JFormattedTextField(format_double);
		formattedTextField_pct_snowdrift.setColumns(5);
		panel_weather_setup.add(formattedTextField_pct_snowdrift, "1, 5, right, default");
		
		JLabel lbl_weather_percSnowDrift = new JLabel("% of Snow Drift per snow event");
		lbl_weather_percSnowDrift.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_setup.add(lbl_weather_percSnowDrift, "2, 5");
		
		formattedTextField_pct_snowRunoff = new JFormattedTextField(format_double);
		formattedTextField_pct_snowRunoff.setColumns(5);
		panel_weather_setup.add(formattedTextField_pct_snowRunoff, "1, 7, right, default");
		
		JLabel lbl_weather_percSnowMelt = new JLabel("% of snowmelt water as runoff/on per event");
		lbl_weather_percSnowMelt.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_setup.add(lbl_weather_percSnowMelt, "2, 7");
		
		chckbx_weather_useMarkov = new JCheckBox("");
		chckbx_weather_useMarkov.addActionListener(this);
		panel_weather_setup.add(chckbx_weather_useMarkov, "1, 9, right, default");
		
		JLabel lbl_weather_UseMarkovProcess = new JLabel("Use Markov process for missing weather.");
		lbl_weather_UseMarkovProcess.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_setup.add(lbl_weather_UseMarkovProcess, "2, 9");
		
		formattedTextField_days_in_runavg = new JFormattedTextField(format_int);
		formattedTextField_days_in_runavg.setColumns(5);
		panel_weather_setup.add(formattedTextField_days_in_runavg, "1, 11, right, default");
		
		JLabel lbl_weather_runavg = new JLabel("Number of Days to use in the running average of temperature.");
		lbl_weather_runavg.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_setup.add(lbl_weather_runavg, "2, 11");
		
		JPanel panel_weather_monthlyScaleParam = new JPanel();
		panel_weather_left.add(panel_weather_monthlyScaleParam);
		panel_weather_monthlyScaleParam.setLayout(new BoxLayout(panel_weather_monthlyScaleParam, BoxLayout.PAGE_AXIS));
		
		JLabel lbl_weather_monthlyScalingParams = new JLabel("Monthly Scaling Parameters");
		panel_weather_monthlyScaleParam.add(lbl_weather_monthlyScalingParams);
		
		JLabel lbl_weather_MonthsDescription = new JLabel("Months : Jan, Feb, March, April, May, June, July, Aug, Sept, Oct, Nov, Dec");
		lbl_weather_MonthsDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_MonthsDescription);
		
		JLabel lbl_weather_PPTDiscription = new JLabel("PPT = multiplicative for PPT (scale*ppt)");
		lbl_weather_PPTDiscription.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_PPTDiscription);
		
		JLabel lbl_weather_MaxTDiscription = new JLabel("MaxT = additive for max temp (scale+maxtemp).");
		lbl_weather_MaxTDiscription.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_MaxTDiscription);
		
		JLabel lbl_weather_MinTDiscription = new JLabel("MinT = additive for min temp (scale+mintemp).");
		lbl_weather_MinTDiscription.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_MinTDiscription);
		
		table_weather_monthlyScalingParam = new JTable();
		table_weather_monthlyScalingParam.setModel(new DefaultTableModel(
			new Object[][] {
				{"January", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"February", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"March", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"April", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"May", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"June", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"July", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"August", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"September", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"October", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"November", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
				{"December", new Double(1.0), new Double(0.0), new Double(0.0), new Double(0.0), new Double(1.0), new Double(0.0), new Double(1.0)},
			},
			new String[] {
				"Month", "PPT", "Max Temp", "Min Temp", "Sky Cover", "Wind", "Relative Humidity", "Transmissivity"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 478168844380016220L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_weather_monthlyScalingParam.getColumnModel().getColumn(0).setResizable(false);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(1).setPreferredWidth(50);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(4).setPreferredWidth(70);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(5).setPreferredWidth(50);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(6).setPreferredWidth(125);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(7).setPreferredWidth(95);
		table_weather_monthlyScalingParam.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(4).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(5).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(6).setCellRenderer(new DoubleColumnRenderer());
		table_weather_monthlyScalingParam.getColumnModel().getColumn(7).setCellRenderer(new DoubleColumnRenderer());
		
		JLabel lbl_weather_skyCover = new JLabel("SkyCover = additive for mean monthly sky cover [%]; min(100, max(0, scale + sky cover))");
		lbl_weather_skyCover.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_skyCover);
		
		JLabel lbl_weather_wind = new JLabel("Wind = multiplicative for mean monthly wind speed; max(0, scale * wind speed)");
		lbl_weather_wind.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_wind);
		
		JLabel lbl_weather_rH = new JLabel("rH = additive for mean monthly relative humidity [%]; min(100, max(0, scale + rel. Humidity))");
		lbl_weather_rH.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_rH);
		
		JLabel lbl_weather_transmissivity = new JLabel("Transmissivity = multiplicative for mean month rel transmis; min(1, max(0, scale * transmissivity))");
		lbl_weather_transmissivity.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_transmissivity);
		panel_weather_monthlyScaleParam.add(table_weather_monthlyScalingParam);
		JScrollPane scrollPane_2 = new JScrollPane(table_weather_monthlyScalingParam);
		scrollPane_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_weather_monthlyScaleParam.add(scrollPane_2);
		
		JPanel panel_weather_WeatherYears = new JPanel();
		panel_weather_left.add(panel_weather_WeatherYears, BorderLayout.SOUTH);
		panel_weather_WeatherYears.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_weather_list = new JPanel();
		panel_weather_WeatherYears.add(panel_weather_list, BorderLayout.WEST);
		panel_weather_list.setLayout(new BoxLayout(panel_weather_list, BoxLayout.PAGE_AXIS));
		
		JLabel lbl_weather_WeatherHistoryList = new JLabel("Weather History Year List");
		lbl_weather_WeatherHistoryList.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_weather_list.add(lbl_weather_WeatherHistoryList);
		
		JScrollPane scrollPane_weather_weatherYearList = new JScrollPane();
		panel_weather_list.add(scrollPane_weather_weatherYearList);
		list_historyYears = new JList<String>();
		list_historyYears.setVisibleRowCount(10);
		list_historyYears.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_historyYears.addListSelectionListener(this);
		scrollPane_weather_weatherYearList.setViewportView(list_historyYears);
		
		JPanel panel_weather_list_control = new JPanel();
		panel_weather_list.add(panel_weather_list_control);
		
		btn_weather_historyRemove = new JButton("-");
		btn_weather_historyRemove.addActionListener(this);
		panel_weather_list_control.add(btn_weather_historyRemove);
		
		btn_weather_historyAdd = new JButton("+");
		btn_weather_historyAdd.addActionListener(this);
		panel_weather_list_control.add(btn_weather_historyAdd);
		
		JLabel lbl_weather_addYear = new JLabel("Year:");
		lbl_weather_addYear.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_list_control.add(lbl_weather_addYear);
		
		formattedTextField_insertYear = new JFormattedTextField(format_int);
		formattedTextField_insertYear.setColumns(4);
		Integer year = null;
		if(this.weatherHist.data())
			year = new Integer(Collections.max(this.weatherHist.getHistYearsInteger())+1);
		else
			year = new Integer(1950);
		formattedTextField_insertYear.setValue(year);
		panel_weather_list_control.add(formattedTextField_insertYear);
		
		JButton plot = new JButton("Graph");
		plot.setFont(new Font("Dialog", Font.PLAIN, 12));
		plot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Weather w = new Weather("SWeather Plot", "Chart", weatherHist,Integer.parseInt(list_historyYears.getSelectedValue().toString()));
				w.pack();
				w.setVisible(true);
			}
		});
		panel_weather_list_control.add(plot);
		
		JPanel panel_source = new JPanel();
		panel_weather_WeatherYears.add(panel_source, BorderLayout.CENTER);
		panel_source.setLayout(new BoxLayout(panel_source, BoxLayout.PAGE_AXIS));
		
		JLabel lblNewLabel = new JLabel("Weather data from LookupWeatherFolder");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_source.add(lblNewLabel);
		
		JPanel panel_lookupweatherfolder = new JPanel();
		panel_source.add(panel_lookupweatherfolder);
		panel_lookupweatherfolder.setLayout(new BoxLayout(panel_lookupweatherfolder, BoxLayout.PAGE_AXIS));
		
		JPanel panel_step_1 = new JPanel();
		panel_lookupweatherfolder.add(panel_step_1);
		
		JLabel lblSelectFolder = new JLabel("1. Select");
		lblSelectFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_1.add(lblSelectFolder);
		
		btn_lookupWeatherFolder = new JButton("LookupWeatherFolder");
		btn_lookupWeatherFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnMap.setEnabled(false);
				JFileChooser fc;
				fc = new JFileChooser();
		    	fc.setAcceptAllFileFilterUsed(false);
		    	fc.setMultiSelectionEnabled(false);
		    	fc.setSelectedFile(new File("/media/ryan/Storage/Users/Ryan_Murphy/My_Documents/Work/LookupWeatherFolder/"));
		    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	lookupWeatherFolder_Path = fc.getSelectedFile().toPath();
		        	try (DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(lookupWeatherFolder_Path.toString()), new DirectoriesFilter())) {
		        		for (Path p : ds) {
		        			if(p.getFileName().toString().startsWith("Weather_NCEPCFSR_")) {
		        				weatherFolders.add(p.getFileName().toString());
		        			}
		        		}
		        	} catch (IOException eio) {
		        		eio.printStackTrace();
		        	}
		        	if(weatherFolders.get(0).startsWith("Weather_NCEPCFSR_") && weatherFolders.size() > 0) {
		        		btnMap.setEnabled(true);
		        	} else {
		        		JOptionPane.showMessageDialog(null, "Folder does not contain any NCEPCFSR data.","Alert", JOptionPane.ERROR_MESSAGE);
		        	}
		        } else {
		        	
		        }
			}
		});
		panel_step_1.add(btn_lookupWeatherFolder);
		btn_lookupWeatherFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JLabel lblNewLabel_1 = new JLabel("2. Site");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_1.add(lblNewLabel_1);
		
		
		btnMap = new JButton("Map");
		panel_step_1.add(btnMap);
		btnMap.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map m = new Map("SWeather Plot");
				m.setVisible(true);
				m.onSetMapMarkers(weatherFolders);
				int result = JOptionPane.showConfirmDialog(m, m.get_MapPanel(), "Select Weather Folder by lat/long", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				List<Integer> temp_years = new ArrayList<Integer>();
				Integer[] years = null;
				
				if(result == JOptionPane.YES_OPTION) {
					String folder = m.getFolder()+"/";
					btn_lookup_load.setEnabled(false);
					lookupWeatherFolder_FolderPath = lookupWeatherFolder_Path.resolve(folder);
					String Prefix = Paths.get(weatherPrefix).getFileName().toString();
					try ( DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(lookupWeatherFolder_FolderPath.toString())) ) {
		        		for (Path p : ds) {
		        			if(p.getFileName().toString().startsWith(Prefix)) {
		        				temp_years.add(Integer.parseInt(p.getFileName().toString().split("\\.", 2)[1]));
		        			}
		        		}
		        		if(temp_years.size() > 0) {
		        			years = new Integer[temp_years.size()];
		        			Collections.sort(temp_years);
		        			temp_years.toArray(years);
		        		} else {
		        			JOptionPane.showMessageDialog(null, "No Weather Data in this folder.","Alert", JOptionPane.ERROR_MESSAGE);
		        		}
		        	} catch (IOException eio) {
		        		eio.printStackTrace();
		        		JOptionPane.showMessageDialog(null, "Folder does not contain any NCEPCFSR data.","Alert", JOptionPane.ERROR_MESSAGE);
		        	}
					if(years != null) {
						comboBox_lookup_start.setEnabled(true);
						comboBox_lookup_stop.setEnabled(true);
						comboBox_lookup_start.setModel(new DefaultComboBoxModel<Integer>(years));
						comboBox_lookup_start.setSelectedIndex(0);
						comboBox_lookup_stop.setModel(new DefaultComboBoxModel<Integer>(years));
						comboBox_lookup_stop.setSelectedIndex(temp_years.size()-1);
						btn_lookup_load.setEnabled(true);
					}
				} else {
					
				}
			}
		});
		btnMap.setEnabled(false);
		
		JPanel panel_step_2 = new JPanel();
		panel_lookupweatherfolder.add(panel_step_2);
		
		JLabel lblNewLabel_2 = new JLabel("3. Year Range");
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_2.add(lblNewLabel_2);
		
		comboBox_lookup_start = new JComboBox<Integer>();
		comboBox_lookup_start.setEnabled(false);
		comboBox_lookup_start.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_2.add(comboBox_lookup_start);
		
		JLabel lblNewLabel_3 = new JLabel(":");
		panel_step_2.add(lblNewLabel_3);
		
		comboBox_lookup_stop = new JComboBox<Integer>();
		comboBox_lookup_stop.setEnabled(false);
		comboBox_lookup_stop.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_2.add(comboBox_lookup_stop);
		
		JLabel lblNewLabel_4 = new JLabel("4. ");
		lblNewLabel_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_2.add(lblNewLabel_4);
		
		btn_lookup_load = new JButton("Load");
		btn_lookup_load.setEnabled(false);
		btn_lookup_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String prefix = Paths.get(weatherPrefix).getFileName().toString();
				int startYear = ((Number)comboBox_lookup_start.getSelectedItem()).intValue();
				int stopYear = ((Number)comboBox_lookup_stop.getSelectedItem()).intValue();
				boolean useMarkov = chckbx_weather_useMarkov.isSelected();
				try {
					weatherHist.onRead(lookupWeatherFolder_FolderPath, prefix, startYear, stopYear, useMarkov);
					onReset_list();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "FAILED TO READ: "+e.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btn_lookup_load.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_step_2.add(btn_lookup_load);
		
		JLabel lblNewLabel_5 = new JLabel("Weather Data form Folder");
		lblNewLabel_5.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_source.add(lblNewLabel_5);
		
		JPanel panel_folder = new JPanel();
		panel_source.add(panel_folder);
		panel_folder.setLayout(new BoxLayout(panel_folder, BoxLayout.PAGE_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_folder.add(panel_1);
		
		JLabel lblNewLabel_6 = new JLabel("1. ");
		lblNewLabel_6.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(lblNewLabel_6);
		
		btnNewButton_folder_select = new JButton("Folder");
		btnNewButton_folder_select.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_folder_select.addActionListener(new ActionListener() {@Override
		public void actionPerformed(ActionEvent e) {
			btnMap.setEnabled(false);
			JFileChooser fc;
			fc = new JFileChooser();
	    	fc.setAcceptAllFileFilterUsed(false);
	    	fc.setMultiSelectionEnabled(false);
	    	fc.setSelectedFile(new File("/home/ryan/workspace/Rsoilwat_v31/tests/"));
	    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);
			List<Integer> temp_years = new ArrayList<Integer>();
			Integer[] years = null;
			
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	btn_folder_load.setEnabled(false);
	        	folderPath = fc.getSelectedFile().toPath();
	        	String Prefix = Paths.get(weatherPrefix).getFileName().toString();
				try ( DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(folderPath.toString())) ) {
	        		for (Path p : ds) {
	        			if(p.getFileName().toString().startsWith(Prefix)) {
	        				temp_years.add(Integer.parseInt(p.getFileName().toString().split("\\.", 2)[1]));
	        			}
	        		}
	        		if(temp_years.size() > 0) {
	        			years = new Integer[temp_years.size()];
	        			Collections.sort(temp_years);
	        			temp_years.toArray(years);
	        		} else {
	        			JOptionPane.showMessageDialog(null, "No Weather Data in this folder.","Alert", JOptionPane.ERROR_MESSAGE);
	        		}
	        	} catch (IOException eio) {
	        		eio.printStackTrace();
	        		JOptionPane.showMessageDialog(null, "Folder does not contain any NCEPCFSR data.","Alert", JOptionPane.ERROR_MESSAGE);
	        	}
				if(years != null) {
					comboBox_folder_start.setEnabled(true);
					comboBox_folder_end.setEnabled(true);
					comboBox_folder_start.setModel(new DefaultComboBoxModel<Integer>(years));
					comboBox_folder_start.setSelectedIndex(0);
					comboBox_folder_end.setModel(new DefaultComboBoxModel<Integer>(years));
					comboBox_folder_end.setSelectedIndex(temp_years.size()-1);
					btn_folder_load.setEnabled(true);
				}
	        } else {
	        	
	        }
		}
		});	
		panel_1.add(btnNewButton_folder_select);
		
		JLabel lblNewLabel_7 = new JLabel("2. Range");
		lblNewLabel_7.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(lblNewLabel_7);
		
		comboBox_folder_start = new JComboBox<Integer>();
		comboBox_folder_start.setEnabled(false);
		comboBox_folder_start.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(comboBox_folder_start);
		
		JLabel lblNewLabel_8 = new JLabel(":");
		panel_1.add(lblNewLabel_8);
		
		comboBox_folder_end = new JComboBox<Integer>();
		comboBox_folder_end.setEnabled(false);
		comboBox_folder_end.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(comboBox_folder_end);
		
		JLabel lblNewLabel_9 = new JLabel("3.");
		lblNewLabel_9.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(lblNewLabel_9);
		
		btn_folder_load = new JButton("Load");
		btn_folder_load.setEnabled(false);
		btn_folder_load.setFont(new Font("Dialog", Font.PLAIN, 12));
		btn_folder_load.addActionListener(new ActionListener() {@Override
			public void actionPerformed(ActionEvent e) {

				String prefix = Paths.get(weatherPrefix).getFileName().toString();
				int startYear = ((Number)comboBox_folder_start.getSelectedItem()).intValue();
				int stopYear = ((Number)comboBox_folder_end.getSelectedItem()).intValue();
				boolean useMarkov = chckbx_weather_useMarkov.isSelected();
				try {
					weatherHist.onRead(folderPath, prefix, startYear, stopYear, useMarkov);
					onReset_list();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "FAILED TO READ: "+e.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});	
		panel_1.add(btn_folder_load);
		
		JLabel lblNewLabel_10 = new JLabel("Weather Data from Database");
		lblNewLabel_10.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_source.add(lblNewLabel_10);
		
		JPanel panel_db = new JPanel();
		panel_source.add(panel_db);
		
		JLabel lblNewLabel_11 = new JLabel("1. Weather Data Extracter Tool:");
		lblNewLabel_11.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_db.add(lblNewLabel_11);
		
		JButton btn_database_tool = new JButton("DB TOOL");
		btn_database_tool.setFont(new Font("Dialog", Font.PLAIN, 12));
		btn_database_tool.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc;
				fc = new JFileChooser();
		    	fc.setAcceptAllFileFilterUsed(false);
		    	fc.setMultiSelectionEnabled(false);
		    	fc.setSelectedFile(new File("/media/ryan/Storage/Users/Ryan_Murphy/My_Documents/Work/dbWeatherData.sqlite"));
		    	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(null);
				
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	Path weatherDB = fc.getSelectedFile().toPath();
		        	WeatherData data = new WeatherData(weatherDB);
		        	if(data.getVersion() != 1) {
		    			Object[] options = {"Yes","No"};
		    			int choice = JOptionPane.showOptionDialog(null, "Weather database is a old version, upgrade?", "Weather Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		    			if(choice == 0) {
		    				ProgressBar b = ProgressBar.createAndShowGUI(null, data, false);
		    				b.register(getInstance());
		    			} else {
		    				
		    			}
		    		} else {
		    			data.closeConnection();
	    				DatabaseWeatherSelector dbWeatherTool = new DatabaseWeatherSelector(weatherDB, weatherHist);
	    		    	dbWeatherTool.addSiteEventListener(getInstance());
	    		    	dbWeatherTool.pack();
	    		    	dbWeatherTool.setVisible(true);
		    		}
		        } else {
		        	
		        }
				
			}
		});
		panel_db.add(btn_database_tool);
				
		table_weather_History = new JTable();
		table_weather_History.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_weather_History.setModel(getModel(366));
		table_weather_History.getColumnModel().getColumn(0).setResizable(false);
		table_weather_History.getColumnModel().getColumn(0).setPreferredWidth(35);
		table_weather_History.getColumnModel().getColumn(0).setCellRenderer(new IntegerColumnRenderer());
		table_weather_History.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table_weather_History.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table_weather_History.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		panel_weather.add(table_weather_History);
		panel_weather.add(new JScrollPane(table_weather_History));
		
		return panel_weather;
	}
	private WEATHER getInstance() {
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

	    if (src == btn_weather_historyAdd) {
	    	int Year = ((Number)formattedTextField_insertYear.getValue()).intValue();
	    	int diy = soilwat.Times.Time_get_lastdoy_y(Year);
	    	if(this.weatherHist.getHistYearsInteger().contains(Year)) {
	    		JOptionPane.showMessageDialog(null, "The weather list already contains year "+String.valueOf(Year), "Alert", JOptionPane.ERROR_MESSAGE);
	    	} else {
	    		if(formattedTextField_insertYear.getText() != "") {
	    			//add to list
	    			this.weatherHist.add_year(Year, new double[diy], new double[diy], new double[diy]);
	    			//Save current Hist
	    			formattedTextField_insertYear.setValue(new Integer(Year+1));
	    			onGet_HIST(this.list_index);
	    			onReset_list();
	    		}
	    	}
	    } else if (src == btn_weather_historyRemove) {
	    	if(list_historyYears.getModel().getSize() != 0 && list_historyYears.getSelectedIndex() != -1) {
	    		int remove = Integer.parseInt(this.list_historyYears.getSelectedValue().toString());
	    		this.weatherHist.remove(remove);
	    		onReset_list();
	    	}
	    } else if (src == chckbx_weather_useMarkov) {
	    	if(chckbx_weather_useMarkov.isSelected()) {
	    		//TODO show tab
	    	} else {
	    		//TODO hide tab
	    	}
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<String> lsm = (JList<String>)e.getSource();
		if(!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
			if(list_index != -1) onGet_HIST(list_index);
			int minIndex = lsm.getMinSelectionIndex();
			int maxIndex = lsm.getMaxSelectionIndex();
			for(int i=minIndex; i<=maxIndex; i++) {
				if(lsm.isSelectedIndex(i)) {
					this.list_index = i;
				}
			}
			//this.weatherHist.setCurrentYear(Integer.parseInt(list_historyYears.getSelectedValue()));
			onSet_HIST();
		}
	}
	
	@Override
	public void siteSelected(int Site) {
		onReset_list();
	}
	
	private DefaultTableModel getModel(int days) {
		DefaultTableModel model = null;
		if(days==366) {
			model = new DefaultTableModel(
				new Object[][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
				},
				new String[] {
					"DOY", "Tmax(C)", "Tmin(C)", "PPT(cm)"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -879153418326758299L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Integer.class, Double.class, Double.class, Double.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, true, true, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		} else if (days==365){

			model = new DefaultTableModel(
				new Object[][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
				},
				new String[] {
					"DOY", "Tmax(C)", "Tmin(C)", "PPT(cm)"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -879153418326758299L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Integer.class, Double.class, Double.class, Double.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, true, true, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		}
		return model;
	}

	@Override
	public void conversionComplete(Path weatherDB) {
		DatabaseWeatherSelector dbWeatherTool = new DatabaseWeatherSelector(weatherDB, weatherHist);
    	dbWeatherTool.addSiteEventListener(getInstance());
    	dbWeatherTool.pack();
    	dbWeatherTool.setVisible(true);
	}
}

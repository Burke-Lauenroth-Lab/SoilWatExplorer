package explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class WEATHER implements ListSelectionListener, ActionListener{
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
	
	JButton btn_weather_historyAdd;
	JButton btn_weather_historyRemove;
	
	private int list_index=-1;
	
	public WEATHER(soilwat.SW_WEATHER.WEATHER_INPUT_DATA weatherSetupIn, soilwat.SW_WEATHER_HISTORY weatherHist) {
		this.weatherHist = weatherHist;
		this.weatherSetupIn = weatherSetupIn;
	}
	
	public void onGetValues() {
		weatherSetupIn.use_snow = chckbx_weather_snowAccumulation.isSelected();
		weatherSetupIn.pct_snowdrift = ((Number)formattedTextField_pct_snowdrift.getValue()).doubleValue();
		weatherSetupIn.pct_snowRunoff = ((Number)formattedTextField_pct_snowRunoff.getValue()).doubleValue();
		weatherSetupIn.use_markov = chckbx_weather_useMarkov.isSelected();
		weatherSetupIn.days_in_runavg = ((Number)formattedTextField_days_in_runavg.getValue()).intValue();
		
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
		int year = Integer.parseInt(list_historyYears.getSelectedValue());
		onGet_HIST(year);
	}
	
	public void onSetValues() {
		chckbx_weather_snowAccumulation.setSelected(weatherSetupIn.use_snow);
		formattedTextField_pct_snowdrift.setValue(weatherSetupIn.pct_snowdrift);
		formattedTextField_pct_snowRunoff.setValue(weatherSetupIn.pct_snowRunoff);
		chckbx_weather_useMarkov.setSelected(weatherSetupIn.use_markov);
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
	
	private void onReset_list() {
		this.list_index = -1;
		list_historyYears.removeAll();
		list_historyYears.setListData(weatherHist.getHistYearsString());
		list_historyYears.setSelectedIndex(0);
		
		if(list_historyYears.getModel().getSize() == 0 && list_historyYears.getSelectedIndex() == -1) {
			table_weather_History.setModel(getModel(365));
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
		
		JLabel lbl_weather_skyCover = new JLabel("# SkyCover = additive for mean monthly sky cover [%]; min(100, max(0, scale + sky cover))");
		lbl_weather_skyCover.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_skyCover);
		
		JLabel lbl_weather_wind = new JLabel("# Wind = multiplicative for mean monthly wind speed; max(0, scale * wind speed)");
		lbl_weather_wind.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_wind);
		
		JLabel lbl_weather_rH = new JLabel("# rH = additive for mean monthly relative humidity [%]; min(100, max(0, scale + rel. Humidity))");
		lbl_weather_rH.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_weather_monthlyScaleParam.add(lbl_weather_rH);
		
		JLabel lbl_weather_transmissivity = new JLabel("# Transmissivity = multiplicative for mean monthly relative transmissivity; min(1, max(0, scale * transmissivity))");
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
		panel_weather_list.add(lbl_weather_WeatherHistoryList);
		
		JScrollPane scrollPane_weather_weatherYearList = new JScrollPane();
		panel_weather_list.add(scrollPane_weather_weatherYearList);
		list_historyYears = new JList<String>();
		list_historyYears.setVisibleRowCount(10);
		list_historyYears.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_historyYears.addListSelectionListener(this);
		scrollPane_weather_weatherYearList.setViewportView(list_historyYears);
		
		JPanel panel_weather_list_control = new JPanel();
		panel_weather_WeatherYears.add(panel_weather_list_control, BorderLayout.CENTER);
			
		btn_weather_historyRemove = new JButton("-");
		btn_weather_historyRemove.addActionListener(this);
		panel_weather_list_control.add(btn_weather_historyRemove);
		
		btn_weather_historyAdd = new JButton("+");
		btn_weather_historyAdd.addActionListener(this);
		panel_weather_list_control.add(btn_weather_historyAdd);
		
		JLabel lbl_weather_addYear = new JLabel("Year:");
		panel_weather_list_control.add(lbl_weather_addYear);
		
		formattedTextField_insertYear = new JFormattedTextField(format_int);
		formattedTextField_insertYear.setColumns(5);
		formattedTextField_insertYear.setValue(new Integer(Collections.max(this.weatherHist.getHistYearsInteger())+1));
		panel_weather_list_control.add(formattedTextField_insertYear);
		
		table_weather_History = new JTable();
		table_weather_History.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_weather_History.setModel(getModel(366));
		table_weather_History.getColumnModel().getColumn(0).setResizable(false);
		table_weather_History.getColumnModel().getColumn(0).setPreferredWidth(35);
		panel_weather.add(table_weather_History);
		panel_weather.add(new JScrollPane(table_weather_History));
		
		return panel_weather;
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
}

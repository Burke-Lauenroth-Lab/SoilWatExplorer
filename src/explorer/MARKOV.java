package explorer;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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

import soilwat.SW_MARKOV;
import charts.Weather;
import java.awt.FlowLayout;

public class MARKOV implements ListSelectionListener, ActionListener {

	private JPanel panelMarkov;
	private JTable table_markov_prod;
	private JTable table_markov_cov;
	
	private JButton btn_weather_historyAdd;
	private JButton btn_weather_historyRemove;
	private JFormattedTextField formattedTextField_insertYear;
	private JList<String> list_historyYears;
	private soilwat.SW_WEATHER_HISTORY weatherHist;
	
	soilwat.InputData.MarkovIn markovIn;
	
	public MARKOV(soilwat.InputData.MarkovIn markovIn, soilwat.SW_WEATHER_HISTORY hist) {
		this.markovIn = markovIn;
		this.weatherHist = hist;
	}
	
	public void onGetValues() {
		for(int i=0; i<366; i++) {
			markovIn.probability.wetprob[i] = ((Number)table_markov_prod.getValueAt(i, 1)).doubleValue();
			markovIn.probability.dryprob[i] = ((Number)table_markov_prod.getValueAt(i, 2)).doubleValue();
			markovIn.probability.avg_ppt[i] = ((Number)table_markov_prod.getValueAt(i, 3)).doubleValue();
			markovIn.probability.std_ppt[i] = ((Number)table_markov_prod.getValueAt(i, 4)).doubleValue();
		}
		
		for(int week=0; week<53; week++) {
			markovIn.covariance.u_cov[week][0] = ((Number)table_markov_cov.getValueAt(week, 1)).doubleValue();
			markovIn.covariance.u_cov[week][1] = ((Number)table_markov_cov.getValueAt(week, 2)).doubleValue();
			markovIn.covariance.v_cov[week][0][0] = ((Number)table_markov_cov.getValueAt(week, 3)).doubleValue();
			markovIn.covariance.v_cov[week][0][1] = ((Number)table_markov_cov.getValueAt(week, 4)).doubleValue();
			markovIn.covariance.v_cov[week][1][0] = ((Number)table_markov_cov.getValueAt(week, 5)).doubleValue();
			markovIn.covariance.v_cov[week][1][1] = ((Number)table_markov_cov.getValueAt(week, 6)).doubleValue();
		}
	}
	
	public void onSetValues() {
		onResetList();
		
		for(int i=0; i<366; i++) {
			table_markov_prod.setValueAt(i+1, i, 0);
			table_markov_prod.setValueAt(markovIn.probability.wetprob[i], i, 1);
			table_markov_prod.setValueAt(markovIn.probability.dryprob[i], i, 2);
			table_markov_prod.setValueAt(markovIn.probability.avg_ppt[i], i, 3);
			table_markov_prod.setValueAt(markovIn.probability.std_ppt[i], i, 4);
		}
		
		for(int week=0; week<53; week++) {
			table_markov_cov.setValueAt(week+1, week, 0);
			table_markov_cov.setValueAt(markovIn.covariance.u_cov[week][0], week, 1);
			table_markov_cov.setValueAt(markovIn.covariance.u_cov[week][1], week, 2);
			table_markov_cov.setValueAt(markovIn.covariance.v_cov[week][0][0], week, 3);
			table_markov_cov.setValueAt(markovIn.covariance.v_cov[week][0][1], week, 4);
			table_markov_cov.setValueAt(markovIn.covariance.v_cov[week][1][0], week, 5);
			table_markov_cov.setValueAt(markovIn.covariance.v_cov[week][1][1], week, 6);
		}
	}
	
	public void onResetList() {
		list_historyYears.removeAll();
		list_historyYears.setListData(weatherHist.getHistYearsString());
		list_historyYears.setSelectedIndex(0);
		
		Integer year = null;
		if(this.weatherHist.data())
			year = new Integer(Collections.max(this.weatherHist.getHistYearsInteger())+1);
		else
			year = new Integer(1950);
		formattedTextField_insertYear.setValue(year);
	}
	
	public JPanel onGetPanel() {
		if(this.panelMarkov == null)
			this.panelMarkov = onGetPanel_Markov();
		return this.panelMarkov;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private JPanel onGetPanel_Markov() {
		Format format_int = NumberFormat.getIntegerInstance();
		((NumberFormat)format_int).setGroupingUsed(false);
		
		JPanel panel_markov = new JPanel();
		panel_markov.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel = new JPanel();
		panel_markov.add(panel);
		//panel_markov.setLayout(new BorderLayout(0, 0));
		
		JPanel panelProd = new JPanel();
		panel.add(panelProd);
		table_markov_prod = new JTable();
		table_markov_prod.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_markov_prod.setModel(getModelProb());
		table_markov_prod.getColumnModel().getColumn(0).setResizable(false);
		table_markov_prod.getColumnModel().getColumn(0).setPreferredWidth(10);
		table_markov_prod.getColumnModel().getColumn(0).setCellRenderer(new IntegerColumnRenderer());
		table_markov_prod.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table_markov_prod.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table_markov_prod.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		table_markov_prod.getColumnModel().getColumn(4).setCellRenderer(new DoubleColumnRenderer());
		panelProd.add(table_markov_prod);
		panelProd.add(new JScrollPane(table_markov_prod));
		
		JPanel panelCov = new JPanel();
		panel.add(panelCov);
		table_markov_cov = new JTable();
		table_markov_cov.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_markov_cov.setModel(getModelCov());
		table_markov_cov.getColumnModel().getColumn(0).setResizable(false);
		table_markov_cov.getColumnModel().getColumn(0).setPreferredWidth(10);
		table_markov_cov.getColumnModel().getColumn(0).setCellRenderer(new IntegerColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(1).setPreferredWidth(25);
		table_markov_cov.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(2).setPreferredWidth(25);
		table_markov_cov.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(3).setPreferredWidth(25);
		table_markov_cov.getColumnModel().getColumn(4).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(4).setPreferredWidth(25);
		table_markov_cov.getColumnModel().getColumn(5).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(5).setPreferredWidth(25);
		table_markov_cov.getColumnModel().getColumn(6).setCellRenderer(new DoubleColumnRenderer());
		table_markov_cov.getColumnModel().getColumn(6).setPreferredWidth(25);
		panelCov.add(table_markov_cov);
		panelCov.add(new JScrollPane(table_markov_cov));
		
		JPanel panel_weather_list = new JPanel();
		panel_markov.add(panel_weather_list);
		panel_weather_list.setLayout(new BoxLayout(panel_weather_list, BoxLayout.PAGE_AXIS));
		
		JLabel lbl_weather_WeatherHistoryList = new JLabel("Generate Sample Weather Data");
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
		
		return panel_markov;
	}
	
	private DefaultTableModel getModelCov() {
		DefaultTableModel model;
		model = new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null } },
				new String[] { "DOY", "t1", "t2", "t3", "t4","t5","t6" }) {
			/**
				 * 
				 */
			private static final long serialVersionUID = -879153418326758289L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { Integer.class, Double.class,
					Double.class, Double.class, Double.class, Double.class, Double.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, true, true, true, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		return model;
	}
	
	private DefaultTableModel getModelProb() {
		DefaultTableModel model;
		model = new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null }, },
				new String[] { "DOY", "wet", "dry", "avg_ppt", "std_ppt" }) {
			/**
				 * 
				 */
			private static final long serialVersionUID = -879153418326758299L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { Integer.class, Double.class,
					Double.class, Double.class, Double.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, true, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		return model;
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
	    			double tmpmax=0, tmpmin=0, PPT=0;
	    			
	    			soilwat.SW_MARKOV markov = new SW_MARKOV(markovIn.getLog());
	    			this.onGetValues();//save
	    			markov.onSetInput(this.markovIn);
	    			markov.setPpt_events(0);
	    			//add to list
	    			double[] tmax = new double[diy];
	    			double[] tmin = new double[diy];
	    			double[] ppt = new double[diy];
	    			
	    			for(int i=0; i<diy; i++) {
	    				if(i==0) {
	    					if(weatherHist.getHistYearsInteger().contains(new Integer(Year-1))) {
	    						int index = soilwat.Times.Time_get_lastdoy_y(Year-1);
	    						PPT = weatherHist.get_ppt_array(Year-1)[index-1];
	    					} else {
	    						PPT=0;
	    					}
	    				}
	    				
	    				markov.set_MaxMinRain(tmpmax, tmpmin, PPT);
	    				try {
							markov.SW_MKV_today(i);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    				PPT = markov.get_MaxMinRain().rain;
	    				tmpmax = markov.get_MaxMinRain().tmax;
	    				tmpmin = markov.get_MaxMinRain().tmin;
	    				tmax[i] = tmpmax;
	    				tmin[i] = tmpmin;
	    				ppt[i] = PPT;
	    			}
	    			
	    			this.weatherHist.add_year(Year, ppt, tmax, tmin);
	    			//Save current Hist
	    			formattedTextField_insertYear.setValue(new Integer(Year+1));
	    			list_historyYears.removeAll();
	    			list_historyYears.setListData(weatherHist.getHistYearsString());
	    			list_historyYears.setSelectedIndex(0);
	    		}
	    	}
	    } else if (src == btn_weather_historyRemove) {
	    	if(list_historyYears.getModel().getSize() != 0 && list_historyYears.getSelectedIndex() != -1) {
	    		int remove = Integer.parseInt(this.list_historyYears.getSelectedValue().toString());
	    		this.weatherHist.remove(remove);
	    		list_historyYears.removeAll();
	    		list_historyYears.setListData(weatherHist.getHistYearsString());
	    		list_historyYears.setSelectedIndex(0);
	    	}
	    }
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

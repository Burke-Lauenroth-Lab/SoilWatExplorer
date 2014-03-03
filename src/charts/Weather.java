package charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
//import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.text.Format;
import java.text.NumberFormat;

public class Weather extends JFrame implements ActionListener, ItemListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private soilwat.SW_WEATHER_HISTORY hist;
	private int year;
	
	private JFormattedTextField formattedTextField_mat;
	private JCheckBox chckbx_mat_c_all;
	private JComboBox<Integer> comboBox_mat_startYear;
	private JComboBox<Integer> comboBox_mat_endYear;
	private JFormattedTextField formattedTextField_map;
	private JCheckBox chckbx_map_all;
	private JComboBox<Integer> comboBox_map_startYear;
	private JComboBox<Integer> comboBox_map_endYear;
	private JComboBox<Integer> comboBox_daily_year;
	
	private JPanel jpanel;
	private JPanel panel_graph_daily;
	private JPanel panel_graph_mean;
	private JPanel daily_temp;
	private JPanel daily_ppt;
	private JPanel mean_temp;
	private JPanel mean_ppt;
	
	private JComboBox<Integer> comboBox_mean_monthly_start;
	private JComboBox<Integer> comboBox_mean_monthly_stop;

	public Weather() {
	}
	
	public Weather(String applicationTitle, String chartTitle, soilwat.SW_WEATHER_HISTORY hist, int year) {
		super(applicationTitle);
		
		this.hist = hist;
		this.year = year;
		
		Format format_double = NumberFormat.getNumberInstance();
		((NumberFormat)format_double).setGroupingUsed(false);
		
		Integer[] years = new Integer[this.hist.get_nYears()];
		this.hist.getHistYearsInteger().toArray(years);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 513);
		jpanel = new JPanel();
		
		JPanel panel_Control = new JPanel();
		jpanel.add(panel_Control);
		panel_Control.setLayout(new BoxLayout(panel_Control, BoxLayout.PAGE_AXIS));
		
		JPanel panel_mat = new JPanel();
		panel_Control.add(panel_mat);
		
		JLabel lbl_mat_c = new JLabel("Mean Annual Temperature in Celsius :");
		panel_mat.add(lbl_mat_c);
		
		formattedTextField_mat = new JFormattedTextField(format_double);
		formattedTextField_mat.setFont(new Font("Dialog", Font.PLAIN, 12));
		formattedTextField_mat.setColumns(5);
		panel_mat.add(formattedTextField_mat);
		
		JLabel lblNewLabel_1 = new JLabel("for");
		panel_mat.add(lblNewLabel_1);
		
		chckbx_mat_c_all = new JCheckBox("All Years or");
		chckbx_mat_c_all.setSelected(true);
		chckbx_mat_c_all.addActionListener(this);
		panel_mat.add(chckbx_mat_c_all);
		
		JLabel lblNewLabel_2 = new JLabel("Start Year:");
		panel_mat.add(lblNewLabel_2);
		
		comboBox_mat_startYear = new JComboBox<Integer>();
		comboBox_mat_startYear.setEnabled(false);
		comboBox_mat_startYear.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_mat_startYear.setSelectedIndex(0);
		comboBox_mat_startYear.addItemListener(this);
		panel_mat.add(comboBox_mat_startYear);
		
		JLabel lblNewLabel = new JLabel(" - ");
		panel_mat.add(lblNewLabel);
		
		JLabel lblNewLabel_3 = new JLabel("End Year:");
		panel_mat.add(lblNewLabel_3);
		
		comboBox_mat_endYear = new JComboBox<Integer>();
		comboBox_mat_endYear.setEnabled(false);
		comboBox_mat_endYear.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_mat_endYear.setSelectedIndex(hist.get_nYears()-1);
		comboBox_mat_endYear.addItemListener(this);
		panel_mat.add(comboBox_mat_endYear);
		
		JPanel panel_map_cm = new JPanel();
		panel_Control.add(panel_map_cm);
		
		JLabel lbl_map_cm = new JLabel("Mean Annual Precipitation in cm:          ");
		panel_map_cm.add(lbl_map_cm);
		
		formattedTextField_map = new JFormattedTextField(format_double);
		formattedTextField_map.setColumns(5);
		panel_map_cm.add(formattedTextField_map);
		
		JLabel lblNewLabel_4 = new JLabel("for");
		panel_map_cm.add(lblNewLabel_4);
		
		chckbx_map_all = new JCheckBox("All Years or ");
		chckbx_map_all.setSelected(true);
		chckbx_map_all.addActionListener(this);
		panel_map_cm.add(chckbx_map_all);
		
		JLabel lbl_map_start = new JLabel("Start Year:");
		panel_map_cm.add(lbl_map_start);
		
		comboBox_map_startYear = new JComboBox<Integer>();
		comboBox_map_startYear.setEnabled(false);
		comboBox_map_startYear.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_map_startYear.setSelectedIndex(0);
		comboBox_map_startYear.addItemListener(this);
		panel_map_cm.add(comboBox_map_startYear);
		
		JLabel lblNewLabel_5 = new JLabel(" - ");
		panel_map_cm.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("End Year:");
		panel_map_cm.add(lblNewLabel_6);
		
		comboBox_map_endYear = new JComboBox<Integer>();
		comboBox_map_endYear.setEnabled(false);
		comboBox_map_endYear.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_map_endYear.setSelectedIndex(hist.get_nYears()-1);
		comboBox_map_endYear.addItemListener(this);
		panel_map_cm.add(comboBox_map_endYear);
		
		JPanel panel_daily = new JPanel();
		panel_Control.add(panel_daily);
		
		JLabel lbl_Daily = new JLabel("Daily Temp and Precip for year : ");
		panel_daily.add(lbl_Daily);
		
		comboBox_daily_year = new JComboBox<Integer>();
		comboBox_daily_year.setEnabled(true);
		comboBox_daily_year.setModel(new DefaultComboBoxModel<Integer>(years));
		int ind = 0;
		for(int i=0; i<hist.get_nYears(); i++)
			if(years[i] == year)
				ind = i;
		comboBox_daily_year.setSelectedIndex(ind);
		comboBox_daily_year.addItemListener(this);
		panel_daily.add(comboBox_daily_year);
		
		JLabel lblNewLabel_7 = new JLabel("Mean monthly values for Precip and Temp: ");
		panel_daily.add(lblNewLabel_7);
		
		comboBox_mean_monthly_start = new JComboBox<Integer>();
		comboBox_mean_monthly_start.setEnabled(true);
		comboBox_mean_monthly_start.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_mean_monthly_start.setSelectedIndex(0);
		comboBox_mean_monthly_start.addItemListener(this);
		panel_daily.add(comboBox_mean_monthly_start);
		
		JLabel lblNewLabel_8 = new JLabel(":");
		panel_daily.add(lblNewLabel_8);
		
		comboBox_mean_monthly_stop = new JComboBox<Integer>();
		comboBox_mean_monthly_stop.setEnabled(true);
		comboBox_mean_monthly_stop.setModel(new DefaultComboBoxModel<Integer>(years));
		comboBox_mean_monthly_stop.setSelectedIndex(hist.get_nYears()-1);
		comboBox_mean_monthly_stop.addItemListener(this);
		panel_daily.add(comboBox_mean_monthly_stop);
		
		
		daily_temp = createWeatherTempPanel();
		daily_ppt = createWeatherPPTPanel();
		
		mean_temp = createWeather_meanMonthly_Temp();
		mean_ppt = createWeather_meanMonthly_PPT();
		
		panel_graph_daily = new JPanel();
		panel_graph_daily.add(daily_temp);
		panel_graph_daily.add(daily_ppt);
		
		panel_graph_mean = new JPanel();
		panel_graph_mean.add(mean_temp);
		panel_graph_mean.add(mean_ppt);
		
		jpanel.add(panel_graph_daily);
		jpanel.add(panel_graph_mean);
		jpanel.setPreferredSize( new Dimension(1350,975));
		setContentPane(jpanel);
		
		onCalcMAT();
		onCalcMAP();
	}
	private void onCalcMAT() {
		int srtYear = ((Integer)comboBox_mat_startYear.getSelectedItem()).intValue();
		int endYear = ((Integer)comboBox_mat_endYear.getSelectedItem()).intValue();
		
		try {
			formattedTextField_mat.setValue(new Double(hist.MAT_C(srtYear, endYear)));
		} catch (Exception e) {
			formattedTextField_mat.setValue(new Double(0));
			JOptionPane.showMessageDialog(null, e.toString(),"Alert", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void onCalcMAP() {
		int srtYear = ((Integer)comboBox_map_startYear.getSelectedItem()).intValue();
		int endYear = ((Integer)comboBox_map_endYear.getSelectedItem()).intValue();
		
		try {
			formattedTextField_map.setValue(new Double(hist.MAP_cm(srtYear, endYear)));
		} catch (Exception e) {
			formattedTextField_map.setValue(new Double(0));
			JOptionPane.showMessageDialog(null, e.toString(),"Alert", JOptionPane.ERROR_MESSAGE);
		}
	}
	private XYDataset createDatasetTemp() {
		hist.setCurrentYear(this.year);
		YIntervalSeries yintervalseries = new YIntervalSeries("Temperature (C)");
		
        Object obj = new Day(1,1,this.year);
        
        for (int i = 0; i < hist.getDays(); i++)
        {
        	yintervalseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), hist.get_temp_avg(i), hist.get_temp_min(i), hist.get_temp_max(i));
           	obj = ((RegularTimePeriod) (obj)).next();
        }
        YIntervalSeriesCollection yintervalseriescollection = new YIntervalSeriesCollection();
        yintervalseriescollection.addSeries(yintervalseries);
        
        return yintervalseriescollection;
	}
	private XYDataset createDatasetPPT() {
		hist.setCurrentYear(this.year);
		YIntervalSeries yintervalseries = new YIntervalSeries("Precipitation (cm)");
		
        Object obj = new Day(1,1,this.year);
        
        for (int i = 0; i < hist.getDays(); i++)
        {
        	yintervalseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), hist.get_ppt(i), 0, 0);
           	obj = ((RegularTimePeriod) (obj)).next();
        }
        YIntervalSeriesCollection yintervalseriescollection = new YIntervalSeriesCollection();
        yintervalseriescollection.addSeries(yintervalseries);
        
        return yintervalseriescollection;
	}
	
	private XYDataset createDataset_meanMonthly_Temp() {
		int strt = ((Integer)comboBox_mean_monthly_start.getSelectedItem()).intValue();
		int stop = ((Integer)comboBox_mean_monthly_stop.getSelectedItem()).intValue();
		
		double[] values = null;
		try {
			values = hist.meanMonthlyTempC(strt, stop);
		} catch (Exception e) {
			values = new double[12];
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		XYSeries xyseries = new XYSeries("Mean Monthly Temp "+String.valueOf(strt)+":"+String.valueOf(stop));
		for(int i=0; i<12; i++) {
			xyseries.add(i+1, values[i]);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}
	private XYDataset createDataset_meanMonthly_PPT() {
		int strt = ((Integer)comboBox_mean_monthly_start.getSelectedItem()).intValue();
		int stop = ((Integer)comboBox_mean_monthly_stop.getSelectedItem()).intValue();
		
		double[] values = null;
		try {
			values = hist.meanMonthlyPPTcm(strt, stop);
		} catch (Exception e) {
			values = new double[12];
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		XYSeries xyseries = new XYSeries("Mean Monthly PPT "+String.valueOf(strt)+":"+String.valueOf(stop));
		for(int i=0; i<12; i++) {
			xyseries.add(i+1, values[i]);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}
	
	//for database
	private XYDataset createDataset_meanMonthly_Temp(double[] values, int Site_id) {
		XYSeries xyseries = new XYSeries("Mean Monthly Temp for Site: "+String.valueOf(Site_id));
		for(int i=0; i<12; i++) {
			xyseries.add(i+1, values[i]);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}
	private XYDataset createDataset_meanMonthly_PPT(double[] values, int Site_id) {
		XYSeries xyseries = new XYSeries("Mean Monthly PPT for Site: "+String.valueOf(Site_id));
		for(int i=0; i<12; i++) {
			xyseries.add(i+1, values[i]);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}
	
	private JFreeChart createChartTEMP(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date", "Temp (C)", dataset);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
        deviationrenderer.setSeriesStroke(0, new BasicStroke(2F, 1, 1));
        deviationrenderer.setSeriesStroke(1, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
        deviationrenderer.setSeriesFillPaint(1, new Color(200, 200, 255));
        xyplot.setRenderer(deviationrenderer);
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }
	private JFreeChart createChartPPT(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date", "PPT (cm)", dataset);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }
	private JFreeChart createChart(XYDataset dataset, String title, String Yaxis) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, "Month", Yaxis, dataset);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setInsets(new RectangleInsets(1D, 1D, 1D, 2D));
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setAxisOffset(new RectangleInsets(1D, 1D, 1D, 1D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }
	public JPanel createWeatherTempPanel() {
		JFreeChart jfreechart = createChartTEMP(createDatasetTemp(), "Temperature Avgerage with min/max for "+String.valueOf(this.year));
		return new ChartPanel(jfreechart);
	}
	public JPanel createWeatherPPTPanel() {
		JFreeChart jfreechart = createChartPPT(createDatasetPPT(), "Precipitation for "+String.valueOf(this.year));
		return new ChartPanel(jfreechart);
	}
	public JPanel createWeather_meanMonthly_Temp() {
		int strt = ((Integer)comboBox_mean_monthly_start.getSelectedItem()).intValue();
		int stop = ((Integer)comboBox_mean_monthly_stop.getSelectedItem()).intValue();
		JFreeChart jfreechart = createChart(createDataset_meanMonthly_Temp(), "Mean Monthly Temperature "+String.valueOf(strt)+":"+String.valueOf(stop), "TEMP (C)");
		return new ChartPanel(jfreechart);
	}
	public JPanel createWeather_meanMonthly_PPT() {
		int strt = ((Integer)comboBox_mean_monthly_start.getSelectedItem()).intValue();
		int stop = ((Integer)comboBox_mean_monthly_stop.getSelectedItem()).intValue();
		JFreeChart jfreechart = createChart(createDataset_meanMonthly_PPT(), "Mean Monthly Precipitation "+String.valueOf(strt)+":"+String.valueOf(stop), "PPT (cm)");
		return new ChartPanel(jfreechart);
	}
	//database//
	public JPanel create_DatabasePanel(double[] temp, double[] ppt, int Site_id) {
		JPanel panel_tempPPT = new JPanel();
		panel_tempPPT.add(new ChartPanel(createChart(createDataset_meanMonthly_Temp(temp, Site_id), "Mean Monthly Temperature for Site: "+String.valueOf(Site_id), "TEMP (C)")));
		panel_tempPPT.add(new ChartPanel(createChart(createDataset_meanMonthly_PPT(ppt, Site_id), "Mean Monthly Precipitation for Site: "+String.valueOf(Site_id), "PPT (cm)")));
		return panel_tempPPT;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == chckbx_mat_c_all) {
			if(chckbx_mat_c_all.isSelected()) {
				comboBox_mat_startYear.setEnabled(false);
				comboBox_mat_startYear.setSelectedIndex(0);
				comboBox_mat_endYear.setEnabled(false);
				comboBox_mat_endYear.setSelectedIndex(hist.get_nYears()-1);
			} else {
				comboBox_mat_startYear.setEnabled(true);
				comboBox_mat_endYear.setEnabled(true);
			}
			onCalcMAT();
		}
		if(src == chckbx_map_all) {
			if(chckbx_map_all.isSelected()) {
				comboBox_map_startYear.setEnabled(false);
				comboBox_map_startYear.setSelectedIndex(0);
				comboBox_map_endYear.setEnabled(false);
				comboBox_map_endYear.setSelectedIndex(hist.get_nYears()-1);
			} else {
				comboBox_map_startYear.setEnabled(true);
				comboBox_map_endYear.setEnabled(true);
			}
			onCalcMAP();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if(src == comboBox_mat_startYear) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_mat_startYear.getSelectedIndex() > comboBox_mat_endYear.getSelectedIndex()) {
					comboBox_mat_startYear.setSelectedIndex(comboBox_mat_endYear.getSelectedIndex());
				}
			}
			onCalcMAT();
		}
		if(src == comboBox_mat_endYear) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_mat_endYear.getSelectedIndex() < comboBox_mat_startYear.getSelectedIndex()) {
					comboBox_mat_endYear.setSelectedIndex(comboBox_mat_startYear.getSelectedIndex());
				}
			}
			onCalcMAT();
		}
		if(src == comboBox_map_startYear) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_map_startYear.getSelectedIndex() > comboBox_map_endYear.getSelectedIndex()) {
					comboBox_map_startYear.setSelectedIndex(comboBox_map_endYear.getSelectedIndex());
				}
			}
			onCalcMAP();
		}
		if(src == comboBox_map_endYear) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_map_endYear.getSelectedIndex() < comboBox_map_startYear.getSelectedIndex()) {
					comboBox_map_endYear.setSelectedIndex(comboBox_map_startYear.getSelectedIndex());
				}
			}
			onCalcMAP();
		}
		if(src == comboBox_daily_year) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				this.year = ((Integer)comboBox_daily_year.getSelectedItem()).intValue();
				panel_graph_daily.remove(daily_temp);
				panel_graph_daily.remove(daily_ppt);
				daily_temp = createWeatherTempPanel();
				daily_ppt = createWeatherPPTPanel();
				panel_graph_daily.add(daily_temp);
				panel_graph_daily.add(daily_ppt);
				panel_graph_daily.revalidate();
			}
		}
		
		if(src == comboBox_mean_monthly_start) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_mean_monthly_start.getSelectedIndex() > comboBox_mean_monthly_stop.getSelectedIndex()) {
					comboBox_mean_monthly_start.setSelectedIndex(comboBox_mean_monthly_stop.getSelectedIndex());
				}
				panel_graph_mean.remove(mean_temp);
				panel_graph_mean.remove(mean_ppt);
				mean_temp = createWeather_meanMonthly_Temp();
				mean_ppt = createWeather_meanMonthly_PPT();
				panel_graph_mean.add(mean_temp);
				panel_graph_mean.add(mean_ppt);
				panel_graph_mean.revalidate();
			}
		}
		if(src == comboBox_mean_monthly_stop) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(comboBox_mean_monthly_stop.getSelectedIndex() < comboBox_mean_monthly_start.getSelectedIndex()) {
					comboBox_mean_monthly_stop.setSelectedIndex(comboBox_mean_monthly_start.getSelectedIndex());
				}
				panel_graph_mean.remove(mean_temp);
				panel_graph_mean.remove(mean_ppt);
				mean_temp = createWeather_meanMonthly_Temp();
				mean_ppt = createWeather_meanMonthly_PPT();
				panel_graph_mean.add(mean_temp);
				panel_graph_mean.add(mean_ppt);
				panel_graph_mean.revalidate();
			}
		}
	}
}

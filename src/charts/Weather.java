package charts;

import java.awt.BasicStroke;
import java.awt.Color;

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
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class Weather extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private soilwat.SW_WEATHER_HISTORY hist;
	private int year;

	public Weather(String applicationTitle, String chartTitle, soilwat.SW_WEATHER_HISTORY hist, int year) {
		super(applicationTitle);
		
		this.hist = hist;
		this.year = year;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		JPanel jpanel = new JPanel();
		jpanel.add(createWeatherTempPanel());
		jpanel.add(createWeatherPPTPanel());
		//jpanel.setPreferredSize( new Dimension(500,440));
		setContentPane(jpanel);
	}
	private XYDataset createDatasetTemp() {
		hist.setCurrentYear(this.year);
		YIntervalSeries yintervalseries = new YIntervalSeries("Temperature");
		
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
		YIntervalSeries yintervalseries = new YIntervalSeries("Temperature");
		
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
	public JPanel createWeatherTempPanel() {
		JFreeChart jfreechart = createChartTEMP(createDatasetTemp(), "Temperature Avgerage with min/max for "+String.valueOf(this.year));
		return new ChartPanel(jfreechart);
	}
	public JPanel createWeatherPPTPanel() {
		JFreeChart jfreechart = createChartPPT(createDatasetPPT(), "Precipitation for "+String.valueOf(this.year));
		return new ChartPanel(jfreechart);
	}
}

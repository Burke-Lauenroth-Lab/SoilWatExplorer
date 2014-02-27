package charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class Weather extends JFrame {

	public Weather(String applicationTitle, String chartTitle, soilwat.SW_WEATHER_HISTORY hist) {
		super(applicationTitle);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		JPanel jpanel = createWeatherPanel();
		jpanel.setPreferredSize( new Dimension(500,270));
		setContentPane(jpanel);
	}
	private static XYDataset createDataset() {
		YIntervalSeries yintervalseries = new YIntervalSeries("Series 1");
        YIntervalSeries yintervalseries1 = new YIntervalSeries("Series 2");
        Object obj = new Week();
        double d = 100D;
        double d1 = 100D;
        for (int i = 0; i <= 52; i++)
        {
                double d2 = 0.050000000000000003D * (double)i;
                yintervalseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), d, d - d2, d + d2);
                d = (d + Math.random()) - 0.45000000000000001D;
                double d3 = 0.070000000000000007D * (double)i;
                yintervalseries1.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), d1, d1 - d3, d1 + d3);
                d1 = (d1 + Math.random()) - 0.55000000000000004D;
                obj = ((RegularTimePeriod) (obj)).next();
        }

        YIntervalSeriesCollection yintervalseriescollection = new YIntervalSeriesCollection();
        yintervalseriescollection.addSeries(yintervalseries);
        yintervalseriescollection.addSeries(yintervalseries1);
        return yintervalseriescollection;
	}
	private JFreeChart createChart(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Temp", "Doy", dataset);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
        deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(1, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
        deviationrenderer.setSeriesFillPaint(1, new Color(200, 200, 255));
        xyplot.setRenderer(deviationrenderer);
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
        
    }
	public JPanel createWeatherPanel() {
		JFreeChart jfreechart = createChart(createDataset(), "Temperature for Year");
		return new ChartPanel(jfreechart);
	}
}

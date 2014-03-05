package charts;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;

public class OUT_weather extends OutputChart {
	private static final long serialVersionUID = 1L;

	public OUT_weather(SW_OUTPUT.SW_OUT_TIME time, SW_CONTROL control, OutPeriod p){
		super(control, time, p);
		this.key = OutKey.eSW_Temp;
		this.data = control.onGetOutput(key, p);
	}
	
	private XYDataset createDatasetTemp() {
		YIntervalSeries yintervalseries = new YIntervalSeries("Temperature (C)");
		
        Object obj = null;
        int timeSteps = time.get_nRows(period);
        
        switch (this.period) {
		case SW_DAY:
			obj = new Day(soilwat.Times.doy2mday(time.days[0][1]),soilwat.Times.doy2month(time.days[0][1]),time.days[0][0]);
			
		break;
		case SW_WEEK:
			obj = new Week(time.weeks[0][1],time.weeks[0][0]);
		break;
		case SW_MONTH:
			obj = new Month(time.months[0][1],time.months[0][0]);
			break;
		case SW_YEAR:
			obj = new Year(time.years[0]);
			break;
		}
        
        for (int i = 0; i < timeSteps; i++)
        {
        	yintervalseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), data[i][2], data[i][1], data[i][0]);
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
	
	public JPanel createWeatherTempPanel() {
		JFreeChart jfreechart = createChartTEMP(createDatasetTemp(), "Temperature Avgerage with min/max");
		return new ChartPanel(jfreechart);
	}

	@Override
	void updateChart() {
		// TODO Auto-generated method stub
		
	}
}

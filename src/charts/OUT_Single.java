package charts;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;

public class OUT_Single extends OutputChart {
	private static final long serialVersionUID = 1L;
	private JPanel chart;

	public OUT_Single(SW_CONTROL control, OutKey key, OutPeriod p){
		super(control, control.onGet_Timing(), p);
		this.key = key;
		this.data = control.onGetOutput(key, p);
		chart = createChartPanel();
		this.add(chart);
	}
	
	private XYDataset createDatasetTemp() {
		XYSeries xyseries = new XYSeries(this.control.onGet_OutputColumnNames(this.key)[0]);
		
		Object obj = null;
        int timeSteps = time.get_nRows(period);
        
        switch (this.period) {
		case SW_DAY:
			int dayOfMonth = soilwat.Times.doy2mday(time.days[0][1]);
			int monthOfYear = soilwat.Times.doy2month(time.days[0][1])+1;
			obj = new Day(dayOfMonth,monthOfYear,time.days[0][0]);
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
		
		for(int i=0; i<timeSteps; i++) {
			xyseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), data[i][0]);
			obj = ((RegularTimePeriod) (obj)).next();
		}
		
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return xyseriescollection;
	}
	
	private JFreeChart createChart(XYDataset dataset, String title) {
		String valueLabel = this.control.onGet_Unit(this.key);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date", valueLabel, dataset);
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
	
	public JPanel createChartPanel() {
		String Title = this.control.onGet_OutputColumnNames(this.key)[0];
		JFreeChart jfreechart = createChart(createDatasetTemp(), Title);
		return new ChartPanel(jfreechart);
	}

	@Override
	void updateChart() {
		this.remove(chart);
		chart = createChartPanel();
		this.add(chart);
		this.revalidate();
	}
}

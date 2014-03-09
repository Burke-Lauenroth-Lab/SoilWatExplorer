package charts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
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

public class OUT_MultipleSingle  extends OutputChart implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel chart;
	private XYSeriesCollection xyseriescollection;
	private List<JCheckBox> chbx_layers = new ArrayList<JCheckBox>();
	private List<XYSeries> series = new ArrayList<XYSeries>();

	public OUT_MultipleSingle(SW_CONTROL control, OutKey key, OutPeriod p){
		super(control, control.onGet_Timing(), p);
		this.key = key;
		this.data = control.onGetOutput(key, p);
		
		JPanel temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));
		JPanel row1 = new JPanel();
		JPanel row2 = new JPanel();
		JPanel row3 = new JPanel();
		JPanel row4 = new JPanel();
	
		String[] names = control.onGet_OutputColumnNames(this.key);
		for(int i=0; i<control.onGet_nColumns(key); i++) {
			JCheckBox cb = new JCheckBox(names[i], true);
			cb.addActionListener(this);
			if(i < 8)
				row1.add(cb);
			if(i >= 8 && i < 16)
				row2.add(cb);
			if(i >= 16 && i < 24)
				row3.add(cb);
			if(i >= 24 && i < 25)
				row4.add(cb);
			chbx_layers.add(cb);
		}
		int i = control.onGet_nColumns(key);
		if(i <= 8)
			temp.add(row1);
		if(i > 8 && i <= 16)
			temp.add(row2);
		if(i > 16 && i <= 24)
			temp.add(row3);
		if(i > 24 && i <= 25)
			temp.add(row4);
		
		chart = createChartPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(temp);
		this.add(chart);
	}
	
	private XYDataset createDatasetTemp() {
		xyseriescollection = new XYSeriesCollection();
		series.clear();
		for(int index=0; index<control.onGet_nColumns(key); index++) {
			XYSeries temp = getLayer(index);
			series.add(temp);
			xyseriescollection.addSeries(temp);
		}
		return xyseriescollection;
	}
	
	private XYSeries getLayer(int index) {
		XYSeries xyseries = new XYSeries(this.control.onGet_OutputColumnNames(this.key)[index]);

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
			xyseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), data[i][index]);
			obj = ((RegularTimePeriod) (obj)).next();
		}
		return xyseries;
	}
	
	private JFreeChart createChart(XYDataset dataset, String title) {
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
	
	public JPanel createChartPanel() {
		String Title = this.key.name();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		for (JCheckBox cb : chbx_layers) {
			if(src == cb) {
				int index = chbx_layers.indexOf(cb);
				if(!cb.isSelected()) {
					xyseriescollection.removeSeries(series.get(index));
				} else {
					xyseriescollection.removeAllSeries();
					for (JCheckBox cbx : chbx_layers) {
						int index_add = chbx_layers.indexOf(cbx);
						if(cbx.isSelected()) {
							xyseriescollection.addSeries(series.get(index_add));
						}
					}
					chart.revalidate();
					chart.repaint();
				}
			}
		}
	}
}

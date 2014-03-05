package charts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;

public class OUT_Layers extends OutputChart implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel chart;
	private XYSeriesCollection xyseriescollection;
	private List<JCheckBox> chbx_layers = new ArrayList<JCheckBox>();
	private List<XYSeries> series = new ArrayList<XYSeries>();

	public OUT_Layers(SW_CONTROL control, OutKey key, OutPeriod p){
		super(control, control.onGet_Timing(), p);
		this.key = key;
		this.data = control.onGetOutput(key, p);
		
		JPanel temp = new JPanel();
		
		for(int lyr=0; lyr<control.onGet_nColumns(key); lyr++) {
			JCheckBox cb = new JCheckBox("Layer:"+String.valueOf(lyr+1), true);
			cb.addActionListener(this);
			temp.add(cb);
			chbx_layers.add(cb);
		}
		
		chart = createChartPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(temp);
		this.add(chart);
	}
	
	private XYDataset createDatasetTemp() {
		xyseriescollection = new XYSeriesCollection();
		for(int lyr=0; lyr<control.onGet_nColumns(key); lyr++) {
			series.add(getLayer(lyr));
			xyseriescollection.addSeries(getLayer(lyr));
		}
		return xyseriescollection;
	}
	
	private XYSeries getLayer(int lyr) {
		XYSeries xyseries = new XYSeries(this.control.onGet_OutputColumnNames(this.key)[lyr]);

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
			xyseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), data[i][lyr]);
			obj = ((RegularTimePeriod) (obj)).next();
		}
		return xyseries;
	}
	
	private JFreeChart createChart(XYDataset dataset, String title) {
		String valueLabel = this.control.onGet_Unit(this.key);
        JFreeChart chart = ChartFactory.createXYAreaChart(title, "Time", valueLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)chart.getPlot();
        DateAxis dateaxis = new DateAxis("Time");
        dateaxis.setLowerMargin(0.0D);
        dateaxis.setUpperMargin(0.0D);
        xyplot.setDomainAxis(dateaxis);
        xyplot.setForegroundAlpha(0.5F);
        XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        xyitemrenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("#,##0.00")));
        //NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        //numberaxis.setAutoRangeIncludesZero(false);
        //numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
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
				int index = Integer.valueOf(cb.getText().replaceFirst("Layer:", ""))-1;
				if(!cb.isSelected()) {
					xyseriescollection.removeSeries(series.get(index));
				} else {
					xyseriescollection.removeAllSeries();
					for (JCheckBox cbx : chbx_layers) {
						int index_add = Integer.valueOf(cbx.getText().replaceFirst("Layer:", ""))-1;
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

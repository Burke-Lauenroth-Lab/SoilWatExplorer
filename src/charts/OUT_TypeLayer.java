package charts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

public class OUT_TypeLayer extends OutputChart implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	private JPanel chart;
	private XYSeriesCollection xyseriescollection;
	private List<JCheckBox> chbx_layers = new ArrayList<JCheckBox>();
	private List<XYSeries> series = new ArrayList<XYSeries>();
	//private JComboBox<String> cmbx_Type = new JComboBox<String>();
	private static String[] typeNames = new String[] {"Total","Trees","Shrubs","Forbs","Grasses"};
	private int type;

	public OUT_TypeLayer(SW_CONTROL control, OutKey key, OutPeriod p){
		super(control, control.onGet_Timing(), p);
		this.key = key;
		this.data = control.onGetOutput(key, p);
		
		JPanel temp = new JPanel();
		temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));
		//cmbx_Type.setModel(new DefaultComboBoxModel<String>(new String[] {"Total","Trees","Shrubs","Forbs","Grasses"}));
		//cmbx_Type.setSelectedIndex(0);
		//cmbx_Type.addItemListener(this);
		
		JPanel row0 = new JPanel();
		//row0.add(cmbx_Type);
		temp.add(row0);
		
		JPanel row1 = new JPanel();
		JPanel row2 = new JPanel();
		JPanel row3 = new JPanel();
		JPanel row4 = new JPanel();
		
		int lyrs = control.onGet_nColumns(key)/5;
		
		for(int lyr=0; lyr<lyrs; lyr++) {
			JCheckBox cb = new JCheckBox("Layer:"+String.valueOf(lyr+1), true);
			cb.addActionListener(this);
			if(lyr < 8)
				row1.add(cb);
			if(lyr >= 8 && lyr < 16)
				row2.add(cb);
			if(lyr >= 16 && lyr < 24)
				row3.add(cb);
			if(lyr >= 24 && lyr < 25)
				row4.add(cb);
			chbx_layers.add(cb);
		}
		if(lyrs > 0)
			temp.add(row1);
		if(lyrs > 8)
			temp.add(row2);
		if(lyrs > 16)
			temp.add(row3);
		if(lyrs > 24)
			temp.add(row4);
		
		chart = createChartPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(temp);
		this.add(chart);
	}
	
	private XYDataset createDatasetTemp() {
		//int type = cmbx_Type.getSelectedIndex();
		xyseriescollection = new XYSeriesCollection();
		series.clear();
		int lyrs = (int)(control.onGet_nColumns(key)/5); 
		for(int lyr=0; lyr<lyrs; lyr++) {
			XYSeries temp = getLayer(lyr, this.type);
			series.add(temp);
			xyseriescollection.addSeries(temp);
		}
		return xyseriescollection;
	}
	
	private XYSeries getLayer(int lyr, int type) {
		int lyrs = (int)(control.onGet_nColumns(key)/5);
		XYSeries xyseries = new XYSeries(this.control.onGet_OutputColumnNames(this.key)[lyr+(lyrs*type)]);

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
			xyseries.add(((RegularTimePeriod) (obj)).getFirstMillisecond(), data[i][lyr+(lyrs*type)]);
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
        return chart;
    }
	
	public JPanel createChartPanel() {
		String Title = this.key.name()+typeNames[this.type];
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

	@SuppressWarnings("unchecked")
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		this.type = ((JComboBox<String>) src).getSelectedIndex();
		updateChart();
	}
	
}

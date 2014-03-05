package charts;

import javax.swing.JPanel;

import explorer.PeriodEvent;
import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;
import soilwat.SW_OUTPUT.SW_OUT_TIME;

public abstract class OutputChart extends JPanel implements PeriodEvent {
	private static final long serialVersionUID = 1L;
	protected SW_CONTROL control;
	protected double[][] data;
	protected OutPeriod period;
	protected SW_OUT_TIME time;
	protected OutKey key;
	
	abstract void updateChart();
	
	public OutputChart(SW_CONTROL control, SW_OUT_TIME t, OutPeriod p) {
		this.period = p;
		this.time = t;
		this.control = control;
	}
	
	public void periodChange(OutPeriod p) {
		this.period = p;
		this.data = control.onGetOutput(key, p);
		updateChart();
	}
}

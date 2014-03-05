package charts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;
import explorer.KeyEvent;
import explorer.PeriodEvent;

public class ChartSelector extends JPanel implements PeriodEvent, KeyEvent {
	private static final long serialVersionUID = 1L;

	protected List<PeriodEvent> periodEvent = new ArrayList<PeriodEvent>();

	//all the charts for the different outputs;
	private OUT_Weather panel_weather;
	private OUT_Single panel_SoilInf;
	private OUT_Single panel_SurfaceWater;
	private OUT_Single panel_AET;
	private OUT_Single panel_PET;
	private OUT_Single panel_WetDays;
	private OUT_Single panel_DeepSWC;
	
	private OUT_Layers panel_VWCBulk;
	private OUT_Layers panel_VWCMatric;
	private OUT_Layers panel_SWCBulk;
	private OUT_Layers panel_SWABulk;
	private OUT_Layers panel_SWAMatric;
	private OUT_Layers panel_SWPMatric;
	private OUT_Layers panel_EvapSoil;
	private OUT_Layers panel_LyrDrain;
	private OUT_Layers panel_SoilTemp;
	
	
	private OutPeriod period;
	private SW_CONTROL control;
	
	public ChartSelector(SW_CONTROL control, OutPeriod period, OutKey key) {
		this.control = control;
		this.period = period;
		
		panel_weather = new OUT_Weather(this.control, this.period);
		this.addPeriodEventListener(panel_weather);
		
		panel_SoilInf = new OUT_Single(this.control, OutKey.eSW_SoilInf, this.period);
		this.addPeriodEventListener(panel_SoilInf);
		panel_SurfaceWater = new OUT_Single(this.control, OutKey.eSW_SurfaceWater, this.period);
		this.addPeriodEventListener(panel_SurfaceWater);
		panel_AET = new OUT_Single(this.control, OutKey.eSW_AET, this.period);
		this.addPeriodEventListener(panel_AET);
		panel_PET = new OUT_Single(this.control, OutKey.eSW_PET, this.period);
		this.addPeriodEventListener(panel_PET);
		panel_WetDays = new OUT_Single(this.control, OutKey.eSW_WetDays, this.period);
		this.addPeriodEventListener(panel_WetDays);
		panel_DeepSWC = new OUT_Single(this.control, OutKey.eSW_DeepSWC, this.period);
		this.addPeriodEventListener(panel_DeepSWC);
		
		panel_VWCBulk = new OUT_Layers(this.control, OutKey.eSW_VWCBulk, this.period);
		this.addPeriodEventListener(panel_VWCBulk);
		panel_VWCMatric = new OUT_Layers(this.control, OutKey.eSW_VWCMatric, this.period);
		this.addPeriodEventListener(panel_VWCMatric);
		panel_SWCBulk = new OUT_Layers(this.control, OutKey.eSW_SWCBulk, this.period);
		this.addPeriodEventListener(panel_SWCBulk);
		panel_SWABulk = new OUT_Layers(this.control, OutKey.eSW_SWABulk, this.period);
		this.addPeriodEventListener(panel_SWABulk);
		panel_SWAMatric = new OUT_Layers(this.control, OutKey.eSW_SWAMatric, this.period);
		this.addPeriodEventListener(panel_SWAMatric);
		panel_SWPMatric = new OUT_Layers(this.control, OutKey.eSW_SWPMatric, this.period);
		this.addPeriodEventListener(panel_SWPMatric);
		panel_EvapSoil = new OUT_Layers(this.control, OutKey.eSW_EvapSoil, this.period);
		this.addPeriodEventListener(panel_EvapSoil);
		panel_LyrDrain = new OUT_Layers(this.control, OutKey.eSW_LyrDrain, this.period);
		this.addPeriodEventListener(panel_LyrDrain);
		panel_SoilTemp = new OUT_Layers(this.control, OutKey.eSW_SoilTemp, this.period);
		this.addPeriodEventListener(panel_SoilTemp);
		
		keyChange(key);
	}
	
	public void addPeriodEventListener(PeriodEvent e) {
		periodEvent.add(e);
	}
	
	private void setChart(OutputChart panel_chart) {
		this.removeAll();
		add(panel_chart);
		revalidate();
	}
	
	//broadcast out the period change to all the graphs
	@Override
	public void periodChange(OutPeriod p) {
		period = p;
		for (PeriodEvent periodEventListeners : periodEvent) {
			periodEventListeners.periodChange(p);
		}
	}

	@Override
	public void keyChange(OutKey key) {
		switch (key) {
		case eSW_NoKey:
			break;
		case eSW_AllWthr:
			break;
		case eSW_Temp:
			setChart(panel_weather);
			break;
		case eSW_Precip:
			break;
		case eSW_SoilInf:
			setChart(panel_SoilInf);
			break;
		case eSW_Runoff:
			break;
		case eSW_AllH2O:
			break;
		case eSW_VWCBulk:
			setChart(panel_VWCBulk);
			break;
		case eSW_VWCMatric:
			setChart(panel_VWCMatric);
			break;
		case eSW_SWCBulk:
			setChart(panel_SWCBulk);
			break;
		case eSW_SWABulk:
			setChart(panel_SWABulk);
			break;
		case eSW_SWAMatric:
			setChart(panel_SWAMatric);
			break;
		case eSW_SWPMatric:
			setChart(panel_SWPMatric);
			break;
		case eSW_SurfaceWater:
			setChart(panel_SurfaceWater);
			break;
		case eSW_Transp:
			break;
		case eSW_EvapSoil:
			setChart(panel_EvapSoil);
			break;
		case eSW_EvapSurface:
			break;
		case eSW_Interception:
			break;
		case eSW_LyrDrain:
			setChart(panel_LyrDrain);
			break;
		case eSW_HydRed:
			break;
		case eSW_ET:
			break;
		case eSW_AET:
			setChart(panel_AET);
			break;
		case eSW_PET:
			setChart(panel_PET);
			break;
		case eSW_WetDays:
			setChart(panel_WetDays);
			break;
		case eSW_SnowPack:
			break;
		case eSW_DeepSWC:
			setChart(panel_DeepSWC);
			break;
		case eSW_SoilTemp:
			setChart(panel_SoilTemp);
			break;
		case eSW_AllVeg:
			break;
		case eSW_Estab:
			break;
		case eSW_LastKey:
			break;
		default:
			break;
		}
	}
}

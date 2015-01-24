package charts;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;
import explorer.KeyEvent;
import explorer.PeriodEvent;

import java.awt.BorderLayout;

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
	
	private OUT_TypeLayer panel_TRANSP;
	private OUT_TypeLayer panel_HYDRED;
	
	private OUT_MultipleSingle panel_Precip;
	private OUT_MultipleSingle panel_Runoff;
	private OUT_MultipleSingle panel_EvapSurface;
	private OUT_MultipleSingle panel_Interception;
	private OUT_MultipleSingle panel_SnowPack;
	private OUT_MultipleSingle panel_Estab;
	
	private OutPeriod period;
	private SW_CONTROL control;
	
	public ChartSelector(SW_CONTROL control, OutPeriod period, OutKey key, soilwat.InputData.OutputIn out, JComboBox<String> typeSelector) {
		this.control = control;
		this.period = period;
		setLayout(new BorderLayout(0, 0));
		
		if(out.outputs[OutKey.eSW_Temp.idx()].use) {
			panel_weather = new OUT_Weather(this.control, period==null?out.outputs[OutKey.eSW_Temp.idx()].periodColumn:this.period);
			this.addPeriodEventListener(panel_weather);
		}
		if(out.outputs[OutKey.eSW_Precip.idx()].use) {
			panel_Precip = new OUT_MultipleSingle(this.control, OutKey.eSW_Precip, period==null?out.outputs[OutKey.eSW_Precip.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_Precip);
		}
		if(out.outputs[OutKey.eSW_Runoff.idx()].use) {
			panel_Runoff = new OUT_MultipleSingle(this.control, OutKey.eSW_Runoff, period==null?out.outputs[OutKey.eSW_Runoff.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_Runoff);
		}
		if(out.outputs[OutKey.eSW_EvapSurface.idx()].use) {
			panel_EvapSurface = new OUT_MultipleSingle(this.control, OutKey.eSW_EvapSurface, period==null?out.outputs[OutKey.eSW_EvapSurface.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_EvapSurface);
		}
		if(out.outputs[OutKey.eSW_Interception.idx()].use) {
			panel_Interception = new OUT_MultipleSingle(this.control, OutKey.eSW_Interception, period==null?out.outputs[OutKey.eSW_Interception.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_Interception);
		}
		if(out.outputs[OutKey.eSW_SnowPack.idx()].use) {
			panel_SnowPack = new OUT_MultipleSingle(this.control, OutKey.eSW_SnowPack, period==null?out.outputs[OutKey.eSW_SnowPack.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SnowPack);
		}
		if(out.outputs[OutKey.eSW_Estab.idx()].use) {
			panel_Estab = new OUT_MultipleSingle(this.control, OutKey.eSW_Estab, period==null?out.outputs[OutKey.eSW_Estab.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_Estab);
		}
		if(out.outputs[OutKey.eSW_SoilInf.idx()].use) {
			panel_SoilInf = new OUT_Single(this.control, OutKey.eSW_SoilInf, period==null?out.outputs[OutKey.eSW_SoilInf.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SoilInf);
		}
		if(out.outputs[OutKey.eSW_SurfaceWater.idx()].use) {
			panel_SurfaceWater = new OUT_Single(this.control, OutKey.eSW_SurfaceWater, period==null?out.outputs[OutKey.eSW_SurfaceWater.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SurfaceWater);
		}
		
		if(out.outputs[OutKey.eSW_AET.idx()].use) {
			panel_AET = new OUT_Single(this.control, OutKey.eSW_AET, period==null?out.outputs[OutKey.eSW_AET.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_AET);
		}
		if(out.outputs[OutKey.eSW_PET.idx()].use) {
			panel_PET = new OUT_Single(this.control, OutKey.eSW_PET, period==null?out.outputs[OutKey.eSW_PET.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_PET);
		}
		if(out.outputs[OutKey.eSW_WetDays.idx()].use) {
			panel_WetDays = new OUT_Single(this.control, OutKey.eSW_WetDays, period==null?out.outputs[OutKey.eSW_WetDays.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_WetDays);
		}
		if(out.outputs[OutKey.eSW_DeepSWC.idx()].use) {
			panel_DeepSWC = new OUT_Single(this.control, OutKey.eSW_DeepSWC, period==null?out.outputs[OutKey.eSW_DeepSWC.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_DeepSWC);
		}
		
		if(out.outputs[OutKey.eSW_VWCBulk.idx()].use) {
			panel_VWCBulk = new OUT_Layers(this.control, OutKey.eSW_VWCBulk, period==null?out.outputs[OutKey.eSW_VWCBulk.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_VWCBulk);
		}
		if(out.outputs[OutKey.eSW_VWCMatric.idx()].use) {
			panel_VWCMatric = new OUT_Layers(this.control, OutKey.eSW_VWCMatric, period==null?out.outputs[OutKey.eSW_VWCMatric.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_VWCMatric);
		}
		if(out.outputs[OutKey.eSW_SWCBulk.idx()].use) {
			panel_SWCBulk = new OUT_Layers(this.control, OutKey.eSW_SWCBulk, period==null?out.outputs[OutKey.eSW_SWCBulk.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SWCBulk);
		}
		if(out.outputs[OutKey.eSW_SWABulk.idx()].use) {
			panel_SWABulk = new OUT_Layers(this.control, OutKey.eSW_SWABulk, period==null?out.outputs[OutKey.eSW_SWABulk.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SWABulk);
		}
		if(out.outputs[OutKey.eSW_SWAMatric.idx()].use) {
			panel_SWAMatric = new OUT_Layers(this.control, OutKey.eSW_SWAMatric, period==null?out.outputs[OutKey.eSW_SWAMatric.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SWAMatric);
		}
		if(out.outputs[OutKey.eSW_SWPMatric.idx()].use) {
			panel_SWPMatric = new OUT_Layers(this.control, OutKey.eSW_SWPMatric, period==null?out.outputs[OutKey.eSW_SWPMatric.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SWPMatric);
		}
		if(out.outputs[OutKey.eSW_EvapSoil.idx()].use) {
			panel_EvapSoil = new OUT_Layers(this.control, OutKey.eSW_EvapSoil, period==null?out.outputs[OutKey.eSW_EvapSoil.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_EvapSoil);
		}
		if(out.outputs[OutKey.eSW_LyrDrain.idx()].use) {
			panel_LyrDrain = new OUT_Layers(this.control, OutKey.eSW_LyrDrain, period==null?out.outputs[OutKey.eSW_LyrDrain.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_LyrDrain);
		}
		if(out.outputs[OutKey.eSW_SoilTemp.idx()].use) {
			panel_SoilTemp = new OUT_Layers(this.control, OutKey.eSW_SoilTemp, period==null?out.outputs[OutKey.eSW_SoilTemp.idx()].periodColumn:period);
			this.addPeriodEventListener(panel_SoilTemp);
		}
		
		if(out.outputs[OutKey.eSW_Transp.idx()].use) {
			panel_TRANSP = new OUT_TypeLayer(this.control, OutKey.eSW_Transp, period==null?out.outputs[OutKey.eSW_Transp.idx()].periodColumn:period);
			typeSelector.addItemListener(panel_TRANSP);
			this.addPeriodEventListener(panel_TRANSP);
		}
		if(out.outputs[OutKey.eSW_HydRed.idx()].use) {
			panel_HYDRED = new OUT_TypeLayer(this.control, OutKey.eSW_HydRed, period==null?out.outputs[OutKey.eSW_HydRed.idx()].periodColumn:period);
			typeSelector.addItemListener(panel_HYDRED);
			this.addPeriodEventListener(panel_HYDRED);
		}
		
		keyChange(key);
	}
	
	public void addPeriodEventListener(PeriodEvent e) {
		periodEvent.add(e);
	}
	
	private void setChart(OutputChart panel_chart) {
		this.removeAll();
		panel_chart.setAlignmentY(Component.TOP_ALIGNMENT);
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
			setChart(panel_Precip);
			break;
		case eSW_SoilInf:
			setChart(panel_SoilInf);
			break;
		case eSW_Runoff:
			setChart(panel_Runoff);
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
			setChart(panel_TRANSP);
			break;
		case eSW_EvapSoil:
			setChart(panel_EvapSoil);
			break;
		case eSW_EvapSurface:
			setChart(panel_EvapSurface);
			break;
		case eSW_Interception:
			setChart(panel_Interception);
			break;
		case eSW_LyrDrain:
			setChart(panel_LyrDrain);
			break;
		case eSW_HydRed:
			setChart(panel_HYDRED);
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
			setChart(panel_SnowPack);
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
			setChart(panel_Estab);
			break;
		case eSW_LastKey:
			break;
		default:
			break;
		}
	}
}

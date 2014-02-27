package explorer;

import java.awt.Font;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JFormattedTextField;

public class SITE {
	private JFormattedTextField formattedTextField_swc_limit_min;
	private JFormattedTextField formattedTextField_swc_limit_init;
	private JFormattedTextField formattedTextField_swc_limit_wet;
	
	private JFormattedTextField formattedTextField_swc_transpiration_xinflec;
	private JFormattedTextField formattedTextField_swc_transpiration_slope;
	private JFormattedTextField formattedTextField_swc_transpiration_yinflec;
	private JFormattedTextField formattedTextField_swc_transpiration_range;
	
	private JCheckBox checkBox_swc_model_resetSWC;
	private JCheckBox checkBox_swc_model_deepdrain;
	private JFormattedTextField formattedTextField_swc_model_petMultiplier;
	private JFormattedTextField formattedTextField_swc_model_percentRunoff;
	
	private JFormattedTextField formattedTextField_swc_intrinsic_latitude;
	private JFormattedTextField formattedTextField_swc_intrinsic_altitude;
	private JFormattedTextField formattedTextField_swc_intrinsic_slope;
	private JFormattedTextField formattedTextField_swc_intrinsic_aspect;
	
	private JFormattedTextField formattedTextField_swc_snow_TminAccu2;
	private JFormattedTextField formattedTextField_swc_snow_TmaxCrit;
	private JFormattedTextField formattedTextField_swc_snow_lambdasnow;
	private JFormattedTextField formattedTextField_swc_snow_RmeltMin;
	private JFormattedTextField formattedTextField_swc_snow_RmeltMax;
	
	private JFormattedTextField formattedTextField_swc_drain_slow_drain_coeff;
	
	private JFormattedTextField formattedTextField_swc_evaporation_xinflec;
	private JFormattedTextField formattedTextField_swc_evaporation_slope;
	private JFormattedTextField formattedTextField_swc_evaporation_yinflec;
	private JFormattedTextField formattedTextField_swc_evaporation_range;
	
	private JCheckBox checkBox_swc_soilTemp_calc;
	private JFormattedTextField formattedTextField_swc_soilTemp_bmLimiter;
	private JFormattedTextField formattedTextField_swc_soilTemp_t1Param1;
	private JFormattedTextField formattedTextField_swc_soilTemp_t1Param2;
	private JFormattedTextField formattedTextField_swc_soilTemp_t1Param3;
	private JFormattedTextField formattedTextField_swc_soilTemp_csParam1;
	private JFormattedTextField formattedTextField_swc_soilTemp_csParam2;
	private JFormattedTextField formattedTextField_swc_soilTemp_shParam;
	private JFormattedTextField formattedTextField_swc_soilTemp_meanAirTemp;
	private JFormattedTextField formattedTextField_swc_soilTemp_stDeltaX;
	private JFormattedTextField formattedTextField_swc_soilTemp_stMaxDepth;
	
	private JSpinner spinner_swc_transpiration_shallow;
	private JCheckBox checkBox_swc_transpiration_use_shallow;
	private JSpinner spinner_swc_transpiration_medium;
	private JCheckBox checkBox_swc_transpiration_use_medium;
	private JSpinner spinner_swc_transpiration_deep;
	private JCheckBox checkBox_swc_transpiration_use_deep;
	private JSpinner spinner_swc_transpiration_veryDeep;
	private JCheckBox checkBox_swc_transpiration_use_veryDeep;
	
	private soilwat.InputData.SiteIn siteIn;
	
	public SITE(soilwat.InputData.SiteIn siteIn) {
		this.siteIn = siteIn;
	}
	
	public void onGetValues() {
		siteIn.swcLimits.swc_min = ((Number)formattedTextField_swc_limit_min.getValue()).doubleValue();
		siteIn.swcLimits.swc_init = ((Number)formattedTextField_swc_limit_init.getValue()).doubleValue();
		siteIn.swcLimits.swc_wet = ((Number)formattedTextField_swc_limit_wet.getValue()).doubleValue();
		
		siteIn.transpCoef.xinflec = ((Number)formattedTextField_swc_transpiration_xinflec.getValue()).doubleValue();
		siteIn.transpCoef.slope = ((Number)formattedTextField_swc_transpiration_slope.getValue()).doubleValue();
		siteIn.transpCoef.yinflec = ((Number)formattedTextField_swc_transpiration_yinflec.getValue()).doubleValue();
		siteIn.transpCoef.range = ((Number)formattedTextField_swc_transpiration_range.getValue()).doubleValue();
		
		siteIn.modelFlagsCoef.flags.reset_yr = checkBox_swc_model_resetSWC.isSelected();
		siteIn.modelFlagsCoef.flags.deepdrain = checkBox_swc_model_deepdrain.isSelected();
		siteIn.modelFlagsCoef.coefficients.petMultiplier = ((Number)formattedTextField_swc_model_petMultiplier.getValue()).doubleValue();
		siteIn.modelFlagsCoef.coefficients.percentRunoff = ((Number)formattedTextField_swc_model_percentRunoff.getValue()).doubleValue();
		
		siteIn.siteIntrinsicParams.latitude = ((Number)formattedTextField_swc_intrinsic_latitude.getValue()).doubleValue();
		siteIn.siteIntrinsicParams.altitude = ((Number)formattedTextField_swc_intrinsic_altitude.getValue()).doubleValue();
		siteIn.siteIntrinsicParams.slope = ((Number)formattedTextField_swc_intrinsic_slope.getValue()).doubleValue();
		siteIn.siteIntrinsicParams.aspect = ((Number)formattedTextField_swc_intrinsic_aspect.getValue()).doubleValue();
		
		siteIn.snowSimParams.TminAccu2 = ((Number)formattedTextField_swc_snow_TminAccu2.getValue()).doubleValue();
		siteIn.snowSimParams.TmaxCrit = ((Number)formattedTextField_swc_snow_TmaxCrit.getValue()).doubleValue();
		siteIn.snowSimParams.lambdasnow = ((Number)formattedTextField_swc_snow_lambdasnow.getValue()).doubleValue();
		siteIn.snowSimParams.RmeltMin = ((Number)formattedTextField_swc_snow_RmeltMin.getValue()).doubleValue();
		siteIn.snowSimParams.RmeltMax = ((Number)formattedTextField_swc_snow_RmeltMax.getValue()).doubleValue();
		
		siteIn.drainageCoef.slow_drain_coeff = ((Number)formattedTextField_swc_drain_slow_drain_coeff.getValue()).doubleValue();
		
		siteIn.evaporationCoef.xinflec = ((Number)formattedTextField_swc_evaporation_xinflec.getValue()).doubleValue();
		siteIn.evaporationCoef.slope = ((Number)formattedTextField_swc_evaporation_slope.getValue()).doubleValue();
		siteIn.evaporationCoef.yinflec = ((Number)formattedTextField_swc_evaporation_yinflec.getValue()).doubleValue();
		siteIn.evaporationCoef.range = ((Number)formattedTextField_swc_evaporation_range.getValue()).doubleValue();
		
		siteIn.soilTempConst.use_soil_temp = checkBox_swc_soilTemp_calc.isSelected();
		siteIn.soilTempConst.bmLimiter = ((Number)formattedTextField_swc_soilTemp_bmLimiter.getValue()).doubleValue();
		siteIn.soilTempConst.t1Param1 = ((Number)formattedTextField_swc_soilTemp_t1Param1.getValue()).doubleValue();
		siteIn.soilTempConst.t1Param2 = ((Number)formattedTextField_swc_soilTemp_t1Param2.getValue()).doubleValue();
		siteIn.soilTempConst.t1Param3 = ((Number)formattedTextField_swc_soilTemp_t1Param3.getValue()).doubleValue();
		siteIn.soilTempConst.csParam1 = ((Number)formattedTextField_swc_soilTemp_csParam1.getValue()).doubleValue();
		siteIn.soilTempConst.csParam2 = ((Number)formattedTextField_swc_soilTemp_csParam2.getValue()).doubleValue();
		siteIn.soilTempConst.shParam = ((Number)formattedTextField_swc_soilTemp_shParam.getValue()).doubleValue();
		siteIn.soilTempConst.meanAirTemp = ((Number)formattedTextField_swc_soilTemp_meanAirTemp.getValue()).doubleValue();
		siteIn.soilTempConst.stDeltaX = ((Number)formattedTextField_swc_soilTemp_stDeltaX.getValue()).doubleValue();
		siteIn.soilTempConst.stMaxDepth = ((Number)formattedTextField_swc_soilTemp_stMaxDepth.getValue()).doubleValue();
		
		siteIn.transpRegions.onClear();
		
		if(checkBox_swc_transpiration_use_shallow.isSelected())
			try {
				siteIn.transpRegions.set(1, ((Number)spinner_swc_transpiration_shallow.getValue()).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if(checkBox_swc_transpiration_use_medium.isSelected())
			try {
				siteIn.transpRegions.set(2, ((Number)spinner_swc_transpiration_medium.getValue()).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if(checkBox_swc_transpiration_use_deep.isSelected())
			try {
				siteIn.transpRegions.set(3, ((Number)spinner_swc_transpiration_deep.getValue()).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if(checkBox_swc_transpiration_use_veryDeep.isSelected())
			try {
				siteIn.transpRegions.set(4, ((Number)spinner_swc_transpiration_veryDeep.getValue()).intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void onSetValues() {
		formattedTextField_swc_limit_min.setValue(siteIn.swcLimits.swc_min);
		formattedTextField_swc_limit_init.setValue(siteIn.swcLimits.swc_init);
		formattedTextField_swc_limit_wet.setValue(siteIn.swcLimits.swc_wet);
		
		formattedTextField_swc_transpiration_xinflec.setValue(siteIn.transpCoef.xinflec);
		formattedTextField_swc_transpiration_slope.setValue(siteIn.transpCoef.slope);
		formattedTextField_swc_transpiration_yinflec.setValue(siteIn.transpCoef.yinflec);
		formattedTextField_swc_transpiration_range.setValue(siteIn.transpCoef.range);
		
		checkBox_swc_model_resetSWC.setSelected(siteIn.modelFlagsCoef.flags.reset_yr);
		checkBox_swc_model_deepdrain.setSelected(siteIn.modelFlagsCoef.flags.deepdrain);;
		formattedTextField_swc_model_petMultiplier.setValue(siteIn.modelFlagsCoef.coefficients.petMultiplier);
		formattedTextField_swc_model_percentRunoff.setValue(siteIn.modelFlagsCoef.coefficients.percentRunoff);
		
		formattedTextField_swc_intrinsic_latitude.setValue(siteIn.siteIntrinsicParams.latitude);
		formattedTextField_swc_intrinsic_altitude.setValue(siteIn.siteIntrinsicParams.altitude);
		formattedTextField_swc_intrinsic_slope.setValue(siteIn.siteIntrinsicParams.slope);
		formattedTextField_swc_intrinsic_aspect.setValue(siteIn.siteIntrinsicParams.aspect);
		
		formattedTextField_swc_snow_TminAccu2.setValue(siteIn.snowSimParams.TminAccu2);
		formattedTextField_swc_snow_TmaxCrit.setValue(siteIn.snowSimParams.TmaxCrit);
		formattedTextField_swc_snow_lambdasnow.setValue(siteIn.snowSimParams.lambdasnow);
		formattedTextField_swc_snow_RmeltMin.setValue(siteIn.snowSimParams.RmeltMin);
		formattedTextField_swc_snow_RmeltMax.setValue(siteIn.snowSimParams.RmeltMax);
		
		formattedTextField_swc_drain_slow_drain_coeff.setValue(siteIn.drainageCoef.slow_drain_coeff);
		
		formattedTextField_swc_evaporation_xinflec.setValue(siteIn.evaporationCoef.xinflec);
		formattedTextField_swc_evaporation_slope.setValue(siteIn.evaporationCoef.slope);
		formattedTextField_swc_evaporation_yinflec.setValue(siteIn.evaporationCoef.yinflec);
		formattedTextField_swc_evaporation_range.setValue(siteIn.evaporationCoef.range);
		
		checkBox_swc_soilTemp_calc.setSelected(siteIn.soilTempConst.use_soil_temp);
		formattedTextField_swc_soilTemp_bmLimiter.setValue(siteIn.soilTempConst.bmLimiter);
		formattedTextField_swc_soilTemp_t1Param1.setValue(siteIn.soilTempConst.t1Param1);
		formattedTextField_swc_soilTemp_t1Param2.setValue(siteIn.soilTempConst.t1Param2);
		formattedTextField_swc_soilTemp_t1Param3.setValue(siteIn.soilTempConst.t1Param3);
		formattedTextField_swc_soilTemp_csParam1.setValue(siteIn.soilTempConst.csParam1);
		formattedTextField_swc_soilTemp_csParam2.setValue(siteIn.soilTempConst.csParam2);
		formattedTextField_swc_soilTemp_shParam.setValue(siteIn.soilTempConst.shParam);
		formattedTextField_swc_soilTemp_meanAirTemp.setValue(siteIn.soilTempConst.meanAirTemp);
		formattedTextField_swc_soilTemp_stDeltaX.setValue(siteIn.soilTempConst.stDeltaX);
		formattedTextField_swc_soilTemp_stMaxDepth.setValue(siteIn.soilTempConst.stMaxDepth);
		
		if(siteIn.transpRegions.get_nRegions() == 0) {
			spinner_swc_transpiration_shallow.setValue(new Integer(0));
			checkBox_swc_transpiration_use_shallow.setSelected(false);
			spinner_swc_transpiration_medium.setValue(new Integer(0));
			checkBox_swc_transpiration_use_medium.setSelected(false);
			spinner_swc_transpiration_deep.setValue(new Integer(0));
			checkBox_swc_transpiration_use_deep.setSelected(false);
			spinner_swc_transpiration_veryDeep.setValue(new Integer(0));
			checkBox_swc_transpiration_use_veryDeep.setSelected(false);
		}
		if(siteIn.transpRegions.get_nRegions() >= 1) {
			spinner_swc_transpiration_shallow.setValue(siteIn.transpRegions.getRegion(0));
			checkBox_swc_transpiration_use_shallow.setSelected(true);
		}
		if(siteIn.transpRegions.get_nRegions() >= 2) {
			spinner_swc_transpiration_medium.setValue(siteIn.transpRegions.getRegion(1));
			checkBox_swc_transpiration_use_medium.setSelected(true);
		}
		if(siteIn.transpRegions.get_nRegions() >= 3) {
			spinner_swc_transpiration_deep.setValue(siteIn.transpRegions.getRegion(2));
			checkBox_swc_transpiration_use_deep.setSelected(true);
		}
		if(siteIn.transpRegions.get_nRegions() >= 4) {
			spinner_swc_transpiration_veryDeep.setValue(siteIn.transpRegions.getRegion(3));
			checkBox_swc_transpiration_use_veryDeep.setSelected(true);
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	protected JPanel onGetPanel_site() {
		Format format = NumberFormat.getNumberInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		JPanel panel_site = new JPanel();
		panel_site.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("25dlu"),
				ColumnSpec.decode("right:default"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("right:default"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("right:default"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblswcLimits = new JLabel("------ SWC Limits ------");
		panel_site.add(lblswcLimits, "1, 1, 2, 1, center, default");
		
		JLabel lblTranspirationCoefficients = new JLabel("------- Transpiration Coefficients -------");
		panel_site.add(lblTranspirationCoefficients, "4, 1, 6, 1, center, default");
		
		JLabel lblSwcMin = new JLabel("SWC Min:");
		lblSwcMin.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSwcMin, "1, 3, right, default");
			
		formattedTextField_swc_limit_min = new JFormattedTextField(format);
		formattedTextField_swc_limit_min.setColumns(10);
		panel_site.add(formattedTextField_swc_limit_min, "2, 3, left, default");
		
		JLabel lblRateShift_1 = new JLabel("Rate Shift:");
		lblRateShift_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRateShift_1, "4, 3, right, default");
		
		formattedTextField_swc_transpiration_xinflec = new JFormattedTextField(format);
		formattedTextField_swc_transpiration_xinflec.setColumns(10);
		panel_site.add(formattedTextField_swc_transpiration_xinflec, "5, 3, 5, 1, left, default");
		
		JLabel lblSwcInit = new JLabel("SWC Init:");
		lblSwcInit.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSwcInit, "1, 5, right, default");
		
		formattedTextField_swc_limit_init = new JFormattedTextField(format);
		formattedTextField_swc_limit_init.setColumns(10);
		panel_site.add(formattedTextField_swc_limit_init, "2, 5, left, default");
		
		JLabel lblRateShape = new JLabel("Rate Shape:");
		lblRateShape.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRateShape, "4, 5, right, default");
		
		formattedTextField_swc_transpiration_slope = new JFormattedTextField();
		formattedTextField_swc_transpiration_slope.setColumns(10);
		panel_site.add(formattedTextField_swc_transpiration_slope, "5, 5, 5, 1, left, default");
		
		JLabel lblSwcWet = new JLabel("SWC Wet:");
		lblSwcWet.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSwcWet, "1, 7, right, default");
		
		formattedTextField_swc_limit_wet = new JFormattedTextField();
		formattedTextField_swc_limit_wet.setColumns(10);
		panel_site.add(formattedTextField_swc_limit_wet, "2, 7, left, default");
		
		JLabel lblInflectionPoint_1 = new JLabel("Inflection Point:");
		lblInflectionPoint_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblInflectionPoint_1, "4, 7, right, default");
		
		formattedTextField_swc_transpiration_yinflec = new JFormattedTextField(format);
		formattedTextField_swc_transpiration_yinflec.setColumns(10);
		panel_site.add(formattedTextField_swc_transpiration_yinflec, "5, 7, 5, 1, left, default");
		
		JLabel lblModelFlags = new JLabel("------- Model Flags and Coefficients -------");
		panel_site.add(lblModelFlags, "1, 9, 2, 1, center, default");
		
		JLabel lblRange_1 = new JLabel("Range:");
		lblRange_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRange_1, "4, 9, right, default");
		
		formattedTextField_swc_transpiration_range = new JFormattedTextField(format);
		formattedTextField_swc_transpiration_range.setColumns(10);
		panel_site.add(formattedTextField_swc_transpiration_range, "5, 9, 5, 1, left, default");
		
		JLabel lblResetSwcYearly = new JLabel("Reset SWC Yearly:");
		lblResetSwcYearly.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblResetSwcYearly, "1, 11, right, default");
		
		checkBox_swc_model_resetSWC = new JCheckBox("");
		panel_site.add(checkBox_swc_model_resetSWC, "2, 11");
		
		JLabel lblSiteIntrinsic = new JLabel("------- Site Intrinsic Parameters -------");
		panel_site.add(lblSiteIntrinsic, "4, 11, 6, 1, center, default");
		
		JLabel lblDeepdrain = new JLabel("Deepdrain:");
		lblDeepdrain.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblDeepdrain, "1, 13, right, default");
		
		checkBox_swc_model_deepdrain = new JCheckBox("");
		panel_site.add(checkBox_swc_model_deepdrain, "2, 13");
		
		JLabel lblLatitude = new JLabel("Latitude:");
		lblLatitude.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblLatitude, "4, 13, right, default");
		
		formattedTextField_swc_intrinsic_latitude = new JFormattedTextField(format);
		formattedTextField_swc_intrinsic_latitude.setColumns(10);
		panel_site.add(formattedTextField_swc_intrinsic_latitude, "5, 13, 5, 1, left, default");
		
		JLabel lblPetMultiplier = new JLabel("PET Multiplier:");
		lblPetMultiplier.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblPetMultiplier, "1, 15, right, default");
		
		formattedTextField_swc_model_petMultiplier = new JFormattedTextField(format);
		formattedTextField_swc_model_petMultiplier.setColumns(10);
		panel_site.add(formattedTextField_swc_model_petMultiplier, "2, 15, left, default");
		
		JLabel lblAltitude = new JLabel("Altitude:");
		lblAltitude.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblAltitude, "4, 15, right, default");
		
		formattedTextField_swc_intrinsic_altitude = new JFormattedTextField(format);
		formattedTextField_swc_intrinsic_altitude.setColumns(10);
		panel_site.add(formattedTextField_swc_intrinsic_altitude, "5, 15, 5, 1, left, default");
		
		JLabel lblProportionOfPonded = new JLabel("Proportion of Ponded Removed:");
		lblProportionOfPonded.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblProportionOfPonded, "1, 17, right, default");
		
		formattedTextField_swc_model_percentRunoff = new JFormattedTextField(format);
		formattedTextField_swc_model_percentRunoff.setColumns(10);
		panel_site.add(formattedTextField_swc_model_percentRunoff, "2, 17, left, default");
		
		JLabel lblSlope = new JLabel("Slope:");
		lblSlope.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSlope, "4, 17, right, default");
		
		formattedTextField_swc_intrinsic_slope = new JFormattedTextField(format);
		formattedTextField_swc_intrinsic_slope.setColumns(10);
		panel_site.add(formattedTextField_swc_intrinsic_slope, "5, 17, 5, 1, left, default");
		
		JLabel lblSnowSimulation = new JLabel("------- Snow Simulation Parameters -------");
		panel_site.add(lblSnowSimulation, "1, 19, 2, 1, center, default");
		
		JLabel lblAspect = new JLabel("Aspect:");
		lblAspect.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblAspect, "4, 19, right, default");
		
		formattedTextField_swc_intrinsic_aspect = new JFormattedTextField(format);
		formattedTextField_swc_intrinsic_aspect.setColumns(10);
		panel_site.add(formattedTextField_swc_intrinsic_aspect, "5, 19, 5, 1, left, default");
		
		JLabel lblTminaccu = new JLabel("TminAccu2:");
		lblTminaccu.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblTminaccu, "1, 21, right, default");
		
		formattedTextField_swc_snow_TminAccu2 = new JFormattedTextField(format);
		formattedTextField_swc_snow_TminAccu2.setColumns(10);
		panel_site.add(formattedTextField_swc_snow_TminAccu2, "2, 21, left, default");
		
		JLabel lblSoilTemperature = new JLabel("------- Soil Temperature Constants -------");
		panel_site.add(lblSoilTemperature, "4, 21, 6, 1, center, default");
		
		JLabel lblTmaxcrit = new JLabel("TmaxCrit:");
		lblTmaxcrit.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblTmaxcrit, "1, 23, right, default");
		
		formattedTextField_swc_snow_TmaxCrit = new JFormattedTextField(format);
		formattedTextField_swc_snow_TmaxCrit.setColumns(10);
		panel_site.add(formattedTextField_swc_snow_TmaxCrit, "2, 23, left, default");
		
		JLabel lblCalcSoilTemp = new JLabel("Calc Soil Temp:");
		lblCalcSoilTemp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblCalcSoilTemp, "4, 23");
		
		checkBox_swc_soilTemp_calc = new JCheckBox("");
		panel_site.add(checkBox_swc_soilTemp_calc, "5, 23");
		
		JLabel lblBiomassLimiter = new JLabel("Biomass Limiter:");
		lblBiomassLimiter.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblBiomassLimiter, "6, 23, 3, 1");
		
		formattedTextField_swc_soilTemp_bmLimiter = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_bmLimiter.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_bmLimiter, "9, 23, left, default");
		
		JLabel lblLambdasnow = new JLabel("lambdasnow:");
		lblLambdasnow.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblLambdasnow, "1, 25, right, default");
		
		formattedTextField_swc_snow_lambdasnow = new JFormattedTextField(format);
		formattedTextField_swc_snow_lambdasnow.setColumns(10);
		panel_site.add(formattedTextField_swc_snow_lambdasnow, "2, 25, left, default");
		
		JLabel lblTEquationConstant = new JLabel("T1 EQN C1:");
		lblTEquationConstant.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblTEquationConstant, "4, 25, right, default");
		
		formattedTextField_swc_soilTemp_t1Param1 = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_t1Param1.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_t1Param1, "5, 25, left, default");
		
		JLabel lblConstant = new JLabel("C2:");
		lblConstant.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblConstant, "6, 25, right, default");
		
		formattedTextField_swc_soilTemp_t1Param2 = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_t1Param2.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_t1Param2, "7, 25, left, default");
		
		JLabel lblConstant_1 = new JLabel("C3:");
		lblConstant_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblConstant_1, "8, 25, right, default");
		
		formattedTextField_swc_soilTemp_t1Param3 = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_t1Param3.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_t1Param3, "9, 25, left, default");
		
		JLabel lblRmeltmin = new JLabel("RmeltMin:");
		lblRmeltmin.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRmeltmin, "1, 27, right, default");
		
		formattedTextField_swc_snow_RmeltMin = new JFormattedTextField(format);
		formattedTextField_swc_snow_RmeltMin.setColumns(10);
		panel_site.add(formattedTextField_swc_snow_RmeltMin, "2, 27, left, default");
		
		JLabel lblSoilthermalConductivityEqn = new JLabel("Soil-Thermal Conductivity EQN CS1:");
		lblSoilthermalConductivityEqn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSoilthermalConductivityEqn, "4, 27, 3, 1, right, default");
		
		formattedTextField_swc_soilTemp_csParam1 = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_csParam1.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_csParam1, "7, 27, left, default");
		
		JLabel lblCs = new JLabel("CS2:");
		lblCs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblCs, "8, 27, right, default");
		
		formattedTextField_swc_soilTemp_csParam2 = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_csParam2.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_csParam2, "9, 27, left, default");
		
		JLabel lblRmeltmax = new JLabel("RmeltMax:");
		lblRmeltmax.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRmeltmax, "1, 29, right, default");
		
		formattedTextField_swc_snow_RmeltMax = new JFormattedTextField(format);
		formattedTextField_swc_snow_RmeltMax.setColumns(10);
		panel_site.add(formattedTextField_swc_snow_RmeltMax, "2, 29, left, default");
		
		formattedTextField_swc_soilTemp_shParam = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_shParam.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_shParam, "9, 29, left, default");
		
		JLabel lblNewLabel = new JLabel("Constant Mean Air Temperature:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblNewLabel, "4, 31, 3, 1");
		
		formattedTextField_swc_soilTemp_meanAirTemp = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_meanAirTemp.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_meanAirTemp, "7, 31, left, default");
		
		formattedTextField_swc_soilTemp_stDeltaX = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_stDeltaX.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_stDeltaX, "9, 31, left, default");
		
		JLabel lblDrainageCoefficient = new JLabel("------- Drainage Coefficient -------");
		panel_site.add(lblDrainageCoefficient, "1, 31, 2, 1, center, default");
		
		JLabel lblSpecificHeatCapacity = new JLabel("Specific Heat Capacity EQN Constant, .18 in Parton's EQN:");
		lblSpecificHeatCapacity.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSpecificHeatCapacity, "4, 29, 5, 1, right, default");
		
		JLabel lblDeltax = new JLabel("deltaX:");
		lblDeltax.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblDeltax, "8, 31, right, default");
		
		JLabel lblMaxDepthFor = new JLabel("Max Depth for Soil Temp Function Equation:");
		lblMaxDepthFor.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblMaxDepthFor, "4, 33, 5, 1, right, default");
		
		JLabel lblSlowDrainCoefficient = new JLabel("Slow Drain Coefficient:");
		lblSlowDrainCoefficient.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblSlowDrainCoefficient, "1, 33, right, default");
		
		formattedTextField_swc_drain_slow_drain_coeff = new JFormattedTextField(format);
		formattedTextField_swc_drain_slow_drain_coeff.setColumns(10);
		panel_site.add(formattedTextField_swc_drain_slow_drain_coeff, "2, 33, left, default");
		
		formattedTextField_swc_soilTemp_stMaxDepth = new JFormattedTextField(format);
		formattedTextField_swc_soilTemp_stMaxDepth.setColumns(10);
		panel_site.add(formattedTextField_swc_soilTemp_stMaxDepth, "9, 33, left, default");
		
		JLabel lblEvaporationCoefficients = new JLabel("------- Evaporation Coefficients -------");
		panel_site.add(lblEvaporationCoefficients, "1, 35, 2, 1, center, default");
		
		JLabel lblTranspirationRegions = new JLabel("------- Transpiration Regions -------");
		panel_site.add(lblTranspirationRegions, "4, 35, 6, 1, center, default");
		
		JLabel lblShallow = new JLabel("Shallow - ");
		panel_site.add(lblShallow, "4, 37, 2, 1, right, default");
		
		JLabel label = new JLabel("1: ");
		panel_site.add(label, "6, 37");
				
		spinner_swc_transpiration_shallow = new JSpinner();
		panel_site.add(spinner_swc_transpiration_shallow, "7, 37, fill, default");
		
		checkBox_swc_transpiration_use_shallow = new JCheckBox("");
		panel_site.add(checkBox_swc_transpiration_use_shallow, "8, 37");
		
		JLabel lblRateShift = new JLabel("Rate Shift:");
		lblRateShift.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRateShift, "1, 37, right, default");
		
		formattedTextField_swc_evaporation_xinflec = new JFormattedTextField(format);
		formattedTextField_swc_evaporation_xinflec.setColumns(10);
		panel_site.add(formattedTextField_swc_evaporation_xinflec, "2, 37, left, default");
		
		JLabel lblMedium = new JLabel("Medium - ");
		panel_site.add(lblMedium, "4, 39, 2, 1, right, default");
		
		JLabel label_1 = new JLabel("2: ");
		panel_site.add(label_1, "6, 39");
		
		spinner_swc_transpiration_medium = new JSpinner();
		panel_site.add(spinner_swc_transpiration_medium, "7, 39, fill, default");
		
		checkBox_swc_transpiration_use_medium = new JCheckBox("");
		panel_site.add(checkBox_swc_transpiration_use_medium, "8, 39");
		
		JLabel lblRateSlope = new JLabel("Rate Slope:");
		lblRateSlope.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRateSlope, "1, 39, right, default");
		
		formattedTextField_swc_evaporation_slope = new JFormattedTextField(format);
		formattedTextField_swc_evaporation_slope.setColumns(10);
		panel_site.add(formattedTextField_swc_evaporation_slope, "2, 39, left, default");
		
		JLabel lblDeep = new JLabel("Deep - ");
		panel_site.add(lblDeep, "4, 41, 2, 1, right, default");
		
		JLabel label_2 = new JLabel("3: ");
		panel_site.add(label_2, "6, 41");
		
		spinner_swc_transpiration_deep = new JSpinner();
		panel_site.add(spinner_swc_transpiration_deep, "7, 41, fill, default");
		
		checkBox_swc_transpiration_use_deep = new JCheckBox("");
		panel_site.add(checkBox_swc_transpiration_use_deep, "8, 41");
		
		JLabel lblInflectionPoint = new JLabel("Inflection Point:");
		lblInflectionPoint.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblInflectionPoint, "1, 41, right, default");
		
		formattedTextField_swc_evaporation_yinflec = new JFormattedTextField(format);
		formattedTextField_swc_evaporation_yinflec.setColumns(10);
		panel_site.add(formattedTextField_swc_evaporation_yinflec, "2, 41, left, default");
		
		JLabel lblVeryDeep = new JLabel("Very Deep - ");
		panel_site.add(lblVeryDeep, "4, 43, 2, 1, right, default");
		
		JLabel label_3 = new JLabel("4: ");
		panel_site.add(label_3, "6, 43");
		
		spinner_swc_transpiration_veryDeep = new JSpinner();
		panel_site.add(spinner_swc_transpiration_veryDeep, "7, 43, fill, default");
		
		checkBox_swc_transpiration_use_veryDeep = new JCheckBox("");
		panel_site.add(checkBox_swc_transpiration_use_veryDeep, "8, 43");
		
		JLabel lblRange = new JLabel("Range:");
		lblRange.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_site.add(lblRange, "1, 43, right, default");
		
		formattedTextField_swc_evaporation_range = new JFormattedTextField(format);
		formattedTextField_swc_evaporation_range.setColumns(10);
		panel_site.add(formattedTextField_swc_evaporation_range, "2, 43, left, default");
		
		return panel_site;
	}
}

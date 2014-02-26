package explorer;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.BoxLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

import java.awt.Font;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import soilwat.InputData;

public class SWExplorer {

	private soilwat.InputData inputData;
	
	private FILES files;
	private MODEL model;
	private SITE site;
	private SOILS soils;
	private PROD prod;
	private ESTABL estab;
	private CLOUD cloud;
	private WEATHER weather;
	private SWC swc;
	
	private JFrame frame;
	
	private JTextField textField_output_prefixTEMP;
	private JTextField textField_output_prefixPRECIP;
	private JTextField textField_output_prefixSOILINFILT;
	private JTextField textField_output_prefixRUNOFF;
	private JTextField textField_output_prefixVWCBULK;
	private JTextField textField_output_prefixVWCMATRIC;
	private JTextField textField_output_prefixSWCBULK;
	private JTextField textField_output_prefixSWABULK;
	private JTextField textField_output_prefixSWAMATRIC;
	private JTextField textField_output_prefixSWPMATRIC;
	private JTextField textField_output_prefixSURFACEWATER;
	private JTextField textField_output_prefixTRANSP;
	private JTextField textField_output_prefixEVAPSOIL;
	private JTextField textField_output_prefixEVAPSURFACE;
	private JTextField textField_output_prefixINTERCEPTION;
	private JTextField textField_output_prefixLYRDRAIN;
	private JTextField textField_output_prefixHYDRED;
	private JTextField textField_output_prefixAET;
	private JTextField textField_output_prefixPET;
	private JTextField textField_output_prefixWETDAY;
	private JTextField textField_output_prefixSNOWPACK;
	private JTextField textField_output_prefixDEEPSWC;
	private JTextField textField_output_prefixSOILTEMP;
	private JTextField textField_output_prefixESTABL;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SWExplorer window = new SWExplorer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SWExplorer() {
		inputData = new InputData();
		inputData.onSetDefaults();
		files = new FILES(inputData.filesIn);
		model = new MODEL(inputData.yearsIn);
		site = new SITE(inputData.siteIn);
		soils = new SOILS(inputData.soilsIn);
		prod = new PROD(inputData.prodIn);
		estab = new ESTABL(inputData.estabIn);
		cloud = new CLOUD(inputData.cloudIn);
		weather = new WEATHER(inputData.weatherSetupIn, inputData.weatherHist);
		swc = new SWC(inputData.swcSetupIn, inputData.swcHist);
		initialize();
		prod.onSetValues();
		soils.onSetValues();
		site.onSetValues();
		model.onSetValues();
		files.onSetValues();
		estab.onSetValues();
		cloud.onSetValues();
		weather.onSetValues();
		swc.onSetValues();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Format format = NumberFormat.getIntegerInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1093, 762);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenuItem mntmFiles = new JMenuItem("Files");
		menuBar.add(mntmFiles);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane);
		
		tabbedPane.addTab("Files", null, files.onGetPanel_files(), null);
		tabbedPane.addTab("Model Timing", null, model.onGetPanel_model(), null);
		tabbedPane.addTab("Site Params", null, site.onGetPanel_site(), null);
		tabbedPane.addTab("Soil Layers", null, soils.onGetPanel_soils(), null);
		tabbedPane.addTab("Plant Production", null, prod.onGetPanel_prod(), null);
		tabbedPane.addTab("Establishment", null, estab.onGetPanel_establ(), null);
		tabbedPane.addTab("Cloud", null, cloud.onGetPanel_cloud(), null);
		tabbedPane.addTab("Weather Setup", null, weather.onGetPanel_weather(), null);
		tabbedPane.addTab("SWC Setup", null, swc.onGetPanel_swc(), null);
		
		JPanel panel_output = new JPanel();
		tabbedPane.addTab("Output Setup", null, panel_output, null);
		panel_output.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lbl_outputSetup_Settings = new JLabel("Output Setup Settings for SOILWAT");
		panel_output.add(lbl_outputSetup_Settings, "1, 1, 15, 1");
		
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"Tab", "Space"}));
		panel_output.add(comboBox_1, "1, 3, left, default");
		
		JLabel lbl_output_sep = new JLabel("Output File Separator");
		lbl_output_sep.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lbl_output_sep, "3, 3, 13, 1");
		
		JLabel lbl_output_timestep = new JLabel("Time Periods for Each Key");
		panel_output.add(lbl_output_timestep, "1, 5, 8, 1");
		
		JCheckBox chckbx_output_dy = new JCheckBox("Day");
		chckbx_output_dy.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(chckbx_output_dy, "1, 7, left, default");
		
		JCheckBox chckbxWeek = new JCheckBox("Week");
		chckbxWeek.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(chckbxWeek, "3, 7");
		
		JCheckBox chckbxMonth = new JCheckBox("Month");
		chckbxMonth.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(chckbxMonth, "5, 7");
		
		JCheckBox chckbxYear = new JCheckBox("Year");
		chckbxYear.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(chckbxYear, "7, 7");
		
		JLabel lblUse = new JLabel("Use");
		panel_output.add(lblUse, "1, 9, center, default");
		
		JLabel lblKey = new JLabel("Key");
		panel_output.add(lblKey, "3, 9, center, default");
		
		JLabel lblSumType = new JLabel("Sum Type");
		panel_output.add(lblSumType, "5, 9, center, default");
		
		JLabel lblPeriod = new JLabel("Period");
		panel_output.add(lblPeriod, "7, 9, center, default");
		
		JLabel lblStart = new JLabel("Start");
		panel_output.add(lblStart, "9, 9, center, default");
		
		JLabel lblEnd = new JLabel(" End ");
		panel_output.add(lblEnd, "11, 9");
		
		JLabel lblFilenamePrefix = new JLabel("Filename Prefix");
		panel_output.add(lblFilenamePrefix, "13, 9, center, default");
		
		JCheckBox checkBox_output_useTEMP = new JCheckBox("");
		panel_output.add(checkBox_output_useTEMP, "1, 11, center, default");
		
		JLabel lblTemp = new JLabel("TEMP");
		lblTemp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblTemp, "3, 11, left, default");
		
		String[] sumTypes = new String[] {"SUM", "AVG", "FIN", "OFF"};
		//DefaultComboBoxModel<String> sumModel = new DefaultComboBoxModel<String>(sumTypes);
		
		String[] periodTypes = new String[] {"SUM", "AVG", "FIN", "OFF"};
		//DefaultComboBoxModel<String> periodModel = new DefaultComboBoxModel<String>(periodTypes);
		
		JComboBox<String> comboBox_output_sumTEMP = new JComboBox<String>();
		comboBox_output_sumTEMP.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_sumTEMP.setModel(new DefaultComboBoxModel<String>(sumTypes));
		panel_output.add(comboBox_output_sumTEMP, "5, 11, left, default");
		
		JComboBox<String> comboBox_output_periodTEMP = new JComboBox<String>();
		comboBox_output_periodTEMP.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodTEMP.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodTEMP, "7, 11, left, default");
		
		JFormattedTextField formattedTextField_output_startTEMP = new JFormattedTextField(format);
		formattedTextField_output_startTEMP.setColumns(3);
		formattedTextField_output_startTEMP.setValue(new Integer(1));
		panel_output.add(formattedTextField_output_startTEMP, "9, 11, left, default");
		
		JFormattedTextField formattedTextField_output_endTEMP = new JFormattedTextField(format);
		formattedTextField_output_endTEMP.setValue(new Integer(366));
		formattedTextField_output_endTEMP.setColumns(3);
		panel_output.add(formattedTextField_output_endTEMP, "11, 11, left, default");
		
		textField_output_prefixTEMP = new JTextField();
		panel_output.add(textField_output_prefixTEMP, "13, 11, left, default");
		textField_output_prefixTEMP.setColumns(10);
		
		JLabel lbl_output_discTEMP = new JLabel("/* max., min, average temperature (C) */");
		lbl_output_discTEMP.setAutoscrolls(true);
		panel_output.add(lbl_output_discTEMP, "15, 11");
		
		JCheckBox checkBox_output_usePRECIP = new JCheckBox("");
		panel_output.add(checkBox_output_usePRECIP, "1, 12, center, default");
		
		JLabel lblPrecip = new JLabel("PRECIP");
		lblPrecip.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblPrecip, "3, 12, left, default");
		
		JComboBox<String> comboBox_output_sumPRECIP = new JComboBox<String>();
		comboBox_output_sumPRECIP.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumPRECIP.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumPRECIP, "5, 12, left, default");
		
		JComboBox<String> comboBox_output_periodPRECIP = new JComboBox<String>();
		comboBox_output_periodPRECIP.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodPRECIP.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodPRECIP, "7, 12, left, default");
		
		JFormattedTextField formattedTextField_output_startPRECIP = new JFormattedTextField(format);
		formattedTextField_output_startPRECIP.setValue(new Integer(1));
		formattedTextField_output_startPRECIP.setColumns(3);
		panel_output.add(formattedTextField_output_startPRECIP, "9, 12, left, default");
		
		JFormattedTextField formattedTextField_output_endPRECIP = new JFormattedTextField((Format) null);
		formattedTextField_output_endPRECIP.setValue(new Integer(366));
		formattedTextField_output_endPRECIP.setColumns(3);
		panel_output.add(formattedTextField_output_endPRECIP, "11, 12, left, default");
		
		textField_output_prefixPRECIP = new JTextField();
		textField_output_prefixPRECIP.setColumns(10);
		panel_output.add(textField_output_prefixPRECIP, "13, 12, left, default");
		
		JLabel lbl_output_discPRECIP = new JLabel("/* total precip = sum(rain, snow), rain, snow-fall, snowmelt, and snowloss (cm) */");
		lbl_output_discPRECIP.setAutoscrolls(true);
		panel_output.add(lbl_output_discPRECIP, "15, 12");
		
		JCheckBox checkBox_output_useSOILINFILT = new JCheckBox("");
		panel_output.add(checkBox_output_useSOILINFILT, "1, 13, center, default");
		
		JLabel lblSoilinfilt = new JLabel("SOILINFILT");
		lblSoilinfilt.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSoilinfilt, "3, 13, left, default");
		
		JComboBox<String> comboBox_output_sumSOILINFILT = new JComboBox<String>();
		comboBox_output_sumSOILINFILT.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSOILINFILT.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSOILINFILT, "5, 13, left, default");
		
		JComboBox<String> comboBox_output_periodSOILINFILT = new JComboBox<String>();
		comboBox_output_periodSOILINFILT.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSOILINFILT.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSOILINFILT, "7, 13, left, default");
		
		JFormattedTextField formattedTextField_output_startSOILINFILT = new JFormattedTextField(format);
		formattedTextField_output_startSOILINFILT.setValue(new Integer(1));
		formattedTextField_output_startSOILINFILT.setColumns(3);
		panel_output.add(formattedTextField_output_startSOILINFILT, "9, 13, left, default");
		
		JFormattedTextField formattedTextField_output_endSOILINFILT = new JFormattedTextField((Format) null);
		formattedTextField_output_endSOILINFILT.setValue(new Integer(366));
		formattedTextField_output_endSOILINFILT.setColumns(3);
		panel_output.add(formattedTextField_output_endSOILINFILT, "11, 13, left, default");
		
		textField_output_prefixSOILINFILT = new JTextField();
		textField_output_prefixSOILINFILT.setColumns(10);
		panel_output.add(textField_output_prefixSOILINFILT, "13, 13, left, default");
		
		JLabel lbl_output_discINFILT = new JLabel("/* water to infiltrate in top soil layer (cm), runoff (cm); (not-intercepted rain)+(snowmelt-runoff) */");
		lbl_output_discINFILT.setAutoscrolls(true);
		panel_output.add(lbl_output_discINFILT, "15, 13");
		
		JCheckBox checkBox_output_useRUNOFF = new JCheckBox("");
		panel_output.add(checkBox_output_useRUNOFF, "1, 14, center, default");
		
		JLabel lblRunoff = new JLabel("RUNOFF");
		lblRunoff.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblRunoff, "3, 14, left, default");
		
		JComboBox<String> comboBox_output_sumRUNOFF = new JComboBox<String>();
		comboBox_output_sumRUNOFF.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumRUNOFF.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumRUNOFF, "5, 14, left, default");
		
		JComboBox<String> comboBox_output_periodRUNOFF = new JComboBox<String>();
		comboBox_output_periodRUNOFF.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodRUNOFF.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodRUNOFF, "7, 14, left, default");
		
		JFormattedTextField formattedTextField_output_startRUNOFF = new JFormattedTextField(format);
		formattedTextField_output_startRUNOFF.setValue(new Integer(1));
		formattedTextField_output_startRUNOFF.setColumns(3);
		panel_output.add(formattedTextField_output_startRUNOFF, "9, 14, left, default");
		
		JFormattedTextField formattedTextField_output_endRUNOFF = new JFormattedTextField((Format) null);
		formattedTextField_output_endRUNOFF.setValue(new Integer(366));
		formattedTextField_output_endRUNOFF.setColumns(3);
		panel_output.add(formattedTextField_output_endRUNOFF, "11, 14, left, default");
		
		textField_output_prefixRUNOFF = new JTextField();
		textField_output_prefixRUNOFF.setColumns(10);
		panel_output.add(textField_output_prefixRUNOFF, "13, 14, left, default");
		
		JLabel lbl_output_discRUNOFF = new JLabel("/* runoff (cm): total runoff, runoff from ponded water, runoff from snowmelt */");
		lbl_output_discRUNOFF.setAutoscrolls(true);
		panel_output.add(lbl_output_discRUNOFF, "15, 14");
		
		JCheckBox checkBox_output_useVWCBULK = new JCheckBox("");
		panel_output.add(checkBox_output_useVWCBULK, "1, 15, center, default");
		
		JLabel lblVwcbulk = new JLabel("VWCBULK");
		lblVwcbulk.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblVwcbulk, "3, 15, left, default");
		
		JComboBox<String> comboBox_output_sumVWCBULK = new JComboBox<String>();
		comboBox_output_sumVWCBULK.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumVWCBULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumVWCBULK, "5, 15, left, default");
		
		JComboBox<String> comboBox_output_periodVWCBULK = new JComboBox<String>();
		comboBox_output_periodVWCBULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodVWCBULK.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodVWCBULK, "7, 15, left, default");
		
		JFormattedTextField formattedTextField_output_startVWCBULK = new JFormattedTextField(format);
		formattedTextField_output_startVWCBULK.setValue(new Integer(1));
		formattedTextField_output_startVWCBULK.setColumns(3);
		panel_output.add(formattedTextField_output_startVWCBULK, "9, 15, left, default");
		
		JFormattedTextField formattedTextField_output_endVWCBULK = new JFormattedTextField((Format) null);
		formattedTextField_output_endVWCBULK.setValue(new Integer(366));
		formattedTextField_output_endVWCBULK.setColumns(3);
		panel_output.add(formattedTextField_output_endVWCBULK, "11, 15, left, default");
		
		textField_output_prefixVWCBULK = new JTextField();
		textField_output_prefixVWCBULK.setColumns(10);
		panel_output.add(textField_output_prefixVWCBULK, "13, 15, left, default");
		
		JLabel lbl_output_discVWCBULK = new JLabel("/* bulk volumetric soilwater (cm / layer) */");
		lbl_output_discVWCBULK.setAutoscrolls(true);
		panel_output.add(lbl_output_discVWCBULK, "15, 15");
		
		JCheckBox checkBox_output_useVWCMATRIC = new JCheckBox("");
		panel_output.add(checkBox_output_useVWCMATRIC, "1, 16, center, default");
		
		JLabel lblVwcmatric = new JLabel("VWCMATRIC");
		lblVwcmatric.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblVwcmatric, "3, 16, left, default");
		
		JComboBox<String> comboBox_output_sumVWCMATRIC = new JComboBox<String>();
		comboBox_output_sumVWCMATRIC.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumVWCMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumVWCMATRIC, "5, 16, left, default");
		
		JComboBox<String> comboBox_output_periodVWCMATRIC = new JComboBox<String>();
		comboBox_output_periodVWCMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodVWCMATRIC.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodVWCMATRIC, "7, 16, left, default");
		
		JFormattedTextField formattedTextField_output_startVWCMATRIC = new JFormattedTextField(format);
		formattedTextField_output_startVWCMATRIC.setValue(new Integer(1));
		formattedTextField_output_startVWCMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_startVWCMATRIC, "9, 16, left, default");
		
		JFormattedTextField formattedTextField_output_endVWCMATRIC = new JFormattedTextField((Format) null);
		formattedTextField_output_endVWCMATRIC.setValue(new Integer(366));
		formattedTextField_output_endVWCMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_endVWCMATRIC, "11, 16, left, default");
		
		textField_output_prefixVWCMATRIC = new JTextField();
		textField_output_prefixVWCMATRIC.setColumns(10);
		panel_output.add(textField_output_prefixVWCMATRIC, "13, 16, left, default");
		
		JLabel lbl_output_discVWCMATRIC = new JLabel("/* matric volumetric soilwater (cm / layer) */");
		lbl_output_discVWCMATRIC.setAutoscrolls(true);
		panel_output.add(lbl_output_discVWCMATRIC, "15, 16");
		
		JCheckBox checkBox_output_useSWCBULK = new JCheckBox("");
		panel_output.add(checkBox_output_useSWCBULK, "1, 17, center, default");
		
		JLabel lblSwcbulk = new JLabel("SWCBULK");
		lblSwcbulk.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSwcbulk, "3, 17, left, default");
		
		JComboBox<String> comboBox_output_sumSWCBULK = new JComboBox<String>();
		comboBox_output_sumSWCBULK.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSWCBULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSWCBULK, "5, 17, left, default");
		
		JComboBox<String> comboBox_output_periodSWCBULK = new JComboBox<String>();
		comboBox_output_periodSWCBULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSWCBULK.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSWCBULK, "7, 17, left, default");
		
		JFormattedTextField formattedTextField_output_startSWCBULK = new JFormattedTextField(format);
		formattedTextField_output_startSWCBULK.setValue(new Integer(1));
		formattedTextField_output_startSWCBULK.setColumns(3);
		panel_output.add(formattedTextField_output_startSWCBULK, "9, 17, left, default");
		
		JFormattedTextField formattedTextField_output_endSWCBULK = new JFormattedTextField((Format) null);
		formattedTextField_output_endSWCBULK.setValue(new Integer(366));
		formattedTextField_output_endSWCBULK.setColumns(3);
		panel_output.add(formattedTextField_output_endSWCBULK, "11, 17, left, default");
		
		textField_output_prefixSWCBULK = new JTextField();
		textField_output_prefixSWCBULK.setColumns(10);
		panel_output.add(textField_output_prefixSWCBULK, "13, 17, left, default");
		
		JLabel lbl_output_discSWCBULK = new JLabel("/* bulk soilwater content (cm / cm layer); swc.l1(today) = swc.l1(yesterday)+inf_soil-lyrdrain.l1-transp.l1-evap_soil.l1; swc.li(today) = swc.li(yesterday)+lyrdrain.l(i-1)-lyrdrain.li-transp.li-evap_soil.li; swc.llast(today) = swc.llast(yesterday)+lyrdrain.l(last-1)-deepswc-transp.llast-evap_soil.llast  */");
		lbl_output_discSWCBULK.setAutoscrolls(true);
		panel_output.add(lbl_output_discSWCBULK, "15, 17");
		
		JCheckBox checkBox_output_useSWABULK = new JCheckBox("");
		panel_output.add(checkBox_output_useSWABULK, "1, 18, center, default");
		
		JLabel lblSwabulk = new JLabel("SWABULK");
		lblSwabulk.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSwabulk, "3, 18, left, default");
		
		JComboBox<String> comboBox_output_sumSWABULK = new JComboBox<String>();
		comboBox_output_sumSWABULK.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSWABULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSWABULK, "5, 18, left, default");
		
		JComboBox<String> comboBox_output_periodSWABULK = new JComboBox<String>();
		comboBox_output_periodSWABULK.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSWABULK.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSWABULK, "7, 18, left, default");
		
		JFormattedTextField formattedTextField_output_startSWABULK = new JFormattedTextField(format);
		formattedTextField_output_startSWABULK.setValue(new Integer(1));
		formattedTextField_output_startSWABULK.setColumns(3);
		panel_output.add(formattedTextField_output_startSWABULK, "9, 18, left, default");
		
		JFormattedTextField formattedTextField_output_endSWABULK = new JFormattedTextField((Format) null);
		formattedTextField_output_endSWABULK.setValue(new Integer(366));
		formattedTextField_output_endSWABULK.setColumns(3);
		panel_output.add(formattedTextField_output_endSWABULK, "11, 18, left, default");
		
		textField_output_prefixSWABULK = new JTextField();
		textField_output_prefixSWABULK.setColumns(10);
		panel_output.add(textField_output_prefixSWABULK, "13, 18, left, default");
		
		JLabel lbl_output_discSWABULK = new JLabel("/* bulk available soil water (cm/layer) = swc - wilting point */");
		lbl_output_discSWABULK.setAutoscrolls(true);
		panel_output.add(lbl_output_discSWABULK, "15, 18");
		
		JCheckBox checkBox_output_useSWAMATRIC = new JCheckBox("");
		panel_output.add(checkBox_output_useSWAMATRIC, "1, 19, center, default");
		
		JLabel lblSwamatric = new JLabel("SWAMATRIC");
		lblSwamatric.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSwamatric, "3, 19, left, default");
		
		JComboBox<String> comboBox_output_sumSWAMATRIC = new JComboBox<String>();
		comboBox_output_sumSWAMATRIC.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSWAMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSWAMATRIC, "5, 19, left, default");
		
		JComboBox<String> comboBox_output_periodSWAMATRIC= new JComboBox<String>();
		comboBox_output_periodSWAMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSWAMATRIC.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSWAMATRIC, "7, 19, left, default");
		
		JFormattedTextField formattedTextField_output_startSWAMATRIC = new JFormattedTextField(format);
		formattedTextField_output_startSWAMATRIC.setValue(new Integer(1));
		formattedTextField_output_startSWAMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_startSWAMATRIC, "9, 19, left, default");
		
		JFormattedTextField formattedTextField_output_endSWAMATRIC = new JFormattedTextField((Format) null);
		formattedTextField_output_endSWAMATRIC.setValue(new Integer(366));
		formattedTextField_output_endSWAMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_endSWAMATRIC, "11, 19, left, default");
		
		textField_output_prefixSWAMATRIC = new JTextField();
		textField_output_prefixSWAMATRIC.setColumns(10);
		panel_output.add(textField_output_prefixSWAMATRIC, "13, 19, left, default");
		
		JLabel label_output_discSWAMATRIC = new JLabel("/* matric available soil water (cm/layer) = swc - wilting point */");
		label_output_discSWAMATRIC.setAutoscrolls(true);
		panel_output.add(label_output_discSWAMATRIC, "15, 19");
		
		JCheckBox checkBox_output_useSWPMATRIC = new JCheckBox("");
		panel_output.add(checkBox_output_useSWPMATRIC, "1, 20, center, default");
		
		JLabel lblSwpmatric = new JLabel("SWPMATRIC");
		lblSwpmatric.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSwpmatric, "3, 20, left, default");
		
		JComboBox<String> comboBox_output_sumSWPMATRIC = new JComboBox<String>();
		comboBox_output_sumSWPMATRIC.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSWPMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSWPMATRIC, "5, 20, left, default");
		
		JComboBox<String> comboBox_output_periodSWPMATRIC = new JComboBox<String>();
		comboBox_output_periodSWPMATRIC.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSWPMATRIC.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSWPMATRIC, "7, 20, left, default");
		
		JFormattedTextField formattedTextField_output_startSWPMATRIC = new JFormattedTextField(format);
		formattedTextField_output_startSWPMATRIC.setValue(new Integer(1));
		formattedTextField_output_startSWPMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_startSWPMATRIC, "9, 20, left, default");
		
		JFormattedTextField formattedTextField_output_endSWPMATRIC = new JFormattedTextField((Format) null);
		formattedTextField_output_endSWPMATRIC.setValue(new Integer(366));
		formattedTextField_output_endSWPMATRIC.setColumns(3);
		panel_output.add(formattedTextField_output_endSWPMATRIC, "11, 20, left, default");
		
		textField_output_prefixSWPMATRIC = new JTextField();
		textField_output_prefixSWPMATRIC.setColumns(10);
		panel_output.add(textField_output_prefixSWPMATRIC, "13, 20, left, default");
		
		JLabel label_output_discSWPMATRIC = new JLabel("/* matric soilwater potential (-bars) */");
		label_output_discSWPMATRIC.setAutoscrolls(true);
		panel_output.add(label_output_discSWPMATRIC, "15, 20");
		
		JCheckBox checkBox_output_useSURFACEWATER = new JCheckBox("");
		panel_output.add(checkBox_output_useSURFACEWATER, "1, 21, center, default");
		
		JLabel lblSurfacewater = new JLabel("SURFACEWATER");
		lblSurfacewater.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSurfacewater, "3, 21, left, default");
		
		JComboBox<String> comboBox_output_sumSURFACEWATER = new JComboBox<String>();
		comboBox_output_sumSURFACEWATER.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSURFACEWATER.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSURFACEWATER, "5, 21, left, default");
		
		JComboBox<String> comboBox_output_periodSURFACEWATER = new JComboBox<String>();
		comboBox_output_periodSURFACEWATER.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSURFACEWATER.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSURFACEWATER, "7, 21, left, default");
		
		JFormattedTextField formattedTextField_output_startSURFACEWATER = new JFormattedTextField(format);
		formattedTextField_output_startSURFACEWATER.setValue(new Integer(1));
		formattedTextField_output_startSURFACEWATER.setColumns(3);
		panel_output.add(formattedTextField_output_startSURFACEWATER, "9, 21, left, default");
		
		JFormattedTextField formattedTextField_output_endSURFACEWATER = new JFormattedTextField((Format) null);
		formattedTextField_output_endSURFACEWATER.setValue(new Integer(366));
		formattedTextField_output_endSURFACEWATER.setColumns(3);
		panel_output.add(formattedTextField_output_endSURFACEWATER, "11, 21, left, default");
		
		textField_output_prefixSURFACEWATER = new JTextField();
		textField_output_prefixSURFACEWATER.setColumns(10);
		panel_output.add(textField_output_prefixSURFACEWATER, "13, 21, left, default");
		
		JLabel label_output_discSURFACEWATER = new JLabel("/* surface water (cm) */");
		label_output_discSURFACEWATER.setAutoscrolls(true);
		panel_output.add(label_output_discSURFACEWATER, "15, 21");
		
		JCheckBox checkBox_output_useTRANSP = new JCheckBox("");
		panel_output.add(checkBox_output_useTRANSP, "1, 22, center, default");
		
		JLabel lblTransp = new JLabel("TRANSP");
		lblTransp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblTransp, "3, 22, left, default");
		
		JComboBox<String> comboBox_output_sumTRANSP = new JComboBox<String>();
		comboBox_output_sumTRANSP.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumTRANSP.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumTRANSP, "5, 22, left, default");
		
		JComboBox<String> comboBox_output_periodTRANSP = new JComboBox<String>();
		comboBox_output_periodTRANSP.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodTRANSP.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodTRANSP, "7, 22, left, default");
		
		JFormattedTextField formattedTextField_output_startTRANSP = new JFormattedTextField(format);
		formattedTextField_output_startTRANSP.setValue(new Integer(1));
		formattedTextField_output_startTRANSP.setColumns(3);
		panel_output.add(formattedTextField_output_startTRANSP, "9, 22, left, default");
		
		JFormattedTextField formattedTextField_output_endTRANSP = new JFormattedTextField((Format) null);
		formattedTextField_output_endTRANSP.setValue(new Integer(366));
		formattedTextField_output_endTRANSP.setColumns(3);
		panel_output.add(formattedTextField_output_endTRANSP, "11, 22, left, default");
		
		textField_output_prefixTRANSP = new JTextField();
		textField_output_prefixTRANSP.setColumns(10);
		panel_output.add(textField_output_prefixTRANSP, "13, 22, left, default");
		
		JLabel label_output_discTRANSP = new JLabel("/* transpiration from each soil layer (cm): total, trees, shrubs, forbs, grasses */");
		label_output_discTRANSP.setAutoscrolls(true);
		panel_output.add(label_output_discTRANSP, "15, 22");
		
		JCheckBox checkBox_output_useEVAPSOIL = new JCheckBox("");
		panel_output.add(checkBox_output_useEVAPSOIL, "1, 23, center, default");
		
		JLabel lblEvapsoil = new JLabel("EVAPSOIL");
		lblEvapsoil.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblEvapsoil, "3, 23, left, default");
		
		JComboBox<String> comboBox_output_sumEVAPSOIL = new JComboBox<String>();
		comboBox_output_sumEVAPSOIL.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumEVAPSOIL.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumEVAPSOIL, "5, 23, left, default");
		
		JComboBox<String> comboBox_output_periodEVAPSOIL= new JComboBox<String>();
		comboBox_output_periodEVAPSOIL.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodEVAPSOIL.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodEVAPSOIL, "7, 23, left, default");
		
		JFormattedTextField formattedTextField_output_startEVAPSOIL = new JFormattedTextField(format);
		formattedTextField_output_startEVAPSOIL.setValue(new Integer(1));
		formattedTextField_output_startEVAPSOIL.setColumns(3);
		panel_output.add(formattedTextField_output_startEVAPSOIL, "9, 23, left, default");
		
		JFormattedTextField formattedTextField_output_endEVAPSOIL = new JFormattedTextField((Format) null);
		formattedTextField_output_endEVAPSOIL.setValue(new Integer(366));
		formattedTextField_output_endEVAPSOIL.setColumns(3);
		panel_output.add(formattedTextField_output_endEVAPSOIL, "11, 23, left, default");
		
		textField_output_prefixEVAPSOIL = new JTextField();
		textField_output_prefixEVAPSOIL.setColumns(10);
		panel_output.add(textField_output_prefixEVAPSOIL, "13, 23, left, default");
		
		JLabel label_output_discEVAPSOIL = new JLabel("/* bare-soil evaporation from each soil layer (cm) */");
		label_output_discEVAPSOIL.setAutoscrolls(true);
		panel_output.add(label_output_discEVAPSOIL, "15, 23");
		
		JCheckBox checkBox_output_useEVAPSURFACE = new JCheckBox("");
		panel_output.add(checkBox_output_useEVAPSURFACE, "1, 24, center, default");
		
		JLabel lblEvapsurface = new JLabel("EVAPSURFACE");
		lblEvapsurface.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblEvapsurface, "3, 24, left, default");
		
		JComboBox<String> comboBox_output_sumEVAPSURFACE = new JComboBox<String>();
		comboBox_output_sumEVAPSURFACE.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumEVAPSURFACE.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumEVAPSURFACE, "5, 24, left, default");
		
		JComboBox<String> comboBox_output_periodEVAPSURFACE = new JComboBox<String>();
		comboBox_output_periodEVAPSURFACE.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodEVAPSURFACE.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodEVAPSURFACE, "7, 24, left, default");
		
		JFormattedTextField formattedTextField_output_startEVAPSURFACE = new JFormattedTextField(format);
		formattedTextField_output_startEVAPSURFACE.setValue(new Integer(1));
		formattedTextField_output_startEVAPSURFACE.setColumns(3);
		panel_output.add(formattedTextField_output_startEVAPSURFACE, "9, 24, left, default");
		
		JFormattedTextField formattedTextField_output_endEVAPSURFACE = new JFormattedTextField((Format) null);
		formattedTextField_output_endEVAPSURFACE.setValue(new Integer(366));
		formattedTextField_output_endEVAPSURFACE.setColumns(3);
		panel_output.add(formattedTextField_output_endEVAPSURFACE, "11, 24, left, default");
		
		textField_output_prefixEVAPSURFACE = new JTextField();
		textField_output_prefixEVAPSURFACE.setColumns(10);
		panel_output.add(textField_output_prefixEVAPSURFACE, "13, 24, left, default");
		
		JLabel label_output_discEVAPSURFACE = new JLabel("/* evaporation (cm): total, trees, shrubs, forbs, grasses, litter, surface water */");
		label_output_discEVAPSURFACE.setAutoscrolls(true);
		panel_output.add(label_output_discEVAPSURFACE, "15, 24");
		
		JCheckBox checkBox_output_useINTERCEPTION = new JCheckBox("");
		panel_output.add(checkBox_output_useINTERCEPTION, "1, 25, center, default");
		
		JLabel lblInterception = new JLabel("INTERCEPTION");
		lblInterception.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblInterception, "3, 25, left, default");
		
		JComboBox<String> comboBox_output_sumINTERCEPTION = new JComboBox<String>();
		comboBox_output_sumINTERCEPTION.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumINTERCEPTION.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumINTERCEPTION, "5, 25, left, default");
		
		JComboBox<String> comboBox_output_periodINTERCEPTION = new JComboBox<String>();
		comboBox_output_periodINTERCEPTION.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodINTERCEPTION.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodINTERCEPTION, "7, 25, left, default");
		
		JFormattedTextField formattedTextField_output_startINTERCEPTION = new JFormattedTextField(format);
		formattedTextField_output_startINTERCEPTION.setValue(new Integer(1));
		formattedTextField_output_startINTERCEPTION.setColumns(3);
		panel_output.add(formattedTextField_output_startINTERCEPTION, "9, 25, left, default");
		
		JFormattedTextField formattedTextField_output_endINTERCEPTION = new JFormattedTextField((Format) null);
		formattedTextField_output_endINTERCEPTION.setValue(new Integer(366));
		formattedTextField_output_endINTERCEPTION.setColumns(3);
		panel_output.add(formattedTextField_output_endINTERCEPTION, "11, 25, left, default");
		
		textField_output_prefixINTERCEPTION = new JTextField();
		textField_output_prefixINTERCEPTION.setColumns(10);
		panel_output.add(textField_output_prefixINTERCEPTION, "13, 25, left, default");
		
		JLabel label_output_discINTERCEPTION = new JLabel("/* intercepted rain (cm): total, trees, shrubs, forbs, grasses, and litter (cm) */");
		label_output_discINTERCEPTION.setAutoscrolls(true);
		panel_output.add(label_output_discINTERCEPTION, "15, 25");
		
		JCheckBox checkBox_output_useLYRDRAIN = new JCheckBox("");
		panel_output.add(checkBox_output_useLYRDRAIN, "1, 26, center, default");
		
		JLabel lblLyrdrain = new JLabel("LYRDRAIN");
		lblLyrdrain.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblLyrdrain, "3, 26, left, default");
		
		JComboBox<String> comboBox_output_sumLYRDRAIN = new JComboBox<String>();
		comboBox_output_sumLYRDRAIN.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumLYRDRAIN.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumLYRDRAIN, "5, 26, left, default");
		
		JComboBox<String> comboBox_output_periodLYRDRAIN = new JComboBox<String>();
		comboBox_output_periodLYRDRAIN.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodLYRDRAIN.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodLYRDRAIN, "7, 26, left, default");
		
		JFormattedTextField formattedTextField_output_startLYRDRAIN = new JFormattedTextField(format);
		formattedTextField_output_startLYRDRAIN.setValue(new Integer(1));
		formattedTextField_output_startLYRDRAIN.setColumns(3);
		panel_output.add(formattedTextField_output_startLYRDRAIN, "9, 26, left, default");
		
		JFormattedTextField formattedTextField_output_endLYRDRAIN = new JFormattedTextField((Format) null);
		formattedTextField_output_endLYRDRAIN.setValue(new Integer(366));
		formattedTextField_output_endLYRDRAIN.setColumns(3);
		panel_output.add(formattedTextField_output_endLYRDRAIN, "11, 26, left, default");
		
		textField_output_prefixLYRDRAIN = new JTextField();
		textField_output_prefixLYRDRAIN.setColumns(10);
		panel_output.add(textField_output_prefixLYRDRAIN, "13, 26, left, default");
		
		JLabel label_output_discLYRDRAIN = new JLabel("/* water percolated from each layer (cm) */");
		label_output_discLYRDRAIN.setAutoscrolls(true);
		panel_output.add(label_output_discLYRDRAIN, "15, 26");
		
		JCheckBox checkBox_output_useHYDRED = new JCheckBox("");
		panel_output.add(checkBox_output_useHYDRED, "1, 27, center, default");
		
		JLabel lblHydred = new JLabel("HYDRED");
		lblHydred.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblHydred, "3, 27, left, default");
		
		JComboBox<String> comboBox_output_sumHYDRED = new JComboBox<String>();
		comboBox_output_sumHYDRED.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumHYDRED.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumHYDRED, "5, 27, left, default");
		
		JComboBox<String> comboBox_output_periodHYDRED = new JComboBox<String>();
		comboBox_output_periodHYDRED.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodHYDRED.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodHYDRED, "7, 27, left, default");
		
		JFormattedTextField formattedTextField_output_startHYDRED = new JFormattedTextField(format);
		formattedTextField_output_startHYDRED.setValue(new Integer(1));
		formattedTextField_output_startHYDRED.setColumns(3);
		panel_output.add(formattedTextField_output_startHYDRED, "9, 27, left, default");
		
		JFormattedTextField formattedTextField_output_endHYDRED = new JFormattedTextField((Format) null);
		formattedTextField_output_endHYDRED.setValue(new Integer(366));
		formattedTextField_output_endHYDRED.setColumns(3);
		panel_output.add(formattedTextField_output_endHYDRED, "11, 27, left, default");
		
		textField_output_prefixHYDRED = new JTextField();
		textField_output_prefixHYDRED.setColumns(10);
		panel_output.add(textField_output_prefixHYDRED, "13, 27, left, default");
		
		JLabel label_output_discHYRED = new JLabel("/* hydraulic redistribution from each layer (cm): total, trees, shrubs, forbs, grasses */");
		label_output_discHYRED.setAutoscrolls(true);
		panel_output.add(label_output_discHYRED, "15, 27");
		
		JCheckBox checkBox_output_useAET = new JCheckBox("");
		panel_output.add(checkBox_output_useAET, "1, 28, center, default");
		
		JLabel lblAet = new JLabel("AET");
		lblAet.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblAet, "3, 28, left, default");
		
		JComboBox<String> comboBox_output_sumAET = new JComboBox<String>();
		comboBox_output_sumAET.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumAET.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumAET, "5, 28, left, default");
		
		JComboBox<String> comboBox_output_periodAET = new JComboBox<String>();
		comboBox_output_periodAET.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodAET.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodAET, "7, 28, left, default");
		
		JFormattedTextField formattedTextField_output_startAET = new JFormattedTextField(format);
		formattedTextField_output_startAET.setValue(new Integer(1));
		formattedTextField_output_startAET.setColumns(3);
		panel_output.add(formattedTextField_output_startAET, "9, 28, left, default");
		
		JFormattedTextField formattedTextField_output_endAET = new JFormattedTextField((Format) null);
		formattedTextField_output_endAET.setValue(new Integer(366));
		formattedTextField_output_endAET.setColumns(3);
		panel_output.add(formattedTextField_output_endAET, "11, 28, left, default");
		
		textField_output_prefixAET = new JTextField();
		textField_output_prefixAET.setColumns(10);
		panel_output.add(textField_output_prefixAET, "13, 28, left, default");
		
		JLabel label_output_discAET = new JLabel("/* actual evapotr. (cm) */");
		label_output_discAET.setAutoscrolls(true);
		panel_output.add(label_output_discAET, "15, 28");
		
		JCheckBox checkBox_output_usePET = new JCheckBox("");
		panel_output.add(checkBox_output_usePET, "1, 29, center, default");
		
		JLabel lblPet = new JLabel("PET");
		lblPet.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblPet, "3, 29, left, default");
		
		JComboBox<String> comboBox_output_sumPET = new JComboBox<String>();
		comboBox_output_sumPET.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumPET.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumPET, "5, 29, left, default");
		
		JComboBox<String> comboBox_output_periodPET = new JComboBox<String>();
		comboBox_output_periodPET.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodPET.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodPET, "7, 29, left, default");
		
		JFormattedTextField formattedTextField_output_startPET = new JFormattedTextField(format);
		formattedTextField_output_startPET.setValue(new Integer(1));
		formattedTextField_output_startPET.setColumns(3);
		panel_output.add(formattedTextField_output_startPET, "9, 29, left, default");
		
		JFormattedTextField formattedTextField_output_endPET = new JFormattedTextField((Format) null);
		formattedTextField_output_endPET.setValue(new Integer(366));
		formattedTextField_output_endPET.setColumns(3);
		panel_output.add(formattedTextField_output_endPET, "11, 29, left, default");
		
		textField_output_prefixPET = new JTextField();
		textField_output_prefixPET.setColumns(10);
		panel_output.add(textField_output_prefixPET, "13, 29, left, default");
		
		JLabel label_output_discPET = new JLabel("/* potential evaptr (cm) */");
		label_output_discPET.setAutoscrolls(true);
		panel_output.add(label_output_discPET, "15, 29");
		
		JCheckBox checkBox_output_useWETDAY = new JCheckBox("");
		panel_output.add(checkBox_output_useWETDAY, "1, 30, center, default");
		
		JLabel lblWetday = new JLabel("WETDAY");
		lblWetday.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblWetday, "3, 30, left, default");
		
		JComboBox<String> comboBox_output_sumWETDAY = new JComboBox<String>();
		comboBox_output_sumWETDAY.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumWETDAY.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumWETDAY, "5, 30, left, default");
		
		JComboBox<String> comboBox_output_periodWETDAY = new JComboBox<String>();
		comboBox_output_periodWETDAY.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodWETDAY.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodWETDAY, "7, 30, left, default");
		
		JFormattedTextField formattedTextField_output_startWETDAY = new JFormattedTextField(format);
		formattedTextField_output_startWETDAY.setValue(new Integer(1));
		formattedTextField_output_startWETDAY.setColumns(3);
		panel_output.add(formattedTextField_output_startWETDAY, "9, 30, left, default");
		
		JFormattedTextField formattedTextField_output_endWETDAY = new JFormattedTextField((Format) null);
		formattedTextField_output_endWETDAY.setValue(new Integer(366));
		formattedTextField_output_endWETDAY.setColumns(3);
		panel_output.add(formattedTextField_output_endWETDAY, "11, 30, left, default");
		
		textField_output_prefixWETDAY = new JTextField();
		textField_output_prefixWETDAY.setColumns(10);
		panel_output.add(textField_output_prefixWETDAY, "13, 30, left, default");
		
		JLabel label_output_discWETDAY = new JLabel("/* days above swc_wet */");
		label_output_discWETDAY.setAutoscrolls(true);
		panel_output.add(label_output_discWETDAY, "15, 30");
		
		JCheckBox checkBox_output_useSNOWPACK = new JCheckBox("");
		panel_output.add(checkBox_output_useSNOWPACK, "1, 31, center, default");
		
		JLabel lblSnowpack = new JLabel("SNOWPACK");
		lblSnowpack.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSnowpack, "3, 31, left, default");
		
		JComboBox<String> comboBox_output_sumSNOWPACK = new JComboBox<String>();
		comboBox_output_sumSNOWPACK.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSNOWPACK.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSNOWPACK, "5, 31, left, default");
		
		JComboBox<String> comboBox_output_periodSNOWPACK = new JComboBox<String>();
		comboBox_output_periodSNOWPACK.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSNOWPACK.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSNOWPACK, "7, 31, left, default");
		
		JFormattedTextField formattedTextField_output_startSNOWPACK = new JFormattedTextField(format);
		formattedTextField_output_startSNOWPACK.setValue(new Integer(1));
		formattedTextField_output_startSNOWPACK.setColumns(3);
		panel_output.add(formattedTextField_output_startSNOWPACK, "9, 31, left, default");
		
		JFormattedTextField formattedTextField_output_endSNOWPACK = new JFormattedTextField((Format) null);
		formattedTextField_output_endSNOWPACK.setValue(new Integer(366));
		formattedTextField_output_endSNOWPACK.setColumns(3);
		panel_output.add(formattedTextField_output_endSNOWPACK, "11, 31, left, default");
		
		textField_output_prefixSNOWPACK = new JTextField();
		textField_output_prefixSNOWPACK.setColumns(10);
		panel_output.add(textField_output_prefixSNOWPACK, "13, 31, left, default");
		
		JLabel label_output_discSNOWPACK = new JLabel("/* snowpack water equivalent (cm), snowdepth (cm); since snowpack is already summed, use avg - sum sums the sums = nonsense */");
		label_output_discSNOWPACK.setAutoscrolls(true);
		panel_output.add(label_output_discSNOWPACK, "15, 31");
		
		JCheckBox checkBox_output_useDEEPSWC = new JCheckBox("");
		panel_output.add(checkBox_output_useDEEPSWC, "1, 32, center, default");
		
		JLabel lblDeepswc = new JLabel("DEEPSWC");
		lblDeepswc.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblDeepswc, "3, 32, left, default");
		
		JComboBox<String> comboBox_output_sumDEEPSWC = new JComboBox<String>();
		comboBox_output_sumDEEPSWC.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumDEEPSWC.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumDEEPSWC, "5, 32, left, default");
		
		JComboBox<String> comboBox_output_periodDEEPSWC = new JComboBox<String>();
		comboBox_output_periodDEEPSWC.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodDEEPSWC.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodDEEPSWC, "7, 32, left, default");
		
		JFormattedTextField formattedTextField_output_startDEEPSWC = new JFormattedTextField(format);
		formattedTextField_output_startDEEPSWC.setValue(new Integer(1));
		formattedTextField_output_startDEEPSWC.setColumns(3);
		panel_output.add(formattedTextField_output_startDEEPSWC, "9, 32, left, default");
		
		JFormattedTextField formattedTextField_output_endDEEPSWC = new JFormattedTextField((Format) null);
		formattedTextField_output_endDEEPSWC.setValue(new Integer(366));
		formattedTextField_output_endDEEPSWC.setColumns(3);
		panel_output.add(formattedTextField_output_endDEEPSWC, "11, 32, left, default");
		
		textField_output_prefixDEEPSWC = new JTextField();
		textField_output_prefixDEEPSWC.setColumns(10);
		panel_output.add(textField_output_prefixDEEPSWC, "13, 32, left, default");
		
		JLabel label_output_discDEEPSWC = new JLabel("/* deep drainage into lowest layer (cm) */");
		label_output_discDEEPSWC.setAutoscrolls(true);
		panel_output.add(label_output_discDEEPSWC, "15, 32");
		
		JCheckBox checkBox_output_useSOILTEMP = new JCheckBox("");
		panel_output.add(checkBox_output_useSOILTEMP, "1, 33, center, default");
		
		JLabel lblSoiltemp = new JLabel("SOILTEMP");
		lblSoiltemp.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblSoiltemp, "3, 33, left, default");
		
		JComboBox<String> comboBox_output_sumSOILTEMP = new JComboBox<String>();
		comboBox_output_sumSOILTEMP.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumSOILTEMP.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumSOILTEMP, "5, 33, left, default");
		
		JComboBox<String> comboBox_output_periodSOILTEMP = new JComboBox<String>();
		comboBox_output_periodSOILTEMP.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodSOILTEMP.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodSOILTEMP, "7, 33, left, default");
		
		JFormattedTextField formattedTextField_output_startSOILTEMP = new JFormattedTextField(format);
		formattedTextField_output_startSOILTEMP.setValue(new Integer(1));
		formattedTextField_output_startSOILTEMP.setColumns(3);
		panel_output.add(formattedTextField_output_startSOILTEMP, "9, 33, left, default");
		
		JFormattedTextField formattedTextField_output_endSOILTEMP = new JFormattedTextField((Format) null);
		formattedTextField_output_endSOILTEMP.setValue(new Integer(366));
		formattedTextField_output_endSOILTEMP.setColumns(3);
		panel_output.add(formattedTextField_output_endSOILTEMP, "11, 33, left, default");
		
		textField_output_prefixSOILTEMP = new JTextField();
		textField_output_prefixSOILTEMP.setColumns(10);
		panel_output.add(textField_output_prefixSOILTEMP, "13, 33, left, default");
		
		JLabel label_output_discSOILTEMP = new JLabel("/* soil temperature from each soil layer (in celsius) */");
		label_output_discSOILTEMP.setAutoscrolls(true);
		panel_output.add(label_output_discSOILTEMP, "15, 33");
		
		JCheckBox checkBox_output_useESTABL = new JCheckBox("");
		panel_output.add(checkBox_output_useESTABL, "1, 34, center, default");
		
		JLabel lblEstabl = new JLabel("ESTABL");
		lblEstabl.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(lblEstabl, "3, 34, left, default");
		
		JComboBox<String> comboBox_output_sumESTABL = new JComboBox<String>();
		comboBox_output_sumESTABL.setModel(new DefaultComboBoxModel<String>(sumTypes));
		comboBox_output_sumESTABL.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_output.add(comboBox_output_sumESTABL, "5, 34, left, default");
		
		JComboBox<String> comboBox_output_periodESTABL = new JComboBox<String>();
		comboBox_output_periodESTABL.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboBox_output_periodESTABL.setModel(new DefaultComboBoxModel<String>(periodTypes));
		panel_output.add(comboBox_output_periodESTABL, "7, 34, left, default");
		
		JFormattedTextField formattedTextField_output_startESTABL = new JFormattedTextField(format);
		formattedTextField_output_startESTABL.setValue(new Integer(1));
		formattedTextField_output_startESTABL.setColumns(3);
		panel_output.add(formattedTextField_output_startESTABL, "9, 34, left, default");
		
		JFormattedTextField formattedTextField_output_endESTABL = new JFormattedTextField((Format) null);
		formattedTextField_output_endESTABL.setValue(new Integer(366));
		formattedTextField_output_endESTABL.setColumns(3);
		panel_output.add(formattedTextField_output_endESTABL, "11, 34, left, default");
		
		textField_output_prefixESTABL = new JTextField();
		textField_output_prefixESTABL.setColumns(10);
		panel_output.add(textField_output_prefixESTABL, "13, 34, left, default");
		
		JLabel label_output_discESTABL = new JLabel("/* yearly establishment results */");
		label_output_discESTABL.setAutoscrolls(true);
		panel_output.add(label_output_discESTABL, "15, 34");
	}
}

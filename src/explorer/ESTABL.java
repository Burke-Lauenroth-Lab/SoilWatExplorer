package explorer;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import soilwat.SW_VEGESTAB.SPP_INPUT_DATA;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ESTABL implements ListSelectionListener, ActionListener{
	private soilwat.InputData.EstabIn estab;
	
	private JTextField text_estab_addSPP_name;
	private JTextField textField_estab_sppName;
	
	private JCheckBox chckbx_estab_use;
	private JList<String> list_estab_spp;
	private JButton btn_estab_addSPP;
	private JButton btn_estab_removeSPP;
	
	
	private JFormattedTextField formattedTextField_estab_lyrs;
	private JFormattedTextField formattedTextField_estab_bars_germ;
	private JFormattedTextField formattedTextField_estab_bars_estab;
	private JFormattedTextField formattedTextField_estab_min_pregerm_days;
	private JFormattedTextField formattedTextField_estab_max_pregerm_days;
	private JFormattedTextField formattedTextField_estab_min_wetdays_for_germ;
	private JFormattedTextField formattedTextField_estab_max_drydays_postgerm;
	private JFormattedTextField formattedTextField_estab_min_wetdays_for_estab;
	private JFormattedTextField formattedTextField_estab_min_days_germ2estab;
	private JFormattedTextField formattedTextField_estab_max_days_germ2estab;
	private JFormattedTextField formattedTextField_estab_min_temp_germ;
	private JFormattedTextField formattedTextField_estab_max_temp_germ;
	private JFormattedTextField formattedTextField_estab_min_temp_estab;
	private JFormattedTextField formattedTextField_estab_max_temp_estab;
	
	private int list_index=-1;
	
	public ESTABL(soilwat.InputData.EstabIn estab) {
		this.estab = estab;
	}
	
	public void onGetValues() {
		//Save current Selection
		onGet_SPP(list_index);
		this.estab.use = chckbx_estab_use.isSelected();
	}
	
	public void onSetValues() {
		list_index=-1;
		
		chckbx_estab_use.setSelected(this.estab.use);
		
		setEnabled(this.estab.use);
		
		list_estab_spp.removeAll();
		if(this.estab.use) {
			onReset_list();
		}
	}
	
	private void onSet_SPP() {
		soilwat.SW_VEGESTAB.SPP_INPUT_DATA spp = this.estab.spps.get(list_index);
		
		textField_estab_sppName.setText(spp.sppName);
		
		formattedTextField_estab_lyrs.setValue(spp.soilLayerParams.estab_lyrs);
		formattedTextField_estab_bars_germ.setValue(spp.soilLayerParams.bars[0]);
		formattedTextField_estab_bars_estab.setValue(spp.soilLayerParams.bars[1]);
		formattedTextField_estab_min_pregerm_days.setValue(spp.timingParams.min_pregerm_days);
		formattedTextField_estab_max_pregerm_days.setValue(spp.timingParams.max_pregerm_days);
		formattedTextField_estab_min_wetdays_for_germ.setValue(spp.timingParams.min_wetdays_for_germ);
		formattedTextField_estab_max_drydays_postgerm.setValue(spp.timingParams.max_drydays_postgerm);
		formattedTextField_estab_min_wetdays_for_estab.setValue(spp.timingParams.min_wetdays_for_estab);
		formattedTextField_estab_min_days_germ2estab.setValue(spp.timingParams.min_days_germ2estab);
		formattedTextField_estab_max_days_germ2estab.setValue(spp.timingParams.max_days_germ2estab);
		formattedTextField_estab_min_temp_germ.setValue(spp.tempParams.min_temp_germ);
		formattedTextField_estab_max_temp_germ.setValue(spp.tempParams.max_temp_germ);
		formattedTextField_estab_min_temp_estab.setValue(spp.tempParams.min_temp_estab);
		formattedTextField_estab_max_temp_estab.setValue(spp.tempParams.max_temp_estab);
	}
	
	private void onGet_SPP(int index) {
		if(list_index != -1) {
			soilwat.SW_VEGESTAB.SPP_INPUT_DATA spp = this.estab.spps.get(index);
			spp.sppName = textField_estab_sppName.getText();
			spp.soilLayerParams.estab_lyrs = ((Number)formattedTextField_estab_lyrs.getValue()).intValue();
			spp.soilLayerParams.bars[0] = ((Number)formattedTextField_estab_bars_germ.getValue()).intValue();
			spp.soilLayerParams.bars[1] = ((Number)formattedTextField_estab_bars_estab.getValue()).intValue();
			spp.timingParams.min_pregerm_days = ((Number)formattedTextField_estab_min_pregerm_days.getValue()).intValue();
			spp.timingParams.max_pregerm_days = ((Number)formattedTextField_estab_max_pregerm_days.getValue()).intValue();
			spp.timingParams.min_wetdays_for_germ = ((Number)formattedTextField_estab_min_wetdays_for_germ.getValue()).intValue();
			spp.timingParams.max_drydays_postgerm = ((Number)formattedTextField_estab_max_drydays_postgerm.getValue()).intValue();
			spp.timingParams.min_wetdays_for_estab = ((Number)formattedTextField_estab_min_wetdays_for_estab.getValue()).intValue();
			spp.timingParams.min_days_germ2estab = ((Number)formattedTextField_estab_min_days_germ2estab.getValue()).intValue();
			spp.timingParams.max_days_germ2estab = ((Number)formattedTextField_estab_max_days_germ2estab.getValue()).intValue();
			spp.tempParams.min_temp_germ = ((Number)formattedTextField_estab_min_temp_germ.getValue()).doubleValue();
			spp.tempParams.max_temp_germ = ((Number)formattedTextField_estab_max_temp_germ.getValue()).doubleValue();
			spp.tempParams.min_temp_estab = ((Number)formattedTextField_estab_min_temp_estab.getValue()).doubleValue();
			spp.tempParams.max_temp_estab = ((Number)formattedTextField_estab_max_temp_estab.getValue()).doubleValue();
		}
	}
	
	private void setEnabled(boolean enabled) {
		if(list_estab_spp.getModel().getSize() == 0 && list_estab_spp.getSelectedIndex() == -1) {
			enabled = false;
		}
		btn_estab_addSPP.setEnabled(enabled);
		btn_estab_removeSPP.setEnabled(enabled);
		text_estab_addSPP_name.setEnabled(enabled);
		list_estab_spp.setEnabled(enabled);
		textField_estab_sppName.setEnabled(enabled);
		formattedTextField_estab_lyrs.setEnabled(enabled);
		formattedTextField_estab_bars_germ.setEnabled(enabled);
		formattedTextField_estab_bars_estab.setEnabled(enabled);
		formattedTextField_estab_min_pregerm_days.setEnabled(enabled);
		formattedTextField_estab_max_pregerm_days.setEnabled(enabled);
		formattedTextField_estab_min_wetdays_for_germ.setEnabled(enabled);
		formattedTextField_estab_max_drydays_postgerm.setEnabled(enabled);
		formattedTextField_estab_min_wetdays_for_estab.setEnabled(enabled);
		formattedTextField_estab_min_days_germ2estab.setEnabled(enabled);
		formattedTextField_estab_max_days_germ2estab.setEnabled(enabled);
		formattedTextField_estab_min_temp_germ.setEnabled(enabled);
		formattedTextField_estab_max_temp_germ.setEnabled(enabled);
		formattedTextField_estab_min_temp_estab.setEnabled(enabled);
		formattedTextField_estab_max_temp_estab.setEnabled(enabled);
		if(list_estab_spp.getModel().getSize() == 0 && list_estab_spp.getSelectedIndex() == -1) {
			if(this.chckbx_estab_use.isSelected()) {
				btn_estab_addSPP.setEnabled(true);
				btn_estab_removeSPP.setEnabled(true);
				text_estab_addSPP_name.setEnabled(true);
				list_estab_spp.setEnabled(true);
				this.btn_estab_removeSPP.setEnabled(false);
			}
		}
	}
	
	private void onReset_list() {
		this.list_index = -1;
		list_estab_spp.removeAll();
		list_estab_spp.setListData(this.estab.estabFiles.toArray(new String[this.estab.estabFiles.size()]));
		list_estab_spp.setSelectedIndex(0);
		
		if(list_estab_spp.getModel().getSize() == 0 && list_estab_spp.getSelectedIndex() == -1) {
			setEnabled(false);
			textField_estab_sppName.setText("");
			formattedTextField_estab_lyrs.setValue(new Integer(0));
			formattedTextField_estab_bars_germ.setValue(new Integer(0));
			formattedTextField_estab_bars_estab.setValue(new Integer(0));
			formattedTextField_estab_min_pregerm_days.setValue(new Integer(0));
			formattedTextField_estab_max_pregerm_days.setValue(new Integer(0));
			formattedTextField_estab_min_wetdays_for_germ.setValue(new Integer(0));
			formattedTextField_estab_max_drydays_postgerm.setValue(new Integer(0));
			formattedTextField_estab_min_wetdays_for_estab.setValue(new Integer(0));
			formattedTextField_estab_min_days_germ2estab.setValue(new Integer(0));
			formattedTextField_estab_max_days_germ2estab.setValue(new Integer(0));
			formattedTextField_estab_min_temp_germ.setValue(new Double(0));
			formattedTextField_estab_max_temp_germ.setValue(new Double(0));
			formattedTextField_estab_min_temp_estab.setValue(new Double(0));
			formattedTextField_estab_max_temp_estab.setValue(new Double(0));
		} else {
			setEnabled(true);
			this.btn_estab_removeSPP.setEnabled(true);
		}
	}
	
	public JPanel onGetPanel_establ() {
		Format format_int = NumberFormat.getIntegerInstance();
		((NumberFormat)format_int).setGroupingUsed(false);
		
		Format format_double = NumberFormat.getNumberInstance();
		((NumberFormat)format_double).setGroupingUsed(false);
		
		JPanel panel_estab = new JPanel();

		JPanel panel_column_left = new JPanel();
		panel_estab.add(panel_column_left);
		panel_column_left.setLayout(new BoxLayout(panel_column_left, BoxLayout.PAGE_AXIS));
		
		JPanel panel_EstablishmentParam = new JPanel();
		panel_column_left.add(panel_EstablishmentParam);
		panel_EstablishmentParam.setLayout(new BoxLayout(panel_EstablishmentParam, BoxLayout.PAGE_AXIS));
		
		JLabel lblEstablishment = new JLabel("Establishment");
		lblEstablishment.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_EstablishmentParam.add(lblEstablishment);
		
		chckbx_estab_use = new JCheckBox("Check Establishment");
		chckbx_estab_use.addActionListener(this);
		panel_EstablishmentParam.add(chckbx_estab_use);
		
		JPanel panel_SPP_List = new JPanel();
		panel_column_left.add(panel_SPP_List);
		panel_SPP_List.setLayout(new BoxLayout(panel_SPP_List, BoxLayout.PAGE_AXIS));
		
		JScrollPane scrollPane_estab_spps = new JScrollPane();
		list_estab_spp = new JList<String>();
		list_estab_spp.setModel(new DefaultListModel<String>());
		list_estab_spp.setVisibleRowCount(15);
		list_estab_spp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_estab_spp.setModel(new AbstractListModel<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4494189897365520564L;
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		list_estab_spp.addListSelectionListener(this);
		scrollPane_estab_spps.setViewportView(list_estab_spp);
		panel_SPP_List.add(scrollPane_estab_spps);
		
		JPanel panel = new JPanel();
		panel_SPP_List.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		text_estab_addSPP_name = new JTextField();
		panel.add(text_estab_addSPP_name);
		text_estab_addSPP_name.setColumns(10);
		
		btn_estab_addSPP = new JButton("+");
		btn_estab_addSPP.addActionListener(this);
		panel.add(btn_estab_addSPP);
		
		btn_estab_removeSPP = new JButton("-");
		btn_estab_removeSPP.addActionListener(this);
		panel.add(btn_estab_removeSPP);
		
		JPanel panel_column_right = new JPanel();
		panel_estab.add(panel_column_right);
		panel_column_right.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSpeciesEstablishmentData = new JLabel("Species Establishment Data");
		panel_column_right.add(lblSpeciesEstablishmentData, "1, 1, 2, 1");
		
		textField_estab_sppName = new JTextField();
		panel_column_right.add(textField_estab_sppName, "1, 3, fill, default");
		textField_estab_sppName.setColumns(10);
		
		JLabel lblSppName = new JLabel("Four Character SPP Name");
		lblSppName.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblSppName, "2, 3, left, default");
		
		JLabel lblSoilLayerParameters = new JLabel("Soil Layer Parameters");
		panel_column_right.add(lblSoilLayerParameters, "1, 5, 2, 1");
		
		formattedTextField_estab_lyrs = new JFormattedTextField(format_int);
		formattedTextField_estab_lyrs.setColumns(10);
		panel_column_right.add(formattedTextField_estab_lyrs, "1, 7, fill, default");
		
		JLabel lblNumberOfLayers = new JLabel("Number of Layers affecting Estab");
		lblNumberOfLayers.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblNumberOfLayers, "2, 7, left, default");
		
		formattedTextField_estab_bars_germ = new JFormattedTextField(format_int);
		formattedTextField_estab_bars_germ.setColumns(10);
		panel_column_right.add(formattedTextField_estab_bars_germ, "1, 9, fill, default");
		
		JLabel lblSwpbarsRequirement = new JLabel("SWP (bars) requirement for germination (top layer)");
		lblSwpbarsRequirement.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblSwpbarsRequirement, "2, 9, left, default");
		
		formattedTextField_estab_bars_estab = new JFormattedTextField(format_int);
		formattedTextField_estab_bars_estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_bars_estab, "1, 11, fill, default");
		
		JLabel lblSwpbarsRequirement_1 = new JLabel("SWP (bars) requirement for establishment (avg of top layers)");
		lblSwpbarsRequirement_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblSwpbarsRequirement_1, "2, 11, left, default");
		
		JLabel lblTimingParametersIn = new JLabel("Timing Parameters in Days");
		lblTimingParametersIn.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_column_right.add(lblTimingParametersIn, "1, 13, 2, 1");
		
		formattedTextField_estab_min_pregerm_days = new JFormattedTextField(format_int);
		formattedTextField_estab_min_pregerm_days.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_pregerm_days, "1, 15, fill, default");
		
		JLabel lblFirstPossibleDay = new JLabel("First Possible Day of Germination");
		lblFirstPossibleDay.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblFirstPossibleDay, "2, 15, left, default");
		
		formattedTextField_estab_max_pregerm_days = new JFormattedTextField(format_int);
		formattedTextField_estab_max_pregerm_days.setColumns(10);
		panel_column_right.add(formattedTextField_estab_max_pregerm_days, "1, 17, fill, default");
		
		JLabel lblLastPossibleDay = new JLabel("Last Possible day of Germination");
		lblLastPossibleDay.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblLastPossibleDay, "2, 17, left, default");
		
		formattedTextField_estab_min_wetdays_for_germ = new JFormattedTextField(format_int);
		formattedTextField_estab_min_wetdays_for_germ.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_wetdays_for_germ, "1, 19, fill, default");
		
		JLabel lblMinimum = new JLabel("Minimum Number of Consecutive Wet Days for Germination to occur");
		lblMinimum.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMinimum, "2, 19, left, default");
		
		formattedTextField_estab_max_drydays_postgerm = new JFormattedTextField(format_int);
		formattedTextField_estab_max_drydays_postgerm.setColumns(10);
		panel_column_right.add(formattedTextField_estab_max_drydays_postgerm, "1, 21, fill, default");
		
		JLabel lblMaxNumberOf = new JLabel("Maximum Number of Consecutive Dry Days after Germination Allowing Establishment");
		lblMaxNumberOf.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMaxNumberOf, "2, 21, left, default");
		
		formattedTextField_estab_min_wetdays_for_estab = new JFormattedTextField(format_int);
		formattedTextField_estab_min_wetdays_for_estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_wetdays_for_estab, "1, 23, fill, default");
		
		JLabel lblNewLabel_1 = new JLabel("Minimum Number of Consecutive Wet Days after Germination before Establishment");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblNewLabel_1, "2, 23");
		
		formattedTextField_estab_min_days_germ2estab = new JFormattedTextField(format_int);
		formattedTextField_estab_min_days_germ2estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_days_germ2estab, "1, 25, fill, default");
		
		JLabel lblMinimumNumberOf = new JLabel("Minimum Number of Days Between Germination and Establishment");
		lblMinimumNumberOf.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMinimumNumberOf, "2, 25");
		
		formattedTextField_estab_max_days_germ2estab = new JFormattedTextField(format_int);
		formattedTextField_estab_max_days_germ2estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_max_days_germ2estab, "1, 27, fill, default");
		
		JLabel lblNewLabel_2 = new JLabel("Maximum Number of Days Between Germination and Establishment");
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblNewLabel_2, "2, 27");
		
		JLabel lblTemperatureParametersIn = new JLabel("Temperature Parameters in C");
		panel_column_right.add(lblTemperatureParametersIn, "1, 29, 2, 1");
		
		formattedTextField_estab_min_temp_germ = new JFormattedTextField(format_double);
		formattedTextField_estab_min_temp_germ.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_temp_germ, "1, 31, fill, default");
		
		JLabel lblMinimumTemperatureThreshold = new JLabel("Minimum Temperature Threshold for Germination");
		lblMinimumTemperatureThreshold.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMinimumTemperatureThreshold, "2, 31");
		
		formattedTextField_estab_max_temp_germ = new JFormattedTextField(format_double);
		formattedTextField_estab_max_temp_germ.setColumns(10);
		panel_column_right.add(formattedTextField_estab_max_temp_germ, "1, 33, fill, default");
		
		JLabel lblMaximumTemperatureThreshold = new JLabel("Maximum Temperature Threshold for Germination");
		lblMaximumTemperatureThreshold.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMaximumTemperatureThreshold, "2, 33");
		
		formattedTextField_estab_min_temp_estab = new JFormattedTextField(format_double);
		formattedTextField_estab_min_temp_estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_min_temp_estab, "1, 35, fill, default");
		
		JLabel lblMinimumTemperatureThreshold_1 = new JLabel("Minimum Temperature Threshold for Establishment");
		lblMinimumTemperatureThreshold_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblMinimumTemperatureThreshold_1, "2, 35");
		
		formattedTextField_estab_max_temp_estab = new JFormattedTextField(format_double);
		formattedTextField_estab_max_temp_estab.setColumns(10);
		panel_column_right.add(formattedTextField_estab_max_temp_estab, "1, 37, fill, default");
		
		JLabel lblNewLabel_3 = new JLabel("Maximum Temperature Threshold for Establishment");
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_column_right.add(lblNewLabel_3, "2, 37");
		
		return panel_estab;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<String> lsm = (JList<String>)e.getSource();
		if(!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
			if(list_index != -1) onGet_SPP(list_index);
			int minIndex = lsm.getMinSelectionIndex();
			int maxIndex = lsm.getMaxSelectionIndex();
			for(int i=minIndex; i<=maxIndex; i++) {
				if(lsm.isSelectedIndex(i)) {
					this.list_index = i;
				}
			}
			onSet_SPP();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

	    if (src == btn_estab_addSPP) {
	    	if(this.estab.estabFiles.contains(text_estab_addSPP_name.getText())) {
	    		JOptionPane.showMessageDialog(null, "The SPP file "+text_estab_addSPP_name.getText()+" already exists.", "Alert", JOptionPane.ERROR_MESSAGE);
	    	} else {
	    		if(text_estab_addSPP_name.getText() != "") {
	    			//add to list
	    			this.estab.estabFiles.add(text_estab_addSPP_name.getText());
	    			this.estab.spps.add(new SPP_INPUT_DATA());
	    			//Save current spp
	    			onGet_SPP(this.list_estab_spp.getSelectedIndex());
	    			onReset_list();
	    		}
	    	}
	    } else if (src == btn_estab_removeSPP) {
	    	if(list_estab_spp.getModel().getSize() != 0 && list_estab_spp.getSelectedIndex() != -1) {
	    		int remove = this.list_estab_spp.getSelectedIndex();
	    		this.estab.estabFiles.remove(remove);
	    		this.estab.spps.remove(remove);
	    		onReset_list();
	    	}
	    } else if (src == chckbx_estab_use) {
	    	setEnabled(chckbx_estab_use.isSelected());
	    }
	    
	}
}

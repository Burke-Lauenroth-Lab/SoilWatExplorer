package explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SWC implements ListSelectionListener, ActionListener, PropertyChangeListener{
	private soilwat.SW_SOILWAT_HISTORY hist;
	private soilwat.SW_SOILWATER.SWC_INPUT_DATA swc;
	
	private JCheckBox chckbx_swc_useSWCData;
	private JTextField textField_swc_filePrefix;
	private JFormattedTextField formattedTextField_swc_firstYear;
	private JComboBox<String> comboBox_swc_method;
	
	private JList<String> list_years;
	private JButton btn_swc_remove;
	private JButton btn_swc_add;
	private JFormattedTextField formattedTextField_swc_historyAddYear;
	private JFormattedTextField formattedTextField_swc_historyLayers;
	
	private JList<String> list_doy;
	
	private JTable table_swc_historyInput;
	
	private int list_index_year = -1;
	private int list_index_day = -1;
	private int nLayers = 25;
	
	private String[] doy365;
	private String[] doy366;
	
	public SWC(soilwat.SW_SOILWATER.SWC_INPUT_DATA swc, soilwat.SW_SOILWAT_HISTORY hist) {
		this.hist = hist;
		this.swc = swc;
		
		doy365 = new String[365];
		doy366 = new String[366];
		
		for(int i=0; i<366; i++) {
			if(i<365)
				doy365[i] = String.valueOf(i+1);
			doy366[i] = String.valueOf(i+1);
		}
	}
	
	public void onGetValues() {
		swc.hist_use = chckbx_swc_useSWCData.isSelected();
		swc.filePrefix = textField_swc_filePrefix.getText();
		swc.yr.setFirst(((Number)formattedTextField_swc_firstYear.getValue()).intValue());
		swc.method = comboBox_swc_method.getSelectedIndex()+1;
		
		onGet_HIST(this.list_index_year);
	}
	
	public void onSetValues() {
		this.chckbx_swc_useSWCData.setSelected(swc.hist_use);
		this.textField_swc_filePrefix.setText(swc.filePrefix);
		this.formattedTextField_swc_firstYear.setValue(swc.yr.getFirst());
		this.comboBox_swc_method.setSelectedIndex(swc.method-1);
		
		this.formattedTextField_swc_historyAddYear.setValue(new Integer(1990));
		this.formattedTextField_swc_historyLayers.setValue(new Integer(25));
		
		onReset_list();
		setEnable();
	}
	
	private void setEnable() {
		if(chckbx_swc_useSWCData.isSelected()) {
			//Show everything
			textField_swc_filePrefix.setEnabled(true);
			formattedTextField_swc_firstYear.setEnabled(true);
			comboBox_swc_method.setEnabled(true);
			list_years.setEnabled(true);
			btn_swc_remove.setEnabled(true);
			btn_swc_add.setEnabled(true);
			formattedTextField_swc_historyAddYear.setEnabled(true);
			formattedTextField_swc_historyLayers.setEnabled(true);
			list_doy.setEnabled(true);
			table_swc_historyInput.setEnabled(true);
			//Hide remove button and disable views if no years
			if(list_years.getModel().getSize() == 0 && list_years.getSelectedIndex() == -1) {
				btn_swc_remove.setEnabled(false);
				list_doy.setEnabled(false);
				table_swc_historyInput.setEnabled(false);
			}
			
		} else {
			// Disable everything
			textField_swc_filePrefix.setEnabled(false);
			formattedTextField_swc_firstYear.setEnabled(false);
			comboBox_swc_method.setEnabled(false);
			list_years.setEnabled(false);
			btn_swc_remove.setEnabled(false);
			btn_swc_add.setEnabled(false);
			formattedTextField_swc_historyAddYear.setEnabled(false);
			formattedTextField_swc_historyLayers.setEnabled(false);
			list_doy.setEnabled(false);
			table_swc_historyInput.setEnabled(false);
		}
	}
	
	private void onReset_list() {
		this.list_index_year = -1;
		list_years.removeAll();
		
		this.list_index_day = -1;
		list_doy.removeAll();
		
		if(hist.data()) {
			list_years.setListData(hist.getHistYearsString());
			list_years.setSelectedIndex(0);

			if(hist.getDays() == 366)
				list_doy.setListData(doy366);
			else
				list_doy.setListData(doy365);
			
			list_doy.setSelectedIndex(0);
		}
		if(list_years.getModel().getSize() == 0 && list_years.getSelectedIndex() == -1) {
			table_swc_historyInput.removeAll();
			table_swc_historyInput.setModel(getModel(this.nLayers));
			for(int i=0; i<this.nLayers; i++) {
				table_swc_historyInput.setValueAt(i+1, i, 0);
				table_swc_historyInput.setValueAt(0.0, i, 1);
				table_swc_historyInput.setValueAt(0.0, i, 2);
			}
		}
		
	}
	
	private void onGet_HIST(int year) {
		if(list_index_year != -1) {
			year = Integer.parseInt(list_years.getSelectedValuesList().get(year));
			this.hist.setCurrentYear(year);
			int rows = this.hist.getLayers();
			int doy = Integer.parseInt(list_doy.getModel().getElementAt(list_index_day))-1;
			
			for(int i=0; i<rows; i++) {
				double swc = ((Number)table_swc_historyInput.getValueAt(i, 1)).doubleValue();
				double std_err = ((Number)table_swc_historyInput.getValueAt(i, 2)).doubleValue();
				
				this.hist.set_day(doy, i, swc, std_err);
			}
		}
	}
	private void onSet_HIST() {
		hist.setCurrentYear(Integer.parseInt(list_years.getSelectedValue()));
		table_swc_historyInput.removeAll();
		table_swc_historyInput.setModel(getModel(this.nLayers));
		int doy = 0;
		if(list_doy.getModel().getSize() != 0 && list_doy.getSelectedIndex() != -1) {
			doy = Integer.parseInt(list_doy.getSelectedValue())-1;
		}
		for(int i=0; i<this.hist.getLayers(); i++) {
			double swc = hist.getSWC(doy,i);
			double std_error = hist.getStd_err(doy, i);
			table_swc_historyInput.setValueAt(i+1, i, 0);
			table_swc_historyInput.setValueAt(swc, i, 1);
			table_swc_historyInput.setValueAt(std_error, i, 2);
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel onGetPanel_swc() {
		Format format_int = NumberFormat.getIntegerInstance();
		((NumberFormat)format_int).setGroupingUsed(false);
		
		JPanel panel_swc = new JPanel();
		panel_swc.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_swc_left = new JPanel();
		panel_swc.add(panel_swc_left, BorderLayout.WEST);
		panel_swc_left.setLayout(new BoxLayout(panel_swc_left, BoxLayout.PAGE_AXIS));
		
		JPanel panel_swc_setup = new JPanel();
		panel_swc_setup.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_swc_left.add(panel_swc_setup);
		panel_swc_setup.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lbl_swc_label = new JLabel("Measured SWC Setup Parameters");
		panel_swc_setup.add(lbl_swc_label, "1, 1, 2, 1");
		
		chckbx_swc_useSWCData = new JCheckBox("");
		chckbx_swc_useSWCData.addActionListener(this);
		panel_swc_setup.add(chckbx_swc_useSWCData, "1, 3");
		
		JLabel lbl_swc_useData = new JLabel("SWC historic data file");
		lbl_swc_useData.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_swc_setup.add(lbl_swc_useData, "2, 3");
		
		textField_swc_filePrefix = new JTextField();
		panel_swc_setup.add(textField_swc_filePrefix, "1, 5, left, default");
		textField_swc_filePrefix.setColumns(7);
		
		JLabel lbl_swc_prefix = new JLabel("Data File Prefix");
		lbl_swc_prefix.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_swc_setup.add(lbl_swc_prefix, "2, 5");
		
		formattedTextField_swc_firstYear = new JFormattedTextField(format_int);
		formattedTextField_swc_firstYear.setColumns(7);
		panel_swc_setup.add(formattedTextField_swc_firstYear, "1, 7, left, default");
		
		JLabel lbl_swc_firstYear = new JLabel("First Year of Measurement data files");
		lbl_swc_firstYear.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_swc_setup.add(lbl_swc_firstYear, "2, 7");
		
		JLabel lbl_swc_Method = new JLabel("Method");
		lbl_swc_Method.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_swc_setup.add(lbl_swc_Method, "1, 9, 2, 1, center, default");
		
		comboBox_swc_method = new JComboBox<String>();
		comboBox_swc_method.setModel(new DefaultComboBoxModel<String>(new String[] {"1 - Average with model", "2 - Measured +/- stderr"}));
		comboBox_swc_method.setSelectedIndex(1);
		panel_swc_setup.add(comboBox_swc_method, "1, 11, 2, 1, fill, default");
		
		JPanel panel_swc_historySelect = new JPanel();
		panel_swc_historySelect.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_swc_left.add(panel_swc_historySelect);
		panel_swc_historySelect.setLayout(new BoxLayout(panel_swc_historySelect, BoxLayout.PAGE_AXIS));
		
		JLabel lbl_year = new JLabel("Historic SWC Years");
		panel_swc_historySelect.add(lbl_year);
		
		JScrollPane scrollPane_swc_swcYearList = new JScrollPane();
		panel_swc_historySelect.add(scrollPane_swc_swcYearList);
		list_years = new JList<String>();
		list_years.setName("year");
		list_years.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_years.setVisibleRowCount(30);
		list_years.addListSelectionListener(this);
		scrollPane_swc_swcYearList.setViewportView(list_years);
		
		JPanel panel_swc_historyButtons = new JPanel();
		panel_swc_historySelect.add(panel_swc_historyButtons);

		btn_swc_remove = new JButton("-");
		btn_swc_remove.addActionListener(this);
		panel_swc_historyButtons.add(btn_swc_remove);
		
		btn_swc_add = new JButton("+");
		btn_swc_add.addActionListener(this);
		panel_swc_historyButtons.add(btn_swc_add);
		
		JLabel lbl_addYear = new JLabel("Add Year");
		panel_swc_historyButtons.add(lbl_addYear);
		
		formattedTextField_swc_historyAddYear = new JFormattedTextField(format_int);
		formattedTextField_swc_historyAddYear.setColumns(4);
		formattedTextField_swc_historyAddYear.setValue(new Integer(0));
		panel_swc_historyButtons.add(formattedTextField_swc_historyAddYear);
		
		JLabel lbl_layers = new JLabel("Layers");
		panel_swc_historyButtons.add(lbl_layers);
		
		formattedTextField_swc_historyLayers = new JFormattedTextField(format_int);
		formattedTextField_swc_historyLayers.addPropertyChangeListener(this);
		formattedTextField_swc_historyLayers.setColumns(2);
		panel_swc_historyButtons.add(formattedTextField_swc_historyLayers);
		
		JPanel panel_center = new JPanel();
		panel_swc.add(panel_center, BorderLayout.CENTER);
		panel_center.setLayout(new BoxLayout(panel_center, BoxLayout.PAGE_AXIS));
		
		JLabel lbl_doy = new JLabel("Select DOY to edit SWC");
		panel_center.add(lbl_doy);
		
		JScrollPane scrollPane_doy_select = new JScrollPane();
		panel_center.add(scrollPane_doy_select);
		
		list_doy = new JList<String>();
		list_doy.setName("day");
		list_doy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_doy.setVisibleRowCount(30);
		list_doy.addListSelectionListener(this);
		scrollPane_doy_select.setViewportView(list_doy);
		
		JPanel panel_swc_right = new JPanel();
		panel_swc.add(panel_swc_right, BorderLayout.EAST);
		
		table_swc_historyInput = new JTable();
		table_swc_historyInput.setModel(getModel(this.nLayers));
		table_swc_historyInput.getColumnModel().getColumn(0).setResizable(false);
		panel_swc_right.setLayout(new BoxLayout(panel_swc_right, BoxLayout.X_AXIS));
		panel_swc_right.add(table_swc_historyInput);
		panel_swc_right.add(new JScrollPane(table_swc_historyInput));
		
		return panel_swc;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

	    if (src == btn_swc_add) {
	    	int Year = ((Number)formattedTextField_swc_historyAddYear.getValue()).intValue();
	    	int diy = soilwat.Times.Time_get_lastdoy_y(Year);
	    	if(this.hist.getHistYearsInteger().contains(Year)) {
	    		JOptionPane.showMessageDialog(null, "The weather list already contains year "+String.valueOf(Year), "Alert", JOptionPane.ERROR_MESSAGE);
	    	} else {
	    		if(formattedTextField_swc_historyAddYear.getText() != "") {
	    			//add to list
	    			this.hist.add_year(Year, this.nLayers, new double[diy][25], new double[diy][25]);
	    			//Save current Hist
	    			formattedTextField_swc_historyAddYear.setValue(new Integer(Year+1));
	    			onGet_HIST(this.list_index_year);
	    			onReset_list();
	    		}
	    	}
	    	setEnable();
	    } else if (src == btn_swc_remove) {
	    	if(list_years.getModel().getSize() != 0 && list_years.getSelectedIndex() != -1) {
	    		int remove = Integer.parseInt(this.list_years.getSelectedValue().toString());
	    		this.hist.remove(remove);
	    		//onReset_list();
	    	}
	    	setEnable();
	    } else if(src == chckbx_swc_useSWCData) {
	    	setEnable();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<String> lsm = (JList<String>)e.getSource();
		if(lsm.getName() == "year") {
			if(!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
				if(list_index_year != -1 && list_index_day != -1) onGet_HIST(list_index_year);
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for(int i=minIndex; i<=maxIndex; i++) {
					if(lsm.isSelectedIndex(i)) {
						this.list_index_year = i;
					}
				}
				//this.weatherHist.setCurrentYear(Integer.parseInt(list_historyYears.getSelectedValue()));
				onSet_HIST();
			}
		}
		if(lsm.getName() == "day") {
			if(!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
				if(list_index_day != -1 && list_index_year != -1) onGet_HIST(list_index_year);
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for(int i=minIndex; i<=maxIndex; i++) {
					if(lsm.isSelectedIndex(i)) {
						this.list_index_day = i;
					}
				}
				//this.weatherHist.setCurrentYear(Integer.parseInt(list_historyYears.getSelectedValue()));
				onSet_HIST();
			}
		}
	}
	
	public DefaultTableModel getModel(int layers) {
		DefaultTableModel model = new DefaultTableModel(
				new Object[layers][3],
				new String[] {
					"Layer", "SWC", "stderr"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -4266626870910874650L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Integer.class, Double.class, Double.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, true, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		
		//for(int i=layers; i<25; i++)
		//	model.removeRow(i);
		return model;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
        if (source == formattedTextField_swc_historyLayers) {
        	if(((Number)formattedTextField_swc_historyLayers.getValue()) != null) {
        		this.nLayers = ((Number)formattedTextField_swc_historyLayers.getValue()).intValue();
        		this.hist.setLayers(this.nLayers);
        	}
        } 
	}
}

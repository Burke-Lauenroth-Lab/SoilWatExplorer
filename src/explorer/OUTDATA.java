package explorer;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import soilwat.SW_CONTROL;
import soilwat.SW_OUTPUT;
import soilwat.SW_OUTPUT.OutKey;
import soilwat.SW_OUTPUT.OutPeriod;

import javax.swing.JComboBox;
import javax.swing.BoxLayout;

import java.awt.Dimension;

import javax.swing.JTable;

import charts.ChartSelector;

public class OUTDATA extends JPanel implements ListSelectionListener, ItemListener {
	
	protected PeriodEvent periodEvent;
	protected KeyEvent keyEvent;
	public void addPeriodEventListener(PeriodEvent e) {
		periodEvent = e;
	}
	public void addKeyEventListener(KeyEvent e) {
		keyEvent = e;
	}
	
	public final class ScrollingTableFix implements ComponentListener {
		private final JTable table;

		public ScrollingTableFix(JTable table, JScrollPane scrollPane) {
			assert table != null;

			this.table = table;

			table.getModel().addTableModelListener(new ColumnAddFix(table, scrollPane));
		}

		public void componentHidden(final ComponentEvent event) {}

		public void componentMoved(final ComponentEvent event) {}

		public void componentResized(final ComponentEvent event) {
			// turn off resize and let the scroll bar appear once the component is smaller than the table
			if (event.getComponent().getWidth() < table.getPreferredSize().getWidth()) {
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			}
			// otherwise resize new columns in the table
			else {
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			}
		}

		public void componentShown(final ComponentEvent event) {}

		// similar behavior is needed when columns are added to the table
		private final class ColumnAddFix implements TableModelListener {
			private final JTable table;
			private final JScrollPane scrollPane;

			ColumnAddFix(JTable table, JScrollPane scrollPane) {
				this.table = table;
				this.scrollPane = scrollPane;
			}

			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
					if (scrollPane.getViewport().getWidth() < table.getPreferredSize().getWidth()) {
						table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					}
					else {
						table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					}
				}
			}
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	private JList<SW_OUTPUT.OUTPUT_INPUT_DATA> list_outData_KeySelect;
	private DefaultListModel<SW_OUTPUT.OUTPUT_INPUT_DATA> key_model = new DefaultListModel<SW_OUTPUT.OUTPUT_INPUT_DATA>();
	private JComboBox<SW_OUTPUT.OutPeriod> comboBox_periodSelector;
	private DefaultComboBoxModel<SW_OUTPUT.OutPeriod> period_model = new DefaultComboBoxModel<SW_OUTPUT.OutPeriod>();
	private JScrollPane scrollPane_2;
	
	private soilwat.InputData.OutputIn out;
	private SW_CONTROL control;
	private boolean usePeriodColumn = true;
	
	private JTable table_data;
	private ChartSelector panel_chart;
	
	public OUTDATA(SW_CONTROL control, soilwat.InputData.OutputIn out, JProgressBar pb) {
		this.out = out;
		this.control = control;
		for(int i=0; i<out.TimeSteps.length; i++) {
			if(out.TimeSteps[i])
				usePeriodColumn = false;
		}
		for(int i=0; i<out.outputs.length; i++)
			if(out.outputs[i].use)
				key_model.addElement(out.outputs[i]);
		if(usePeriodColumn)
			period_model.addElement(out.outputs[0].periodColumn);
		else
			for(int i=0; i<out.TimeSteps.length; i++) {
				if(out.TimeSteps[i])
					period_model.addElement(OutPeriod.fromInteger(i));
			}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setMinimumSize(new Dimension(2000, 275));
		panel.setMaximumSize(new Dimension(2000, 275));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		pb.setValue(pb.getValue()+10);
		
		JPanel panel_side = new JPanel();
		panel.add(panel_side);
		panel_side.setMinimumSize(new Dimension(200, 270));
		panel_side.setMaximumSize(new Dimension(200, 270));
		panel_side.setLayout(new BoxLayout(panel_side, BoxLayout.PAGE_AXIS));
		
		JScrollPane scrollPane_out_outTypes = new JScrollPane();
		panel_side.add(scrollPane_out_outTypes);
		list_outData_KeySelect = new JList<SW_OUTPUT.OUTPUT_INPUT_DATA>();
		list_outData_KeySelect.setName("OutputTypes");
		list_outData_KeySelect.setModel(key_model);
		list_outData_KeySelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_outData_KeySelect.setVisibleRowCount(15);
		list_outData_KeySelect.setSelectedIndex(0);
		scrollPane_out_outTypes.setViewportView(list_outData_KeySelect);
		
		comboBox_periodSelector = new JComboBox<SW_OUTPUT.OutPeriod>();
		panel_side.add(comboBox_periodSelector);
		comboBox_periodSelector.setModel(period_model);
		comboBox_periodSelector.setSelectedIndex(0);
		
		onSetTable();
		
		pb.setValue(pb.getValue()+15);
		
		scrollPane_2 = new JScrollPane(table_data);
		scrollPane_2.setPreferredSize(new Dimension(750,280));
		panel.add(scrollPane_2);
		scrollPane_2.addComponentListener(new ScrollingTableFix(table_data, scrollPane_2));
		OutPeriod period = (SW_OUTPUT.OutPeriod)comboBox_periodSelector.getSelectedItem();
		comboBox_periodSelector.addItemListener(this);
		
		OutKey key = list_outData_KeySelect.getSelectedValue().mykey;
		
		pb.setValue(pb.getValue()+10);
		//listeners
		list_outData_KeySelect.addListSelectionListener(this);
		
		panel_chart = new ChartSelector(control, period, key);
		add(panel_chart);
		
		pb.setValue(pb.getValue()+15);
		
		this.addPeriodEventListener(this.panel_chart);
		this.addKeyEventListener(this.panel_chart);
	}
	
	private void onSetTable() {
		OutKey key = list_outData_KeySelect.getSelectedValue().mykey;
		OutPeriod period = (SW_OUTPUT.OutPeriod)comboBox_periodSelector.getSelectedItem();
		
		int rows = control.onGet_Timing().get_nRows(period);
		int columns = control.onGet_Timing().get_nColumns(period);
		columns+=control.onGet_nColumns(key);
		
		Object[][] data = new Object[rows][columns];
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				if(j<control.onGet_Timing().get_nColumns(period))
					data[i][j] = new Integer(control.onGet_Timing().timingValue(period, i, j));
				else
					data[i][j] = new Double(control.onGetOutput(key, period)[i][j-control.onGet_Timing().get_nColumns(period)]);
			}
		}
		String[] time_names = control.onGet_Timing().getColumnNames(period);
		String[] data_names = control.onGet_OutputColumnNames(key);
		String[] names = new String[time_names.length+data_names.length];
		for(int i=0; i<names.length; i++) {
			if(i < time_names.length)
				names[i] = time_names[i];
			else
				names[i] = data_names[i-time_names.length];
		}
		table_data = new JTable(data, names);
		if(scrollPane_2 != null) {
			scrollPane_2.getViewport().remove(0);
			scrollPane_2.setViewportView(table_data);
			scrollPane_2.revalidate();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object src = e.getSource();
		if(src == list_outData_KeySelect) {
			@SuppressWarnings("unchecked")
			JList<SW_OUTPUT.OUTPUT_INPUT_DATA> keySelect = (JList<SW_OUTPUT.OUTPUT_INPUT_DATA>)src;
			OutKey key = keySelect.getSelectedValue().mykey;
			//1. reset the period combobox
			if(usePeriodColumn) {//Only if we are using per key period
				period_model.removeAllElements();
				period_model.addElement(out.outputs[key.idx()].periodColumn);
			}
			//2. Update the table
			onSetTable();
			//3. Notify listeners
			keyEvent.keyChange(key);
			
			this.revalidate();
			this.repaint();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();
		if(src==comboBox_periodSelector) {
			onSetTable();
			periodEvent.periodChange((SW_OUTPUT.OutPeriod)comboBox_periodSelector.getSelectedItem());
			this.revalidate();
			this.repaint();
		}
	}	
}

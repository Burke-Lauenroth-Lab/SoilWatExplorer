package explorer;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class PROD_MONTHLY_BIOMASS extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	private soilwat.SW_VEGPROD.MonthlyProductionValues.Params values;
	/**
	 * Create the frame.
	 */
	public PROD_MONTHLY_BIOMASS(soilwat.SW_VEGPROD.MonthlyProductionValues.Params values, String Title) {
		this.values = values;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"January", null, null, null, null},
				{"February", null, null, null, null},
				{"March", null, null, null, null},
				{"April", null, null, null, null},
				{"May", null, null, null, null},
				{"June", null, null, null, null},
				{"July", null, null, null, null},
				{"August", null, null, null, null},
				{"September", null, null, null, null},
				{"October", null, null, null, null},
				{"November", null, null, null, null},
				{"December", null, null, null, null},
			},
			new String[] {
				"Month", "Litter", "Biomass", "Percent Live", "LAI_conv"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setCellRenderer(new DoubleColumnRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new DoubleColumnRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new DoubleColumnRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new DoubleColumnRenderer());
		contentPane.add(table, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(table));
		
		JPanel panel_south = new JPanel();
		contentPane.add(panel_south, BorderLayout.SOUTH);
		
		JButton btn_save = new JButton("Save");
		btn_save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onGetValues();
			}
		});
		panel_south.add(btn_save);
		
		JButton btn_close = new JButton("Close");
		btn_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				close();
			}
		});
		panel_south.add(btn_close);
		
		this.setTitle(Title);
		
		onSetValues();
	}
	public void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	public void onGetValues() {
		for(int i=0; i<12; i++) {
			this.values.litter[i] = ((Number)table.getValueAt(i, 1)).doubleValue();
			this.values.biomass[i] = ((Number)table.getValueAt(i, 2)).doubleValue();
			this.values.percLive[i] = ((Number)table.getValueAt(i, 3)).doubleValue();
			this.values.lai_conv[i] = ((Number)table.getValueAt(i, 4)).doubleValue();
		}
	}
	public void onSetValues() {
		for(int i=0; i<12; i++) {
			table.setValueAt(this.values.litter[i], i, 1);
			table.setValueAt(this.values.biomass[i], i, 2);
			table.setValueAt(this.values.percLive[i], i, 3);
			table.setValueAt(this.values.lai_conv[i], i, 4);
		}
	}
}

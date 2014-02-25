package explorer;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CLOUD {
	private JTable table_cloud_setup;
	private soilwat.InputData.CloudIn cloud;
	
	public CLOUD(soilwat.InputData.CloudIn cloud) {
		this.cloud = cloud;
	}
	
	public void onGetValues() {
		for(int i=0; i<12; i++) {
			this.cloud.cloudcov[i] = ((Number)table_cloud_setup.getValueAt(0, i+1)).doubleValue();
			this.cloud.windspeed[i] = ((Number)table_cloud_setup.getValueAt(1, i+1)).doubleValue();
			this.cloud.r_humidity[i] = ((Number)table_cloud_setup.getValueAt(2, i+1)).doubleValue();
			this.cloud.transmission[i] = ((Number)table_cloud_setup.getValueAt(3, i+1)).doubleValue();
			this.cloud.snow_density[i] = ((Number)table_cloud_setup.getValueAt(4, i+1)).doubleValue();
		}
	}
	
	public void onSetValues() {
		for(int i=0; i<12; i++) {
			table_cloud_setup.setValueAt(this.cloud.cloudcov[i], 0, i+1);
			table_cloud_setup.setValueAt(this.cloud.windspeed[i], 1, i+1);
			table_cloud_setup.setValueAt(this.cloud.r_humidity[i], 2, i+1);
			table_cloud_setup.setValueAt(this.cloud.transmission[i], 3, i+1);
			table_cloud_setup.setValueAt(this.cloud.snow_density[i], 4, i+1);
		}
	}
	
	public JPanel onGetPanel_cloud() {
		JPanel panel_cloud = new JPanel();
		
		table_cloud_setup = new JTable();
		panel_cloud.add(table_cloud_setup, BorderLayout.NORTH);
		table_cloud_setup.setModel(new DefaultTableModel(
			new Object[][] {
				{"Cloud Cover:", null, null, null, null, null, null, null, null, null, null, null, null},
				{"Wind Speed:", null, null, null, null, null, null, null, null, null, null, null, null},
				{"R Humidity:", null, null, null, null, null, null, null, null, null, null, null, null},
				{"Transmission:", null, null, null, null, null, null, null, null, null, null, null, null},
				{"Snow Density:", null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"label", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4741272571019538461L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_cloud_setup.getColumnModel().getColumn(0).setResizable(false);
		table_cloud_setup.getColumnModel().getColumn(0).setPreferredWidth(105);
		panel_cloud.setLayout(new BoxLayout(panel_cloud, BoxLayout.X_AXIS));
		panel_cloud.add(table_cloud_setup);
		panel_cloud.add(new JScrollPane(table_cloud_setup));
		
		return panel_cloud;
	}
}

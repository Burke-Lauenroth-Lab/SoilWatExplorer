package explorer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SOILS {

	private JTable table_soils;
	private soilwat.InputData.SoilsIn soilsIn;
	
	public SOILS(soilwat.InputData.SoilsIn soilsIn) {
		this.soilsIn = soilsIn;
	}
	
	public void onGetValues() {
		int nLayers=0;
		for(int i=0; i<this.soilsIn.nLayers; i++) {
			if(((Boolean)table_soils.getValueAt(i, 1)).booleanValue()) {
				nLayers++;
				this.soilsIn.layers[i].depth = ((Double)table_soils.getValueAt(i, 2)).doubleValue();
				this.soilsIn.layers[i].soilMatric_density = ((Double)table_soils.getValueAt(i, 3)).doubleValue();
				this.soilsIn.layers[i].fractionVolBulk_gravel = ((Double)table_soils.getValueAt(i, 4)).doubleValue();
				this.soilsIn.layers[i].evap_coeff = ((Double)table_soils.getValueAt(i, 5)).doubleValue();
				this.soilsIn.layers[i].transp_coeff_grass = ((Double)table_soils.getValueAt(i, 6)).doubleValue();
				this.soilsIn.layers[i].transp_coeff_shrub = ((Double)table_soils.getValueAt(i, 7)).doubleValue();
				this.soilsIn.layers[i].transp_coeff_tree = ((Double)table_soils.getValueAt(i, 8)).doubleValue();
				this.soilsIn.layers[i].transp_coeff_forb = ((Double)table_soils.getValueAt(i, 9)).doubleValue();
				this.soilsIn.layers[i].fractionWeightMatric_sand = ((Double)table_soils.getValueAt(i, 10)).doubleValue();
				this.soilsIn.layers[i].fractionWeightMatric_clay = ((Double)table_soils.getValueAt(i, 11)).doubleValue();
				this.soilsIn.layers[i].impermeability = ((Double)table_soils.getValueAt(i, 12)).doubleValue();
				this.soilsIn.layers[i].sTemp = ((Double)table_soils.getValueAt(i, 13)).doubleValue();
			}
		}
		this.soilsIn.nLayers = nLayers;
	}
	
	public void onSetValues() {
		for(int i=0; i<this.soilsIn.nLayers; i++) {
			table_soils.setValueAt(true, i, 1);
			table_soils.setValueAt(this.soilsIn.layers[i].depth, i, 2);
			table_soils.setValueAt(this.soilsIn.layers[i].soilMatric_density, i, 3);
			table_soils.setValueAt(this.soilsIn.layers[i].fractionVolBulk_gravel, i, 4);
			table_soils.setValueAt(this.soilsIn.layers[i].evap_coeff, i, 5);
			table_soils.setValueAt(this.soilsIn.layers[i].transp_coeff_grass, i, 6);
			table_soils.setValueAt(this.soilsIn.layers[i].transp_coeff_shrub, i, 7);
			table_soils.setValueAt(this.soilsIn.layers[i].transp_coeff_tree, i, 8);
			table_soils.setValueAt(this.soilsIn.layers[i].transp_coeff_forb, i, 9);
			table_soils.setValueAt(this.soilsIn.layers[i].fractionWeightMatric_sand, i, 10);
			table_soils.setValueAt(this.soilsIn.layers[i].fractionWeightMatric_clay, i, 11);
			table_soils.setValueAt(this.soilsIn.layers[i].impermeability, i, 12);
			table_soils.setValueAt(this.soilsIn.layers[i].sTemp, i, 13);
		}
		for(int i=this.soilsIn.nLayers; i<25; i++) {
			table_soils.setValueAt(false, i, 1);
			table_soils.setValueAt(0, i, 2);
			table_soils.setValueAt(0, i, 3);
			table_soils.setValueAt(0, i, 4);
			table_soils.setValueAt(0, i, 5);
			table_soils.setValueAt(0, i, 6);
			table_soils.setValueAt(0, i, 7);
			table_soils.setValueAt(0, i, 8);
			table_soils.setValueAt(0, i, 9);
			table_soils.setValueAt(0, i, 10);
			table_soils.setValueAt(0, i, 11);
			table_soils.setValueAt(0, i, 12);
			table_soils.setValueAt(0, i, 13);
		}
	}
	
	protected JPanel onGetPanel_soils() {
		JPanel panel_soils = new JPanel();
		panel_soils.setLayout(new BoxLayout(panel_soils, BoxLayout.X_AXIS));
		
		table_soils = new JTable();
		table_soils.setModel(new DefaultTableModel(
			new Object[][] {
				{"Layer 1:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 2:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 3:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 4:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 5:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 6:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 7:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 8:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 9:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 10:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 11:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 12:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 13:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 14:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 15:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 16:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 17:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 18:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 19:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 20:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 21:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 22:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 23:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 24:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
				{"Layer 25:", Boolean.FALSE, null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Layer", "Use", "Depth", "Matricd", "Gravel Content", "Evco", "trco grass", "trco shrub", "trco tree", "trco forb", "%Sand", "%Clay", "Imperm", "Soil Temp"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3673959860653918288L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Boolean.class, Integer.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class, Float.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true, true, true, true, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_soils.getColumnModel().getColumn(0).setResizable(false);
		table_soils.getColumnModel().getColumn(0).setPreferredWidth(65);
		table_soils.getColumnModel().getColumn(1).setResizable(false);
		table_soils.getColumnModel().getColumn(1).setPreferredWidth(30);
		table_soils.getColumnModel().getColumn(2).setPreferredWidth(45);
		table_soils.getColumnModel().getColumn(3).setPreferredWidth(55);
		table_soils.getColumnModel().getColumn(4).setPreferredWidth(100);
		table_soils.getColumnModel().getColumn(5).setPreferredWidth(50);
		table_soils.getColumnModel().getColumn(10).setPreferredWidth(65);
		table_soils.getColumnModel().getColumn(11).setPreferredWidth(65);
		table_soils.getColumnModel().getColumn(12).setPreferredWidth(65);
		panel_soils.add(table_soils);
		panel_soils.add(new JScrollPane(table_soils));
		
		return panel_soils;
	}
}

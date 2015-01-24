package explorer;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DoubleColumnRenderer extends JLabel implements TableCellRenderer{

	private static final long serialVersionUID = -6483162324490204959L;

	public DoubleColumnRenderer() {
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		DecimalFormat df = new DecimalFormat("#.#######"); 
		setText(df.format(value));

		// Set tool tip if desired
		setToolTipText((String) value.toString());

		return this;
	}

}

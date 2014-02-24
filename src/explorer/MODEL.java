package explorer;

import java.awt.Font;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JCheckBox;

public class MODEL {
	private JFormattedTextField textField_years_StartYear;
	private JFormattedTextField textField_year_EndYear;
	private JFormattedTextField textField_year_FDOFY;
	private JFormattedTextField textField_year_EDOEY;
	private JCheckBox chckbx_isNorth;
	
	
	private soilwat.SW_MODEL.MODEL_INPUT_DATA yearsIn;
	
	public MODEL(soilwat.SW_MODEL.MODEL_INPUT_DATA yearsIn) {
		this.yearsIn = yearsIn;
	}
	
	public void onGetValues() {
		this.yearsIn.startYear = ((Number)textField_years_StartYear.getValue()).intValue();
		this.yearsIn.endYear = ((Number)textField_year_EndYear.getValue()).intValue();
		this.yearsIn.startstart = ((Number)textField_year_FDOFY.getValue()).intValue();
		this.yearsIn.endend = ((Number)textField_year_EDOEY.getValue()).intValue();
		this.yearsIn.isNorth = chckbx_isNorth.isSelected();
	}
	
	public void onSetValues() {
		textField_years_StartYear.setValue(this.yearsIn.startYear);
		textField_year_EndYear.setValue(this.yearsIn.endYear);
		textField_year_FDOFY.setValue(this.yearsIn.startstart);
		textField_year_EDOEY.setValue(this.yearsIn.endend);
		chckbx_isNorth.setSelected(this.yearsIn.isNorth);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel onGetPanel_model() {
		Format format = NumberFormat.getIntegerInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		JPanel panel_years = new JPanel();
		panel_years.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default"),
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("left:default"),
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblModelTimeDefinition = new JLabel("Model Time Definition Table");
		panel_years.add(lblModelTimeDefinition, "1, 1, 3, 1, center, default");
		
		JLabel lblStartYear = new JLabel("Start Year:");
		lblStartYear.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(lblStartYear, "1, 3, right, default");
		
		textField_years_StartYear = new JFormattedTextField(format);
		panel_years.add(textField_years_StartYear, "2, 3, left, default");
		textField_years_StartYear.setColumns(5);
		
		JLabel lblEndYear = new JLabel("End Year:");
		lblEndYear.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(lblEndYear, "1, 5, right, default");
		
		textField_year_EndYear = new JFormattedTextField(format);
		panel_years.add(textField_year_EndYear, "2, 5, left, default");
		textField_year_EndYear.setColumns(5);
		
		JLabel lblFirstDayOf = new JLabel("First Day of First Year:");
		lblFirstDayOf.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(lblFirstDayOf, "1, 7, right, default");
		
		textField_year_FDOFY = new JFormattedTextField(format);
		panel_years.add(textField_year_FDOFY, "2, 7, left, default");
		textField_year_FDOFY.setColumns(5);
		
		JLabel lblEndDayOf = new JLabel("End Day of Last Year:");
		lblEndDayOf.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(lblEndDayOf, "1, 9, right, default");
		
		textField_year_EDOEY = new JFormattedTextField(format);
		textField_year_EDOEY.setText("");
		panel_years.add(textField_year_EDOEY, "2, 9, left, default");
		textField_year_EDOEY.setColumns(5);
		
		JLabel lblHemisphere = new JLabel("Hemisphere:");
		lblHemisphere.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(lblHemisphere, "1, 11");
		
		chckbx_isNorth = new JCheckBox("North");
		chckbx_isNorth.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_years.add(chckbx_isNorth, "2, 11");
		
		return panel_years;
	}
}

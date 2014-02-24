package explorer;

import java.awt.Component;
import java.awt.Font;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JFormattedTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PROD implements ActionListener{
	private JTable table_prod_canopy;
	private JTable table_prod_VegInterception;
	private JTable table_prod_LitterInterception;
	private JTable table_prod_shade;
	private JTable table_prod_hydraulicRedist;
	
	private JButton btn_prod_monthlyProd_grass;
	private JButton btn_prod_monthlyProd_shrub;
	private JButton btn_prod_monthlyProd_tree;
	private JButton btn_prod_monthlyProd_forb;
	
	private JFormattedTextField formattedTextField_vegComp_grassFrac;
	private JFormattedTextField formattedTextField_vegComp_shrubFrac;
	private JFormattedTextField formattedTextField_vegComp_treeFrac;
	private JFormattedTextField formattedTextField_vegComp_forbFrac;
	private JFormattedTextField formattedTextField_vegComp_bareGroundFrac;
	
	private JFormattedTextField formattedTextField_albedo_grass;
	private JFormattedTextField formattedTextField_albedo_shrub;
	private JFormattedTextField formattedTextField_albedo_tree;
	private JFormattedTextField formattedTextField_albedo_forb;
	private JFormattedTextField formattedTextField_albedo_bareGround;
	
	private JFormattedTextField formattedTextField_cover_grass;
	private JFormattedTextField formattedTextField_cover_shrub;
	private JFormattedTextField formattedTextField_cover_tree;
	private JFormattedTextField formattedTextField_cover_forb;
	
	private JFormattedTextField formattedTextField_estpart_grass;
	private JFormattedTextField formattedTextField_estpart_shrub;
	private JFormattedTextField formattedTextField_estpart_tree;
	private JFormattedTextField formattedTextField_estpart_forb;
	
	private JFormattedTextField formattedTextField_esLimit_grass;
	private JFormattedTextField formattedTextField_esLimit_shrub;
	private JFormattedTextField formattedTextField_esLimit_tree;
	private JFormattedTextField formattedTextField_esLimit_forb;
	
	private JFormattedTextField formattedTextField_swpCrit_grass;
	private JFormattedTextField formattedTextField_swpCrit_shrub;
	private JFormattedTextField formattedTextField_swpCrit_tree;
	private JFormattedTextField formattedTextField_swpCrit_forb;
	
	private soilwat.InputData.ProdIn prodIn;
	
	public PROD(soilwat.InputData.ProdIn prodIn) {
		this.prodIn = prodIn;
	}
	
	public void onGetValues() {
		prodIn.vegComp.grass = ((Number)formattedTextField_vegComp_grassFrac.getValue()).doubleValue();
		prodIn.vegComp.shrub = ((Number)formattedTextField_vegComp_shrubFrac.getValue()).doubleValue();
		prodIn.vegComp.tree = ((Number)formattedTextField_vegComp_treeFrac.getValue()).doubleValue();
		prodIn.vegComp.forb = ((Number)formattedTextField_vegComp_forbFrac.getValue()).doubleValue();
		prodIn.vegComp.bareGround = ((Number)formattedTextField_vegComp_bareGroundFrac.getValue()).doubleValue();
		
		prodIn.albedo.grass = ((Number)formattedTextField_albedo_grass.getValue()).doubleValue();
		prodIn.albedo.shrub = ((Number)formattedTextField_albedo_shrub.getValue()).doubleValue();
		prodIn.albedo.tree = ((Number)formattedTextField_albedo_tree.getValue()).doubleValue();
		prodIn.albedo.forb = ((Number)formattedTextField_albedo_forb.getValue()).doubleValue();
		prodIn.albedo.bareGround = ((Number)formattedTextField_albedo_bareGround.getValue()).doubleValue();
		
		prodIn.albedo.grass = ((Number)formattedTextField_cover_grass.getValue()).doubleValue();
		prodIn.albedo.shrub = ((Number)formattedTextField_cover_shrub.getValue()).doubleValue();
		prodIn.albedo.tree = ((Number)formattedTextField_cover_tree.getValue()).doubleValue();
		prodIn.albedo.forb = ((Number)formattedTextField_cover_forb.getValue()).doubleValue();
		
		prodIn.esTpart.grass = ((Number)formattedTextField_estpart_grass.getValue()).doubleValue();
		prodIn.esTpart.shrub = ((Number)formattedTextField_estpart_shrub.getValue()).doubleValue();
		prodIn.esTpart.tree = ((Number)formattedTextField_estpart_tree.getValue()).doubleValue();
		prodIn.esTpart.forb = ((Number)formattedTextField_estpart_forb.getValue()).doubleValue();
		
		prodIn.esLimit.grass = ((Number)formattedTextField_esLimit_grass.getValue()).doubleValue();
		prodIn.esLimit.shrub = ((Number)formattedTextField_esLimit_shrub.getValue()).doubleValue();
		prodIn.esLimit.tree  = ((Number)formattedTextField_esLimit_tree.getValue()).doubleValue();
		prodIn.esLimit.forb  = ((Number)formattedTextField_esLimit_forb.getValue()).doubleValue();
		
		prodIn.criticalSWP.grass = ((Number)formattedTextField_swpCrit_grass.getValue()).doubleValue();
		prodIn.criticalSWP.shrub = ((Number)formattedTextField_swpCrit_shrub.getValue()).doubleValue();
		prodIn.criticalSWP.tree  = ((Number)formattedTextField_swpCrit_tree.getValue()).doubleValue();
		prodIn.criticalSWP.forb  = ((Number)formattedTextField_swpCrit_forb.getValue()).doubleValue();
		
		prodIn.canopyHeight.grass.xinflec = ((Number)table_prod_canopy.getValueAt(0, 1)).doubleValue();
		prodIn.canopyHeight.grass.yinflec = ((Number)table_prod_canopy.getValueAt(1, 1)).doubleValue();
		prodIn.canopyHeight.grass.range = ((Number)table_prod_canopy.getValueAt(2, 1)).doubleValue();
		prodIn.canopyHeight.grass.slope = ((Number)table_prod_canopy.getValueAt(3, 1)).doubleValue();
		prodIn.canopyHeight.grass.canopyHeight = ((Number)table_prod_canopy.getValueAt(4, 1)).doubleValue();
		prodIn.canopyHeight.shrub.xinflec = ((Number)table_prod_canopy.getValueAt(0, 2)).doubleValue();
		prodIn.canopyHeight.shrub.yinflec = ((Number)table_prod_canopy.getValueAt(1, 2)).doubleValue();
		prodIn.canopyHeight.shrub.range = ((Number)table_prod_canopy.getValueAt(2, 2)).doubleValue();
		prodIn.canopyHeight.shrub.slope = ((Number)table_prod_canopy.getValueAt(3, 2)).doubleValue();
		prodIn.canopyHeight.shrub.canopyHeight = ((Number)table_prod_canopy.getValueAt(4, 2)).doubleValue();
		prodIn.canopyHeight.tree.xinflec = ((Number)table_prod_canopy.getValueAt(0, 3)).doubleValue();
		prodIn.canopyHeight.tree.yinflec = ((Number)table_prod_canopy.getValueAt(1, 3)).doubleValue();
		prodIn.canopyHeight.tree.range = ((Number)table_prod_canopy.getValueAt(2, 3)).doubleValue();
		prodIn.canopyHeight.tree.slope = ((Number)table_prod_canopy.getValueAt(3, 3)).doubleValue();
		prodIn.canopyHeight.tree.canopyHeight = ((Number)table_prod_canopy.getValueAt(4, 3)).doubleValue();
		prodIn.canopyHeight.forb.xinflec = ((Number)table_prod_canopy.getValueAt(0, 4)).doubleValue();
		prodIn.canopyHeight.forb.yinflec = ((Number)table_prod_canopy.getValueAt(1, 4)).doubleValue();
		prodIn.canopyHeight.forb.range = ((Number)table_prod_canopy.getValueAt(2, 4)).doubleValue();
		prodIn.canopyHeight.forb.slope = ((Number)table_prod_canopy.getValueAt(3, 4)).doubleValue();
		prodIn.canopyHeight.forb.canopyHeight = ((Number)table_prod_canopy.getValueAt(4, 4)).doubleValue();

		prodIn.vegIntercParams.grass.a = ((Number)table_prod_VegInterception.getValueAt(0, 1)).doubleValue();
		prodIn.vegIntercParams.grass.b = ((Number)table_prod_VegInterception.getValueAt(1, 1)).doubleValue();
		prodIn.vegIntercParams.grass.c = ((Number)table_prod_VegInterception.getValueAt(2, 1)).doubleValue();
		prodIn.vegIntercParams.grass.d = ((Number)table_prod_VegInterception.getValueAt(3, 1)).doubleValue();
		prodIn.vegIntercParams.shrub.a = ((Number)table_prod_VegInterception.getValueAt(0, 2)).doubleValue();
		prodIn.vegIntercParams.shrub.b = ((Number)table_prod_VegInterception.getValueAt(1, 2)).doubleValue();
		prodIn.vegIntercParams.shrub.c = ((Number)table_prod_VegInterception.getValueAt(2, 2)).doubleValue();
		prodIn.vegIntercParams.shrub.d = ((Number)table_prod_VegInterception.getValueAt(3, 2)).doubleValue();
		prodIn.vegIntercParams.tree.a = ((Number)table_prod_VegInterception.getValueAt(0, 3)).doubleValue();
		prodIn.vegIntercParams.tree.b = ((Number)table_prod_VegInterception.getValueAt(1, 3)).doubleValue();
		prodIn.vegIntercParams.tree.c = ((Number)table_prod_VegInterception.getValueAt(2, 3)).doubleValue();
		prodIn.vegIntercParams.tree.d = ((Number)table_prod_VegInterception.getValueAt(3, 3)).doubleValue();
		prodIn.vegIntercParams.forb.a = ((Number)table_prod_VegInterception.getValueAt(0, 4)).doubleValue();
		prodIn.vegIntercParams.forb.b = ((Number)table_prod_VegInterception.getValueAt(1, 4)).doubleValue();
		prodIn.vegIntercParams.forb.c = ((Number)table_prod_VegInterception.getValueAt(2, 4)).doubleValue();
		prodIn.vegIntercParams.forb.d = ((Number)table_prod_VegInterception.getValueAt(3, 4)).doubleValue();
	
		prodIn.litterIntercParams.grass.a = ((Number)table_prod_LitterInterception.getValueAt(0, 1)).doubleValue();
		prodIn.litterIntercParams.grass.b = ((Number)table_prod_LitterInterception.getValueAt(1, 1)).doubleValue();
		prodIn.litterIntercParams.grass.c = ((Number)table_prod_LitterInterception.getValueAt(2, 1)).doubleValue();
		prodIn.litterIntercParams.grass.d = ((Number)table_prod_LitterInterception.getValueAt(3, 1)).doubleValue();
		prodIn.litterIntercParams.shrub.a = ((Number)table_prod_LitterInterception.getValueAt(0, 2)).doubleValue();
		prodIn.litterIntercParams.shrub.b = ((Number)table_prod_LitterInterception.getValueAt(1, 2)).doubleValue();
		prodIn.litterIntercParams.shrub.c = ((Number)table_prod_LitterInterception.getValueAt(2, 2)).doubleValue();
		prodIn.litterIntercParams.shrub.d = ((Number)table_prod_LitterInterception.getValueAt(3, 2)).doubleValue();
		prodIn.litterIntercParams.tree.a = ((Number)table_prod_LitterInterception.getValueAt(0, 3)).doubleValue();
		prodIn.litterIntercParams.tree.b = ((Number)table_prod_LitterInterception.getValueAt(1, 3)).doubleValue();
		prodIn.litterIntercParams.tree.c = ((Number)table_prod_LitterInterception.getValueAt(2, 3)).doubleValue();
		prodIn.litterIntercParams.tree.d = ((Number)table_prod_LitterInterception.getValueAt(3, 3)).doubleValue();
		prodIn.litterIntercParams.forb.a = ((Number)table_prod_LitterInterception.getValueAt(0, 4)).doubleValue();
		prodIn.litterIntercParams.forb.b = ((Number)table_prod_LitterInterception.getValueAt(1, 4)).doubleValue();
		prodIn.litterIntercParams.forb.c = ((Number)table_prod_LitterInterception.getValueAt(2, 4)).doubleValue();
		prodIn.litterIntercParams.forb.d = ((Number)table_prod_LitterInterception.getValueAt(3, 4)).doubleValue();
		
		prodIn.shade.grass.shadeScale = ((Number)table_prod_shade.getValueAt(0, 1)).doubleValue();
		prodIn.shade.grass.shadeMaximalDeadBiomass = ((Number)table_prod_shade.getValueAt(1, 1)).doubleValue();
		prodIn.shade.grass.xinflec = ((Number)table_prod_shade.getValueAt(2, 1)).doubleValue();
		prodIn.shade.grass.yinflec = ((Number)table_prod_shade.getValueAt(3, 1)).doubleValue();
		prodIn.shade.grass.range = ((Number)table_prod_shade.getValueAt(4, 1)).doubleValue();
		prodIn.shade.grass.slope = ((Number)table_prod_shade.getValueAt(5, 1)).doubleValue();
		prodIn.shade.shrub.shadeScale = ((Number)table_prod_shade.getValueAt(0, 2)).doubleValue();
		prodIn.shade.shrub.shadeMaximalDeadBiomass = ((Number)table_prod_shade.getValueAt(1, 2)).doubleValue();
		prodIn.shade.shrub.xinflec = ((Number)table_prod_shade.getValueAt(2, 2)).doubleValue();
		prodIn.shade.shrub.yinflec = ((Number)table_prod_shade.getValueAt(3, 2)).doubleValue();
		prodIn.shade.shrub.range = ((Number)table_prod_shade.getValueAt(4, 2)).doubleValue();
		prodIn.shade.shrub.slope = ((Number)table_prod_shade.getValueAt(5, 2)).doubleValue();
		prodIn.shade.tree.shadeScale = ((Number)table_prod_shade.getValueAt(0, 3)).doubleValue();
		prodIn.shade.tree.shadeMaximalDeadBiomass = ((Number)table_prod_shade.getValueAt(1, 3)).doubleValue();
		prodIn.shade.tree.xinflec = ((Number)table_prod_shade.getValueAt(2, 3)).doubleValue();
		prodIn.shade.tree.yinflec = ((Number)table_prod_shade.getValueAt(3, 3)).doubleValue();
		prodIn.shade.tree.range = ((Number)table_prod_shade.getValueAt(4, 3)).doubleValue();
		prodIn.shade.tree.slope = ((Number)table_prod_shade.getValueAt(5, 3)).doubleValue();
		prodIn.shade.forb.shadeScale = ((Number)table_prod_shade.getValueAt(0, 4)).doubleValue();
		prodIn.shade.forb.shadeMaximalDeadBiomass = ((Number)table_prod_shade.getValueAt(1, 4)).doubleValue();
		prodIn.shade.forb.xinflec = ((Number)table_prod_shade.getValueAt(2, 4)).doubleValue();
		prodIn.shade.forb.yinflec = ((Number)table_prod_shade.getValueAt(3, 4)).doubleValue();
		prodIn.shade.forb.range = ((Number)table_prod_shade.getValueAt(4, 4)).doubleValue();
		prodIn.shade.forb.slope = ((Number)table_prod_shade.getValueAt(5, 4)).doubleValue();
		
		prodIn.hydraulicRedist.grass.flag = ((Boolean)table_prod_hydraulicRedist.getValueAt(0, 1)).booleanValue();
		prodIn.hydraulicRedist.grass.maxCondRoot = ((Number)table_prod_hydraulicRedist.getValueAt(0, 2)).doubleValue();
		prodIn.hydraulicRedist.grass.swp50 = ((Number)table_prod_hydraulicRedist.getValueAt(0, 3)).doubleValue();
		prodIn.hydraulicRedist.grass.shapeCond = ((Number)table_prod_hydraulicRedist.getValueAt(0, 4)).doubleValue();
		prodIn.hydraulicRedist.shrub.flag = ((Boolean)table_prod_hydraulicRedist.getValueAt(1, 1)).booleanValue();
		prodIn.hydraulicRedist.shrub.maxCondRoot = ((Number)table_prod_hydraulicRedist.getValueAt(1, 2)).doubleValue();
		prodIn.hydraulicRedist.shrub.swp50 = ((Number)table_prod_hydraulicRedist.getValueAt(1, 3)).doubleValue();
		prodIn.hydraulicRedist.shrub.shapeCond = ((Number)table_prod_hydraulicRedist.getValueAt(1, 4)).doubleValue();
		prodIn.hydraulicRedist.tree.flag = ((Boolean)table_prod_hydraulicRedist.getValueAt(2, 1)).booleanValue();
		prodIn.hydraulicRedist.tree.maxCondRoot = ((Number)table_prod_hydraulicRedist.getValueAt(2, 2)).doubleValue();
		prodIn.hydraulicRedist.tree.swp50 = ((Number)table_prod_hydraulicRedist.getValueAt(2, 3)).doubleValue();
		prodIn.hydraulicRedist.tree.shapeCond = ((Number)table_prod_hydraulicRedist.getValueAt(2, 4)).doubleValue();
		prodIn.hydraulicRedist.forb.flag = ((Boolean)table_prod_hydraulicRedist.getValueAt(3, 1)).booleanValue();
		prodIn.hydraulicRedist.forb.maxCondRoot = ((Number)table_prod_hydraulicRedist.getValueAt(3, 2)).doubleValue();
		prodIn.hydraulicRedist.forb.swp50 = ((Number)table_prod_hydraulicRedist.getValueAt(3, 3)).doubleValue();
		prodIn.hydraulicRedist.forb.shapeCond = ((Number)table_prod_hydraulicRedist.getValueAt(3, 4)).doubleValue();
	}
	
	public void onSetValues() {
		formattedTextField_vegComp_grassFrac.setValue(prodIn.vegComp.grass);
		formattedTextField_vegComp_shrubFrac.setValue(prodIn.vegComp.shrub);
		formattedTextField_vegComp_treeFrac.setValue(prodIn.vegComp.tree);
		formattedTextField_vegComp_forbFrac.setValue(prodIn.vegComp.forb);
		formattedTextField_vegComp_bareGroundFrac.setValue(prodIn.vegComp.bareGround);
		
		formattedTextField_albedo_grass.setValue(prodIn.albedo.grass);
		formattedTextField_albedo_shrub.setValue(prodIn.albedo.shrub);
		formattedTextField_albedo_tree.setValue(prodIn.albedo.tree);
		formattedTextField_albedo_forb.setValue(prodIn.albedo.forb);
		formattedTextField_albedo_bareGround.setValue(prodIn.albedo.bareGround);
		
		formattedTextField_cover_grass.setValue(prodIn.albedo.grass);
		formattedTextField_cover_shrub.setValue(prodIn.albedo.shrub);
		formattedTextField_cover_tree.setValue(prodIn.albedo.tree);
		formattedTextField_cover_forb.setValue(prodIn.albedo.forb);
		
		formattedTextField_estpart_grass.setValue(prodIn.esTpart.grass);
		formattedTextField_estpart_shrub.setValue(prodIn.esTpart.shrub);
		formattedTextField_estpart_tree.setValue(prodIn.esTpart.tree);
		formattedTextField_estpart_forb.setValue(prodIn.esTpart.forb);
		
		formattedTextField_esLimit_grass.setValue(prodIn.esLimit.grass);
		formattedTextField_esLimit_shrub.setValue(prodIn.esLimit.shrub);
		formattedTextField_esLimit_tree.setValue(prodIn.esLimit.tree);
		formattedTextField_esLimit_forb.setValue(prodIn.esLimit.forb);
		
		formattedTextField_swpCrit_grass.setValue(prodIn.criticalSWP.grass);
		formattedTextField_swpCrit_shrub.setValue(prodIn.criticalSWP.shrub);
		formattedTextField_swpCrit_tree.setValue(prodIn.criticalSWP.tree);
		formattedTextField_swpCrit_forb.setValue(prodIn.criticalSWP.forb);
		
		table_prod_canopy.setValueAt(prodIn.canopyHeight.grass.xinflec, 0, 1);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.grass.yinflec, 1, 1);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.grass.range, 2, 1);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.grass.slope, 3, 1);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.grass.canopyHeight, 4, 1);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.shrub.xinflec, 0, 2);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.shrub.yinflec, 1, 2);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.shrub.range, 2, 2);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.shrub.slope, 3, 2);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.shrub.canopyHeight, 4, 2);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.tree.xinflec, 0, 3);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.tree.yinflec, 1, 3);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.tree.range, 2, 3);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.tree.slope, 3, 3);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.tree.canopyHeight, 4, 3);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.forb.xinflec, 0, 4);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.forb.yinflec, 1, 4);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.forb.range, 2, 4);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.forb.slope, 3, 4);
		table_prod_canopy.setValueAt(prodIn.canopyHeight.forb.canopyHeight, 4, 4);
		
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.grass.a, 0, 1);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.grass.b, 1, 1);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.grass.c, 2, 1);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.grass.d, 3, 1);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.shrub.a, 0, 2);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.shrub.b, 1, 2);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.shrub.c, 2, 2);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.shrub.d, 3, 2);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.tree.a, 0, 3);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.tree.b, 1, 3);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.tree.c, 2, 3);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.tree.d, 3, 3);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.forb.a, 0, 4);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.forb.b, 1, 4);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.forb.c, 2, 4);
		table_prod_VegInterception.setValueAt(prodIn.vegIntercParams.forb.d, 3, 4);
		
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.grass.a, 0, 1);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.grass.b, 1, 1);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.grass.c, 2, 1);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.grass.d, 3, 1);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.shrub.a, 0, 2);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.shrub.b, 1, 2);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.shrub.c, 2, 2);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.shrub.d, 3, 2);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.tree.a, 0, 3);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.tree.b, 1, 3);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.tree.c, 2, 3);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.tree.d, 3, 3);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.forb.a, 0, 4);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.forb.b, 1, 4);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.forb.c, 2, 4);
		table_prod_LitterInterception.setValueAt(prodIn.litterIntercParams.forb.d, 3, 4);
		
		table_prod_shade.setValueAt(prodIn.shade.grass.shadeScale, 0, 1);
		table_prod_shade.setValueAt(prodIn.shade.grass.shadeMaximalDeadBiomass, 1, 1);
		table_prod_shade.setValueAt(prodIn.shade.grass.xinflec, 2, 1);
		table_prod_shade.setValueAt(prodIn.shade.grass.yinflec, 3, 1);
		table_prod_shade.setValueAt(prodIn.shade.grass.range, 4, 1);
		table_prod_shade.setValueAt(prodIn.shade.grass.slope, 5, 1);
		table_prod_shade.setValueAt(prodIn.shade.shrub.shadeScale, 0, 2);
		table_prod_shade.setValueAt(prodIn.shade.shrub.shadeMaximalDeadBiomass, 1, 2);
		table_prod_shade.setValueAt(prodIn.shade.shrub.xinflec, 2, 2);
		table_prod_shade.setValueAt(prodIn.shade.shrub.yinflec, 3, 2);
		table_prod_shade.setValueAt(prodIn.shade.shrub.range, 4, 2);
		table_prod_shade.setValueAt(prodIn.shade.shrub.slope, 5, 2);
		table_prod_shade.setValueAt(prodIn.shade.tree.shadeScale, 0, 3);
		table_prod_shade.setValueAt(prodIn.shade.tree.shadeMaximalDeadBiomass, 1,3);
		table_prod_shade.setValueAt(prodIn.shade.tree.xinflec, 2, 3);
		table_prod_shade.setValueAt(prodIn.shade.tree.yinflec, 3, 3);
		table_prod_shade.setValueAt(prodIn.shade.tree.range, 4, 3);
		table_prod_shade.setValueAt(prodIn.shade.tree.slope, 5, 3);
		table_prod_shade.setValueAt(prodIn.shade.forb.shadeScale, 0, 4);
		table_prod_shade.setValueAt(prodIn.shade.forb.shadeMaximalDeadBiomass, 1, 4);
		table_prod_shade.setValueAt(prodIn.shade.forb.xinflec, 2, 4);
		table_prod_shade.setValueAt(prodIn.shade.forb.yinflec, 3, 4);
		table_prod_shade.setValueAt(prodIn.shade.forb.range, 4, 4);
		table_prod_shade.setValueAt(prodIn.shade.forb.slope, 5, 4);
		
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.grass.flag, 0, 1);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.grass.maxCondRoot, 0, 2);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.grass.swp50, 0, 3);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.grass.shapeCond, 0, 4);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.shrub.flag, 1, 1);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.shrub.maxCondRoot, 1, 2);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.shrub.swp50, 1, 3);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.shrub.shapeCond, 1, 4);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.tree.flag, 2, 1);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.tree.maxCondRoot, 2, 2);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.tree.swp50, 2, 3);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.tree.shapeCond, 2, 4);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.forb.flag, 3, 1);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.forb.maxCondRoot, 3, 2);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.forb.swp50, 3, 3);
		table_prod_hydraulicRedist.setValueAt(prodIn.hydraulicRedist.forb.shapeCond, 3, 4);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel onGetPanel_prod() {
		Format format = NumberFormat.getNumberInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		JPanel panel_prod = new JPanel();
		panel_prod.setLayout(new BoxLayout(panel_prod, BoxLayout.X_AXIS));
		
		JPanel panel_column1 = new JPanel();
		panel_prod.add(panel_column1);
		panel_column1.setLayout(new BoxLayout(panel_column1, BoxLayout.PAGE_AXIS));
		
		JPanel panel_Composition = new JPanel();
		panel_column1.add(panel_Composition);
		panel_Composition.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblCompositionOfVegetation = new JLabel("Composition of Vegetation Type Compenents");
		panel_Composition.add(lblCompositionOfVegetation,"1, 1, 5, 1, center, default");
		
		JLabel lbl_prod_comp_Grasses = new JLabel("Grasses");
		lbl_prod_comp_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Composition.add(lbl_prod_comp_Grasses, "1, 3");
		
		JLabel lbl_prod_comp_Shrubs = new JLabel("Shrubs");
		lbl_prod_comp_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Composition.add(lbl_prod_comp_Shrubs, "2, 3");
		
		JLabel lbl_prod_comp_Trees = new JLabel("Trees");
		lbl_prod_comp_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Composition.add(lbl_prod_comp_Trees, "3, 3");
		
		JLabel lbl_prod_comp_Forbs = new JLabel("Forbs");
		lbl_prod_comp_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Composition.add(lbl_prod_comp_Forbs, "4, 3");
		
		JLabel lbl_prod_comp_BareGround = new JLabel("Bare Ground");
		lbl_prod_comp_BareGround.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Composition.add(lbl_prod_comp_BareGround, "5, 3");
		
		formattedTextField_vegComp_grassFrac = new JFormattedTextField(format);
		formattedTextField_vegComp_grassFrac.setColumns(10);
		panel_Composition.add(formattedTextField_vegComp_grassFrac, "1, 5, fill, default");
		
		formattedTextField_vegComp_shrubFrac = new JFormattedTextField();
		formattedTextField_vegComp_shrubFrac.setColumns(10);
		panel_Composition.add(formattedTextField_vegComp_shrubFrac, "2, 5, fill, default");
		
		formattedTextField_vegComp_treeFrac = new JFormattedTextField(format);
		formattedTextField_vegComp_treeFrac.setColumns(10);
		panel_Composition.add(formattedTextField_vegComp_treeFrac, "3, 5, fill, default");
		
		formattedTextField_vegComp_forbFrac = new JFormattedTextField(format);
		formattedTextField_vegComp_forbFrac.setColumns(10);
		panel_Composition.add(formattedTextField_vegComp_forbFrac, "4, 5, fill, default");
		
		formattedTextField_vegComp_bareGroundFrac = new JFormattedTextField(format);
		formattedTextField_vegComp_bareGroundFrac.setColumns(10);
		panel_Composition.add(formattedTextField_vegComp_bareGroundFrac, "5, 5, fill, default");
		
		JPanel panel_Albedo = new JPanel();
		panel_column1.add(panel_Albedo);
		panel_Albedo.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblAlbedo = new JLabel("Albedo");
		panel_Albedo.add(lblAlbedo,"1, 1, 5, 1, center, default");
		
		JLabel lbl_prod_albedo_Grasses = new JLabel("Grasses");
		lbl_prod_albedo_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Albedo.add(lbl_prod_albedo_Grasses, "1, 3");
		
		JLabel lbl_prod_albedo_Shrubs = new JLabel("Shrubs");
		lbl_prod_albedo_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Albedo.add(lbl_prod_albedo_Shrubs, "2, 3");
		
		JLabel lbl_prod_albedo_Trees = new JLabel("Trees");
		lbl_prod_albedo_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Albedo.add(lbl_prod_albedo_Trees, "3, 3");
		
		JLabel lbl_prod_albedo_Forbs = new JLabel("Forbs");
		lbl_prod_albedo_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Albedo.add(lbl_prod_albedo_Forbs, "4, 3");
		
		JLabel lbl_prod_albedo_BareGround = new JLabel("Bare Ground");
		lbl_prod_albedo_BareGround.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Albedo.add(lbl_prod_albedo_BareGround, "5, 3");
		
		formattedTextField_albedo_grass = new JFormattedTextField(format);
		formattedTextField_albedo_grass.setColumns(10);
		panel_Albedo.add(formattedTextField_albedo_grass, "1, 5, fill, default");
		
		formattedTextField_albedo_shrub = new JFormattedTextField(format);
		formattedTextField_albedo_shrub.setColumns(10);
		panel_Albedo.add(formattedTextField_albedo_shrub, "2, 5, fill, default");
		
		formattedTextField_albedo_tree = new JFormattedTextField(format);
		formattedTextField_albedo_tree.setColumns(10);
		panel_Albedo.add(formattedTextField_albedo_tree, "3, 5, fill, default");
		
		formattedTextField_albedo_forb = new JFormattedTextField(format);
		formattedTextField_albedo_forb.setColumns(10);
		panel_Albedo.add(formattedTextField_albedo_forb, "4, 5, fill, default");
		
		formattedTextField_albedo_bareGround = new JFormattedTextField(format);
		formattedTextField_albedo_bareGround.setColumns(10);
		panel_Albedo.add(formattedTextField_albedo_bareGround, "5, 5, fill, default");
		
		JPanel panel_Canopy = new JPanel();
		panel_column1.add(panel_Canopy);
		
		table_prod_canopy = new JTable();
		table_prod_canopy.setModel(new DefaultTableModel(
			new Object[][] {
				{"xinflec: ", null, null, null, null},
				{"yinflec: ", null, null, null, null},
				{"range: ", null, null, null, null},
				{"slope:", null, null, null, null},
				{"Canopy Height: ", null, null, null, null},
			},
			new String[] {
				"label", "Grasses", "Shrubs", "Trees", "Forbs"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3189294466837951919L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Float.class, Float.class, Float.class, Float.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_prod_canopy.getColumnModel().getColumn(0).setResizable(false);
		table_prod_canopy.getColumnModel().getColumn(0).setPreferredWidth(105);
		table_prod_canopy.getColumnModel().getColumn(0).setMaxWidth(105);
		panel_Canopy.setLayout(new BoxLayout(panel_Canopy, BoxLayout.PAGE_AXIS));
		
		JLabel lblCanopyHeightcm = new JLabel("Canopy Height (cm) Parameters");
		lblCanopyHeightcm.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_Canopy.add(lblCanopyHeightcm);
		panel_Canopy.add(table_prod_canopy);
		panel_Canopy.add(new JScrollPane(table_prod_canopy));
		
		JPanel panel_VegInterception = new JPanel();
		panel_VegInterception.setLayout(new BoxLayout(panel_VegInterception, BoxLayout.PAGE_AXIS));
		panel_column1.add(panel_VegInterception);
		
		table_prod_VegInterception = new JTable();
		table_prod_VegInterception.setModel(new DefaultTableModel(
			new Object[][] {
				{"a: ", null, null, null, null},
				{"b: ", null, null, null, null},
				{"c: ", null, null, null, null},
				{"d:", null, null, null, null},
			},
			new String[] {
				"label", "Grasses", "Shrubs", "Trees", "Forbs"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5953329468888953419L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Float.class, Float.class, Float.class, Float.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_prod_VegInterception.getColumnModel().getColumn(0).setResizable(false);
		table_prod_VegInterception.getColumnModel().getColumn(0).setPreferredWidth(35);
		table_prod_VegInterception.getColumnModel().getColumn(0).setMaxWidth(35);
		
		JLabel lblVegInterception = new JLabel("Vegetation Interception Parameters");
		lblVegInterception.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_VegInterception.add(lblVegInterception);
		panel_VegInterception.add(table_prod_VegInterception);
		panel_VegInterception.add(new JScrollPane(table_prod_VegInterception));
		////
		JPanel panel_LitterInterception = new JPanel();
		panel_LitterInterception.setLayout(new BoxLayout(panel_LitterInterception, BoxLayout.PAGE_AXIS));
		panel_column1.add(panel_LitterInterception);
		
		table_prod_LitterInterception = new JTable();
		table_prod_LitterInterception.setModel(new DefaultTableModel(
			new Object[][] {
				{"a: ", null, null, null, null},
				{"b: ", null, null, null, null},
				{"c: ", null, null, null, null},
				{"d:", null, null, null, null},
			},
			new String[] {
				"label", "Grasses", "Shrubs", "Trees", "Forbs"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2042673330597335391L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Float.class, Float.class, Float.class, Float.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_prod_LitterInterception.getColumnModel().getColumn(0).setResizable(false);
		table_prod_LitterInterception.getColumnModel().getColumn(0).setPreferredWidth(35);
		table_prod_LitterInterception.getColumnModel().getColumn(0).setMaxWidth(35);
		
		JLabel lblLitterInterception = new JLabel("Litter Interception Parameters");
		lblLitterInterception.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_LitterInterception.add(lblLitterInterception);
		panel_LitterInterception.add(table_prod_LitterInterception);
		panel_LitterInterception.add(new JScrollPane(table_prod_LitterInterception));
		
		JPanel panel_hydraulicRedist = new JPanel();
		panel_column1.add(panel_hydraulicRedist);
		panel_hydraulicRedist.setLayout(new BoxLayout(panel_hydraulicRedist, BoxLayout.PAGE_AXIS));
		
		JLabel lblHydraulicRedistribution = new JLabel("Hydraulic Redistribution");
		lblHydraulicRedistribution.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_hydraulicRedist.add(lblHydraulicRedistribution);
		
		table_prod_hydraulicRedist = new JTable();
		table_prod_hydraulicRedist.setModel(new DefaultTableModel(
			new Object[][] {
				{"Grasses:", null, null, null, null},
				{"Shrubs:", null, null, null, null},
				{"Trees:", null, null, null, null},
				{"Forbs:", null, null, null, null},
			},
			new String[] {
				"Type", "flag", "maxCondroot", "swp50", "ShapeCond"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4753338599549758927L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				Object.class, Boolean.class, Double.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_prod_hydraulicRedist.getColumnModel().getColumn(0).setResizable(false);
		table_prod_hydraulicRedist.getColumnModel().getColumn(0).setPreferredWidth(60);
		table_prod_hydraulicRedist.getColumnModel().getColumn(0).setMaxWidth(60);
		table_prod_hydraulicRedist.getColumnModel().getColumn(1).setPreferredWidth(35);
		table_prod_hydraulicRedist.getColumnModel().getColumn(2).setPreferredWidth(95);
		panel_hydraulicRedist.add(table_prod_hydraulicRedist);
		panel_hydraulicRedist.add(new JScrollPane(table_prod_hydraulicRedist));
		
		JPanel panel_column2 = new JPanel();
		panel_prod.add(panel_column2);
		panel_column2.setLayout(new BoxLayout(panel_column2, BoxLayout.PAGE_AXIS));
		
		JPanel panel_Cover = new JPanel();
		panel_column2.add(panel_Cover);
		panel_Cover.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblCover = new JLabel("% Cover");
		panel_Cover.add(lblCover,"1, 1, 4, 1, center, default");
		
		JLabel lbl_prod_cover_Grasses = new JLabel("Grasses");
		lbl_prod_cover_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Cover.add(lbl_prod_cover_Grasses, "1, 3");
		
		JLabel lbl_prod_cover_Shrubs = new JLabel("Shrubs");
		lbl_prod_cover_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Cover.add(lbl_prod_cover_Shrubs, "2, 3");
		
		JLabel lbl_prod_cover_Trees = new JLabel("Trees");
		lbl_prod_cover_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Cover.add(lbl_prod_cover_Trees, "3, 3");
		
		JLabel lbl_prod_cover_Forbs = new JLabel("Forbs");
		lbl_prod_cover_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_Cover.add(lbl_prod_cover_Forbs, "4, 3");
		lbl_prod_cover_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbl_prod_cover_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbl_prod_cover_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbl_prod_cover_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		formattedTextField_cover_grass = new JFormattedTextField(format);
		formattedTextField_cover_grass.setColumns(10);
		panel_Cover.add(formattedTextField_cover_grass, "1, 5, fill, default");
		
		formattedTextField_cover_shrub = new JFormattedTextField(format);
		formattedTextField_cover_shrub.setColumns(10);
		panel_Cover.add(formattedTextField_cover_shrub, "2, 5, fill, default");
		
		formattedTextField_cover_tree = new JFormattedTextField(format);
		formattedTextField_cover_tree.setColumns(10);
		panel_Cover.add(formattedTextField_cover_tree, "3, 5, fill, default");
		
		formattedTextField_cover_forb = new JFormattedTextField(format);
		formattedTextField_cover_forb.setColumns(10);
		panel_Cover.add(formattedTextField_cover_forb, "4, 5, fill, default");
		
		JPanel panel_EsTPartitioning = new JPanel();
		panel_column2.add(panel_EsTPartitioning);
		panel_EsTPartitioning.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblEsTPartitioning = new JLabel("Parameter for Part of Bare-Soil Evap and Transpiration");
		panel_EsTPartitioning.add(lblEsTPartitioning,"1, 1, 4, 1, center, default");
		
		JLabel lbl_prod_estpart_Grasses = new JLabel("Grasses");
		lbl_prod_estpart_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_EsTPartitioning.add(lbl_prod_estpart_Grasses, "1, 3");
		
		JLabel lbl_prod_estpart_Shrubs = new JLabel("Shrubs");
		lbl_prod_estpart_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_EsTPartitioning.add(lbl_prod_estpart_Shrubs, "2, 3");
		
		JLabel lbl_prod_estpart_Trees = new JLabel("Trees");
		lbl_prod_estpart_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_EsTPartitioning.add(lbl_prod_estpart_Trees, "3, 3");
		
		JLabel lbl_prod_estpart_Forbs = new JLabel("Forbs");
		lbl_prod_estpart_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_EsTPartitioning.add(lbl_prod_estpart_Forbs, "4, 3");
		
		formattedTextField_estpart_grass = new JFormattedTextField(format);
		formattedTextField_estpart_grass.setColumns(10);
		panel_EsTPartitioning.add(formattedTextField_estpart_grass, "1, 5, fill, default");
		
		formattedTextField_estpart_shrub = new JFormattedTextField(format);
		formattedTextField_estpart_shrub.setColumns(10);
		panel_EsTPartitioning.add(formattedTextField_estpart_shrub, "2, 5, fill, default");
		
		formattedTextField_estpart_tree = new JFormattedTextField(format);
		formattedTextField_estpart_tree.setColumns(10);
		panel_EsTPartitioning.add(formattedTextField_estpart_tree, "3, 5, fill, default");
		
		formattedTextField_estpart_forb = new JFormattedTextField(format);
		formattedTextField_estpart_forb.setColumns(10);
		panel_EsTPartitioning.add(formattedTextField_estpart_forb, "4, 5, fill, default");
		
		JPanel panel_esLimit = new JPanel();
		panel_column2.add(panel_esLimit);
		panel_esLimit.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblParameterForScaling = new JLabel("Parameter for Scaling and Limiting Bare Soil Evaporation Rate");
		panel_esLimit.add(lblParameterForScaling, "1, 1, 4, 1");
		
		JLabel lbl_prod_esLimit_Grasses = new JLabel("Grasses");
		lbl_prod_esLimit_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_esLimit.add(lbl_prod_esLimit_Grasses, "1, 3");
		
		JLabel lbl_prod_esLimit_Shrubs = new JLabel("Shrubs");
		lbl_prod_esLimit_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_esLimit.add(lbl_prod_esLimit_Shrubs, "2, 3");
		
		JLabel lbl_prod_esLimit_Trees = new JLabel("Trees");
		lbl_prod_esLimit_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_esLimit.add(lbl_prod_esLimit_Trees, "3, 3");
		
		JLabel lbl_prod_esLimit_Forbs = new JLabel("Forbs");
		lbl_prod_esLimit_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_esLimit.add(lbl_prod_esLimit_Forbs, "4, 3");
			
		formattedTextField_esLimit_grass = new JFormattedTextField(format);
		formattedTextField_esLimit_grass.setColumns(10);
		panel_esLimit.add(formattedTextField_esLimit_grass, "1, 5, fill, default");
		
		formattedTextField_esLimit_shrub = new JFormattedTextField(format);
		formattedTextField_esLimit_shrub.setColumns(10);
		panel_esLimit.add(formattedTextField_esLimit_shrub, "2, 5, fill, default");
		
		formattedTextField_esLimit_tree = new JFormattedTextField(format);
		formattedTextField_esLimit_tree.setColumns(10);
		panel_esLimit.add(formattedTextField_esLimit_tree, "3, 5, fill, default");
		
		formattedTextField_esLimit_forb = new JFormattedTextField(format);
		formattedTextField_esLimit_forb.setColumns(10);
		panel_esLimit.add(formattedTextField_esLimit_forb, "4, 5, fill, default");
		
		JPanel panel_CriticalSWP = new JPanel();
		panel_column2.add(panel_CriticalSWP);
		panel_CriticalSWP.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),
				ColumnSpec.decode("left:default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblCriticalSoilWater = new JLabel("Critical Soil Water Potential");
		panel_CriticalSWP.add(lblCriticalSoilWater, "1, 1, 4, 1, center, default");
		
		JLabel lbl_prod_critswp_Grasses = new JLabel("Grasses");
		lbl_prod_critswp_Grasses.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_CriticalSWP.add(lbl_prod_critswp_Grasses, "1, 3");
		
		JLabel lbl_prod_critswp_Shrubs = new JLabel("Shrubs");
		lbl_prod_critswp_Shrubs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_CriticalSWP.add(lbl_prod_critswp_Shrubs, "2, 3");
		
		JLabel lbl_prod_critswp_Trees = new JLabel("Trees");
		lbl_prod_critswp_Trees.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_CriticalSWP.add(lbl_prod_critswp_Trees, "3, 3");
		
		JLabel lbl_prod_critswp_Forbs = new JLabel("Forbs");
		lbl_prod_critswp_Forbs.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_CriticalSWP.add(lbl_prod_critswp_Forbs, "4, 3");
		
		formattedTextField_swpCrit_grass = new JFormattedTextField(format);
		formattedTextField_swpCrit_grass.setColumns(10);
		panel_CriticalSWP.add(formattedTextField_swpCrit_grass, "1, 5, fill, default");
		
		formattedTextField_swpCrit_shrub = new JFormattedTextField(format);
		formattedTextField_swpCrit_shrub.setColumns(10);
		panel_CriticalSWP.add(formattedTextField_swpCrit_shrub, "2, 5, fill, default");
		
		formattedTextField_swpCrit_tree = new JFormattedTextField(format);
		formattedTextField_swpCrit_tree.setColumns(10);
		panel_CriticalSWP.add(formattedTextField_swpCrit_tree, "3, 5, fill, default");
		
		formattedTextField_swpCrit_forb = new JFormattedTextField(format);
		formattedTextField_swpCrit_forb.setColumns(10);
		panel_CriticalSWP.add(formattedTextField_swpCrit_forb, "4, 5, fill, default");
		
		JPanel panel_Shade = new JPanel();
		panel_Shade.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_column2.add(panel_Shade);
		panel_Shade.setLayout(new BoxLayout(panel_Shade, BoxLayout.PAGE_AXIS));
		
		JLabel lblShadeEffectsOn = new JLabel("Shade Effects on Transpiration");
		lblShadeEffectsOn.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_Shade.add(lblShadeEffectsOn);
		
		table_prod_shade = new JTable();
		table_prod_shade.setModel(new DefaultTableModel(
			new Object[][] {
				{"Shade Scale:", null, null, null, null},
				{"Shade Maximal Dead Biomass:", null, null, null, null},
				{"tanfunc: xinflec", null, null, null, null},
				{"yinflec", null, null, null, null},
				{"range", null, null, null, null},
				{"slope", null, null, null, null},
			},
			new String[] {
				"label", "Grasses", "Shrubs", "Trees", "Forbs"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1988020503002233535L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_prod_shade.getColumnModel().getColumn(0).setResizable(false);
		table_prod_shade.getColumnModel().getColumn(0).setPreferredWidth(105);
		table_prod_shade.getColumnModel().getColumn(0).setMaxWidth(105);
		panel_Shade.add(table_prod_shade);
		JScrollPane scrollPane = new JScrollPane(table_prod_shade);
		panel_Shade.add(scrollPane);
		
		JPanel panel_monthlyProd = new JPanel();
		panel_column2.add(panel_monthlyProd);
		panel_monthlyProd.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("left:default"),},
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
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		JLabel lblMonthlyProductionValues = new JLabel("Monthly Production Values");
		panel_monthlyProd.add(lblMonthlyProductionValues, "1, 1, 4, 1, left, center");
		
		JLabel lblLitter = new JLabel("Litter - dead leafy material on the ground (g/m^2).");
		lblLitter.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_monthlyProd.add(lblLitter, "1, 3, 4, 1, left, center");
		
		JLabel lblBiomassLiving = new JLabel("Biomass - Living and dead/woody aboveground standing biomass (g/m^2).");
		lblBiomassLiving.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_monthlyProd.add(lblBiomassLiving, "1, 5, 4, 1, left, center");
		
		JLabel lblliveProportion = new JLabel("%Live - Proportion of Biomass that is actually living (0-1.0).");
		lblliveProportion.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_monthlyProd.add(lblliveProportion, "1, 7, 4, 1, left, center");
		
		JLabel lblLaiconvMonthly = new JLabel("LAI_conv - Monthly amount of biomass needed to produce LAI=1.0 (g/m^2).");
		lblLaiconvMonthly.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_monthlyProd.add(lblLaiconvMonthly, "1, 9, 4, 1, left, center");
		
		btn_prod_monthlyProd_grass = new JButton("Grasslands");
		btn_prod_monthlyProd_grass.addActionListener(this);
		panel_monthlyProd.add(btn_prod_monthlyProd_grass, "1, 11");
		
		btn_prod_monthlyProd_shrub = new JButton("Shrublands");
		btn_prod_monthlyProd_shrub.addActionListener(this);
		panel_monthlyProd.add(btn_prod_monthlyProd_shrub, "2, 11");
		
		btn_prod_monthlyProd_tree = new JButton("Forestlands");
		btn_prod_monthlyProd_tree.addActionListener(this);
		panel_monthlyProd.add(btn_prod_monthlyProd_tree, "3, 11");
		
		btn_prod_monthlyProd_forb = new JButton("Forblands");
		btn_prod_monthlyProd_forb.addActionListener(this);
		panel_monthlyProd.add(btn_prod_monthlyProd_forb, "4, 11");
		
		return panel_prod;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		PROD_MONTHLY_BIOMASS biomass = null;
	    if (src == btn_prod_monthlyProd_grass) {
	    	biomass = new PROD_MONTHLY_BIOMASS(this.prodIn.monthlyProd.grass, "Grasslands");
	    } else if (src == btn_prod_monthlyProd_shrub) {
	    	biomass = new PROD_MONTHLY_BIOMASS(this.prodIn.monthlyProd.shrub, "Shrublands");
	    } else if (src == btn_prod_monthlyProd_tree) {
	    	biomass = new PROD_MONTHLY_BIOMASS(this.prodIn.monthlyProd.tree, "Forestlands");
	    }  else if (src == btn_prod_monthlyProd_forb) {
	    	biomass = new PROD_MONTHLY_BIOMASS(this.prodIn.monthlyProd.forb, "Forblands");
	    }
	    biomass.pack();
	    biomass.setVisible(true);
	}
}

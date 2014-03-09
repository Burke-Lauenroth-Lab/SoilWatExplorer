package explorer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import soilwat.InputData;
import soilwat.SW_CONTROL;

public class OutputExplorer extends JFrame {
	private static final long serialVersionUID = 1L;
	List<OUTDATA> runs = new ArrayList<OUTDATA>();
	
	public OutputExplorer() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 827, 513);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
	
	public void addOutput(SW_CONTROL control, InputData.OutputIn out) {
		//OUTDATA output = new OUTDATA(control, out);
		//this.setContentPane(output);
		//this.setVisible(true);
		//this.pack();
	}
}

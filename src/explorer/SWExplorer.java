package explorer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import soilwat.InputData;
import soilwat.SW_CONTROL;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import events.SoilwatEvent;
import events.SoilwatListener;

public class SWExplorer implements ActionListener, MenuListener{
	
	private static class RunModel implements Runnable {
		
		private SoilWat model;
		
		public RunModel(SoilWat model) {
			this.model = model;
		}
		public void run() {
			model.onSave(null);
			model.onRun();
		}
	}
	
	public class SoilWat implements SoilwatListener, ChangeListener {
		private soilwat.InputData inputData;
		private soilwat.SW_CONTROL control = new SW_CONTROL();
	
		private String ProjectName;
		
		private FILES files;
		private MODEL model;
		private SITE site;
		private SOILS soils;
		private PROD prod;
		private ESTABL estab;
		private CLOUD cloud;
		private WEATHER weather;
		private MARKOV markov;
		private SWC swc;
		private OUTPUT out;
		private OUTDATA output;
		
		private JTabbedPane tabbedPane;
		
		public SoilWat(String ProjectName, boolean defaultData, String filesIn) {
			this.control.addSoilwatEventListener(this);
			this.ProjectName = ProjectName;
			if(filesIn == "") {
				inputData = new InputData();
				if(defaultData) {
					try {
						inputData.onSetDefaults();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				inputData = new InputData();
				try {
					control.onReadInputs(filesIn);
					control.onGetInput(inputData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			files = new FILES(inputData.filesIn);
			model = new MODEL(inputData.yearsIn);
			site = new SITE(inputData.siteIn);
			soils = new SOILS(inputData.soilsIn);
			prod = new PROD(inputData.prodIn);
			estab = new ESTABL(inputData.estabIn);
			cloud = new CLOUD(inputData.cloudIn);
			weather = new WEATHER(inputData.weatherSetupIn, inputData.weatherHist, inputData.filesIn);
			markov = new MARKOV(inputData.markovIn, inputData.weatherHist);
			swc = new SWC(inputData.swcSetupIn, inputData.swcHist);
			out = new OUTPUT(inputData.outputSetupIn);
		}
		public String onGetPrjName() {
			return this.ProjectName;
		}
		
		private void onSetInput() {
			files.onSetValues();
			model.onSetValues();
			site.onSetValues();
			soils.onSetValues();
			prod.onSetValues();
			estab.onSetValues();
			cloud.onSetValues();
			weather.onSetValues();
			markov.onSetValues();
			swc.onSetValues();
			out.onSetValues();
		}
		
		private void onGetInput() {
			files.onGetValues();
			model.onGetValues();
			site.onGetValues();
			soils.onGetValues();
			prod.onGetValues();
			estab.onGetValues();
			cloud.onGetValues();
			weather.onGetValues();
			markov.onGetValues();
			swc.onGetValues();
			out.onGetValues();
		}
		
		public void onSave(String Project) {
			control.onClear();
			control.onGetLog().clear();
			onGetInput();
			if(Project!=null)
				inputData.filesIn.ProjectDirectory = Project;
			try {
				control.onSetInput(inputData);
				control.onWriteOutputs(inputData.filesIn.ProjectDirectory.toString());
				if(control.onGetLog().size() != 0)
					files.textArea_logFile.setText(control.onGetLog().toString());
					//JOptionPane.showMessageDialog(null, control.onGetLog(),"Saved", JOptionPane.INFORMATION_MESSAGE);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(),"Alert", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		public void onRun() {
			tabbedPane.setSelectedIndex(0);			
			control.onGetLog().clear();
			try {
				control.onStartModel(false, true, files.getSaveOutData());
				if(control.onGetLog().size() != 0)
					files.textArea_logFile.setText(control.onGetLog().toString());
					//JOptionPane.showMessageDialog(null, control.onGetLog(),"Executed", JOptionPane.OK_OPTION);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}
			if(output != null) {
				tabbedPane.remove(output);
			}
			output = new OUTDATA(control, inputData.outputSetupIn, files.progressBar);
			soilwatEvent(new SoilwatEvent(0, 100));
			tabbedPane.addTab("Output Data", output);
			tabbedPane.setSelectedComponent(output);
		}
		
		public void onVerify() {
			control.onGetLog().clear();
			try {
				if(control.onVerify()) {
					if(control.onGetLog().size() != 0)
						files.textArea_logFile.setText(control.onGetLog().toString());
						//JOptionPane.showMessageDialog(null, control.onGetLog(),"Verified", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, control.onGetLog(),"Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		public void resetProgressBar() {
			files.setProgress(0);
		}
		private JTabbedPane onGetPanel() {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			//frame.getContentPane().add(tabbedPane);
			
			tabbedPane.addTab("Files & Model", null, files.onGetPanel_files(model.onGetPanel_model()), null);
			//tabbedPane.addTab("Model Timing", null, model.onGetPanel_model(), null);
			tabbedPane.addTab("Site Params", null, site.onGetPanel_site(), null);
			tabbedPane.addTab("Soil Layers", null, soils.onGetPanel_soils(), null);
			tabbedPane.addTab("Plant Production", null, prod.onGetPanel_prod(), null);
			tabbedPane.addTab("Establishment", null, estab.onGetPanel_establ(), null);
			tabbedPane.addTab("Cloud", null, cloud.onGetPanel_cloud(), null);
			tabbedPane.addTab("Weather Setup", null, weather.onGetPanel_weather(), null);
			tabbedPane.addTab("Markov", null, markov.onGetPanel());
			tabbedPane.addTab("SWC Setup", null, swc.onGetPanel_swc(), null);
			tabbedPane.addTab("Output Setup", null, out.onGetPanel_output(), null);
			tabbedPane.addChangeListener(this);
			
			onSetInput();
			
			
			return tabbedPane;
		}
		@Override
		public void soilwatEvent(SoilwatEvent e) {
			files.setProgress((int)e.getPercent()*50);
		}
		@Override
		public void stateChanged(ChangeEvent e) {
			if(tabbedPane.getSelectedIndex() == 6)
				weather.onReset_list();
			if(tabbedPane.getSelectedIndex() == 7)
				markov.onResetList();
		}
	}
	
	private JFrame frame;
	private JTabbedPane tabbedPane;
	//private List<SoilWat> projects;
	private List<String> titles;
	private SortedMap<String, SoilWat> nameToProject;
	private Path file;
	
	private JFileChooser fc;
	
	private JMenu mn_File;
	private JMenuItem mntm_new;
	private JMenuItem mntm_newDefault;
	private JMenuItem mntm_open;
	private JMenuItem mntm_close;
	private JMenuItem mntm_closeAll;
	private JMenuItem mntm_save;
	private JMenuItem mntm_saveAs;
	private JMenuItem mntm_saveAll;
	private JMenuItem mntm_exit;
	private JMenu mn_Project;
	private JMenuItem mntm_Verify;
	private JSeparator separator_3;
	private JMenuItem mntm_Run;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SWExplorer window = new SWExplorer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SWExplorer() {
		//projects = new ArrayList<SWExplorer.SoilWat>();
		nameToProject = new TreeMap<String,SoilWat>();
		titles = new ArrayList<String>();
				
		fc = new JFileChooser();
    	fc.setAcceptAllFileFilterUsed(false);
    	fc.setMultiSelectionEnabled(false);
    	fc.setFileFilter(new FileNameExtensionFilter("files_v30", "in"));
		
		initialize();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Format format = NumberFormat.getIntegerInstance();
		((NumberFormat)format).setGroupingUsed(false);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1093, 790);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mn_File = new JMenu("File");
		mn_File.addMenuListener(this);
		menuBar.add(mn_File);
		
		JMenu mnNewMenu_new = new JMenu("New");
		mn_File.add(mnNewMenu_new);
		
		mntm_new = new JMenuItem("Project");
		mntm_new.addActionListener(this);
		mnNewMenu_new.add(mntm_new);
		
		mntm_newDefault = new JMenuItem("Default Project");
		mntm_newDefault.addActionListener(this);
		mnNewMenu_new.add(mntm_newDefault);
		
		mntm_open = new JMenuItem("Open Project");
		mntm_open.addActionListener(this);
		mn_File.add(mntm_open);
		
		JSeparator separator = new JSeparator();
		mn_File.add(separator);
		
		mntm_close = new JMenuItem("Close");
		mntm_close.addActionListener(this);
		mn_File.add(mntm_close);
		
		mntm_closeAll = new JMenuItem("Close All");
		mntm_closeAll.addActionListener(this);
		mn_File.add(mntm_closeAll);
		
		JSeparator separator_1 = new JSeparator();
		mn_File.add(separator_1);
		
		mntm_save = new JMenuItem("Save");
		mntm_save.addActionListener(this);
		mn_File.add(mntm_save);
		
		mntm_saveAs = new JMenuItem("Save As");
		mntm_saveAs.addActionListener(this);
		mn_File.add(mntm_saveAs);
		
		mntm_saveAll = new JMenuItem("Save All");
		mntm_saveAll.addActionListener(this);
		mn_File.add(mntm_saveAll);
		
		JSeparator separator_2 = new JSeparator();
		mn_File.add(separator_2);
		
		mntm_exit = new JMenuItem("Exit");
		mntm_exit.addActionListener(this);
		mn_File.add(mntm_exit);
		
		mn_Project = new JMenu("Project");
		mn_Project.addMenuListener(this);
		menuBar.add(mn_Project);
		
		mntm_Verify = new JMenuItem("Verify Project");
		mntm_Verify.addActionListener(this);
		mn_Project.add(mntm_Verify);
		
		separator_3 = new JSeparator();
		mn_Project.add(separator_3);
		
		mntm_Run = new JMenuItem("Run Project");
		mntm_Run.addActionListener(this);
		mn_Project.add(mntm_Run);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane);
	}
	
	private void addProject(String Name, boolean defaultData, String files) {
		SoilWat prj = new SoilWat(Name, defaultData, files);
		//projects.add(prj);
		nameToProject.put(Name, prj);
		tabbedPane.addTab(Name, nameToProject.get(Name).onGetPanel());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = (Object) e.getSource();
		if(src == mntm_new) {
			String prjName = JOptionPane.showInputDialog("Project Name:");
			if(prjName != null) {
				if(titles.contains(prjName)) {
					JOptionPane.showMessageDialog(null, "Project Name Already Taken","Alert", JOptionPane.ERROR_MESSAGE);
				} else {
					if(prjName != "") {
						titles.add(prjName);
						addProject(prjName, false, "");
					}
				}
			}
		}
		if(src == mntm_newDefault) {
			String prjName = JOptionPane.showInputDialog("Project Name:");
			if(prjName != null) {
				if(titles.contains(prjName)) {
					JOptionPane.showMessageDialog(null, "Project Name Already Taken","Alert", JOptionPane.ERROR_MESSAGE);
				} else {
					if(prjName != "") {
						titles.add(prjName);
						addProject(prjName, true, "");
					}
				}
			} else {
				
			}
		}
		if(src == mntm_open) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	file = fc.getSelectedFile().toPath();
	        	//file = file.resolve("files_v30.in");
	        	String prjName;
	        	if(file.getNameCount()>=2) {
	        		prjName = file.getName(file.getNameCount()-2).toString();
	        	} else {
	        		prjName = file.getRoot().toString();
	        	}
	        	prjName = JOptionPane.showInputDialog("Project Name:", prjName);
	        	
	        	if(titles.contains(prjName)) {
	        		JOptionPane.showMessageDialog(null, "Project Name Already Taken","Alert", JOptionPane.ERROR_MESSAGE);
				} else {
					if(prjName != "") {
						titles.add(prjName);
						addProject(prjName, false, file.toString());
					}
				}
	        } else {
	        	JOptionPane.showMessageDialog(null, "Could not open file.","Alert", JOptionPane.ERROR_MESSAGE);
	        }
		}
		if(src == mntm_close) {
			String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			int index = titles.indexOf(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
			titles.remove(index);
			nameToProject.remove(title);
			tabbedPane.remove(tabbedPane.getSelectedComponent());
		}
		if(src == mntm_closeAll) {
			tabbedPane.removeAll();
			titles.clear();
			nameToProject.clear();
		}
		if(src == mntm_save) {
			String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			nameToProject.get(title).onSave(null);
		}
		if(src == mntm_saveAs) {
			fc.setCurrentDirectory(new File("files_v30.in"));
			int returnVal = fc.showSaveDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	file = fc.getSelectedFile().toPath();
	        	String prjName;
	        	if(file.getNameCount()>=2) {
	        		prjName = file.getName(file.getNameCount()-2).toString();
	        	} else {
	        		prjName = file.getRoot().toString();
	        	}
	        	
	        	if(titles.contains(prjName)) {
	        		JOptionPane.showMessageDialog(null, "Project Already Exists.","Alert", JOptionPane.ERROR_MESSAGE);
	        	} else {
	        		int index = titles.indexOf(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
		        	titles.add(index, prjName);
		        	nameToProject.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())).ProjectName = prjName;
		        	nameToProject.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())).onSave(file.getParent().toString());
		        	nameToProject.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())).onSetInput();
		        	SoilWat obj = nameToProject.remove(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
		        	nameToProject.put(prjName, obj);
		        	tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), prjName);
	        	}
	        } else {
	        	JOptionPane.showMessageDialog(null, "Not Saved.","Alert", JOptionPane.ERROR_MESSAGE);
	        }
		}
		if(src==mntm_saveAll) {
			for(int i=0; i<tabbedPane.getTabCount(); i++) {
				String title = tabbedPane.getTitleAt(i);
				nameToProject.get(title).onSave(null);
			}
		}
		if(src==mntm_Verify) {
			//Save the project
			String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			nameToProject.get(title).onSave(null);
			
			nameToProject.get(title).onVerify();
		}
		if(src==mntm_Run) {
			String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			
			nameToProject.get(title).resetProgressBar();
			//Save the project
			Thread exe = new Thread(new RunModel(nameToProject.get(title)));
			exe.start();
			//nameToProject.get(title).onSave(null);
			//nameToProject.get(title).onRun();
		}
		if(src==mntm_exit) {
			System.exit(0);
		}
		
	}

	@Override
	public void menuCanceled(MenuEvent arg0) {		
	}

	@Override
	public void menuDeselected(MenuEvent arg0) {		
	}

	@Override
	public void menuSelected(MenuEvent e) {
		Object src = (Object) e.getSource();
		if(src==mn_File) {
			int count = tabbedPane.getTabCount();
			if(count == 0) {
				mntm_close.setEnabled(false);
				mntm_closeAll.setEnabled(false);
				mntm_save.setEnabled(false);
				mntm_saveAs.setEnabled(false);
				mntm_saveAll.setEnabled(false);
			} else {
				mntm_close.setEnabled(true);
				mntm_closeAll.setEnabled(true);
				mntm_save.setEnabled(true);
				mntm_saveAs.setEnabled(true);
				mntm_saveAll.setEnabled(true);
			}
		}
	}
	
	public List<Integer> FindDuplicates(List<Integer> ints)
	{
		List<Integer> duplicates = new ArrayList<Integer>();
		for (int idx1 = 0; idx1 < ints.size(); idx1++)
		{
			for (int idx2 = 0; idx2 < ints.size(); idx2++)
			{
				if (ints.get(idx1) == ints.get(idx2))
				{
					duplicates.add(ints.get(idx1));
				}
			}
		}
		return duplicates;
	}
}

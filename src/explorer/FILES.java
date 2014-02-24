package explorer;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FILES {
	
	private JTextField textField_ProjectDirectory;
	private JTextField textField_FileIn;
	private JTextField textField_YearsIn;
	private JTextField textField_LogFile;
	private JTextField textField_WeatherSetupIn;
	private JTextField textField_WeatherPathPrefix;
	private JTextField textField_SiteIn;
	private JTextField textField_SoilsIn;
	private JTextField textField_MarkovProb;
	private JTextField textField_MarkovCovar;
	private JTextField textField_CloudIn;
	private JTextField textField_ProdIn;
	private JTextField textField_EstabIn;
	private JTextField textField_swcSetupIn;
	private JTextField textField_OutputFolder;
	private JTextField textField_OutputSetupIn;
	
	soilwat.SW_FILES.FILES_INPUT_DATA filesIn;
	
	public FILES(soilwat.SW_FILES.FILES_INPUT_DATA filesIn) {
		this.filesIn = filesIn;
	}
	
	public void onGetValues() {
		this.filesIn.ProjectDirectory = textField_ProjectDirectory.getText();
		this.filesIn.FilesIn = textField_FileIn.getText();
		this.filesIn.YearsIn = textField_YearsIn.getText();
		this.filesIn.LogFile = textField_LogFile.getText();
		this.filesIn.WeatherSetupIn = textField_WeatherSetupIn.getText();
		this.filesIn.WeatherPathAndPrefix = textField_WeatherPathPrefix.getText();
		this.filesIn.SiteParametersIn = textField_SiteIn.getText();
		this.filesIn.SoilsIn = textField_SoilsIn.getText();
		this.filesIn.MarkovProbabilityIn = textField_MarkovProb.getText();
		this.filesIn.MarkovCovarianceIn = textField_MarkovCovar.getText();
		this.filesIn.CloudIn = textField_CloudIn.getText();
		this.filesIn.PlantProductivityIn = textField_ProdIn.getText();
		this.filesIn.EstablishmentIn = textField_EstabIn.getText();
		this.filesIn.SWCSetupIn = textField_swcSetupIn.getText();
		this.filesIn.OutputDirectory = textField_OutputFolder.getText();
		this.filesIn.OutputSetupIn = textField_OutputSetupIn.getText();
	}
	
	public void onSetValues() {
		textField_ProjectDirectory.setText(this.filesIn.ProjectDirectory);
		textField_FileIn.setText(this.filesIn.FilesIn);
		textField_YearsIn.setText(this.filesIn.YearsIn);
		textField_LogFile.setText(this.filesIn.LogFile);
		textField_WeatherSetupIn.setText(this.filesIn.WeatherSetupIn);
		textField_WeatherPathPrefix.setText(this.filesIn.WeatherPathAndPrefix);
		textField_SiteIn.setText(this.filesIn.SiteParametersIn);
		textField_SoilsIn.setText(this.filesIn.SoilsIn);
		textField_MarkovProb.setText(this.filesIn.MarkovProbabilityIn);
		textField_MarkovCovar.setText(this.filesIn.MarkovCovarianceIn);
		textField_CloudIn.setText(this.filesIn.CloudIn);
		textField_ProdIn.setText(this.filesIn.PlantProductivityIn);
		textField_EstabIn.setText(this.filesIn.EstablishmentIn);
		textField_swcSetupIn.setText(this.filesIn.SWCSetupIn);
		textField_OutputFolder.setText(this.filesIn.OutputDirectory);
		textField_OutputSetupIn.setText(this.filesIn.OutputSetupIn);
	}
	
	public JPanel onGetPanel_files() {
		JPanel panel_files = new JPanel();
		
		panel_files.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
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
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblProjectLocation = new JLabel("Project Location & Files Setup Location");
		panel_files.add(lblProjectLocation, "1, 1, 2, 1, center, center");
		
		JLabel lblProjectDirectory = new JLabel("Project Directory:");
		lblProjectDirectory.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblProjectDirectory, "1, 3, left, center");
		
		textField_ProjectDirectory = new JTextField();
		panel_files.add(textField_ProjectDirectory, "2, 3, left, center");
		textField_ProjectDirectory.setColumns(30);
		
		JLabel lblFilesIn = new JLabel("Files In:");
		lblFilesIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblFilesIn, "1, 5, right, default");
		
		textField_FileIn = new JTextField();
		panel_files.add(textField_FileIn, "2, 5, left, default");
		textField_FileIn.setColumns(30);
		
		JLabel lblModel = new JLabel("Model");
		panel_files.add(lblModel, "1, 7, 2, 1, center, default");
		
		JLabel lblYearsIn = new JLabel("Years In:");
		lblYearsIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblYearsIn, "1, 9, right, default");
		
		textField_YearsIn = new JTextField();
		panel_files.add(textField_YearsIn, "2, 9, left, default");
		textField_YearsIn.setColumns(30);
		
		JLabel lblLogfile = new JLabel("LogFile:");
		lblLogfile.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblLogfile, "1, 11, right, default");
		
		textField_LogFile = new JTextField();
		panel_files.add(textField_LogFile, "2, 11, left, default");
		textField_LogFile.setColumns(30);
		
		JLabel lblNewLabel = new JLabel("Site");
		panel_files.add(lblNewLabel, "1, 13, 2, 1, center, default");
		
		JLabel lblSiteIn = new JLabel("Site In:");
		lblSiteIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblSiteIn, "1, 15, right, default");
		
		textField_SiteIn = new JTextField();
		panel_files.add(textField_SiteIn, "2, 15, left, default");
		textField_SiteIn.setColumns(30);
		
		JLabel lblSoilsIn = new JLabel("Soils In:");
		lblSoilsIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblSoilsIn, "1, 17, right, default");
		
		textField_SoilsIn = new JTextField();
		panel_files.add(textField_SoilsIn, "2, 17, left, default");
		textField_SoilsIn.setColumns(30);
		
		JLabel lblWeather = new JLabel("Weather");
		panel_files.add(lblWeather, "1, 19, 2, 1, center, default");
		
		JLabel lblWeatherSetup = new JLabel("Weather Setup:");
		lblWeatherSetup.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblWeatherSetup, "1, 21, right, default");
		
		textField_WeatherSetupIn = new JTextField();
		panel_files.add(textField_WeatherSetupIn, "2, 21, left, default");
		textField_WeatherSetupIn.setColumns(30);
		
		JLabel lblWeatherPrefix = new JLabel("Weather Prefix:");
		lblWeatherPrefix.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblWeatherPrefix, "1, 23, right, default");
		
		textField_WeatherPathPrefix = new JTextField();
		panel_files.add(textField_WeatherPathPrefix, "2, 23, left, default");
		textField_WeatherPathPrefix.setColumns(30);
		
		JLabel lblMarkovProb = new JLabel("Markov Prob:");
		lblMarkovProb.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblMarkovProb, "1, 25, right, default");
		
		textField_MarkovProb = new JTextField();
		panel_files.add(textField_MarkovProb, "2, 25, left, default");
		textField_MarkovProb.setColumns(30);
		
		JLabel lblMarkovCovar = new JLabel("Markov Covar:");
		lblMarkovCovar.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblMarkovCovar, "1, 27, right, default");
		
		textField_MarkovCovar = new JTextField();
		panel_files.add(textField_MarkovCovar, "2, 27, left, default");
		textField_MarkovCovar.setColumns(30);
		
		JLabel lblCloudIn = new JLabel("Cloud In:");
		lblCloudIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblCloudIn, "1, 29, right, default");
		
		textField_CloudIn = new JTextField();
		panel_files.add(textField_CloudIn, "2, 29, left, default");
		textField_CloudIn.setColumns(30);
		
		JLabel lblVegetation = new JLabel("Vegetation");
		panel_files.add(lblVegetation, "1, 31, 2, 1, center, default");
		
		JLabel lblPlantProductionIn = new JLabel("Plant Production In:");
		lblPlantProductionIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblPlantProductionIn, "1, 33, right, default");
		
		textField_ProdIn = new JTextField();
		panel_files.add(textField_ProdIn, "2, 33, left, default");
		textField_ProdIn.setColumns(30);
		
		JLabel lblEstablishmentIn = new JLabel("Establishment In:");
		lblEstablishmentIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblEstablishmentIn, "1, 35, right, default");
		
		textField_EstabIn = new JTextField();
		panel_files.add(textField_EstabIn, "2, 35, left, default");
		textField_EstabIn.setColumns(30);
		
		JLabel lblSwcMeasurements = new JLabel("SWC Measurements");
		panel_files.add(lblSwcMeasurements, "1, 37, 2, 1, center, default");
		
		JLabel lblSwcSetupIn = new JLabel("SWC Setup In:");
		lblSwcSetupIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblSwcSetupIn, "1, 39, right, default");
		
		textField_swcSetupIn = new JTextField();
		panel_files.add(textField_swcSetupIn, "2, 39, left, default");
		textField_swcSetupIn.setColumns(30);
		
		JLabel lblOutput = new JLabel("Output");
		panel_files.add(lblOutput, "1, 41, 2, 1, center, default");
		
		JLabel lblOutputFolder = new JLabel("Output Folder:");
		lblOutputFolder.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblOutputFolder, "1, 43, right, default");
		
		textField_OutputFolder = new JTextField();
		panel_files.add(textField_OutputFolder, "2, 43, left, default");
		textField_OutputFolder.setColumns(30);
		
		JLabel lblOutputSetupIn = new JLabel("Output Setup In:");
		lblOutputSetupIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_files.add(lblOutputSetupIn, "1, 45, right, default");
		
		textField_OutputSetupIn = new JTextField();
		panel_files.add(textField_OutputSetupIn, "2, 45, left, default");
		textField_OutputSetupIn.setColumns(30);
		
		return panel_files;
	}
}

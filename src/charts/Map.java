package charts;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.SiteEvent;


public class Map extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mapMarkers;
	@SuppressWarnings("unused")
	private static class DirectoriesFilter implements Filter<Path> {
	    @Override
	    public boolean accept(Path entry) throws IOException {
	        return Files.isDirectory(entry);
	    }
	}
	private JMapViewer mapViewer;
	private List<Coordinate> points = new ArrayList<Coordinate>();
	private DefaultMapController mapController;

	public Map(String applicationTitle) {
		super(applicationTitle);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		drawOpenStreetMap(-105.5833,41.3167,10);
		//this.setContentPane(mapViewer);
	}
	public void drawOpenStreetMap(double lon, double lat, int zoom) {
		mapViewer = new JMapViewer();
		mapViewer.setZoom(zoom);
		mapViewer.setDisplayPositionByLatLon(lat, lon, zoom);
		mapViewer.repaint();
		mapController = new DefaultMapController(mapViewer);
		mapController.setMovementMouseButton(MouseEvent.BUTTON1);	
	}
	public void addSiteListener(SiteEvent e) {
		mapViewer.addSiteEventListener(e);
	}
	public JPanel get_MapPanel() {
		return mapViewer;
	}
	public String getFolder() {
		return mapViewer.getFolderName();
	}
	public void set_map() {
		this.setContentPane(mapViewer);
	}
	public void onSetMapMarkers(List<String> weatherFolders) {
		List<String[]> latlong = new ArrayList<String[]>();
		for (String name : weatherFolders) {
			latlong.add(name.replaceFirst("Weather_NCEPCFSR_", "").split("(?<=[[-]?[1234567890.]+])\\-", 2));
		}
		for (String[] point : latlong) {
			points.add(new Coordinate(Double.valueOf(point[0]),Double.valueOf(point[1])));
		}
		
		for (Coordinate p : points) {
			mapMarkers++;
			mapViewer.addMapMarker(new MapMarkerDot(p));
		}
	}
	public void onSetMapMarkers_db(List<MapMarkerDot> sites) {
		if(sites.size() >0) {
			for (MapMarkerDot mapMarkerDot : sites) {
				mapMarkers++;
				mapViewer.addMapMarker(mapMarkerDot);
			}
		}
	}
	public void removeAllMarkers() {
		mapMarkers = 0;
		mapViewer.removeAllMapMarkers();
	}
	public int getNumberMarkers() {
		return this.mapMarkers;
	}
}

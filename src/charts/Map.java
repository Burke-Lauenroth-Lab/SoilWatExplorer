package charts;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

public class Map extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMapViewer mapViewer;

	public Map(String applicationTitle) {
		super(applicationTitle);
		
		drawOpenStreetMap(0,0,1);
		this.setContentPane(mapViewer);
	}
	public void drawOpenStreetMap(double lon, double lat, int zoom) {
		mapViewer = new JMapViewer();
		mapViewer.setZoom(zoom);
		mapViewer.setDisplayPositionByLatLon(lat, lon, zoom);
		mapViewer.repaint();
		DefaultMapController mapController = new DefaultMapController(mapViewer);
		mapController.setMovementMouseButton(MouseEvent.BUTTON1);
		mapViewer.addMapMarker(new MapMarkerDot(52, 5.5));
	}

}

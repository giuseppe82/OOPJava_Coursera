package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Giuseppe Di Bernardo
 * Date: January, 08 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		/** In the setup method, you should create a SimplePointMarker object 
		 * for each PointFeature in the list features, inside the setUp method. 
		 * You will find the documentation page for SimplePointMarker and PointFeature 
		 * helpful from the Unfolding Maps javadoc page 
		 * (you can find these class names in large list of classes in the lower left sidebar). 
		 * In particular, notice that the SimplePointMarker constructor takes a Location object, 
		 * and the PointFeature has a way of getting its location. In addition, you’ll definitely 
		 * want to use a loop here…		
		 */
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    /** Let's check types of object and flow of thought in 
	     * this example. 
	     * earthquakes: List of PointFeature Objects
	     * f: just ONE PointFeature object from earthquakes
	     * magObj: "magnitude" value as a object from f
	     * mag: final float value of magnitude
	     **/
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    
	    // Create markers and add them to the map
	    // Use enhanced for~ loop 
	    for (PointFeature feature : earthquakes) {
	    	markers.add(createMarker(feature));	    	
	    }
	    map.addMarkers(markers); // add multiple markers to the map
	    
	    //TODO: Add code here as appropriate
	}// end of void setup
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		// Create new instance of SimplePointMarker
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		int blue = color(0, 0, 255);
		int yellow = color(255, 255, 0);
		int red = color(255, 0, 0); 
		
		Object magObj = feature.getProperty("magnitude");
    	float mag = Float.parseFloat(magObj.toString());
    	
    	// Add code to style each marker 
    	if (mag < 4.0)
    	{
    		marker.setColor(blue);
    		marker.setRadius(4f);
    	}
    	else if (mag >= 4.0 && mag <= 4.9)
    	{
    		marker.setColor(yellow);
    		marker.setRadius(8f);
    	}
    	else if (mag > 5.0) 
    	{
    		marker.setColor(red);
    		marker.setRadius(16f);
    	}
		// return new SimplePointMarker(feature.getLocation());
		return marker; 
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		rect(25, 50, 150, 250);
		
		int blue = color(0, 0, 255);
		int yellow = color(255, 255, 0);
		int red = color(255, 0, 0);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);
		
		fill(red);
		ellipse(50, 125, 15, 15);
		fill(yellow);
		ellipse(50, 175, 10, 10);
		fill(blue);
		ellipse(50, 225, 5, 5);
		
		fill(0, 0, 0);
		text("5.0+ Magnitude", 75, 125);
		text("4.0+ Magnitude", 75, 175);
		text("Below 4.0", 75, 225);
	}
}

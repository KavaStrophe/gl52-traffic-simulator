package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;

import environnement.AbstractStaticObject;
import environnement.Car;
import environnement.LightPanel;
import environnement.StopPanel;
import environnement.SpeedPanel;

public class PersonalizedRoadNetwork extends StandardRoadNetwork {
	private ArrayList<RoadConnection> impasses = new ArrayList<RoadConnection> ();
	private ArrayList<RoadConnection> threeRoadConnections = new ArrayList<RoadConnection>(); //Croisement entre 3 routes
	private ArrayList<RoadConnection> fourRoadConnections = new ArrayList<RoadConnection>(); //Croisement entre au moins 4 routes
	private ArrayList<StopPanel> stopPanel = new ArrayList();
	private ArrayList<LightPanel> lightPanel = new ArrayList();
	private ArrayList<SpeedPanel> speedPanel = new ArrayList();
	
	public PersonalizedRoadNetwork(Rectangle2afp<?, ?, ?, ?, ?, ?> originalBounds) {
		super(originalBounds);
	}

	public ArrayList<RoadConnection> getImpasses()
	{
		return this.impasses;
	}

	public ArrayList<RoadConnection> getThreeRoadConnections()
	{
		return this.threeRoadConnections;
	}

	public ArrayList<RoadConnection> getFourRoadConnections()
	{
		return this.fourRoadConnections;
	}
	
	public void analizeNetwork()
	{
		Collection<RoadSegment> allSegments = (Collection<RoadSegment>) this.getRoadSegments();
		HashSet<RoadConnection> allRoadConnections = new HashSet<RoadConnection>();
		for (RoadSegment segment : allSegments) {
			allRoadConnections.add( segment.getBeginPoint());
			allRoadConnections.add( segment.getEndPoint());
			/*RoadConnection firstPoint = segment.getBeginPoint();
			if (!impasses.contains(firstPoint) && !threeRoadConnections.contains(firstPoint) && !fourRoadConnections.contains(firstPoint)) {
				if (firstPoint.isFinalConnectionPoint()) {
					this.impasses.add(firstPoint);
				} else if (firstPoint.getConnectedSegmentCount() == 3) {
					this.threeRoadConnections.add(firstPoint);
				} else if (firstPoint.getConnectedSegmentCount() == 4) {
					this.fourRoadConnections.add(firstPoint);
				}
			}

			RoadConnection lastPoint = segment.getEndPoint();
			if (!impasses.contains(firstPoint) && !threeRoadConnections.contains(firstPoint) && !fourRoadConnections.contains(firstPoint)) {
				if (lastPoint.isFinalConnectionPoint()) {
					this.impasses.add(lastPoint);
				} else if (lastPoint.getConnectedSegmentCount() == 3) {
					this.threeRoadConnections.add(lastPoint);
				} else if (lastPoint.getConnectedSegmentCount() == 3) {
					this.fourRoadConnections.add(lastPoint);
				}
			}*/
		}
		for(RoadConnection point : allRoadConnections)
		{
			if (point.getConnectedSegmentCount() == 1) {
				this.impasses.add(point);
			} else if (point.getConnectedSegmentCount() == 3) {
				this.threeRoadConnections.add(point);
			} else if (point.getConnectedSegmentCount() == 4) {
				this.fourRoadConnections.add(point);
			}
		}
	}
	
	public ArrayList<StopPanel> getStopPanel()
	{
		return this.stopPanel; 
	}
	public ArrayList<LightPanel> getLightPanel()
	{
		return this.lightPanel;
	}
	public ArrayList<SpeedPanel> getSpeedPanel()
	{
		return this.speedPanel;
	}
	
	public void addStopPanelOnThisRoadConnection(RoadConnection point) {
		int randomNumSegment = (int) Math.round(Math.random() * 2);
		RoadSegment segment = point.getConnectedSegment(randomNumSegment);
		float distance = 10;
		if (point == segment.getEndPoint()) {
			distance = (float) (segment.getDistanceToEnd(0) - 10);
		} 
		addStopPanel(distance, segment, point);
	}
	public StopPanel addStopPanel(float position, RoadSegment segment, RoadConnection entryPoint)
	{
		UUID id = UUID.randomUUID();
		StopPanel panel = new StopPanel(id, entryPoint, segment, position);
		this.stopPanel.add(panel);
		addObjectToThisSegment("PANEL", panel, segment);
		return panel;
	}
	public LightPanel addLightPanel(float position, RoadSegment segment, RoadConnection entryPoint, boolean state)
	{
		UUID id = UUID.randomUUID();
		LightPanel panel = new LightPanel(id, entryPoint, segment, position, state);
		this.lightPanel.add(panel);
		addObjectToThisSegment("PANEL", panel, segment);
		return panel;
	}
	public SpeedPanel addSpeedPanel(float position, RoadSegment segment, RoadConnection entryPoint, int speedLimit){
		UUID id = UUID.randomUUID();
		SpeedPanel panel = new SpeedPanel(id, entryPoint, segment, position, speedLimit);
		this.speedPanel.add(panel);
		addObjectToThisSegment("PANEL", panel, segment);
		return panel;
	}	
	public void addObjectToThisSegment(String type, AbstractStaticObject obj, RoadSegment segment){
		segment.addUserData(type, obj);
	}
	public void removeObjectFromHisSegment(String type, AbstractStaticObject obj) {
		System.out.println("CARS OF THE SEGMENT : " + obj.getSegment().getUserDataCollection(type));
		obj.getSegment().removeUserData(type, obj);
		System.out.println("REST OF THE SEGMENT : " + obj.getSegment().getUserDataCollection(type));

	}
	public void moveCarToSegment(Car car, RoadSegment segment) {
		removeObjectFromHisSegment("CAR", car);
		addObjectToThisSegment("CAR", car, segment);
	}
}

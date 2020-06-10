package storedobjects;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.driver.*;
import redis.clients.jedis.Jedis;
import com.mongodb.client.MongoDatabase; 
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential; 


public class District {
	
	private Car car;
	private String district;
	private final Driver driverNeo4j;
    private final Jedis driverRedis;
    private final MongoClient mongoClient;
	
	public District(Car car){
		this.car = car;
		driverNeo4j = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "WJ7cbBLbwvuDaFF"));
        driverRedis = new Jedis("localhost");
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
	}
	
	
	
	//find district of car
	private String getDistrict() {
		
		//get geo-location of car
		double lng = car.getLongitude();
		double lat = car.getLatitude();
		
		//transfer coordinates to district address
		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(API_KEY);

		JOpenCageReverseRequest request = new JOpenCageReverseRequest(lat, lng);
		
		request.setNoAnnotations(true);

		JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
		
		//handle the json response to get the district of car
		JSONObject obj = new JSONObject(response);
        
        JSONArray arr = obj.getJSONArray("results");
        
        for (int i = 0; i < arr.length(); i++) {
        	
            district = arr.getJSONObject(i).getJSONObject("components").getString("city_district");
            
        }
        
        //caching district of this car into Redis
        driverRedis.set(car.getObjectID() + "_district", district);
		
		return district;
		 
	}
	
	//calculate #cars in the district
	private int getCarAmount() {
		int amount;
		
		//retrieving district from redis cache
		String district = driverRedis.get(car.getObjectID() + "_district");
		
		try (Session session = driverNeo4j.session())
        {
            
            Result result = session.run("MATCH (n:Car)-[:LOCATES]->(m:Location {district: $district})"
            		+ " RETURN count(n)",
            		parameters("district", district));
            amount = Integer.parseInt(result.toString());
        }
		
		//caching #car into Redis
        driverRedis.incr(district + "_car_amount");
		
		return amount;
		
	}
		



	//determine price of this district by #cars
	private double getPrice() {
		double price;
		
		//retrieving car amount from redis
		int carAmount = Integer.parseInt(driverRedis.get(district + "_car_amount"));
		
		if(carAmount <= 5) price = 2.0;       //per kilometer
		else if(carAmount <= 10) price = 1.5;
		else price = 1.0;
		
		return price;	
	}
	
	
	
	
	//store data necessary into mongodb
	
	
	public static void main(String[] args) {
		
	}
	
}

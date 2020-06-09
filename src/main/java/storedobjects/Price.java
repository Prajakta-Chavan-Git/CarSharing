package storedobjects;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import org.bson.Document;


public class Price {
	
	private String address;
	private Car car;
	
	public Price(Car car){
		this.car = car;
	}
	
	//get geo-location of car
	private double lng = car.getLongitude();
	private double lat = car.getLatitude();
	
	//find district of car
	private JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(API_KEY);

	private JOpenCageReverseRequest request = new JOpenCageReverseRequest(lat, lng);
	
	request.setNoAnnotations(true);

	private JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
	

	
	//caching district of a car into redis
	
	
	//calculate #cars in the district
	
	
	//determine price of this district by the #cars in it
	
	
	//caching data into redis and store it into mongodb
	
	
	public static void main(String[] args) {
		
	}
	
}

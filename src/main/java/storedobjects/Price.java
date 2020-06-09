package storedobjects;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import org.bson.Document;


public class Price {
	
	private String district;
	private Car car;
	
	public Price(Car car){
		this.car = car;
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
		
		return district = response.results[0].componets.city_district;
		 
	}
	
	
	

	
	//caching district of a car into redis
	
	
	//calculate #cars in the district
	
	
	//determine price of this district by the #cars in it
	
	
	//caching data into redis and store it into mongodb
	
	
	public static void main(String[] args) {
		
	}
	
}

package storedobjects;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;


public class Price {

	
	JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(YOUR_API_KEY);

	JOpenCageReverseRequest request = new JOpenCageReverseRequest(-22.6792, 14.5272);
	request.setNoAnnotations(true);

	JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
}

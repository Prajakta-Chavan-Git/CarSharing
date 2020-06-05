package storedobjects;

public class Car {

    private int seats;
    private String objectID;
    private String manufacturer;
    private String carType;
    private double fuelConsumption;
    private double longitude;
    private double latitude;
    private String status;
    private String fuelType;

    public void setObjectID(String pID){
        if(objectID == null)
            objectID=pID;
    }

    public Car(int seats, String objectID, String manufacturer, String carType, double fuelConsumption, double longitude, double latitude, String status, String fuelType) {
        this.seats = seats;
        this.objectID = objectID;
        this.manufacturer = manufacturer;
        this.carType = carType;
        this.fuelConsumption = fuelConsumption;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.fuelType = fuelType;
    }

}

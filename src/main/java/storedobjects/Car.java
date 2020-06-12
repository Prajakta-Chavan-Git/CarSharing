package storedobjects;

import org.bson.Document;

import java.util.ArrayList;

public class Car implements IStoreableObject{


    private int seats;
    private String objectID;
    private String manufacturer;
    private String carType;
    private double fuelConsumption;
    private double longitude;
    private double latitude;
    private String status;
    private String fuelType;
    private ArrayList<Comment> comments;
    private double expenses;

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }


    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }



    public void setObjectID(String pID){
        if(objectID == null)
            objectID=pID;
    }

    public Car(int seats, String objectID, String manufacturer, String carType, double fuelConsumption, double longitude, double latitude, String status, String fuelType, double expenses) {
        this.seats = seats;
        this.objectID = objectID;
        this.manufacturer = manufacturer;
        this.carType = carType;
        this.fuelConsumption = fuelConsumption;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.fuelType = fuelType;
        this.expenses = expenses;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (seats == 0)
            doc.append("seats", seats);
        if (manufacturer != null)
            doc.append("manufacturer", manufacturer);
        if (carType != null)
            doc.append("carType", carType);
        if (fuelConsumption == 0)
            doc.append("fuelConsumption", fuelConsumption);
        if (longitude ==-360)
            doc.append("longitude", longitude);
        if (latitude ==-360)
            doc.append("latitude", latitude);
        if (expenses ==0)
            doc.append("expenses", expenses);
        if (status != null)
            doc.append("status", status);
        if (fuelType != null)
            doc.append("fuelType", fuelType);
        if (comments != null)
            doc.append("comments", storeComments());
        return doc;
    }

    private ArrayList<Document> storeComments(){
        ArrayList<Document> docs =new ArrayList<>();
        for(Comment comment : comments){
            docs.add(comment.toDocument());
        }
        return docs;
    }
}

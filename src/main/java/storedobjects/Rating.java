package storedobjects;

import org.bson.Document;

public class Rating implements IStoreableObject {
    //Maurice C for Usecase 5
    private int cleanliness;
    private int comfort;
    private int reliability;
    private String comment;
    private String objectID;
    private User user;
    private Car car;

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public void setComments(String comments) {
        this.comment = comments;
    }

    public void setObjectID(String pID){
        if(objectID == null)
            objectID=pID;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public User getUser() {
        return user;
    }

    public int getReliability() {
        return reliability;
    }

    public String getComments() {
        return comment;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public int getComfort() {
        return comfort;
    }

    public String getObjectID() {
        return objectID;
    }



    public Rating(int cleanliness, int comfort, int reliability, String comment, String objectID, Car car, User user){
        this.cleanliness = cleanliness;
        this.comfort = comfort;
        this.comment = comment;
        this.objectID = objectID;
        this.reliability = reliability;
        this.user = user;
        this.car = car;
    }
    public Rating(int cleanliness, int comfort, int reliability, String comment){
        this.cleanliness = cleanliness;
        this.comfort = comfort;
        this.comment = comment;
        this.reliability = reliability;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (objectID != null)
            doc.append("_id", objectID);
        if (cleanliness == 0)
            doc.append("cleanliness", cleanliness);
        if (comfort == 0)
            doc.append("comfort", comfort);
        if (reliability == 0)
            doc.append("reliability", reliability);
        if (comment != null)
            doc.append("comment", comment);
        if (user != null)
            doc.append("user", user.getObjectID());
        if (car != null)
            doc.append("car", car.getObjectID());
        return doc;
    }
}

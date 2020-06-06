package storedobjects;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements IStoreableObject{
    private Date birthDate;
    private String objectID;
    private String name;
    private Address address;
    private String phoneNumber;
    private String email;
    private String bankAccount;
    private String status;
    private ArrayList<Query> queries;
    private ArrayList<Car> ownedCars;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getObjectID() {
        return objectID;
    }

    public User(){}

    public User(Date birthDate, String objectID, String name, Address address, String phoneNumber, String email, String bankAccount, String status, ArrayList<Query> queries) {
        this.birthDate = birthDate;
        this.objectID = objectID;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bankAccount = bankAccount;
        this.status = status;
        this.queries = queries;
    }

    public void setObjectID(String objectID) {
        if(this.objectID==null)
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<Query> queries) {
        this.queries = queries;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (objectID != null)
            doc.append("objectID", objectID);
        if (birthDate != null)
            doc.append("birthDate", birthDate);
        if (name != null)
            doc.append("name", name);
        if (address != null)
            doc.append("address", address.toDocument());
        if (phoneNumber != null)
            doc.append("phoneNumber", phoneNumber);
        if (email != null)
            doc.append("email", email);
        if (bankAccount != null)
            doc.append("bankAccount", bankAccount);
        if (status != null)
            doc.append("status", status);
        if (queries != null)
            doc.append("queries", storeQueries());
        if (ownedCars != null)
            doc.append("ownedCars", storeCars());
        return doc;
    }

    private ArrayList<Document> storeQueries(){
        ArrayList<Document> docs =new ArrayList<>();
        for(Query query : queries){
            docs.add(query.toDocument());
        }
        return docs;
    }

    private ArrayList<Document> storeCars(){
        ArrayList<Document> docs =new ArrayList<>();
        for(Car car : ownedCars){
            docs.add(new Document("ObjectID",car.getObjectID()));
        }
        return docs;
    }


}

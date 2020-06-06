package storedobjects;

import org.bson.Document;

public class Address implements IStoreableObject {


    private String city;
    private String street;
    private String houseNumber;
    private String postalCode;

    public Address(String city, String street, String houseNumber, String postalCode) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (city != null)
            doc.append("city", city);
        if (street != null)
            doc.append("street", street);
        if (houseNumber != null)
            doc.append("houseNumber", houseNumber);
        if (postalCode != null)
            doc.append("postalCode", postalCode);
        return doc;
    }
}

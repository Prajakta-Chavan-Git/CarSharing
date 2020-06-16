public class Area {

    private double longitude;
    private double latitude;
    private int areaSize;
    private double rating;
    private int cars;
    private int searchRequests;

    public Area(double longitude, double latitude, int areaSize, double rating, int cars, int searchRequests) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.areaSize = areaSize;
        this.rating = rating;
        this.cars = cars;
        this.searchRequests = searchRequests;
    }

    public int getCars() {
        return cars;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public int getSearchRequests() {
        return searchRequests;
    }

    public void setSearchRequests(int searchRequests) {
        this.searchRequests = searchRequests;
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

    public int getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(int areaSize) {
        this.areaSize = areaSize;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String toString(){
        return "Longitude: " + longitude + ", Latitude: " + latitude + ", AreaSize: " + areaSize + ", cars: " + cars + ", Searches: " + searchRequests+ ", Score: " + rating;
    }
}


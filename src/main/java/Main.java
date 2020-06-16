

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import createObjects.CarFactory;
import createObjects.QueryFactory;
import createObjects.ReviewFactory;
import createObjects.UserFactory;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
//import org.neo4j.driver.v1.*;
import redis.clients.jedis.Jedis;
import storedobjects.*;
import storedobjects.Query;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
//import org.neo4j.graphdb.spatial.Geometry;
//import org.neo4j.graphdb.spatial.Point;

import static org.neo4j.driver.Values.ofLocalDateTime;
import static org.neo4j.driver.Values.parameters;
//import static org.neo4j.driver.Values.

//import static org.neo4j.driver.v1.Values.parameters;

public class Main implements AutoCloseable {
    private final Driver driverNeo4j;
    private final Jedis driverRedis;
    private final MongoClient mongoClient;

    public Main() {
        driverNeo4j = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "WJ7cbBLbwvuDaFF"));
        driverRedis = new Jedis("localhost");
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    }

    @Override
    public void close() throws Exception {
        driverNeo4j.close();
        driverRedis.close();
        mongoClient.close();
    }

    public void neo4jTest(final String message) {
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run("Create (a:Greeting)" + "SET a.message = $message " + "RETURN a.message + ', from node ' + id(a)", parameters("message", message));
                System.out.println("neo4j");
                return result.single().get(0).asString();
            }
        });
        System.out.println(greeting);
    }


    public void redisTest(final String message) {
        redis.clients.jedis.Transaction transaction = driverRedis.multi();
        transaction.set("Testkey", message);
        transaction.get("Testkey");
        List<Object> result = transaction.exec();
        for (Object response : result) {
            System.out.println(response);
        }
    }

    public void mongoTest(final String message) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("testCollection");
        Document doc1 = new Document("key1", "val1").append("arg1", "valArg1");
        mongoCollection.insertOne(doc1);
        mongoCollection.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document);
            }
        });
    }

    public static void main(String[] args) {
        try (Main main = new Main()) {
            //main.neo4jTest("hello, world");
            //main.redisTest("Redishallo");
            //main.mongoTest("");
            main.init();
            //CarFactory carfactory = new CarFactory(100);
            //carfactory.getCarList();
            //main.addUser(main.createUser());
            //Car car = main.createCar();
            //main.storeCar(car,main.createUser());
            //car.setStatus("Damaged");
            //main.updateCarStatus(car);
            //CarFactory carfactory = new CarFactory(1);
//            Car car = carfactory.getCarList().get(0);
//            UserFactory userFactory = new UserFactory(1);
//            User user = userFactory.getUserList().get(0);
//            main.addUser(user);
//            ReviewFactory reviewFactory = new ReviewFactory(1);
//            Rating rating = reviewFactory.getRatingList().get(0);
//            main.borrowCar(user,car,LocalDateTime.now(),new Date());
//            main.addUser(main.createUser());
//            Car car = main.createCar();
//            main.storeCar(car, user);
//            car.setStatus("Damaged");
//            main.updateCarStatus(car);
//            main.returnCar(user,car,rating,CarFactory.randDouble(49.0,50.0),CarFactory.randDouble(8.0,10.0),12.0);
//            System.out.println("Car Status: " + car.getStatus());
//            System.out.println("Car ID: " + car.getObjectID());

            System.out.println("Demand: " +main.demandArea(8.3,49.3,50000,2020,6));

            //main.demandArea(8.3,49.3,50000,2020,6);
            ArrayList<Area> areas = main.findHighDemandZone(6, 2020, 50000);
            for(Area area: areas){
                System.out.println(area);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeCar(Car car, User owner) {

        //Mongo
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        Document doc1 = car.toDocument();
        mongoCollection.insertOne(doc1);
        car.setObjectID(doc1.getObjectId("_id").toString());

        //Neo4j
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {
            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "CREATE (c:Car{objectID:$cID, manufacturer: $manufacturer, seats:$seats, type:$type, fuelConsumption:$fuelConsumption, status:$status, fuelType:$fuelType})" +
                                "MERGE (l: Location{longitude:$longitude, latitude:$latitude})" +
                                "WITH (l), (c)" +
                                "MATCH (u:User{objectID:$uID}) " +
                                "WITH (u), (l), (c)" +
                                "MERGE (u) -[:OWNES {offeringSince:$today}]-> (c) " +
                                "MERGE (c) -[:WAITING_HERE {from:$today}]-> (l) ",
                        parameters("$cID", car.getObjectID(),
                                "manufacturer", car.getManufacturer(),
                                "seats", car.getSeats(),
                                "type", car.getCarType(),
                                "fuelConsumption", car.getFuelConsumption(),
                                "longitude", car.getLongitude(),
                                "latitude", car.getLatitude(),
                                "status", car.getStatus(),
                                "fuelType", car.getFuelType(),
                                "uID", owner.getObjectID(),
                                "cID", car.getObjectID(),
                                "today", LocalDateTime.now()
                        ));
                return "done";
            }
        });
    }

    //Method to store a user in MongoDB and Neo4J
    public void addUser(User user) {


        //Mongo
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        Document doc1 = user.toDocument();

        mongoCollection.insertOne(doc1);
        System.out.println("Successfully added: " + doc1 + " to MongoDB");
        user.setObjectID(doc1.getObjectId("_id").toString());


        //Neo4j
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run("Create (a:User{objectID:$id})", parameters("id", user.getObjectID()));
                return "done";
            }
        });
        System.out.println("Successfully added: " + user + " to Neo4J");
    }

    public void storeSearchQuery(Query query, User user, double latitude, double longitude) {
        //Mongo
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("query");
        Document doc1 = query.toDocument();

        mongoCollection.insertOne(doc1);
        System.out.println("Successfully added: " + doc1 + " to MongoDB");
        query.setObjectID(doc1.getObjectId("_id").toString());


        //Neo4j
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run("MATCH (u:User{objectID:$uid}) " +
                                "MERGE (l:Location{longitude:$longitude, latitude:$latitude}) " +
                                "WITH (l),(u) " +
                                "MATCH (old:Location{}) " +
                                "WITH (u),(l),(old), " +
                                "ROUND(DISTANCE(point({ longitude: l.longitude, latitude: l.latitude }), point({ longitude: old.longitude, latitude: old.latitude }))) as dist " +
                                "WHERE dist<50000 AND NOT id(l) = id(old) " +
                                //"MERGE (l)-[:Dist{value:dist}]->(old) " +
                                //"MERGE (l)<-[:Dist{value:dist}]-(old) " +
                                "CREATE (u) -[:LOOKING_FOR_CARS{radius:$radius, date:$today}]-> (l)"
                        , parameters("uid", user.getObjectID(), "longitude", longitude, "latitude", latitude,
                                "radius", query.getRadius(), "today", LocalDateTime.now()));
                return "done";
            }
        });
        System.out.println("Successfully added: " + user + " to Neo4J");
    }


    public void updateUser(User user) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        Document doc1 = user.toDocument();

        mongoCollection.findOneAndUpdate(new Document("_id", user.getObjectID()), doc1);
    }

    public void updateCarStatus(Car car) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        Document doc1 = car.toDocument();
        BasicDBObject update = new BasicDBObject();
        update.put("$set", new Document("status", car.getStatus()));
        mongoCollection.findOneAndUpdate(new Document("_id", new ObjectId(car.getObjectID())), update);

        //Neo4j
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (c:Car{objectID : $c_ID})" + " SET c.status = $status" + " RETURN c.status",
                        parameters("c_ID", car.getObjectID(),
                                "status", car.getStatus()
                        ));
                return result.single().get(0).asString();
            }
        });
    }

    public void updateCarLocation(Car car) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        Document doc1 = car.toDocument();

        mongoCollection.findOneAndUpdate(new Document("_id", car.getObjectID()), doc1);

        //Neo4j
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (c:Car{id:$c_ID})" +
                                "MERGE (l: Location{longitude:$longitude, latitude:$latitude})" +
                                "MERGE (c) -[:WAITING_HERE {from:$today}]-> (l) ",
                        parameters("c_ID", car.getObjectID(),
                                "status", car.getStatus(),
                                "today", LocalDateTime.now()

                        ));
                return "done";
            }
        });
    }


    public void init() {
        ArrayList<Car> cars = new CarFactory(10).getCarList();
        ArrayList<User> users = new UserFactory(10).getUserList();
        ArrayList<Query> queries = new QueryFactory(10).create();
        ArrayList<Rating> ratings = new ReviewFactory(10).getRatingList();
        createSearches(users, queries);

        for (User user : users) {
            addUser(user);
            if (user.getQueries() != null)
                for (Query query : user.getQueries()) {
                    storeSearchQuery(query, user, CarFactory.randDouble(49.008091, 51), CarFactory.randDouble(8.403760, 10));

                }
        }

        for (Car car : cars) {
            storeCar(car, users.get(QueryFactory.randInt(0, (users.size() - 1) / 20 + 1)));
        }
        for (Rating rating : ratings) {
            User user = users.get(QueryFactory.randInt(0, (users.size() - 1)));
            Car car = cars.get(QueryFactory.randInt(0, (cars.size() - 1)));

            rating.setCar(car);
            rating.setUser(user);

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 2); // adds one hour

            borrowCar(user, car, LocalDateTime.now(), cal.getTime());
            returnCar(user, car, rating, CarFactory.randDouble(49.008091, 51), CarFactory.randDouble(8.403760, 10), CarFactory.randDouble(1, 100));
        }

        for (Car car: cars
             ) {
            System.out.println("UC5 This car has a general rating of: " + getCarRating(car));
        }

    }

    //matches search queries and user. Also stores them in our DBs
    private void createSearches(ArrayList<User> users, ArrayList<Query> queries) {
        for (Query query : queries) {
            User user = users.get(QueryFactory.randInt(0, users.size() - 1));
            user.addQuery(query);
        }
    }

    public void borrowCar(User user, Car car, LocalDateTime from, Date till) {


        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {

                Result result = tx.run(
                        "MATCH (c:Car{objectID:$cID}), (u:User{objectID:$uID})" +
                                "WITH (u), (c)" +
                                "MERGE (u) -[b:BORROWS{from:$from, till:$till}]->(c)"
                        ,
                        parameters("cID", car.getObjectID(),
                                "from", from.toString(),
                                "till", till.toString(),
                                "uID", user.getObjectID()
                        ));
                return "done";
            }
        });
    }

    //Part of UC 5
    public void returnCar(User user, Car car, Rating rating, double latitude, double longitude, double km){

        //Update Status of Car, Save the Raing in Mongo DB
        car.setStatus("Available");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");

        //Store car review
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("review");
        Document doc1 = rating.toDocument();
        mongoCollection.insertOne(doc1);
        updateCarStatus(car);

        //Create Review Relations
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (c:Car{objectID:$c_ID}) " +
                                "MATCH (u:User{objectID:$u_ID}) " +
                                "SET c.status = $status " +
                                "WITH (c),(u)" +
                                "MATCH (u) -[b:BORROWS]->(c) " +
                                "SET b.returned = $today " +
                                "WITH (u), (c) " +
                                "MERGE (l: Location{longitude:$longitude, latitude:$latitude}) " +
                                "MERGE (c) -[:WAITING_HERE {from:$today}]-> (l) " +
                                "MERGE (u) -[r:GIVES_RATING {CLEAN:$clean, RELIABLE:$reliable, COMFORT:$comfort, COMMENT:$comment, FROM:$today}]-> (c) " +
                                "MERGE (u) -[:BORROWS{returned:$today, km:$km}]-> (c) ",
                        parameters("c_ID", car.getObjectID(),
                                "status", car.getStatus(),
                                "today", LocalDateTime.now(),
                                "clean", rating.getCleanliness(),
                                "reliable", rating.getReliability(),
                                "comfort", rating.getComfort(),
                                "comment", rating.getComments(),
                                "u_ID", user.getObjectID(),
                                "longitude", longitude,
                                "latitude", latitude,
                                "km", km
                        ));
                return "done";
            }
        });
        calculateCarRating(car);
    }

    //Use Case 5 Maurice Chrisnach
    public double calculateCarRating(Car car) {
        //calculate Car rating
        Session session = driverNeo4j.session();
        String rating = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (c:Car{objectID:$c_ID})<-[rating:GIVES_RATING]-() " +
                                "RETURN apoc.number.format(((avg(rating.RELIABLE) + avg(rating.CLEAN) + avg(rating.COMFORT))/3), '#.#')",
                        parameters(
                                "c_ID", car.getObjectID()
                        ));
                return result.next().get(0).toString();
            }
        });
        double carRating = 0;
        if(rating != "NULL"){
            carRating = Double.parseDouble(rating.replace('\"',' '));
        }
        System.out.println("Car Rating: " + carRating);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        car.setRating(carRating);
        updateCarRating(car);
        return carRating;
    }

    //Part of UC5
    public void updateCarRating(Car car) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        BasicDBObject update = new BasicDBObject();
        update.put("$set", new Document("rating", car.getRating()));
        mongoCollection.findOneAndUpdate(new Document("_id", new ObjectId(car.getObjectID())), update);
    }

    public double getCarRating(Car car){
        double carRating = -1;
        MongoDatabase mongoDatabase = mongoClient.getDatabase("CarSharing");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("car");
        Document doc1 = mongoCollection.find(new Document("_id", new ObjectId(car.getObjectID()))).first();
        if(doc1.containsKey("rating"))
            carRating = doc1.getDouble("rating");
        return carRating;
    }
    //Use Case 2 Maximilian Schuhmacher

    public ArrayList<Area> findHighDemandZone(int month, int year, int areaSize) {
        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (l:Location), (o:Location)\n" +
                                "WHERE ROUND(DISTANCE(point({ longitude: l.longitude, latitude: l.latitude }), point({ longitude: o.longitude, latitude: o.latitude}))) <= $areaSize\n" +
                                "OPTIONAL MATCH  (l)<-[s:LOOKING_FOR_CARS]-(u)\n" +
                                "WHERE (s.date.year = $year AND s.date.month=$month)\n" +
                                "WITH l,s, u ,o\n" +
                                "OPTIONAL MATCH (l)<-[w:WAITING_HERE]-(c)\n" +
                                "WHERE (w.from.month<=$month AND (w.till.month>=$month OR w.till IS NULL) AND w.from.year<=$year AND (w.till.year>=$year OR w.till IS NULL))\n" +
                                "RETURN (count(u)*1.0)/(count(c)+1) as score, o.longitude, o.latitude, count(distinct(c)), count(u)",
                        parameters(
                                "month", month, "year", year, "areaSize", areaSize
                        ));
                String retResult = "";
                //0:score, 1:long, 2:lat
                for (Record record : result.list()) {
                    retResult += record.get(0).toString() + "," + record.get(1).toString() + "," + record.get(2).toString() + ","+record.get(3)+","+record.get(4)+ ";";
                }
                System.out.println(retResult);
                return retResult;
            }
        });
        String[] string = greeting.split(";");

        ArrayList<Area> result = new ArrayList<>();
        for (int i = 0; i < string.length; i++) {
            String[] values = string[i].split(",");
            Area area = new Area(Double.parseDouble(values[1]), Double.parseDouble(values[2]), areaSize, Double.parseDouble(values[0]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
            result.add(area);
        }
        return result;
    }

    public ArrayList<Integer> demandArea(double longitude, double latitude, int areaSize, int year, int month) {

/*____
MATCH (l:Location)
WHERE ROUND(DISTANCE(point({ longitude: l.longitude, latitude: l.latitude }), point({ longitude: 8.3, latitude: 49.3}))) < 50000
OPTIONAL MATCH  (l)<-[s:LOOKING_FOR_CARS]-(u)
WHERE (s.date.year = 2020 AND s.date.month=6)
WITH l,s,u
OPTIONAL MATCH (l)<-[w:WAITING_HERE]-(c)
WHERE (w.from.month<=6 AND (w.till.month>=6 OR w.till IS NULL) AND w.from.year<=2020 AND (w.till.year>=2020 OR w.till IS NULL))
RETURN count(distinct (c)), count(u)
         */

        Session session = driverNeo4j.session();
        String greeting = session.writeTransaction(new TransactionWork<String>() {

            @Override
            public String execute(Transaction tx) {
                Result result = tx.run(
                        "MATCH (l:Location) " +
                                "WHERE ROUND(DISTANCE(point({ longitude: l.longitude, latitude: l.latitude }), point({ longitude: $longitude, latitude: $latitude}))) < $areaSize " +
                                "OPTIONAL MATCH  (l)<-[s:LOOKING_FOR_CARS]-(u) " +
                                "WHERE (s.date.year = $year AND s.date.month=$month) " +
                                "WITH l,s,u " +
                                "OPTIONAL MATCH (l)<-[w:WAITING_HERE]-(c) " +
                                "WHERE (w.from.month<=$month AND (w.till.month>=$month OR w.till IS NULL) AND w.from.year<=$year AND (w.till.year>=$year OR w.till IS NULL)) " +
                                "RETURN count(distinct (c)), count(u)",
                        parameters(
                                "longitude", longitude, "latitude", latitude, "month", month, "year", year, "areaSize", areaSize
                        ));
                Record record = result.single();
                String retResult = record.get(0).toString() + ":" + record.get(1).toString();

                return retResult;
            }
        });
        String[] string = greeting.split(":");
        ArrayList<Integer> result = new ArrayList<>();
        result.add(Integer.parseInt(string[0]));
        result.add(Integer.parseInt(string[1]));
        return result;

    }
}

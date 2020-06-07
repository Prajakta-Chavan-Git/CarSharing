

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import createObjects.CarFactory;
import createObjects.UserFactory;
import jdk.vm.ci.aarch64.AArch64;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
//import org.neo4j.driver.v1.*;
import redis.clients.jedis.Jedis;
import storedobjects.Address;
import storedobjects.Car;
import storedobjects.Query;
import storedobjects.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

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
            //main.init();
            //main.addUser(main.createUser());
            //main.storeCar(main.createCar(),main.createUser());
            CarFactory carFactory = new CarFactory(10);
            System.out.println(carFactory.getCarList());
            UserFactory userFactory = new UserFactory(20);
            System.out.println(userFactory.getUserList());
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
                        "CREATE (a:Car{objectID:$cID, manufacturer: $manufacturer, seats:$seats, type:$type, fuelConsumption:$fuelConsumption, status:$status, fuelType:$fuelType})" +
                                "MERGE (l: Location{longitude:$longitude, latitude:$latitude})"+
                                "WITH (l)" +
                                "MATCH (u:User{objectID:$uID}) " +
                                "WITH (u)"+
                                "MERGE (u) -[:OWNES {offeringSince:$today}]-> (c) "+
                                "MERGE (c) -[:WAITING_HERE {FROM:$today}]-> (l) ",
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
                                "today", LocalDate.now()
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

    public void init(){
        User alice =  new User(new Date(1998,5,23), null, "Alice BlueDress", new Address("Heidelberg", "WonderlandStreet","314", "67123"),
                "+49152514586436", "wonderland@redQueen.com", "DE15000523001125", "User",null);
        addUser(alice);


    }

    //Creating Test Data
    public Car createCar(){
            Car car = new Car(
                    (int)(Math.random() * ((7-2) +1))+2,
                    null,
                    "Audi",
                    "Limousine",
                    9.9,
                    5.101030103101,
                    4.1212100113,
                    "Available",
                    "Diesel");
            return car;
    }

    public User createUser(){
        User user1 = new User(
                new Date(2000,10,25),
                null,
                "Max Mustermann",
                new Address("Heidelberg","Haydnstrasse", "14", "21031"),
                "+123111222333",
                "maxmuster@gmail.com",
                "DE10 1002 3124 2749 1752",
                "Available",
                null);
        return user1;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db4o_pilot;

import db4o_pilot.*;
import java.io.*;
import java.util.*;
import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;
import com.db4o.ObjectContainer;

public class db4o_pilot {//kelas ini di-run untuk mengeksekusi semua methods

    final static String DB4OFILENAME = "Car_Pilot.yap";

    // System.getProperty("/Car_Pilot.yap");
    public static void main(String[] args) {
         new File(DB4OFILENAME).delete();
        //ObjectContainer db=Db4o.openFile("Car_Pilot.yap");
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4OFILENAME);
        try {
                storeCarCommit(db);
                storeCarRollback(db);
//            storeFirstCar(db);
//            storeSecondCar(db);
//            retrieveAllCarsQBE(db);
//            retrieveAllPilotsNative(db);
//            retrieveAllPilotsQBE(db);
//            retrieveCarByPilotQBE(db);
//            retrieveCarByPilotNameQuery(db);
//            retrieveCarByPilotProtoQuery(db);
//            retrievePilotByCarModelQuery(db);
//            updateCar(db);
//            updatePilotSingleSession(db);
//            updatePilotSeparateSessionsPart1(db);
//            db.close();
//            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4OFILENAME);
//            updatePilotSeparateSessionsPart2(db);
//            db.close();
//            updatePilotSeparateSessionsImprovedPart1();
//            updatePilotSeparateSessionsImprovedPart2();
//            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4OFILENAME);
//            deleteFlat(db);
//            db.close();
//            deleteDeep();
//            deleteDeepRevisited();
//            retrieveAllPilotsNative(db);
        } finally {
            db.close();
        }
    }
    
    public static void storeCarCommit(ObjectContainer db){
        Pilot pilot     =   new Pilot("Ruben Barrichelo", 99);
        Car car         =   new Car("BMW");
        car.setPilot(pilot);
        db.store(car);
        db.commit();    
        ObjectSet result =  db.queryByExample(Car.class);
        listResult(result);
    }
    
    public static void storeCarRollback(ObjectContainer db){
        Pilot pilot     =   new Pilot("Michael Schumacher", 100);
        Car car         =   new Car("Ferrari");
        car.setPilot(pilot);;
        db.store(car);
        db.rollback();
        ObjectSet result=   db.queryByExample(Car.class);
        listResult(result);
    }

    public static void storeFirstCar(ObjectContainer db) {
        Car car1 = new Car("Ferrari");
        Pilot pilot1 = new Pilot("Michael Schumacher", 100);
        car1.setPilot(pilot1);
        db.store(car1);
    }

    public static void storeSecondCar(ObjectContainer db) {
        Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
        db.store(pilot2);
        Car car2 = new Car("BMW");
        car2.setPilot(pilot2);
        db.store(car2);
    }

    public static void retrieveAllCarsQBE(ObjectContainer db) {
        Car proto = new Car(null);
        ObjectSet result = db.queryByExample(proto);
        listResult(result);
    }

    public static void retrieveAllPilotsQBE(ObjectContainer db) {
        Pilot proto = new Pilot(null, 0);
        ObjectSet result = db.queryByExample(proto);
        listResult(result);
    }

    public static void retrieveAllPilots(ObjectContainer db) {
        ObjectSet result = db.queryByExample(Pilot.class);
        listResult(result);
    }

    public static void retrieveCarByPilotQBE(ObjectContainer db) {
        Pilot pilotproto = new Pilot("Rubens Barrichello", 0);
        Car carproto = new Car(null);
        carproto.setPilot(pilotproto);
        ObjectSet result = db.queryByExample(carproto);
        listResult(result);
    }

    public static void retrieveCarByPilotNameQuery(ObjectContainer db) {
        Query query = db.query();
        query.constrain(Car.class);

        query.descend("pilot").descend("name").constrain("Rubens Barrichello");
        ObjectSet result = query.execute();
        listResult(result);
    }

    public static void retrieveCarByPilotProtoQuery(ObjectContainer db) {
        Query query = db.query();
        query.constrain(Car.class);
        Pilot proto = new Pilot("Rubens Barrichello", 0);
        query.descend("pilot").constrain(proto);
        ObjectSet result = query.execute();
        listResult(result);
    }

    public static void retrievePilotByCarModelQuery(ObjectContainer db) {
        Query carquery = db.query();
        carquery.constrain(Car.class);
        carquery.descend("model").constrain("Ferrari");
        Query pilotquery = carquery.descend("pilot");
        ObjectSet result = pilotquery.execute();
        listResult(result);
    }

    public static void retrieveAllPilotsNative(ObjectContainer db) {
        List<Pilot> results = db.query(new Predicate<Pilot>() {
            public boolean match(Pilot pilot) {
                return true;
            }
        });
        //listResult(results);
        listNQPilot(results);

    }

    public static void retrieveAllCars(ObjectContainer db) {
        ObjectSet results = db.queryByExample(Car.class);
        listResult(results);
    }

    public static void retrieveCarsByPilotNameNative(ObjectContainer db) {
        final String pilotName = "Rubens Barrichello";
        List<Car> results = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getPilot().getName().equals(pilotName);
            }
        });
        //listResult(results);
        listNQCar(results);
    }

    public static void updateCar(ObjectContainer db) {
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        Car found = (Car) result.get(0);
        found.setPilot(new Pilot("Somebody else", 0));
        db.store(found);
        result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        // listResult(result);
        listNQCar(result);
    }

    public static void updatePilotSingleSession(ObjectContainer db) {
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });

        Car found = result.get(0);
        found.getPilot().addPoints(1);
        db.store(found);
        result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        //listResult(result);
        listNQCar(result);
    }

    public static void
            updatePilotSeparateSessionsPart1(ObjectContainer db) {
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        Car found = result.get(0);
        found.getPilot().addPoints(1);
        db.store(found);
    }

    public static void
            updatePilotSeparateSessionsPart2(ObjectContainer db) {
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        //listResult(result);
        listNQCar(result);
    }

    public static void updatePilotSeparateSessionsImprovedPart1() {
        EmbeddedConfiguration config
                = Db4oEmbedded.newConfiguration();
        config.common().objectClass("db4o_structured_storing.Car").cascadeOnUpdate(true);

        ObjectContainer db = Db4oEmbedded.openFile(config,
                DB4OFILENAME);

        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        if (result.size() > 0) {
            Car found = result.get(0);
            found.getPilot().addPoints(1);
            db.store(found);
        }
        db.close();
    }

    public static void updatePilotSeparateSessionsImprovedPart2() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded
                .newConfiguration(), DB4OFILENAME);

        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        Car car = result.get(0);
        //listResult(result);
        listNQCar(result);
        db.close();
    }

    public static void deleteFlat(ObjectContainer db) {
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("Ferrari");
            }
        });
        Car found = result.get(0);
        db.delete(found);
        result = db.queryByExample(new Car(null));
        //listResult(result);
        listNQCar(result);

    }

    public static void deleteDeep() {
        EmbeddedConfiguration config
                = Db4oEmbedded.newConfiguration();
        config.common().objectClass("db4o_structured_storing.Car").cascadeOnDelete(true);
        ObjectContainer db = Db4oEmbedded.openFile(config, DB4OFILENAME);
        List<Car> result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return car.getModel().equals("BMW");
            }
        });
        if (result.size() > 0) {
            Car found = result.get(0);
            db.delete(found);
        }
        result = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return true;
            }
        });
        //listResult(result);
        listNQCar(result);
        db.close();
    }

    public static void deleteDeepRevisited() {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        //db4o_structured_storing
        config.common().objectClass("db4o_structured_storing.Car").cascadeOnDelete(true);
        ObjectContainer db = Db4oEmbedded.openFile(config, DB4OFILENAME);
        ObjectSet<Pilot> result = db.query(new Predicate<Pilot>() {
            public boolean match(Pilot pilot) {
                return pilot.getName().equals("Michael Schumacher");
            }
        });
        if (!result.hasNext()) {
            System.out.println("Pilot not found!");
            db.close();
            return;
        }
        Pilot pilot = (Pilot) result.next();
        Car car1 = new Car("Ferrari");
        Car car2 = new Car("BMW");
        car1.setPilot(pilot);
        car2.setPilot(pilot);
        db.store(car1);
        db.store(car2);
        db.delete(car2);
        List<Car> cars = db.query(new Predicate<Car>() {
            public boolean match(Car car) {
                return true;
            }
        });
        //listResult(cars);
        listNQCar(cars);
        db.close();
    }

//ObjectSet
    public static void listResult(ObjectSet result) {
        System.out.println(result.size());
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void listNQCar(List<Car> car1) {
        Iterator iterCar = car1.iterator();
        System.out.println("----ListNQ--Car----");
        while (iterCar.hasNext()) {
            System.out.println(iterCar.next());
        }
    }

    public static void listNQPilot(List<Pilot> pilot1) {
        Iterator iterpilot = pilot1.iterator();
        System.out.println("----ListNQ-PILOT----");
        while (iterpilot.hasNext()) {
            System.out.println(iterpilot.next());
        }
    }

}//class

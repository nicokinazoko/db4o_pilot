/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db4o_pilot;

/**
 *
 * @author TUF
 */
public class Car// class Car.java
{

    public String model;
    public Pilot pilot;

    public Car(String model) {
        this.model = model;
        this.pilot = null;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public String getModel() {
        return model;
    }

    public String toString() {
        return model + "[" + pilot + "]";
    }
}

package stmu_cs.viaratingsystem;

import java.io.Serializable;

/**
 * Created by Catherine Etter on 2/24/2018.
 */

public class DriverModel implements Serializable {
    public String firstName;
    public String lastName;
    public String id;
    public int currentBus;
    public double currentRating;

    public DriverModel(String firstName, String lastName, String id, int currentBus, double currentRating) {
        setFirstName(firstName);
        setLastName(lastName);
        setId(id);
        setCurrentBus(currentBus);
        setCurrentRating(currentRating);
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getIdName() { return id; }

    public int getCurrentBus() { return currentBus; }

    public double getCurrentRatting() { return currentRating; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCurrentBus(int currentBus) {
        this.currentBus = currentBus;
    }

    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }
}

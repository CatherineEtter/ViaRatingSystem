package stmu_cs.viaratingsystem;

import java.io.Serializable;

/**
 * Created by Catherine Etter on 2/24/2018.
 */

public class DriverModel implements Serializable {
    public String firstName;
    public String lastName;
    public int id;
    public int currentBus;
    public double currentRating;

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public int getIdName() { return id; }

    public int getCurrentBus() { return currentBus; }

    public double getCurrentRatting() { return currentRating; }
}

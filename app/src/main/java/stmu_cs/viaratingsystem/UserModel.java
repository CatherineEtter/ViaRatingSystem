package stmu_cs.viaratingsystem;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by mchri on 2/23/2018.
 */

public class UserModel implements Serializable {
    public String email;
    public String userId;
    public String password;
    public int points;

    public UserModel(String email, String userId, String password, int points) {
        setEmail(email);
        setPoints(points);
        setUserId(userId);
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}

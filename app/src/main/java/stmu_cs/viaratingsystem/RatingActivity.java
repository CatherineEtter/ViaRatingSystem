package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mchri on 2/23/2018.
 */

public class RatingActivity extends AppCompatActivity {
    Button submitButton;
    TextView driverLabel;
    EditText comments;
    RatingBar ratingBar;
    DriverModel driver;
    UserModel user;
    ActionBar actionBar;
    DatabaseReference reference;
    DatabaseReference driversReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rating);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        driver = (DriverModel) getIntent().getSerializableExtra("Driver");
        driverLabel = findViewById(R.id.driver_name_label);
        comments = findViewById(R.id.comments_input);
        ratingBar = findViewById(R.id.rating_bar);
        submitButton = findViewById(R.id.submit_rating_button);
        comments.setImeOptions(EditorInfo.IME_ACTION_DONE);

        driverLabel.setText(driver.firstName);
        user = (UserModel) getIntent().getSerializableExtra("User");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double rating = Double.parseDouble(Float.toString(ratingBar.getRating()));
                if(rating == 0) {

                    Toast toast = Toast.makeText(getBaseContext(),"Please enter a rating",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else {
                    user.points += 10;
                    Intent intent = new Intent(getBaseContext(),EarnedActivity.class);
                    startActivity(intent);

                    getIntent().putExtra("User", user);

                    reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    driversReference = FirebaseDatabase.getInstance().getReference().child("Drivers");

                    updateDB(rating);
                }
            }
        });

//        Bundle extras = getIntent().getExtras();
//        //If something was sent into this activity
//        if(extras != null) {
//
//            driver = (DriverModel) getIntent().getSerializableExtra("Driver");
//
//            //temporarily appends
//            driverLabel.append(driver.firstName);
//        }
//        else{
//            driverLabel.append("Null Driver");
//        }
    }
    private void updateDB(Double rating) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(user.userId, user);

        driver.currentRating = (driver.currentRating+rating)/2;

        Map<String, Object> driverMap = new HashMap<>();
        driverMap.put(driver.id, driver);

        driversReference.updateChildren(driverMap);
        reference.updateChildren(userMap);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

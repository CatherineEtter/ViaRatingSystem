package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by mchri on 2/23/2018.
 */

public class RatingActivity extends AppCompatActivity {
    Button submitButton;
    EditText busNumber;
    EditText comments;
    RatingBar ratingBar;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rating);

        busNumber = findViewById(R.id.bus_number_input);
        comments = findViewById(R.id.comments_input);
        ratingBar = findViewById(R.id.rating_bar);
        submitButton = findViewById(R.id.submit_rating_button);

        //TODO: Go to redeem page only when rating is entered
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),EarnedActivity.class);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        //If something was sent into this activity
        if(extras != null) {

            String sarray = extras.getString("data");
/*
            ValueEventListener listener = new ValueEventListener() {
                // ...
            };
            ref.addValueEventListener(listener);

*/

            //temporarily appends
            comments.append(sarray);
        }
        else{
            comments.append("Null Array");
        }
    }
}

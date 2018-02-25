package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

public class activity_qrcode extends AppCompatActivity {
    private Button scan_btn;
    private Button redeem_btn;
    private Button rate_btn;
    private UserModel user;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    private TextView pointsDisplay;
    private String resultContents;
    private DatabaseReference driversReference;
    DriverModel driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("User");
        String value = intent.getStringExtra("start");
        setContentView(R.layout.activity_qrcode);
        scan_btn = findViewById(R.id.scan_btn);
        redeem_btn = findViewById(R.id.redeem_btn);
        rate_btn = findViewById(R.id.rate_btn);
        rate_btn.setEnabled(false);
        pointsDisplay = findViewById(R.id.points_txt);

        pointsDisplay.setText(Integer.toString(user.points));

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity_qrcode.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        redeem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),ClaimRewardsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driversReference = FirebaseDatabase.getInstance().getReference().child("Drivers");
                driversReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(searchForDriver(Integer.parseInt(resultContents), dataSnapshot)) {
                                    //do a thing
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle error
                            }
                        }
                );

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast toast = Toast.makeText(this,"Scan cancelled",Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(getBaseContext(),"Scan Cancelled",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
            else {
                user.points += 2;
                pointsDisplay.setText(Integer.toString(user.points));
                upDateUserDB(user);
                rate_btn.setEnabled(true);
                resultContents = result.getContents();


            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean searchForDriver(int busNumber, DataSnapshot dataSnapshot) {
        for(DataSnapshot child : dataSnapshot.getChildren()) {
            String driverId = child.getKey();
            int currentBus = Integer.parseInt(dataSnapshot.child(driverId).child("currentBus").getValue().toString());
            if(currentBus == busNumber) {
                Toast.makeText(getApplicationContext(), "Driver Found!", Toast.LENGTH_SHORT).show();
                String fName = dataSnapshot.child(driverId).child("firstName").getValue().toString();
                String lName = dataSnapshot.child(driverId).child("lastName").getValue().toString();
                Double rating = Double.parseDouble(dataSnapshot.child(driverId).child("currentRating").getValue().toString());
//                driver.currentBus = currentBus;
//                driver.id = driverId;
//                driver.firstName = fName;
//                driver.lastName = lName;
//                driver.currentRating = rating;
                driver = new DriverModel(fName, lName, driverId, currentBus, rating);
                Intent intent = new Intent(getBaseContext(),RatingActivity.class);
                System.out.print("++++++++++++++++++++++++++\n" + driver.firstName + "\n++++++++++++++++++++++++");
                intent.putExtra("Driver",driver);
                intent.putExtra("User",user);
                //go to rating screen
                startActivity(intent);
                return(true);
            }
        }
        return(false);
    }



    private void upDateUserDB(UserModel user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(user.userId, user);

        reference.updateChildren(userMap);
    }
}

package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class activity_qrcode extends AppCompatActivity {
    private Button scan_btn;
    private Button redeem_btn;
    private UserModel user;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    private TextView pointsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("User");
        String value = intent.getStringExtra("start");
        setContentView(R.layout.activity_qrcode);
        scan_btn = findViewById(R.id.scan_btn);
        redeem_btn = findViewById(R.id.redeem_btn);
        pointsDisplay = findViewById(R.id.yourpoints_txt);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this,"Scan cancelled",Toast.LENGTH_LONG).show();
            }
            else {
                user.points += 2;
                pointsDisplay.setText(Integer.toString(user.points));
                upDateUserDB(user);
                Intent intent = new Intent(getBaseContext(),RatingActivity.class);
                intent.putExtra("data",result.getContents());
                //go to rating screen
                startActivity(intent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void upDateUserDB(UserModel user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(user.userId, user);

        reference.updateChildren(userMap);
    }
}

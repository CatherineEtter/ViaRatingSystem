package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mchri on 2/24/2018.
 */

public class ClaimRewardsActivity extends AppCompatActivity{
    ProgressBar progressBar;
    TextView textView;
    Button claimButton;
    UserModel user;
    DatabaseReference reference;
    ActionBar actionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("User");
        setContentView(R.layout.activity_claim_rewards);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.reward_name);
        claimButton = findViewById(R.id.claim_button);

        progressBar.setMax(100);
        progressBar.setProgress(user.points);

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.points >= 100) {
                    user.points -= 100;
                    upDateUserDB(user);
                    progressBar.setProgress(user.points);
                    Toast.makeText(getApplicationContext(),"You claimed Reward 1!. Remaining point is "+user.points,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ClaimRewardsActivity.this, activity_qrcode.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You don't have enough points !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void upDateUserDB(UserModel user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(user.userId, user);

        reference.updateChildren(userMap);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

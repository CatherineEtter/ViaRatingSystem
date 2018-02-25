package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EarnedActivity extends AppCompatActivity {
    Button dashboard_btn;
    TextView points_earned_txt;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dashboard_btn = findViewById(R.id.dashboard_btn);
        points_earned_txt = findViewById(R.id.points_earned_txt);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_bounce );
        points_earned_txt.startAnimation(animation);

        dashboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(),activity_qrcode.class);
//                startActivity(intent);
                onSupportNavigateUp();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

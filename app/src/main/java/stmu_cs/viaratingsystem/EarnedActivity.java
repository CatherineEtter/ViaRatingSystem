package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EarnedActivity extends AppCompatActivity {
    Button dashboard_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned);

        dashboard_btn = findViewById(R.id.dashboard_btn);

        dashboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),activity_qrcode.class);
                startActivity(intent);
            }
        });
    }
}

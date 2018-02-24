package stmu_cs.viaratingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    UserModel user;
    Button button;
    EditText editText;
    DatabaseReference reference;
//    Gson gson;
//    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.CheckInput);
        editText = findViewById(R.id.emailInput);
        user = new UserModel();
        reference = FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() != null) {
                    user.setEmail(editText.getText().toString().toLowerCase());
                    user.setPoints(0);
                    user.setPassword("password");
                    user.setUserId("12345678");
                    JSONObject json = new JSONObject();

                    try {
                        json.put("email", user.getEmail());
                        json.put("password", user.getPassword());
                        json.put("userId", user.getUserId());
                        json.put("points", user.getPoints());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    reference.child("viaratingsystem").child(user.getUserId()).child(json.toString());
                }
            }
        });
    }
}

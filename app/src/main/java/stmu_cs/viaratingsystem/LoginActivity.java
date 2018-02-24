package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.autofill.Dataset;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    UserModel user;
    Button button;
    Button newUserButton;
    EditText editText;
    DatabaseReference reference;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.CheckInput);
        editText = findViewById(R.id.emailInput);
        imageView = findViewById(R.id.via_image);
        imageView.setImageResource(R.drawable.via_logo);
        newUserButton = findViewById(R.id.newUser);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() != null) {
                    EditText email_edittxt = (EditText) findViewById(R.id.emailInput);
                    EditText password_edittxt = (EditText) findViewById(R.id.passwordInput);

                    final String semail_edittxt = email_edittxt.getText().toString();
                    final String spassword_edittxt = password_edittxt.getText().toString();
                    if(semail_edittxt.length() == 0) {
                        //Toast.makeText(getApplicationContext(),"Email cannot be blank!",Toast.LENGTH_SHORT).show();
                        Toast toast = Toast.makeText(getBaseContext(),"Email cannot be blank!",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                    }
                    else if(spassword_edittxt.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Must enter a password!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //user = new UserModel(editText.getText().toString().toLowerCase(), "34567890", "wordpass", 0);
                        reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(!userExists(semail_edittxt.toLowerCase(), spassword_edittxt, dataSnapshot)) {
                                            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
                                            //createNewUser(reference, semail_edittxt.toLowerCase());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //handle error
                                    }
                                }
                        );

                    }
                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void createNewUser(DatabaseReference reference, String email) {
        Random random = new Random();
        long id = random.nextLong();
        while(id < 0) {
            id = random.nextLong();
        }
        String userId = Long.toString(id);

        UserModel newUser = new UserModel(email, userId, "password", 0);
        Map<String, Object> userMap = new HashMap<>();
        user = newUser;
        userMap.put(newUser.userId, newUser);

        reference.updateChildren(userMap);

        Intent intent = new Intent(LoginActivity.this, activity_qrcode.class);
        intent.putExtra("User", user);
        LoginActivity.this.startActivity(intent);

    }

    private Boolean userExists(String emailEntered, String passwordEntered, DataSnapshot snapshot) {
        for(DataSnapshot child : snapshot.getChildren()) {
            String userId = child.getKey();
            String email = snapshot.child(userId).child("email").getValue().toString().toLowerCase();
            String password = snapshot.child(userId).child("password").getValue().toString();

            if(emailEntered.equals(email) && passwordEntered.equals(password)) {
                int points = Integer.parseInt(snapshot.child(userId).child("points").getValue().toString());
                String id = snapshot.child(userId).child("userId").getValue().toString();
                user = new UserModel(email, id, password, points);

                Intent intent = new Intent(LoginActivity.this, activity_qrcode.class);
                intent.putExtra("User", user);
                LoginActivity.this.startActivity(intent);

                return (true);
            }
        }
        return (false);
    }

}

package stmu_cs.viaratingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mchri on 2/24/2018.
 */

public class NewUserActivity extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    EditText confirmPasswordInput;
    Button createUserButton;
    UserModel user;
    ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");;
        emailInput = findViewById(R.id.newEmailInput);
        passwordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        createUserButton = findViewById(R.id.createUserButton);



        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput.getText().toString() != null) {
                    EditText email_edittxt = (EditText) findViewById(R.id.newEmailInput);
                    EditText password1_edittxt = (EditText) findViewById(R.id.newPasswordInput);
                    EditText password2_edittxt = (EditText) findViewById(R.id.confirmPasswordInput);

                    final String sEmail = email_edittxt.getText().toString().toLowerCase();
                    final String sPassword1 = password1_edittxt.getText().toString();
                    final String sPassword2 = password2_edittxt.getText().toString();

                    if(checkInput(sEmail, sPassword1, sPassword2)) {
                        reference.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(checkDatabase(sEmail, dataSnapshot)) {
                                            Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            createUser(sEmail, sPassword1);
                                            Intent intent = new Intent(NewUserActivity.this, activity_qrcode.class);
                                            intent.putExtra("User", user);

                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
                    }
                }

            }
        });




    }

    private boolean checkInput(String email, String password1, String password2){
        if(email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Email cannot be blank!", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(password1.length() == 0){
            Toast.makeText(getApplicationContext(), "Must enter a password!", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(password2.length() == 0) {
            Toast.makeText(getApplicationContext(), "Must confirm password!", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return(false);
        }

        return(true);
    }

    private boolean checkDatabase(String email, DataSnapshot snapshot) {
        for(DataSnapshot child : snapshot.getChildren()) {
            String userId = child.getKey();
            String emailInDB = snapshot.child(userId).child("email").getValue().toString().toLowerCase();

            if(emailInDB.equals(email)) {
                return (true);
            }
        }
        return (false);
    }

    private void createUser(String email, String password) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        Random random = new Random();
        long id = random.nextLong();
        while(id < 0) {
            id = random.nextLong();
        }
        String userId = Long.toString(id);

        UserModel newUser = new UserModel(email, userId, "password", 0);
        Map<String, Object> userMap = new HashMap<>();

        userMap.put(newUser.userId, newUser);

        reference.updateChildren(userMap);
        user = newUser;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

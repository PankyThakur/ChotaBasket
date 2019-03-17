package mysecond.example.pankajthakr.barcodescanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity implements View.OnClickListener {
    EditText etuser, etpwd;
    Button btnsign;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etuser = findViewById(R.id.editTextusername1);
        etpwd = findViewById(R.id.editTextpassword1);
        btnsign = findViewById(R.id.buttonsignup1);
        mAuth = FirebaseAuth.getInstance();

        btnsign.setOnClickListener((View.OnClickListener) this);
        /*btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });*/

    }

    private void registerUser() {
        String email = etuser.getText().toString().trim();
        String password = etpwd.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),step1.class));
                        } else {
                            Toast.makeText(signup.this, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    @Override
    public void onClick(View view) {
        registerUser();

    }
}


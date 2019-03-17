package mysecond.example.pankajthakr.barcodescanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextemail,editTextpassword;
    Button buttonlogin,buttonsignup;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        editTextemail=findViewById(R.id.editTextusername);
        editTextpassword = findViewById(R.id.editTextpassword);
        buttonlogin = findViewById(R.id.buttonlogin);
        buttonsignup = findViewById(R.id.buttonsignup);

        buttonsignup.setOnClickListener((View.OnClickListener) this);
        buttonlogin.setOnClickListener(this);
    }
    private void userLogin(){
        String email = editTextemail.getText().toString().trim();
        String password  = editTextpassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog



        //logging in the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successfull
                        if(task.isSuccessful()){

                            if(editTextemail.getText().toString().equals("pankajmthakur01@gmail.com")){
                                Toast.makeText(LoginActivity.this,"Welcome Admin",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                            //start the profile activity
                            else {
                                Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), FetchActivity.class));
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Invalid Emailid or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonlogin){
            userLogin();
        }
       if(view == buttonsignup){
            startActivity(new Intent(getApplicationContext(),signup.class));
       }
    }
}

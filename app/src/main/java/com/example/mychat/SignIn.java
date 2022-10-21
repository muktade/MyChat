package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    ///sing in
    ActivitySignInBinding binding;
    ///progress bar
    ProgressDialog progressDialog;
    ///database
    private FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ///fire base auth
        auth = FirebaseAuth.getInstance();
        //progress Dialog bar
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("SignIn Progress");
        progressDialog.setMessage("Wait For SignIn...");

        getSupportActionBar().hide();

        ////
        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                auth.signInWithEmailAndPassword(
                        binding.etEmail.getText().toString(),
                        binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Intent intent= new Intent(SignIn.this, MainActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                binding.txClickSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SignIn.this, SignUp.class);
                        startActivity(intent);
                    }
                });

                if(auth.getCurrentUser()!= null){
                     Intent intent= new Intent(SignIn.this, MainActivity.class);
                     startActivity(intent);
                }
            }
        });

    }
}
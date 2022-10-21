package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.Model.Users;
import com.example.mychat.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class SignIn extends AppCompatActivity {

    ///sing in
    ActivitySignInBinding binding;
    ///progress bar
    ProgressDialog progressDialog;
    ///database
    private FirebaseAuth auth;
    FirebaseDatabase database;

    //google login
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ///database
        database=FirebaseDatabase.getInstance();

        ///fire base auth
        auth = FirebaseAuth.getInstance();
        //progress Dialog bar
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("SignIn Progress");
        progressDialog.setMessage("Wait For SignIn...");

        getSupportActionBar().hide();

        //google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

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

            }
        });

        binding.txClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        if(auth.getCurrentUser()!= null){
            Intent intent= new Intent(SignIn.this, MainActivity.class);
            startActivity(intent);
        }
    }

    ///google login
    private int RC_SIGN_IN=65;

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //result from GoogleSignInApi.getSignInIntent(....)

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Tag", "firebaseAuthWithGoogle:" +account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (Exception e){
                Log.w("Tag", "Google Sign in failed", e);
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("tag", "signIntWithCredential: success");
                            FirebaseUser user = auth.getCurrentUser();


                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);


                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignIn.this, "Sign in with Google", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.w("tag", "signInWithCredential: Failure");
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(binding.getRoot(), "Authentication Failed", Snackbar.LENGTH_SHORT);
                        }
                    }
                });

    }
}
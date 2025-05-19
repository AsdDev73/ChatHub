package com.example.chathub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.*;

public class LogIn extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin, googleSignInButton;
    TextView textViewRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextContra);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.Registrar);

        mAuth = FirebaseAuth.getInstance();

        // botón Login con correo y contraseña
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogIn.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogIn.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            irAHomeActivity();
                        } else {
                            Toast.makeText(LogIn.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // abre ventana de registro
        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In fue exitoso, ahora autenticar con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Fallo en Google Sign-In", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // autenticar con Firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Bienvenido, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        irAHomeActivity();
                    } else {
                        Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void irAHomeActivity() {
        Intent intent = new Intent(LogIn.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

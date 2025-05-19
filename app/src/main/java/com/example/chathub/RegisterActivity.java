package com.example.chathub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String AVATAR_POR_DEFECTO = "ic_usuario";
    private static final int REQUEST_AVATAR = 1;

    private MaterialToolbar toolbar;
    private ShapeableImageView imageButtonAvatar;
    private EditText editTextName, editTextEmail, editTextPassword, editTextPasswordConfirm, editTextFecha;
    private Spinner spinnerSexo;
    private Button buttonRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String avatarSeleccionado = AVATAR_POR_DEFECTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        // UI
        imageButtonAvatar       = findViewById(R.id.imageButtonAvatar);
        editTextName            = findViewById(R.id.editTextName);
        editTextEmail           = findViewById(R.id.editTextEmailRegister);
        editTextPassword        = findViewById(R.id.editTextPasswordRegister);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextFecha           = findViewById(R.id.editTextFechaNacimiento);
        spinnerSexo             = findViewById(R.id.spinnerSexo);
        buttonRegister          = findViewById(R.id.buttonRegister);

        // Spinner de sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        // Date picker para fecha de nacimiento
        editTextFecha.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) ->
                            editTextFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Seleccionar avatar
        imageButtonAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeleccionarAvatarActivity.class);
            startActivityForResult(intent, REQUEST_AVATAR);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        // Registro
        buttonRegister.setOnClickListener(v -> {
            limpiarErrores();

            String name     = editTextName.getText().toString().trim();
            String email    = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirm  = editTextPasswordConfirm.getText().toString().trim();
            String fecha    = editTextFecha.getText().toString().trim();
            String sexo     = spinnerSexo.getSelectedItem().toString();

            boolean valido = true;

            if (AVATAR_POR_DEFECTO.equals(avatarSeleccionado)) {
                Toast.makeText(this, "Por favor elige un avatar", Toast.LENGTH_SHORT).show();
                valido = false;
            }

            if (name.isEmpty()) {
                marcarError(editTextName, "Campo requerido");
                valido = false;
            }

            if (email.isEmpty()) {
                marcarError(editTextEmail, "Campo requerido");
                valido = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                marcarError(editTextEmail, "Correo inválido (ej: ejemplo@correo.com)");
                valido = false;
            }

            if (password.isEmpty()) {
                marcarError(editTextPassword, "Campo requerido");
                valido = false;
            }

            if (confirm.isEmpty()) {
                marcarError(editTextPasswordConfirm, "Campo requerido");
                valido = false;
            } else if (!password.equals(confirm)) {
                marcarError(editTextPasswordConfirm, "Las contraseñas no coinciden");
                valido = false;
            }

            if (fecha.isEmpty()) {
                marcarError(editTextFecha, "Campo requerido");
                valido = false;
            }

            if (!valido) return;

            // Crear usuario
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", name);
                                userMap.put("email", email);
                                userMap.put("fechaNacimiento", fecha);
                                userMap.put("sexo", sexo);
                                userMap.put("avatar", avatarSeleccionado);

                                db.collection("usuarios")
                                        .document(user.getUid())
                                        .set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this,
                                                        "Error al guardar: " + e.getMessage(),
                                                        Toast.LENGTH_LONG).show()
                                        );
                            }
                        } else {
                            Toast.makeText(this,
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void marcarError(EditText campo, String mensaje) {
        campo.setError(mensaje);
        campo.requestFocus();
        campo.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_bg_error));
    }

    private void limpiarErrores() {
        EditText[] campos = {
                editTextName, editTextEmail, editTextPassword,
                editTextPasswordConfirm, editTextFecha
        };
        for (EditText campo : campos) {
            campo.setError(null);
            campo.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_bg));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AVATAR && resultCode == RESULT_OK && data != null) {
            avatarSeleccionado = data.getStringExtra("avatarName");
            int resId = getResources().getIdentifier(avatarSeleccionado, "drawable", getPackageName());
            imageButtonAvatar.setImageResource(resId);
        }
    }
}

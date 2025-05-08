package com.example.chathub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextFecha;
    private Spinner spinnerSexo;
    private Button btnGuardar, btnCerrarSesion;
    private ImageButton buttonBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // UI
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextFecha = findViewById(R.id.editTextFechaNacimiento);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        btnGuardar = findViewById(R.id.btnGuardarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        buttonBack = findViewById(R.id.buttonBack);

        // Spinner sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        // Fecha
        editTextFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            editTextFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        cargarDatosUsuario();

        btnGuardar.setOnClickListener(v -> guardarCambios());

        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(PerfilActivity.this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cargarDatosUsuario() {
        if (currentUser != null) {
            db.collection("usuarios").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("name");
                            String fechaNacimiento = documentSnapshot.getString("fechaNacimiento");
                            String sexo = documentSnapshot.getString("sexo");

                            if (nombre != null) editTextNombre.setText(nombre);
                            if (fechaNacimiento != null) editTextFecha.setText(fechaNacimiento);
                            if (sexo != null) {
                                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerSexo.getAdapter();
                                int position = adapter.getPosition(sexo);
                                if (position >= 0) spinnerSexo.setSelection(position);
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al cargar perfil: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void guardarCambios() {
        String nombre = editTextNombre.getText().toString().trim();
        String fechaNacimiento = editTextFecha.getText().toString().trim();
        String sexo = spinnerSexo.getSelectedItem().toString();

        if (nombre.isEmpty() || fechaNacimiento.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", nombre);
            updates.put("fechaNacimiento", fechaNacimiento);
            updates.put("sexo", sexo);

            db.collection("usuarios").document(currentUser.getUid())
                    .update(updates)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}

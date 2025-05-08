package com.example.chathub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextFecha = findViewById(R.id.editTextFechaNacimiento);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        Button btnGuardar = findViewById(R.id.btnGuardarPerfil);
        Button btnVolver = findViewById(R.id.btnVolverChats);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Adaptador para el spinner de sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sexo_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        // Picker de fecha
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

        // Cargar datos del usuario actual
        FirebaseUser usuario = mAuth.getCurrentUser();
        if (usuario != null) {
            String uid = usuario.getUid();

            db.collection("usuarios").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("name");
                            if (nombre != null) {
                                editTextNombre.setText(nombre);
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        // Botón guardar cambios
        btnGuardar.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                String nuevoNombre = editTextNombre.getText().toString().trim();

                if (nuevoNombre.isEmpty()) {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> updates = new HashMap<>();
                updates.put("name", nuevoNombre);

                db.collection("usuarios").document(uid)
                        .update(updates)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Botón volver
        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, HomeActivity.class));
            finish();
        });

        // Botón cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(PerfilActivity.this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}

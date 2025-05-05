    package com.example.chathub;

    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import java.util.Calendar;

    public class PerfilActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_perfil);

            Button btnVolver = findViewById(R.id.btnVolverChats);
            Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

            btnVolver.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });

            btnCerrarSesion.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilActivity.this, LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el historial
                startActivity(intent);
                finish();
            });

            EditText editTextFecha = findViewById(R.id.editTextFechaNacimiento);
            Spinner spinnerSexo = findViewById(R.id.spinnerSexo);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.sexo_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSexo.setAdapter(adapter);

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


        }
    }
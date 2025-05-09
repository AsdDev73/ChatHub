package com.example.chathub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionarAvatarActivity extends AppCompatActivity {

    private final int[] avatarIds = {
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5
    };

    private final String[] avatarNames = {
            "avatar1",
            "avatar2",
            "avatar3",
            "avatar4",
            "avatar5"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_avatar);

        GridLayout gridLayout = findViewById(R.id.gridAvatares);

        for (int i = 0; i < avatarIds.length; i++) {
            int avatarId = avatarIds[i];
            String avatarName = avatarNames[i];

            ImageView avatarView = new ImageView(this);
            avatarView.setImageResource(avatarId);
            avatarView.setPadding(16, 16, 16, 16);
            avatarView.setAdjustViewBounds(true);
            avatarView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int size = getResources().getDimensionPixelSize(R.dimen.avatar_size);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = size;
            params.height = size;
            avatarView.setLayoutParams(params);

            avatarView.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("avatarName", avatarName);
                setResult(RESULT_OK, resultIntent);
                finish();
            });

            gridLayout.addView(avatarView);
        }
    }
}

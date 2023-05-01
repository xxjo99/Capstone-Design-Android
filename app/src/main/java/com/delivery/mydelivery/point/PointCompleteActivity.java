package com.delivery.mydelivery.point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;

import java.text.NumberFormat;

@SuppressLint("SetTextI18n")
public class PointCompleteActivity extends AppCompatActivity {

    TextView addPointTV;
    TextView afterPointTV;
    Button confirmBtn;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_complete);
        context = this;

        Intent intent = getIntent();
        int addPoint = intent.getIntExtra("addPoint", 10000);
        int afterPoint = intent.getIntExtra("afterPoint", 10000);

        addPointTV = findViewById(R.id.addPointTV);
        afterPointTV = findViewById(R.id.afterPointTV);
        confirmBtn = findViewById(R.id.confirmBtn);

        NumberFormat numberFormat = NumberFormat.getInstance();

        String addPointFormat = numberFormat.format(addPoint);
        addPointTV.setText(addPointFormat + "P");

        String afterPointFormat = numberFormat.format(afterPoint);
        afterPointTV.setText(afterPointFormat + "P");

        confirmBtn.setOnClickListener(view -> finish());
    }
}

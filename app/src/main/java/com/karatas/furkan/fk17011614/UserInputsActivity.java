package com.karatas.furkan.fk17011614;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class UserInputsActivity extends AppCompatActivity {

    private TextView dateOfBirthTV;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Input inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inputs);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dateOfBirthTV = findViewById(R.id.et_date_of_birth);
        EditText[] editTexts = new EditText[]{
                findViewById(R.id.et_name),
                findViewById(R.id.et_surname),
                findViewById(R.id.et_place_of_birth),
                findViewById(R.id.et_id_number),
                findViewById(R.id.et_phone_number),
                findViewById(R.id.et_email_address)};
        inputs = new Input(this, editTexts);
        inputs.setTextViews(new TextView[]{dateOfBirthTV});
        inputs.setInvisibleText((TextView) findViewById(R.id.tv_blank_lines_alert));

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "/" + month + "/" + year;
                dateOfBirthTV.setText(date);
                inputs.clearWarnings();
            }
        };

        dateOfBirthTV.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = 1990, month = 0, day = 1;
                DatePickerDialog dialog = new DatePickerDialog(
                        UserInputsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day
                );
                Objects.requireNonNull(dialog.getWindow())
                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputs.empty();
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputs.warnForBlanks();
            }
        });

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(inputs);
        }
    }
}

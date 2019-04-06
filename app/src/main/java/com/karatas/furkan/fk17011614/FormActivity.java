package com.karatas.furkan.fk17011614;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class FormActivity extends AppCompatActivity {

    private TextView dateOfBirthTV;
    EditText[] editTexts;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Input inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dateOfBirthTV = findViewById(R.id.et_date_of_birth);
        editTexts = new EditText[]{
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
                        FormActivity.this,
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
                if (inputs.warnForBlanks()) { //başına ünlem koyulacak. development sırasında her
                    // seferinde uğraşmamak için şimdilik böyle
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    sendInformation(intent);
                    startActivity(intent);
                }
            }
        });

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(inputs);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int i = 0;
        for (EditText editText : editTexts){
            outState.putString(String.valueOf(i++), editText.getText().toString());
        }
        outState.putString(String.valueOf(i), dateOfBirthTV.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int i = 0;
        for (EditText editText : editTexts){
            editText.setText(savedInstanceState.getString(String.valueOf(i++)));
        }
        dateOfBirthTV.setText(savedInstanceState.getString(String.valueOf(i)));
    }

    public void sendInformation(Intent intent) {
        for (EditText editText : editTexts) {
            intent.putExtra(
                    getResources().getResourceEntryName(editText.getId()),
                    editText.getText().toString());
        }
        intent.putExtra(
                getResources().getResourceEntryName(dateOfBirthTV.getId()),
                dateOfBirthTV.getText().toString());
    }
}

package com.karatas.furkan.fk17011614;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    TextView nameTV, surnameTV, placeOfBirthTV, dateOfBirthTV,
            idNumberTV, phoneNumberTV, emailAddressTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        nameTV = findViewById(R.id.tv_name);
        surnameTV = findViewById(R.id.tv_surname);
        placeOfBirthTV = findViewById(R.id.tv_place_of_birth);
        dateOfBirthTV = findViewById(R.id.tv_date_of_birth);
        idNumberTV = findViewById(R.id.tv_id_number);
        phoneNumberTV = findViewById(R.id.tv_phone_number);
        emailAddressTV = findViewById(R.id.tv_email_address);
        ImageView profilePicture = findViewById(R.id.iv_profile_picture);

        nameTV.setText(intent.getStringExtra("et_name"));
        surnameTV.setText(intent.getStringExtra("et_surname"));
        placeOfBirthTV.setText(intent.getStringExtra("et_place_of_birth"));
        String dateOfBirth = intent.getStringExtra("et_date_of_birth") + " (" +
                calculateAge(intent.getStringExtra("et_date_of_birth")) + ")";
        dateOfBirthTV.setText(dateOfBirth);
        idNumberTV.setText(intent.getStringExtra("et_id_number"));
        phoneNumberTV.setText(intent.getStringExtra("et_phone_number"));
        emailAddressTV.setText(intent.getStringExtra("et_email_address"));

        byte[] byteArray = getIntent().getByteArrayExtra("IMAGE");
        profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    private int calculateAge(String dateOfBirth) {
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        String[] dateOfBirthArray = dateOfBirth.split("/");

        int yearOfBirth = Integer.parseInt(dateOfBirthArray[2]);
        int monthOfBirth = Integer.parseInt(dateOfBirthArray[1]);
        int dayOfBirth = Integer.parseInt(dateOfBirthArray[0]);
        int age = currentYear - yearOfBirth;

        if (monthOfBirth > currentMonth) {
            age--;
        } else if (monthOfBirth == currentMonth) {
            if (dayOfBirth > currentDay) {
                age--;
            }
        }

        return age;
    }
}

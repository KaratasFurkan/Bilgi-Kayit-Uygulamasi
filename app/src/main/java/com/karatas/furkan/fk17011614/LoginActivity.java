package com.karatas.furkan.fk17011614;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Input inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        usernameET = findViewById(R.id.et_username);
        passwordET = findViewById(R.id.et_password);

        inputs = new Input(this, new EditText[]{usernameET, passwordET});
        inputs.setInvisibleText((TextView) findViewById(R.id.tv_login_alert));

        findViewById(R.id.btn_login).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameET.getText().toString().trim().equals("admin") &&
                        passwordET.getText().toString().equals("password")) {
                    Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                    startActivity(intent);

                } else {
                    inputs.empty();
                    inputs.warnForWrongs();
                }
            }
        });
        usernameET.addTextChangedListener(inputs);
        passwordET.addTextChangedListener(inputs);
    }
}
package com.karatas.furkan.fk17011614;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private TextView alertTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.et_username);
        passwordET = findViewById(R.id.et_password);
        alertTV = findViewById(R.id.tv_login_alert);
        Button loginBtn = findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameET.getText().toString().equals("admin") &&
                        passwordET.getText().toString().equals("password")) {

                    Intent intent = new Intent(getApplicationContext(), UserInputsActivity.class);
                    startActivity(intent);

                } else {
                    emptyInputs(new EditText[]{usernameET, passwordET});
                    warnForInputs(new EditText[]{usernameET, passwordET});
                    alertTV.setVisibility(View.VISIBLE);
                }
            }
        });

        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i1, int i2) {
                clearWarningsForInputs(new EditText[]{usernameET, passwordET});
                alertTV.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {}
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence,
                                      int i, int i1, int i2) {
                clearWarningsForInputs(new EditText[]{usernameET, passwordET});
                alertTV.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {}
        });
    }

    public void warnForInputs(EditText[] editTexts){
        for (EditText editText : editTexts){
            editText.setBackground(getResources().getDrawable
                    (R.drawable.et_rounded_light_red_background)
            );
        }
    }

    public void clearWarningsForInputs(EditText[] editTexts){
        for (EditText editText : editTexts){
            editText.setBackground(getResources().getDrawable
                    (R.drawable.et_rounded_background)
            );
        }
    }

    public void emptyInputs(EditText[] editTexts){
        for (EditText editText : editTexts){
            editText.setText("");
        }
    }
}

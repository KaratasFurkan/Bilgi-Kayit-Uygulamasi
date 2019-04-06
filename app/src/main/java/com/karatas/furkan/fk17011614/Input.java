package com.karatas.furkan.fk17011614;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Input implements TextWatcher {
    private Context context;
    private EditText[] editTexts;
    private TextView[] textViews;
    private TextView invisibleText; //for warnings

    Input(Context context, EditText[] editTexts) {
        this.context = context;
        this.editTexts = editTexts;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        clearWarnings();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void empty() {
        for (EditText editText : editTexts) {
            editText.setText("");
        }

        if (textViews != null) {
            for (TextView textView : textViews) {
                textView.setText("");
            }
        }
        editTexts[0].requestFocus();
    }

    public void warnForWrongs(String[] expectedValues) {
        int i = 0;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().equals(expectedValues[i++])) {
                editText.setBackground(context.getResources().getDrawable
                        (R.drawable.et_rounded_light_red_background)
                );
            }
        }
    }

    //Overloading
    public void warnForWrongs() {
        for (EditText editText : editTexts) {
            editText.setBackground(context.getResources().getDrawable
                    (R.drawable.et_rounded_light_red_background)
            );
        }

        if (invisibleText != null) invisibleText.setVisibility(View.VISIBLE);
    }

    public void warnForBlanks() {
        int i, indexOfFirstEmptyInput = -1;
        for (i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().toString().equals("")) {
                editTexts[i].setBackground(context.getResources().getDrawable
                        (R.drawable.et_rounded_light_red_background)
                );
                if (indexOfFirstEmptyInput == -1) {
                    indexOfFirstEmptyInput = i;
                }
            }
        }

        if (textViews != null) {
            for (TextView textView : textViews) {
                if (textView.getText().toString().equals("")) {
                    textView.setBackground(context.getResources().getDrawable
                            (R.drawable.et_rounded_light_red_background)
                    );
                }
            }
        }

        if (indexOfFirstEmptyInput != -1) {
            editTexts[indexOfFirstEmptyInput].requestFocus();
            if (invisibleText != null) invisibleText.setVisibility(View.VISIBLE);
        } else {
            editTexts[0].requestFocus();
        }
    }

    public void clearWarnings() {
        for (EditText editText : editTexts) {
            editText.setBackground(context.getResources().getDrawable
                    (R.drawable.et_rounded_background)
            );
        }

        if (textViews != null) {
            for (TextView textView : textViews) {
                textView.setBackground(context.getResources().getDrawable
                        (R.drawable.et_rounded_background)
                );
            }
        }

        if (invisibleText != null) invisibleText.setVisibility(View.INVISIBLE);
    }

    public void setEditTexts(EditText[] editTexts) {
        this.editTexts = editTexts;
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public void setInvisibleText(TextView invisibleText) {
        this.invisibleText = invisibleText;
    }
}
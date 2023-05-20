package com.example.petfinderapp.model;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneMaskWatcher implements TextWatcher {
    private EditText editText;

    public PhoneMaskWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        //remove todos os caracteres não numéricos
        String digitsOnly = text.replaceAll("[^0-9]", "");

        //verifica se o número de caracteres não excede o limite máximo (11)
        if (digitsOnly.length() > 11) {
            digitsOnly = digitsOnly.substring(0, 11);
        }

        //formata o número de telefone
        StringBuilder formatted = new StringBuilder();
        int digitCount = 0;
        for (int i = 0; i < digitsOnly.length(); i++) {
            char c = digitsOnly.charAt(i);
            if (digitCount == 0) {
                formatted.append('(');
            } else if (digitCount == 2) {
                formatted.append(") ");
            } else if (digitCount == 7) {
                formatted.append('-');
            }
            formatted.append(c);
            digitCount++;
        }

        //define o texto formatado no EditText
        editText.removeTextChangedListener(this);
        editText.setText(formatted.toString());
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}

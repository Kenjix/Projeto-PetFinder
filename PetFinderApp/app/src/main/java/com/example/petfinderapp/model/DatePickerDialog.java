package com.example.petfinderapp.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.petfinderapp.R;

import java.util.Calendar;

public class DatePickerDialog {
    private Context context;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private EditText editDataNasc;

    public DatePickerDialog(Context context, EditText editDataNasc) {
        this.context = context;
        this.editDataNasc = editDataNasc;
    }

    public void showDatePickerDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.date_picker_dialog, null);

        final NumberPicker dayPicker = dialogView.findViewById(R.id.day_picker);
        final NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);
        final NumberPicker yearPicker = dialogView.findViewById(R.id.year_picker);
        final Button okButton = dialogView.findViewById(R.id.ok_button);

        //limites e valores iniciais para os pickers
        int minDay = 1;
        int maxDay = 31;
        int minMonth = 1;
        int maxMonth = 12;
        int minYear = Calendar.getInstance().get(Calendar.YEAR)-120;
        int maxYear = Calendar.getInstance().get(Calendar.YEAR);

        dayPicker.setMinValue(minDay);
        dayPicker.setMaxValue(maxDay);
        dayPicker.setValue(selectedDay);

        monthPicker.setMinValue(minMonth);
        monthPicker.setMaxValue(maxMonth);
        monthPicker.setValue(selectedMonth);

        yearPicker.setMinValue(minYear);
        yearPicker.setMaxValue(maxYear);
        yearPicker.setValue(selectedYear);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setTitle("Selecione uma data")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDay = dayPicker.getValue();
                selectedMonth = monthPicker.getValue();
                selectedYear = yearPicker.getValue();
                String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth, selectedYear);
                editDataNasc.setText(selectedDate);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

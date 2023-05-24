package com.example.petfinderapp.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.petfinderapp.CadastroUsuario;
import com.example.petfinderapp.MainActivity;
import com.example.petfinderapp.R;

import java.util.Calendar;

public class DatePickerDialog {
    private Context context;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private EditText editText;

    public DatePickerDialog(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    public void showDatePickerDialog() {
        if (context instanceof CadastroUsuario) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_date_picker_user, null);

            final NumberPicker dayPicker = dialogView.findViewById(R.id.day_picker);
            final NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);
            final NumberPicker yearPicker = dialogView.findViewById(R.id.year_picker);
            final Button okButton = dialogView.findViewById(R.id.ok_button);

            //limites e valores iniciais para os pickers
            int minDay = 1;
            int maxDay = 31;
            int minMonth = 1;
            int maxMonth = 12;
            int minYear = Calendar.getInstance().get(Calendar.YEAR) - 120;
            int maxYear = Calendar.getInstance().get(Calendar.YEAR);

            dayPicker.setMinValue(minDay);
            dayPicker.setMaxValue(maxDay);
            dayPicker.setValue(minDay);

            monthPicker.setMinValue(minMonth);
            monthPicker.setMaxValue(maxMonth);
            monthPicker.setValue(minMonth);

            yearPicker.setMinValue(minYear);
            yearPicker.setMaxValue(maxYear);
            yearPicker.setValue(maxYear);

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
                    editText.setText(selectedDate);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if(context instanceof MainActivity) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_date_picker_publicacao, null);

            final NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);
            final NumberPicker yearPicker = dialogView.findViewById(R.id.year_picker);
            final Button okButton = dialogView.findViewById(R.id.ok_button);

            //limites e valores iniciais para os pickers
            int minMonth = 1;
            int maxMonth = 12;
            int minYear = 0;
            int maxYear = 30;

            monthPicker.setMinValue(minMonth);
            monthPicker.setMaxValue(maxMonth);
            monthPicker.setValue(minMonth);

            yearPicker.setMinValue(minYear);
            yearPicker.setMaxValue(maxYear);
            yearPicker.setValue(minYear);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView)
                    .setTitle("Selecione a idade do pet")
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
                    selectedMonth = monthPicker.getValue();
                    selectedYear = yearPicker.getValue();
                    String selectedDate = "";
                    if(selectedYear > 0){
                        selectedDate = String.format("%02d %s e %02d %s",
                                selectedYear,
                                (selectedYear > 1) ? "anos" : "ano",
                                selectedMonth,
                                (selectedMonth > 1) ? "meses" : "mês");
                    } else{
                        selectedDate = String.format("%02d %s",
                                selectedMonth,
                                (selectedMonth > 1) ? "meses" : "mês");
                    }


                    editText.setText(selectedDate);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}

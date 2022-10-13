package com.example.cartehab.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cartehab.R;

public class DialogCustom extends Dialog {

    public interface FullNameListener {
        void fullNameEntered(String fullName);
    }

    protected EditText textInput;
    protected Button buttonOk;
    protected Context context;
    protected DialogCustom.FullNameListener listener;

    public DialogCustom(@NonNull Context context, DialogCustom.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_custom);
        textInput = findViewById(R.id.text_input);
        buttonOk = findViewById(R.id.ok_button);
        buttonOk.setOnClickListener(view -> {
            String fullName = this.textInput.getText().toString();

            if(fullName== null || fullName.isEmpty())  {
                Toast.makeText(this.context, "Please enter your name", Toast.LENGTH_LONG).show();
                return;
            }
            this.dismiss(); // Close Dialog

            if(this.listener!= null)  {
                this.listener.fullNameEntered(fullName);
            }

        });
    }
}
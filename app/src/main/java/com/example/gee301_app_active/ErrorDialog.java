package com.example.gee301_app_active;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
//import androidx.support.v7.app.AlertDialog;
//import androidx.support.v7.app.AppCompatDialogFragment;


public class ErrorDialog extends AppCompatDialogFragment {

    String message = "Error occurred.";
    public static ErrorDialog newInstance(String mess) {
        ErrorDialog f = new ErrorDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("mess", mess);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getArguments().getString("mess");
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ALERT")
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
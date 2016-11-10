package com.jobnotes.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertActivity {

    public void showAlert(Activity activity, String message, String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(title);

        alertDialog
                .setMessage(message);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}

package stb.com.testmapapp.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import stb.com.testmapapp.util.Preconditions;

public class Alert {

    private final Context context;

    public Alert(Context context) {
        this.context = Preconditions.checkNotNull(context);
    }

    public AlertDialog modal(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        return alertDialogBuilder.create();
    }
}

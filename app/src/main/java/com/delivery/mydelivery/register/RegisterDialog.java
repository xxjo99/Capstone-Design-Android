package com.delivery.mydelivery.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;

import com.delivery.mydelivery.R;

@SuppressLint("SetTextI18n")
public class RegisterDialog {

    private final Context context;

    public RegisterDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_login_modify_pw_cancel_dialog);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
        Button quitBtn = dialog.findViewById(R.id.quitBtn);

        quitBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity)context).finish();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }
}

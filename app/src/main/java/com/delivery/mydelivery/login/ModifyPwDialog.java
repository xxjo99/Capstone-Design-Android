package com.delivery.mydelivery.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;

import com.delivery.mydelivery.R;

public class ModifyPwDialog {

    private final Context context;

    public ModifyPwDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_login_modify_pw_dialog);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        Button finishBtn = dialog.findViewById(R.id.finishBtn);

        finishBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });

    }
}

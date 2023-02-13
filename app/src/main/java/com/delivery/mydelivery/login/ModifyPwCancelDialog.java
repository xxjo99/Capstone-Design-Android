package com.delivery.mydelivery.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;

import com.delivery.mydelivery.R;

public class ModifyPwCancelDialog {

    private final Context context;

    public ModifyPwCancelDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_modify_pw_cancel);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
        Button quitBtn = dialog.findViewById(R.id.quitBtn);

        quitBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

}

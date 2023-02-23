package com.delivery.mydelivery.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
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
        dialog.setContentView(R.layout.dialog_cancel_register);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

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

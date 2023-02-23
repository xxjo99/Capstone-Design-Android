package com.delivery.mydelivery.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.point.PointActivity;

public class AddPointDialog {
    private final Context context;

    public AddPointDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_add_point);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        Button addPointBtn = dialog.findViewById(R.id.addPointBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        addPointBtn.setOnClickListener(view -> {
            dialog.dismiss();

            Intent intent = new Intent(context, PointActivity.class);
            context.startActivity(intent);
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }
}

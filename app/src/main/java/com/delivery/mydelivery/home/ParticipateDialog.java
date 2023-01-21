package com.delivery.mydelivery.home;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

public class ParticipateDialog {

    private Context context;

    private String storeName;
    private String recruitPerson;
    private String place;
    private String deliveryTime;
    private String deliveryTip;

    public ParticipateDialog(Context context) {
        this.context = context;
    }

    public void setData(String storeName, String recruitPerson, String place, String deliveryTime, String deliveryTip) {
        this.storeName = storeName;
        this.recruitPerson = recruitPerson;
        this.place = place;
        this.deliveryTime = deliveryTime;
        this.deliveryTip = deliveryTip;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_recruit_participate_dialog);
        dialog.show();

        TextView storeNameTV = dialog.findViewById(R.id.storeNameTV);
        TextView recruitPersonTV = dialog.findViewById(R.id.recruitPersonTV);
        TextView placeTV = dialog.findViewById(R.id.placeTV);
        TextView deliveryTimeTV = dialog.findViewById(R.id.deliveryTimeTV);
        TextView deliveryTipTV = dialog.findViewById(R.id.deliveryTipTV);

        Button participateBtn = dialog.findViewById(R.id.participateBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        storeNameTV.setText(storeName);
        recruitPersonTV.setText(recruitPerson);
        placeTV.setText(place);
        deliveryTimeTV.setText(deliveryTime);
        deliveryTipTV.setText(deliveryTip);

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }
}

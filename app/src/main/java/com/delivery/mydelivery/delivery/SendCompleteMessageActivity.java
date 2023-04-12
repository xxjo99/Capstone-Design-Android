package com.delivery.mydelivery.delivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendCompleteMessageActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    LinearLayout ivLayout;
    ImageView pictureIV;
    Button deliveryFinishBtn;

    Bitmap pictureBitmap;

    RetrofitService retrofitService;
    DeliveryApi deliveryApi;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_send_message);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.deliveryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        Intent intent = getIntent();
        int recruitId = intent.getIntExtra("recruitId", 0);

        // 초기화
        ivLayout = findViewById(R.id.ivLayout);
        pictureIV = findViewById(R.id.pictureIV);
        deliveryFinishBtn = findViewById(R.id.deliveryFinishBtn);

        // 사진 촬영
        pictureIV.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 0);
        });

        // 알림 전송, 배달 완료
        // 촬영하지 않았다면 전송 불가
        deliveryFinishBtn.setOnClickListener(view -> {
            if (pictureBitmap != null) {
                completeDelivery(recruitId, pictureBitmap);
            } else {
                StyleableToast.makeText(context, "먼저 촬영을 완료해주세요.", R.style.warningToast).show();
            }
        });


    }

    // 이미지뷰에 촬영한 이미지 저장
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); // Bundle로 데이터를 입력
            pictureBitmap = (Bitmap) extras.get("data"); // Bitmap으로 변환

            // 레이아웃의 크기에 맞춰서 이미지 저장
            int width = ivLayout.getWidth();
            int height = ivLayout.getHeight();

            Bitmap resizedPicture = Bitmap.createScaledBitmap(pictureBitmap, width, height, true);
            pictureIV.setImageBitmap(resizedPicture);
        }
    }

    // 알림전송, 배달 완료 처리
    private void completeDelivery(int recruitId, Bitmap bitmap) {
        retrofitService = new RetrofitService();
        deliveryApi = retrofitService.getRetrofit().create(DeliveryApi.class);

        // 비트맵 이미지 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

        deliveryApi.completeDelivery(recruitId, filePart)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        System.out.println(response);
                        StyleableToast.makeText(context, "배달이 완료되었습니다.", R.style.successToast).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });

    }

}

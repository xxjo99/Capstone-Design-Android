package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.RecruitActivity;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;
import com.delivery.mydelivery.user.ParticipationRestrictionVO;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 모집글 리스트 어댑터
@SuppressLint("SetTextI18n")
public class RecruitListAdapter extends RecyclerView.Adapter<RecruitListAdapter.ViewHolder> {

    private final List<RecruitVO> recruitList; // 모집글 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    UserApi userApi;
    RecruitApi recruitApi;


    // 생성자
    public RecruitListAdapter(List<RecruitVO> recruitList, Context context) {
        this.recruitList = recruitList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_recruit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitListAdapter.ViewHolder holder, int position) {
        RecruitVO recruit = recruitList.get(position);

        int storeId = recruit.getStoreId();
        int userId = recruit.getUserId();
        String deliveryTime = changeDeliveryTime(recruit.getDeliveryTime());
        int person = recruit.getPerson();
        String place = recruit.getPlace();

        setStoreName(storeId, holder); // 매장이름
        setRegistrantName(userId, holder); // 등록자 이름
        holder.deliveryTimeTV.setText(deliveryTime); // 배달 시간
        holder.recruitPersonTV.setText(person + "명"); // 모집 인원
        holder.placeTV.setText(place); // 배달 장소

        // 참가자수 구하기
        getParticipantCount(recruit.getRecruitId(), holder);

        // 클릭시 다이얼로그 열기
        holder.itemView.setOnClickListener(view -> {

            String loginInfo = PreferenceManager.getLoginInfo(context);
            Gson gson = new Gson();
            UserVO user = gson.fromJson(loginInfo, UserVO.class);

            int recruitId = recruit.getRecruitId();
            int participateUserId = user.getUserId();

            // 참가상태 확인
            checkParticipate(participateUserId, recruitId, holder, person, place, deliveryTime, storeId);
        });
    }

    @Override
    public int getItemCount() {
        if (recruitList != null) {
            return recruitList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTV;
        TextView registrantTV;
        TextView deliveryTimeTV;
        TextView recruitPersonTV;
        TextView placeTV;

        // 다이얼로그에 사용
        String deliveryTip; // 배달팁
        Integer participantCount; // 참가자 수

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            registrantTV = itemView.findViewById(R.id.registrantTV);
            deliveryTimeTV = itemView.findViewById(R.id.deliveryTimeTV);
            recruitPersonTV = itemView.findViewById(R.id.recruitPersonTV);
            placeTV = itemView.findViewById(R.id.placeTV);
        }
    }

    // 매장이름 지정
    private void setStoreName(int storeId, RecruitListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        holder.storeNameTV.setText(store.getStoreName());
                        holder.deliveryTip = store.getDeliveryTip();
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 등록자 지정
    private void setRegistrantName(int userId, RecruitListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        assert user != null;
                        holder.registrantTV.setText(user.getName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 해당 등록글의 현재 참가자수 반환
    private void getParticipantCount(int recruitId, RecruitListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantCount(recruitId)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        holder.participantCount = response.body();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                    }
                });
    }

    // 참가상태 확인, 미참가시 이용제한 확인
    private void checkParticipate(int participateUserId, int recruitId, RecruitListAdapter.ViewHolder holder, int person, String place, String deliveryTime, int storeId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.findUserInRecruit(recruitId, participateUserId)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {

                        if (Boolean.TRUE.equals(response.body())) { // 참가
                            Intent intent = new Intent(context, RecruitActivity.class);
                            intent.putExtra("recruitId", recruitId);
                            intent.putExtra("storeId", storeId);
                            context.startActivity(intent);
                        } else { // 미참가, 이용제한 확인
                            checkRestriction(participateUserId, recruitId, holder, person, place, deliveryTime, storeId);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    }
                });
    }

    // 이용제한 확인
    private void checkRestriction(int participateUserId, int recruitId, RecruitListAdapter.ViewHolder holder, int person, String place, String deliveryTime, int storeId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.checkRestriction(participateUserId)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        Boolean checkRestrictionFlag = response.body();

                        if (Boolean.TRUE.equals(checkRestrictionFlag)) { // 참가 가능, 다이얼로그 생성
                            createDialog(participateUserId, recruitId, holder, person, place, deliveryTime, storeId);
                        } else { // 참가불가, 이용제한 안내 다이얼로그 생성
                            createRestrictionDialog(participateUserId);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                    }
                });
    }

    // 모집글 상세 다이얼로그 생성
    private void createDialog(int participateUserId, int recruitId, RecruitListAdapter.ViewHolder holder, int recruitPerson, String place, String deliveryTime, int storeId) {
        ParticipateDialog participateDialog = new ParticipateDialog(context);

        String storeName = holder.storeNameTV.getText().toString(); // 매장 이름
        // 매장 이름, 모집인원 수, 현재 참가자 수, 장소, 배달시간, 배달팁, 모집글 아이디, 참가자 아이디, 매장 아이디
        participateDialog.setData(storeName, holder.participantCount, recruitPerson, place, deliveryTime, holder.deliveryTip, recruitId, participateUserId, storeId);
        participateDialog.callDialog();
    }

    // 이용제한 안내 다이얼로그 생성
    private void createRestrictionDialog(int userId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getParticipationRestriction(userId)
                .enqueue(new Callback<ParticipationRestrictionVO>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipationRestrictionVO> call, @NonNull Response<ParticipationRestrictionVO> response) {
                        ParticipationRestrictionVO participationRestriction = response.body();
                        Timestamp restrictionPeriod = Objects.requireNonNull(participationRestriction).getRestrictionPeriod();

                        ParticipationRestrictionDialog dialog = new ParticipationRestrictionDialog(restrictionPeriod, context);
                        dialog.callDialog();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipationRestrictionVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 배달 시간 변환
    private String changeDeliveryTime(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        int month = localDateTime.getMonthValue(); // 월
        int day = localDateTime.getDayOfMonth(); // 일
        int hour = localDateTime.getHour(); // 시간
        int minute = localDateTime.getMinute(); // 분

        // 요일
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        String dayOfWeekStr = "";
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekStr = "일";
                break;
            case Calendar.MONDAY:
                dayOfWeekStr = "월";
                break;
            case Calendar.TUESDAY:
                dayOfWeekStr = "화";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekStr = "수";
                break;
            case Calendar.THURSDAY:
                dayOfWeekStr = "목";
                break;
            case Calendar.FRIDAY:
                dayOfWeekStr = "금";
                break;
            case Calendar.SATURDAY:
                dayOfWeekStr = "토";
                break;
        }

        return month + "/" + day + "(" + dayOfWeekStr + ") " + hour + "시 " + minute + "분";
    }

}

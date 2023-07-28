# 캡스톤 디자인 - 배달비 공유 시스템 Android


## 개요
- SpringBoot와 안드로이드 스튜디오를 통한 배달비 공유 앱 프로젝트의 앱

 __제작기간__
- 22.12.30 ~ 23.05.25


## 사용 기술 및 개발 환경
- Language : JAVA
- Tool : AndroidStudio
- openApi : Retrofit2, BootPay, FireBase Cloud Messaging

## 구현기능
- 앱에서 수행할 기능의 api 구현

### 주요 api
- 서버와의 통신을 위한 retrofit2 설정 : RetrofitService.java
```
public void init() {
    Gson gson = new GsonBuilder().setLenient().create();

    retrofit = new Retrofit.Builder()
            .baseUrl("http://1.254.120.139:50000") // 주소
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
```

- 로그인
```
private void login(String email, String pw) {
        // 레트로핏, api 초기화
      retrofitService = new RetrofitService();
      loginApi = retrofitService.getRetrofit().create(LoginApi.class);

      loginApi.login(email, pw)
              .enqueue(new Callback<>() {
                  @Override
                  public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                      UserVO userInfo = response.body(); // 유저 정보를 받아옴

                      // api를 통해 받아온 유저정보가 들어있는 객체를 json으로 변환
                      gson = new GsonBuilder().create();
                      String userInfoJson = gson.toJson(userInfo, UserVO.class);

                      // 변환된 데이터를 sharedPreference에 저장 -> 로그인 유지 기능
                      PreferenceManager.setLoginInfo(context, userInfoJson);

                      FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService(email);

                      // 어플 홈화면 진입후 로그인 종료
                      Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                      startActivity(intent);
                      MainActivity mainActivity = MainActivity.mainActivity;
                      mainActivity.finish();
                      finish();
                  }

                  @Override
                  public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) { // 로그인 실패시
                      StyleableToast.makeText(context, "이메일 또는 비밀번호를 다시 입력해주세요.", R.style.errorToast).show();
                  }
              });
  }
```

- 검색
```
searchET.setOnEditorActionListener((textView, actionId, keyEvent) -> {

    if (actionId == IME_ACTION_SEARCH) {
        InputMethodManager mInputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(searchET.getWindowToken(), 0);

        String keyword = searchET.getText().toString();

        SearchResultStore.searchOpenStore(keyword, user.getSchool());
        SearchResultRecruit.searchRecruitResult(keyword, user.getSchool());

        searchResultLayout.setVisibility(View.VISIBLE);
        searchImageLayout.setVisibility(View.GONE);
    }
    return true;
});
```

- 장바구니에 메뉴 추가
```
private void addMenu(int userId, int storeId, OrderVO order) {
    // 레트로핏, api 초기화
    retrofitService = new RetrofitService();
    orderApi = retrofitService.getRetrofit().create(OrderApi.class);

    orderApi.findStoreInCart(userId, storeId)
            .enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<List<OrderVO>> call, @NonNull Response<List<OrderVO>> response) {
                    List<OrderVO> orderList = response.body();

                    assert orderList != null;
                    if (orderList.size() != 0) {
                        StyleableToast.makeText(context, "장바구니에 다른 매장의 메뉴가 담겨있습니다.", R.style.errorToast).show();
                    } else {
                        orderApi.addMenu(order)
                                .enqueue(new Callback<>() {
                                    @Override
                                    public void onResponse(@NonNull Call<OrderVO> call, @NonNull Response<OrderVO> response) {
                                        StyleableToast.makeText(context, "장바구니에 추가되었습니다.", R.style.successToast).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<OrderVO> call, @NonNull Throwable t) {
                                    }
                                });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<OrderVO>> call, @NonNull Throwable t) {
                }
            });
}
```

- 결제
```
private void payment(int recruitId, int userId, int usedPoint, String content, UserVO user) {
  retrofitService = new RetrofitService();
  recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);
  pointApi = retrofitService.getRetrofit().create(PointApi.class);

  // 결제
  recruitApi.payment(recruitId, userId, usedPoint, content)
          .enqueue(new Callback<>() {
              @Override
              public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                  user.setPoint(user.getPoint() - usedPoint);
                  gson = new GsonBuilder().create();
                  String userInfoJson = gson.toJson(user, UserVO.class);
                  PreferenceManager.setLoginInfo(context, userInfoJson);

                  Intent completePaymentIntent = new Intent(PaymentActivity.this, CompletePaymentActivity.class);
                  startActivity(completePaymentIntent);
                  finish();
              }

              @Override
              public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

              }
          });

}
```

- 포인트 충전
```
public void goRequest(double point) {
    // 사용자정보
    String loginInfo = PreferenceManager.getLoginInfo(context);
    Gson gson = new Gson();
    UserVO user = gson.fromJson(loginInfo, UserVO.class);

    // 결제자 정보 삽입
    BootUser bootUser = new BootUser();
    bootUser.setUsername(user.getName());

    String orderName = (int) point + "P";

    Payload payload = new Payload();
    payload.setApplicationId(applicationId)
            .setOrderName(orderName) // 상품 이름
            .setPrice(point) // 가격
            .setUser(bootUser) // 사용자
            .setOrderId(user.getUserId() + " : " + orderName); // 주문번호


    Bootpay.init(getSupportFragmentManager(), getApplicationContext())
            .setPayload(payload)
            .setEventListener(new BootpayEventListener() {
                @Override
                public void onCancel(String data) {
                }

                @Override
                public void onError(String data) {
                    Log.d("error", "error" + " " + data);
                }

                @Override
                public void onClose() {
                    Log.d("close", "close");
                    Bootpay.removePaymentWindow();
                }

                @Override
                public void onIssued(String data) {
                    Log.d("issued", "issued" + " " + data);
                }

                @Override
                public boolean onConfirm(String data) {
                    Log.d("confirm", "confirm" + " " + data);
                    return true;
                }

                @Override
                public void onDone(String data) {
                    // 사용자 정보
                    String loginInfo = PreferenceManager.getLoginInfo(context);
                    Gson gson = new Gson();
                    UserVO user = gson.fromJson(loginInfo, UserVO.class);

                    // 포인트 수정
                    int userPoint = user.getPoint() + (int) point;
                    user.setPoint(userPoint);

                    // 포인트 충전 내역 추가
                    PointHistoryVO pointHistory = new PointHistoryVO();
                    pointHistory.setUserId(user.getUserId());
                    pointHistory.setPoint((int) point);
                    pointHistory.setType("충전");
                    pointHistory.setBalance(userPoint);
                    pointHistory.setContent("포인트 충전");

                    addPoint(user, pointHistory);

                    Intent pointCompleteIntent = new Intent(PointActivity.this, PointCompleteActivity.class);
                    pointCompleteIntent.putExtra("addPoint", (int) point);
                    pointCompleteIntent.putExtra("afterPoint", userPoint);
                    startActivity(pointCompleteIntent);

                    finish();
                }

            }).requestPayment();
}
```

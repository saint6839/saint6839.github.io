# 파이어베이스 구글 자동로그인

- **구글 자동로그인 구현하는방법**

    ```java
    // 현재 사용자 정보를 가져옴
    mAuth = FirebaseAuth.getInstance(); 
    FirebaseUser user = mAuth.getCurrentUser();
    
    // 현재 사용자의 idToken을 확인하여 자동 로그인 시킬지 말지 결정
    user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
        @Override
                public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                    if(task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        Log.d(TAG,"아이디 토큰 = " + idToken);
                        Intent homeMove_intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(homeMove_intent);
                    }
                }
            });        
    ```

    → 기존에 시도했던 방법들은 자꾸 이전 로그인 기록과 비교하여 로그인 시키려는 방법이었음, 그렇기 때문에 로그인 후 바로 다음 로그인은 자동로그인이 되지만, 그 다음부터는 다시 로그인해야하는 문제가 생김

    → <span style="color:red">**이를 본질적으로 해결하기 위해서는 로그인 기록을 비교하는것이 아니라, 클라이언트(파이어베이스)에 저장되어있는 해당 유저의 idToken을 비교하여야 한다.**</span>


package com.example.android.chat_firebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class First_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_);


        SharedPreferences sp1=this.getSharedPreferences("Login",0);

        String email=sp1.getString("Email", null);
        String user_id = sp1.getString("User_id", null);
        String password = sp1.getString("Password",null);

        if(email != null && user_id != null && password != null){
            Intent mainIntent = new Intent (First_Activity.this,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }
        else{
            Intent mainIntent = new Intent (First_Activity.this,StartActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }
    }
}

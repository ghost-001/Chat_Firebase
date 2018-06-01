package com.example.android.chat_firebase;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        }


        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group");
        mButton = findViewById(R.id.click_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllUsersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.main_account_setting_btn:
               // Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                //startActivity(intent);
                return true;
            case  R.id.main_logout_btn :
                if(mCurrentUser != null) {
                    mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
                }
                Context context = MainActivity.this;
                context.getSharedPreferences("Login", 0).edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                return true;
            case R.id.main_all_users_btn:
                Intent intent_all_user = new Intent(MainActivity.this,AllUsersActivity.class);
                startActivity(intent_all_user);
                return true;
        }
        return true;
    }
    private void  sendToStart(){
       Intent startIntent = new Intent ( this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }
}

package com.example.android.chat_firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mAge;
    private TextInputLayout mCity;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;

    private ProgressDialog mRegProgress;
    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;
    private  RadioButton mRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRegProgress = new ProgressDialog(this);

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mAge = (TextInputLayout) findViewById(R.id.reg_age);
        mCity = (TextInputLayout) findViewById(R.id.reg_city);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mRadioGroup = (RadioGroup)findViewById(R.id.reg_radio_group);

        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String age = mAge.getEditText().getText().toString();
                String city = mCity.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                mRadioButton = (RadioButton) findViewById(selectedId);
                String radio_text = mRadioButton.getText().toString();
                Log.i("FUCK OFF",radio_text);

                if (!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(radio_text)) {
                    register_user(display_name, age, city, email, password,radio_text);

                    mRegProgress.setTitle("Registering you . . .");
                    mRegProgress.setMessage("Please wait while we register you .");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } else {
                    Toast.makeText(RegisterActivity.this, "All fields are compulsory !!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void register_user(final String display_name, final String age, final String city,
                               final String email, String password, final String type) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser current_user = mAuth.getCurrentUser();
                            String user_uid = current_user.getUid();
                            myRef = mFirebaseDatabase.getReference().child("Users").child(user_uid);

                            String device_token = FirebaseInstanceId.getInstance().getToken();

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", display_name);
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");
                            userMap.put("age", age);
                            userMap.put("city", city);
                            userMap.put("device_token", device_token);
                            userMap.put("email", email);
                            userMap.put("uid", user_uid);
                            userMap.put("type",type);


                            myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mRegProgress.dismiss();

                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            mRegProgress.hide();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}

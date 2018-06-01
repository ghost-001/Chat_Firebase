package com.example.android.chat_firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserRecycler;

    private DatabaseReference mAllUsersDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String TAG = " ALL USERS ACTIVITY";


    FirebaseRecyclerAdapter<Users, AllUsersViewHolder> firebaseRecyclerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar) findViewById(R.id.users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mAllUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAllUsersDatabase.keepSynced(true);
        Log.i(TAG, "Toolbar DOne");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mUserRecycler = (RecyclerView) findViewById(R.id.allusers_recycler_view);
        mUserRecycler.setHasFixedSize(true);
        mUserRecycler.setLayoutManager(linearLayoutManager);


    }


    @Override
    protected void onStart() {
        super.onStart();
        mUserRef.child("online").setValue("true");


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mAllUsersDatabase, Users.class).build();
        Log.i(TAG, "onStart - ");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, AllUsersViewHolder>(personsOptions) {
            @NonNull
            @Override
            public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.allusers_single, parent, false);
                Log.i(TAG, "Create VIewHolder ");

                return new AllUsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position,
                                            @NonNull final Users model) {
                Log.i(TAG, "onBindVIewHolder - " + model.getName());
                holder.setDisplayName(model.getName());
                holder.setUserImage(model.getThumb_image(), getApplicationContext());

/*                final String user_id = getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Intent chat_intent = new Intent(AllUsersActivity.this, ChatActivity.class);
                        chat_intent.putExtra("user_id", user_id);
                        chat_intent.putExtra("user_name", model.getName());
                        startActivity(chat_intent);
                    }
                });*/

            }
        };
        mUserRecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }


    @Override
    public void onStop() {

        super.onStop();
        if (mCurrentUser != null) {
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
        firebaseRecyclerAdapter.stopListening();
    }


    public static class AllUsersViewHolder extends RecyclerView.ViewHolder {
        public View mView;


        public AllUsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String a) {
            TextView userName = (TextView) mView.findViewById(R.id.allusers_name);
            userName.setText(a);
        }

        public void setUserImage(String thumb_image, Context ct) {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.allusers_image);
            Picasso.get().load(thumb_image).placeholder(R.drawable.user).into(userImageView);
        }
    }
}


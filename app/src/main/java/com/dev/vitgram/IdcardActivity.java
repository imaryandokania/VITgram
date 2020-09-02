package com.dev.vitgram;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class IdcardActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DatabaseReference Rootref;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private TextView iname,ireg;
    private ImageView iprof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        iname=(TextView)findViewById(R.id.idname);
        ireg=(TextView)findViewById(R.id.regid);
        iprof=(ImageView)findViewById(R.id.imagey);
        mToolbar=(Toolbar)findViewById(R.id.idcard_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Vit Id Card");
        Rootref= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        currentUserID= mAuth.getCurrentUser().getUid();
        Rootref.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("regno"))&&(dataSnapshot.hasChild("image")))
                {
                    String name=dataSnapshot.child("name").getValue().toString();
                    String regno=dataSnapshot.child("regno").getValue().toString();
                    String image=dataSnapshot.child("image").getValue().toString();
                    iname.setText(name);
                    ireg.setText(regno);
                    Picasso.get().load(image).into(iprof);



                }
                else
                {
                    String name=dataSnapshot.child("name").getValue().toString();
                    String regno=dataSnapshot.child("regno").getValue().toString();
                    iname.setText(name);
                    ireg.setText(regno);
                    iprof.setImageResource(R.drawable.profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

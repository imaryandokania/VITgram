package com.dev.vitgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QrcodeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String curret;
    private FirebaseUser currentuser;
    ImageView imageView;
    Button button;
    private String currentUserID;
    public TextView text;
    String reg;
    String name;
    private DatabaseReference Rootref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        imageView=(ImageView)findViewById(R.id.qr_code);
        Rootref= FirebaseDatabase.getInstance().getReference();
        loadingbar=new ProgressDialog(this);
        text=(TextView)findViewById(R.id.qrtext);
        button=(Button)findViewById(R.id.back_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();

            }
        });
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        currentUserID= mAuth.getCurrentUser().getUid();
        //loadingbar.setTitle("Qr Code Generator invoked");
        //loadingbar.setMessage("Generating Please wait");
       // loadingbar.show();

        if(!currentUserID.equals(""))
                {

                    Rootref.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if((dataSnapshot.exists()) && (dataSnapshot.hasChild("regno")))
                            {
                                String retriveUserName =dataSnapshot.child("regno").getValue().toString();
                                String retriveStatus =dataSnapshot.child("name").getValue().toString();
                                // String retriveProfileImage =dataSnapshot.child("image").getValue().toString();    //image change
                                reg=retriveUserName;
                                name=retriveStatus;

                                new ImageDownloaderTask(imageView).execute("https://api.qrserver.com/v1/create-qr-code/?size=1000x100&data="+reg+"  "+name );



                            }
                            else
                            {
                                Toast.makeText(QrcodeActivity.this, "Sorry!", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                else
                {
                    Toast.makeText(QrcodeActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }





    }


    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(QrcodeActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //user cant use back button.
        startActivity(mainIntent);
        finish();

    }
}

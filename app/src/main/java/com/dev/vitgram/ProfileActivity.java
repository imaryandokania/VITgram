package com.dev.vitgram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserID,senderUserID,current_stat;
    private CircleImageView userProfileImage;
    private TextView userProfileName,userProfileStatus;
    private Button SendMessageRequestButton;
    private DatabaseReference UserRef,ChatRequestRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        UserRef= FirebaseDatabase.getInstance().getReference();
        ChatRequestRef= FirebaseDatabase.getInstance().getReference().child("Chat Request");
        receiverUserID=getIntent().getExtras().get("visit_user_id").toString();
        senderUserID=mAuth.getCurrentUser().getUid();
        Log.i("User",receiverUserID);
       // Toast.makeText(this,receiverUserID, Toast.LENGTH_SHORT).show();
        userProfileImage=(CircleImageView)findViewById(R.id.visit_profile_image);
        userProfileName=(TextView)findViewById(R.id.visit_user_name);
        userProfileStatus=(TextView)findViewById(R.id.visit_user_status);
        SendMessageRequestButton=(Button)findViewById(R.id.send_message_request_button);
        current_stat="new";
        RetrieveUserInfo();


    }

    private void RetrieveUserInfo() {
        UserRef.child("Users").child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists())  && (dataSnapshot.hasChild("image")))
                {
                    String userImage=dataSnapshot.child("image").getValue().toString();
                    String userNamee =dataSnapshot.child("regno").getValue().toString();
                    String userStatus=dataSnapshot.child("name").getValue().toString();
                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(userNamee);
                    userProfileStatus.setText(userStatus);
                    ManageChatRequests();
                }
                else
                {
                    String userNamee=dataSnapshot.child("regno").getValue().toString();
                    String userStatus=dataSnapshot.child("name").getValue().toString();
                    userProfileName.setText(userNamee);
                    userProfileStatus.setText(userStatus);
                    ManageChatRequests();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequests() {
        ChatRequestRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(receiverUserID))
                {
                    String  request_type=dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();
                    if(request_type.equals("sent"))
                    {
                        current_stat="request_sent";
                        SendMessageRequestButton.setText("Cancel Chat Request");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(!senderUserID.equals(receiverUserID))
        {
            SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessageRequestButton.setEnabled(false);
                    if(current_stat.equals("new"))
                    {
                        SendChatRequest();
                    }
                    if (current_stat.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }
                }
            });
        }
        else
        {
          SendMessageRequestButton.setVisibility(View.INVISIBLE);
        }
    }

    private void CancelChatRequest() {
        ChatRequestRef.child(senderUserID).child(receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful())
               {
                   ChatRequestRef.child(receiverUserID).child(senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful())
                           {
                               SendMessageRequestButton.setEnabled(true);
                               current_stat="new";
                               SendMessageRequestButton.setText("Send Message");
                           }

                       }
                   });
               }
            }
        });
    }

    private void SendChatRequest()
    {
        ChatRequestRef.child(senderUserID).child(receiverUserID).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    ChatRequestRef.child(receiverUserID).child(senderUserID).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                SendMessageRequestButton.setEnabled(true);
                                current_stat="request_sent";
                                SendMessageRequestButton.setText("Cancel Chat Request");
                            }

                        }
                    });
                }

            }
        });

    }
}

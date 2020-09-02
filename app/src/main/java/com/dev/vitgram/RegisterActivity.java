package com.dev.vitgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;
    private TextView AlreadyHaveAccountLink;
    private ProgressDialog loadingbar;
    private DatabaseReference RootRef;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        InitializeFields();
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();

            }
        });
    }

    private void CreateNewAccount() {
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast toast=Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.parseColor("#0F9BDA"));
            toast.show();

        }
        if(TextUtils.isEmpty(password))
        {
           Toast toast= Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.parseColor("#0F9BDA"));
            toast.show();
        }
        else
        {
            loadingbar.setTitle("Creating new Account");
            loadingbar.setMessage("Please wait we are creating new account for you");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {   String currentUserID=mAuth.getCurrentUser().getUid();
                         RootRef.child("Users").child(currentUserID).setValue("");
                        sendUserToMainActivity();
                        Toast toast=Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#0F9BDA"));
                        toast.show();
                        loadingbar.dismiss();
                    }
                    else
                    {
                        String message=task.getException().toString();
                        Toast toast=Toast.makeText(RegisterActivity.this,message, Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#0F9BDA"));
                        toast.show();
                        loadingbar.dismiss();
                    }

                }
            });
        }

    }
    private void sendUserToSettingsActivity() {
        Intent settingsIntent=new Intent(RegisterActivity.this,SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }
    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);

    }
    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //user cant use back button.
        startActivity(mainIntent);
        finish();

    }

    private void InitializeFields() {
        CreateAccountButton=(Button)findViewById(R.id.register_button);
        UserEmail=(EditText)findViewById(R.id.register_email);
        UserPassword=(EditText)findViewById(R.id.register_password);
        AlreadyHaveAccountLink=(TextView)findViewById(R.id.already_have_an_account_link);
        loadingbar=new ProgressDialog(this, R.style.MyAlertDialogStyle);

    }
}

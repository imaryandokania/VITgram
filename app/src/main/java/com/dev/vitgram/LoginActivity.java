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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentuser;
    private Button LoginButton,PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccountLink, ForgotPasswordLink;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeFields();
        mAuth=FirebaseAuth.getInstance();
       // currentuser =mAuth.getCurrentUser();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();

            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneloginintent=new Intent(LoginActivity.this,PhoneLoginActivity.class);
                startActivity(phoneloginintent);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
    private void AllowUserToLogin() {
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
            loadingbar.setTitle("Logging In");
            loadingbar.setMessage("Please wait we are logging you in");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        sendUserToMainActivity();
                        Toast toast=Toast.makeText(LoginActivity.this, "Logged in successful", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#EFEEEE"), PorterDuff.Mode.SRC_IN);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#0F9BDA"));
                        toast.show();
                        loadingbar.dismiss();

                    }
                    else
                    {
                        String message=task.getException().toString();
                        Toast toast=Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT);
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


    private void InitializeFields() {
        LoginButton=(Button)findViewById(R.id.login_button);
        PhoneLoginButton=(Button)findViewById(R.id.phone_login_button);
        UserEmail=(EditText)findViewById(R.id.login_email);
        UserPassword=(EditText)findViewById(R.id.login_password);
        NeedNewAccountLink=(TextView)findViewById(R.id.need_new_account_link);
        ForgotPasswordLink=(TextView)findViewById(R.id.forget_password_link);
        loadingbar=new ProgressDialog(this, R.style.MyAlertDialogStyle);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //user cant use back button.
        startActivity(mainIntent);
        finish();

    }
    private void sendUserToRegisterActivity() {
        Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);

    }
}

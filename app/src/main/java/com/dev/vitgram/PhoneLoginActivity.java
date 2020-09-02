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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private Button SendverificationcodeButton,VerifyButton;
    private EditText InputPhoneNumber,InputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        loadingbar=new ProgressDialog(this, R.style.MyAlertDialogStyle);
        mAuth=FirebaseAuth.getInstance();
        SendverificationcodeButton=(Button)findViewById(R.id.send_ver_code_button);
        VerifyButton=(Button)findViewById(R.id.verify_button);
        InputPhoneNumber=(EditText)findViewById(R.id.phone_number_input);
        InputVerificationCode=(EditText)findViewById(R.id.verification_code_input);
        SendverificationcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber=InputPhoneNumber.getText().toString();

                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast toast =Toast.makeText(PhoneLoginActivity.this, "Phone Number Required", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#0F9BDA"));
                    toast.show();


                }
                else
                {

                    loadingbar.setTitle("Phone Number Verification");
                    loadingbar.setMessage("Authenticating");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                           phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendverificationcodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);
                String verificationcode=InputVerificationCode.getText().toString();
                if(TextUtils.isEmpty(verificationcode))
                {
                   Toast toast= Toast.makeText(PhoneLoginActivity.this, "Please Enter Code", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#0F9BDA"));
                    toast.show();

                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                    loadingbar.setTitle("Code Verification");
                    loadingbar.setMessage("Authenticating");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                }

            }
        });
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
               signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                loadingbar.dismiss();
                Toast toast=Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number,enter with country code", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#0F9BDA"));
                toast.show();
                SendverificationcodeButton.setVisibility(View.VISIBLE);
                InputPhoneNumber.setVisibility(View.VISIBLE);
                VerifyButton.setVisibility(View.INVISIBLE);
                InputVerificationCode.setVisibility(View.INVISIBLE);
            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {




                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                SendverificationcodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);
                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);

            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            loadingbar.dismiss();
                            Toast toast=Toast.makeText(PhoneLoginActivity.this, "You are logged in", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                            TextView text = view.findViewById(android.R.id.message);
                            text.setTextColor(Color.parseColor("#0F9BDA"));
                            toast.show();
                            SendUserToMainActivity();

                        } else
                            {
                                loadingbar.dismiss();
                                String message=task.getException().toString();
                                Toast toast= Toast.makeText(PhoneLoginActivity.this,"Error"+message, Toast.LENGTH_SHORT);
                                View view = toast.getView();
                                view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                                TextView text = view.findViewById(android.R.id.message);
                                text.setTextColor(Color.parseColor("#0F9BDA"));
                                toast.show();

                        }
                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainintent=new Intent(PhoneLoginActivity.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }
}

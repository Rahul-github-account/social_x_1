package com.example.socialx.Sign_UP_In;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialx.FacebookAuthentication;
import com.example.socialx.GoogleAuth;
import com.example.socialx.Home_page.HomePage;
import com.example.socialx.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class SignUpIn extends AppCompatActivity {

    TextView loginBtnTv, signUpBtnTv,
            termsAgreeTv, alreadyAccTv,
            forgetPassTv, notAccTv;

    TextInputEditText nameEt, signUpEmailEt, contactNoEt, signUpPassEt,
            loginEmailEt, loginPassEt;

    CheckBox agreementCh;

    Button regOrLoginBtn;

    ImageView googleBtnImv, fbBtnImv;

    ScrollView signUPLayout, loginLayout;

    CountryCodePicker ccp;

    ProgressDialog progressDialog;

    boolean isLogin;

    FirebaseDatabase realTimeDatabase;

    FirebaseAuth mAuth;

    private void initialisation() {
        mAuth = FirebaseAuth.getInstance();

        loginBtnTv = findViewById(R.id.loginTv);
        signUpBtnTv = findViewById(R.id.signUpTv);
        termsAgreeTv = findViewById(R.id.agreementTv);
        alreadyAccTv = findViewById(R.id.alreadyAcSignInTv);
        forgetPassTv = findViewById(R.id.forgotPassTv);
        notAccTv = findViewById(R.id.notAccRegisterNowTv);
        nameEt = findViewById(R.id.nameEt);
        signUpEmailEt = findViewById(R.id.signUpEmailEt);
        contactNoEt = findViewById(R.id.contactNoEt);
        signUpPassEt = findViewById(R.id.signUpPasswordEt);
        agreementCh = findViewById(R.id.agreementChBox);
        regOrLoginBtn = findViewById(R.id.registerOrLoginBtn);
        loginEmailEt = findViewById(R.id.signInEmailEt);
        loginPassEt = findViewById(R.id.signInPasswordEt);
        googleBtnImv = findViewById(R.id.googleSignInImv);
        fbBtnImv = findViewById(R.id.facebookSignInImv);
        ccp = findViewById(R.id.countryCodePicker);

        signUPLayout = findViewById(R.id.signUpLayout);
        loginLayout = findViewById(R.id.signInLayout);

        ccp.registerCarrierNumberEditText(contactNoEt);

        realTimeDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        initialisation();
        textFormatting();
        buttonClicks();

    }

    private void textFormatting() {
        agreementTextFormatting();
        signInFormatting();
        signUpFormatting();
    }

    private void buttonClicks() {
        loginBtnTv.setOnClickListener(this::loginClick);
        signUpBtnTv.setOnClickListener(this::signUpClicks);
        googleBtnImv.setOnClickListener(this::googleLogin);
        fbBtnImv.setOnClickListener(this::fbLogin);
        regOrLoginBtn.setOnClickListener(this::regOrLoginClicks);
        forgetPassTv.setOnClickListener(this::forgetBottomSheet);
    }

    private void googleLogin(View view) {
        startActivity(new Intent(this, GoogleAuth.class)
                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void fbLogin(View view) {
        startActivity(new Intent(
                this,
                FacebookAuthentication.class)
                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void regOrLoginClicks(View view) {
        if (!isLogin) {
            userRegistration();
        } else {
            userSignIn();
        }
    }

    private void userSignIn() {
        String email, password;
        email = loginEmailEt.getText().toString();
        password = loginPassEt.getText().toString();

        if (email.isEmpty()) {
            loginEmailEt.requestFocus();
            loginEmailEt.setError("Should not be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmailEt.requestFocus();
            loginEmailEt.setError("Write correct formatted email Ex. socialx@domain.com");
        } else if (password.isEmpty()) {
            loginPassEt.requestFocus();
            loginPassEt.setError("Should not be empty!");
        } else if (password.length() < 6) {
            loginPassEt.requestFocus();
            loginPassEt.setError("Password must contain at least 6 characters");
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            startActivity(new Intent(this, HomePage.class));
                            finish();
                        } else {
                            Toast.makeText(this,
                                    task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    });
        }
    }

    private void forgetBottomSheet(View view) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.forget_password_bottom_layout);
        dialog.show();

        TextInputEditText emailEt;
        TextView sendResetLink, retrySignIn;
        emailEt = dialog.findViewById(R.id.bottomEmailEt);
        sendResetLink = dialog.findViewById(R.id.getResetLinkTv);
        retrySignIn = dialog.findViewById(R.id.retryLoginTv);

        final String[] emailId = {signUpEmailEt.getText().toString()};

        if (!emailId[0].isEmpty()) {
            emailEt.setText(emailId[0]);
        }

        sendResetLink.setOnClickListener(view1 -> {
            emailId[0] = emailEt.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(emailId[0]).matches()) {
                emailEt.requestFocus();
                emailEt.setError("Write correct formatted email Ex. socialx@domain.com");
            } else {
                mAuth.sendPasswordResetEmail(emailId[0])
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                Toast.makeText(SignUpIn.this, "Link sent", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(SignUpIn.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        retrySignIn.setOnClickListener(view1 -> dialog.dismiss());

    }

    private void userRegistration() {
        String name, email, contactNo, password;
        name = nameEt.getText().toString();
        email = signUpEmailEt.getText().toString();
        contactNo = contactNoEt.getText().toString();
        password = signUpPassEt.getText().toString();

        if (name.isEmpty()) {
            nameEt.requestFocus();
            nameEt.setError("Should not be empty!");
        } else if (!name.matches("[a-zA-Z]{3,}(?: [a-zA-Z]*){0,3}$")) {
            nameEt.requestFocus();
            nameEt.setError("Write correct formatted name Ex. Rahul Kumar");
        } else if (email.isEmpty()) {
            signUpEmailEt.requestFocus();
            signUpEmailEt.setError("Should not be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmailEt.requestFocus();
            signUpEmailEt.setError("Write correct formatted email Ex. socialx@domain.com");
        } else if (!ccp.isValidFullNumber()) {
            contactNoEt.requestFocus();
            contactNoEt.setError("Enter correct contact no");
        } else if (password.isEmpty()) {
            signUpPassEt.requestFocus();
            signUpPassEt.setError("Should not be empty!");
        } else if (password.length() < 6) {
            signUpPassEt.requestFocus();
            signUpPassEt.setError("Password must contain at least 6 characters");
        } else if (!agreementCh.isChecked()) {
            Toast.makeText(this, "Please Agree to term & condition", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Processing...");
            progressDialog.show();
            userSignUp(name, contactNo, email, password);
        }


    }

    private void userSignUp(String name, String contactNo, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult ->
                        saveEmailRegData(name, contactNo, email, authResult.getUser().getUid()))
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                });
    }

    private void saveEmailRegData(String name, String contactNo, String email, String uid) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("Name", name);
        userData.put("Contact No", contactNo);
        userData.put("Email ID", email);

        realTimeDatabase.getReference()
                .child(uid)
                .setValue(userData)
                .addOnSuccessListener(unused -> {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                });
        startActivity(new Intent(this, HomePage.class));
        finish();

    }

    private void loginClick(View view) {
        loginLayout.setVisibility(View.VISIBLE);
        signUPLayout.setVisibility(View.GONE);
        loginBtnTv.setTextColor(Color.parseColor("#FFFFFF"));
        loginBtnTv.setBackground(this.getDrawable(R.drawable.bottom_corner_filled));
        signUpBtnTv.setTextColor(Color.parseColor("#000000"));
        signUpBtnTv.setBackground(null);
        isLogin = true;
        regOrLoginBtn.setText("LOGIN");
    }

    private void signUpClicks(View view) {
        signUPLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        signUpBtnTv.setTextColor(Color.parseColor("#FFFFFF"));
        signUpBtnTv.setBackground(this.getDrawable(R.drawable.bottom_corner_filled));
        loginBtnTv.setTextColor(Color.parseColor("#000000"));
        loginBtnTv.setBackground(null);
        isLogin = false;
        regOrLoginBtn.setText("REGISTER");
    }

    private void signUpFormatting() {
        String registerString = "Don't have an Account ? Register Now";
        SpannableString ssH = new SpannableString(registerString);

        ClickableSpan registerSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                signUPLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
                signUpBtnTv.setTextColor(Color.parseColor("#FFFFFF"));
                signUpBtnTv.setBackground(widget.getContext().getDrawable(R.drawable.bottom_corner_filled));
                loginBtnTv.setTextColor(Color.parseColor("#000000"));
                loginBtnTv.setBackground(null);
                isLogin = false;
                regOrLoginBtn.setText("REGISTER");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF0000"));
                ds.setUnderlineText(true);
            }
        };
        ssH.setSpan(registerSpan, 24, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        notAccTv.setText(ssH);
        notAccTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void signInFormatting() {
        String signInLink = "Already have an Account ? Sign In!";
        SpannableString ssH = new SpannableString(signInLink);

        ClickableSpan signInSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                loginLayout.setVisibility(View.VISIBLE);
                signUPLayout.setVisibility(View.GONE);
                loginBtnTv.setTextColor(Color.parseColor("#FFFFFF"));
                loginBtnTv.setBackground(widget.getContext().getDrawable(R.drawable.bottom_corner_filled));
                signUpBtnTv.setTextColor(Color.parseColor("#000000"));
                signUpBtnTv.setBackground(null);
                isLogin = true;
                regOrLoginBtn.setText("LOGIN");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF0000"));
                ds.setUnderlineText(true);
            }
        };
        ssH.setSpan(signInSpan, 26, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alreadyAccTv.setText(ssH);
        alreadyAccTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void agreementTextFormatting() {
        String agreementText = "I agree with term & condition";
        SpannableString ssH = new SpannableString(agreementText);

        ClickableSpan agreementSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignUpIn.this,
                        "Terms Condition Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF0000"));
                ds.setUnderlineText(true);
            }
        };
        ssH.setSpan(agreementSpan, 13, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsAgreeTv.setText(ssH);
        termsAgreeTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpIn.this, HomePage.class));
            finish();
        }
        super.onStart();
    }
}
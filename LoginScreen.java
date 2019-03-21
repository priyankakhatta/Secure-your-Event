package event.secure.secureyourevent.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import event.secure.secureyourevent.Activity.Utils.InternetConnectionDetector;
import event.secure.secureyourevent.Activity.Utils.My_Prefrence_Class;
import event.secure.secureyourevent.R;




public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "show" ;
    private Button loginSubmitButton,signUpButton;
    private TextView forgotPassTextview,chngeLangTextview;
    private EditText userEmailEditText,passwordEditText;
    private String editTextUsernameStr = "p",editTextPasswordStr = "p";


    private ProgressDialog pDialog;
    private My_Prefrence_Class getPref;
    private InternetConnectionDetector cd;
    Boolean isInternetPresent = false;
    Locale myLocale;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            loadUserInformation();

            startActivity(new Intent(LoginScreen.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.login_screen);

        cd = new InternetConnectionDetector(getApplicationContext());
        getPref = new My_Prefrence_Class(getApplicationContext());
        pDialog = new ProgressDialog(LoginScreen.this);
        pDialog.setMessage(getResources().getText(R.string.splash_text2));


        userEmailEditText=(EditText)findViewById(R.id.userEmailEditText);
        passwordEditText=(EditText)findViewById(R.id.passwordEditText);
        loginSubmitButton=(Button)findViewById(R.id.loginSubmitButton);
        forgotPassTextview=(TextView)findViewById(R.id.forgotPassTextview);
        signUpButton=(Button)findViewById(R.id.signUpButton);
       // chngeLangTextview=(TextView)findViewById(R.id.chngeLangTextview);
//        chngeLangTextview.setOnClickListener(this);
        loginSubmitButton.setOnClickListener(this);
        forgotPassTextview.setOnClickListener(this);
        signUpButton.setOnClickListener(this);



    }

    private void loadUserInformation() {

        FirebaseUser user = mAuth.getCurrentUser();

       String name = user.getDisplayName().toString();
//        String b = user.getPhoneNumber().toString();
        String b = user.getPhotoUrl().toString();
//        String b = user.getProviderId().toString();
//        String b = user.getEmail().toString();


    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.loginSubmitButton:
            {
                String email = userEmailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.isShowing();
                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                    pDialog.dismiss();

                                    if (!task.isSuccessful()) {
                                    // there was an error
//                                    if (password.length() < 6) {
//                                        passwordEditText.setError(getString(R.string.minimum_password));
//                                    } else {
                                        Toast.makeText(LoginScreen.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//                                    }
                                } else {
                                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
        }

                break;

            case R.id.signUpButton:


                Intent objj = new Intent(LoginScreen.this, SignUp.class);
                startActivity(objj);
                finish();
                break;

            case R.id.forgotPassTextview:

                Intent obj = new Intent(LoginScreen.this, ForgotPasswordScreen.class);
                startActivity(obj);
                finish();
                break;


//            case R.id.chngeLangTextview:
//                final Dialog dial=new Dialog(LoginScreen.this);
//                dial.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                dial.setContentView(R.layout.select_language);
//
//                RadioGroup radiogroup=(RadioGroup)dial.findViewById(R.id.radiogroup);
//                RadioButton en_radioButton=(RadioButton)dial.findViewById(R.id.en_radioButton);
//                RadioButton fr_radioButton=(RadioButton)dial.findViewById(R.id.fr_radioButton);
//
//
//                String default_lang= Locale.getDefault().toString();
//                Log.d("default_lang",default_lang);
//                if (default_lang.equals("en_US"))
//                {
//                    radiogroup.check(R.id.en_radioButton);
//                }
//                else if(default_lang.equals("fr"))
//                {
//                    radiogroup.check(R.id.fr_radioButton);
//                }
//                else {
//                    radiogroup.check(R.id.en_radioButton);
//                }
//                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        // find which radio button is selected
//                        if(checkedId == R.id.en_radioButton) {
//                            getPref.setlang("en");
//                            changeLang("en");
//                            dial.dismiss();
//                            startActivity(getIntent());
//                            finish();
//
//                        } else if(checkedId == R.id.fr_radioButton) {
//                            getPref.setlang("fr");
//                            changeLang("fr");
//                            dial.dismiss();
//                            startActivity(getIntent());
//                            finish();
//
//                        } else {
//                            getPref.setlang("en");
//                            changeLang("en");
//                            dial.dismiss();
//                            startActivity(getIntent());
//                        }
//                    }
//                });
//
//                dial.show();
//
//                break;
//
        }
    }

    private void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
    @Override
    public void onBackPressed() {

//            super.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,  getResources().getText(R.string.double_back), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}




package event.secure.secureyourevent.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import event.secure.secureyourevent.Activity.Utils.InternetConnectionDetector;
import event.secure.secureyourevent.Activity.Utils.My_Prefrence_Class;
import event.secure.secureyourevent.R;

public class SignUp extends AppCompatActivity {

    private EditText emailEditText,passEditText,conPassEditText;
    private Button signupSubmitButton;


    private ProgressDialog pDialog;
    private My_Prefrence_Class getPref;
    private InternetConnectionDetector cd;
    Boolean isInternetPresent = false;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        cd = new InternetConnectionDetector(getApplicationContext());
        getPref = new My_Prefrence_Class(getApplicationContext());
        pDialog = new ProgressDialog(SignUp.this);
        pDialog.setMessage( getResources().getText(R.string.splash_text2));
        mAuth = FirebaseAuth.getInstance();


        emailEditText =(EditText)findViewById(R.id.emailEditText);
        passEditText =(EditText)findViewById(R.id.passEditText);
        conPassEditText =(EditText)findViewById(R.id.conPassEditText);

        signupSubmitButton =(Button)findViewById(R.id.signupSubmitButton);

        signupSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.isShowing();
                //create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this, "Sign Up successful! ", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    finish();
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                }
                            }
                        });

            }
        });

            }
            

//        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), conPassEditText.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(SignUp.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });




@Override
protected void onResume() {
        super.onResume();
    pDialog.dismiss();
        }

//    public class AsyncSignUp extends AsyncTask<Void, Void, Void> {
//        String id="", data="",  success="" ;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // Internet Connection
//            ServiceHandler sh = new ServiceHandler();
//
//            String jsonStr = sh.makeServiceCall(Constants.serviceType.APP_SIGN_UP
//                    +"first_name="+ URLEncoder.encode(firstNameEditText.getText().toString())
//                    +"&last_name="+ URLEncoder.encode(lastNameEditText.getText().toString())
//                    +"&email="+ URLEncoder.encode(emailEditText.getText().toString())
//                    +"&phone="+ URLEncoder.encode(phoneEditText.getText().toString())
//                    +"&password="+ URLEncoder.encode(passwordEditText.getText().toString())
//                    +"&address="+ URLEncoder.encode(addressEditText.getText().toString())
//                    +"&register="+ URLEncoder.encode("1")
//                    +"&aboutme="+ URLEncoder.encode(aboutMeEditText.getText().toString()), ServiceHandler.GET);
//
//            Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    success = jsonObj.getString("success");
//                    data = jsonObj.getString("data");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e( getResources().getText(R.string.service_handler).toString(),  getResources().getText(R.string.service_handler_message).toString());
//                Toast.makeText(getApplicationContext(),  getResources().getText(R.string.error), Toast.LENGTH_LONG).show();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//
//            if(success.equals("1"))
//            {
//                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
//                startActivity(intent);
//                finish();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(),data, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

      public void onBackPressed() {
        Intent obj = new Intent(SignUp.this, LoginScreen.class);
        startActivity(obj);
        finish();

    }
}

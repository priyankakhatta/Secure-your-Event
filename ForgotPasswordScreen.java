package event.secure.secureyourevent.Activity.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import event.secure.secureyourevent.Activity.Utils.Constants;
import event.secure.secureyourevent.Activity.Utils.InternetConnectionDetector;
import event.secure.secureyourevent.Activity.Utils.My_Prefrence_Class;
import event.secure.secureyourevent.Activity.Utils.ServiceHandler;
import event.secure.secureyourevent.R;

/**
 * Created by Priyanka on 11/2/2016.
 */
public class ForgotPasswordScreen extends Activity {


    private EditText emailEditText;
    private String emailTextStr="";
    private Button forgotSubmitButton;
    private ProgressDialog pDialog;
    private My_Prefrence_Class getPref;
    private InternetConnectionDetector cd;
    Boolean isInternetPresent = false;
    private String TAG = "Response";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        forgotSubmitButton=(Button)findViewById(R.id.forgotSubmitButton);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
//        forgetMsgTextview=(TextView)findViewById(R.id.forgetMsgTextview);
        cd = new InternetConnectionDetector(getApplicationContext());
        getPref = new My_Prefrence_Class(getApplicationContext());
        pDialog = new ProgressDialog(ForgotPasswordScreen.this);
        pDialog.setMessage(String.valueOf(R.string.splash_text2));
        mAuth = FirebaseAuth.getInstance();


        forgotSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailTextStr=emailEditText.getText().toString();

                isInternetPresent = cd.isConnectingToInternet();
                if (!isInternetPresent) {
                    Toast.makeText(getApplicationContext(), R.string.check_Internet_connection, Toast.LENGTH_SHORT).show();
                } else {
//                    AsyncForgot task = new AsyncForgot();
//                    task.execute();


                    if (TextUtils.isEmpty(emailTextStr)) {
                        Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                        return;
                    }

                        pDialog.isShowing();
                    mAuth.sendPasswordResetEmail(emailTextStr
                    )
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPasswordScreen.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                                        Intent obj = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
                                        startActivity(obj);
                                        finish();

                                    } else {
                                        Toast.makeText(ForgotPasswordScreen.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }

                                    pDialog.dismiss();
                                }
                            });

                }

            }
        });
    }
//    public class AsyncForgot extends AsyncTask<Void, Void, Void> {
//        String success="",sms="" ;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog.show();
//            pDialog.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
//            ServiceHandler sh = new ServiceHandler();
//
//            String jsonStr = sh.makeServiceCall(Constants.serviceType.APP_FORGOT_PASSWORD
//                    +"email="+ URLEncoder.encode(emailTextStr), ServiceHandler.GET);
//
//            Log.d("Response: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    success = jsonObj.getString("success");
//
//                        String data = jsonObj.getString("data");
//                        sms=data;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e( getResources().getText(R.string.service_handler).toString(), getResources().getText(R.string.service_handler_message).toString());
//                Toast.makeText(getApplicationContext(), sms, Toast.LENGTH_LONG).show();
//            }
//            return null;
//        }
//
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
//               // forgetMsgTextview.setText(""+sms);
//            }
//            else
//            {
//                //forgetMsgTextview.setText(""+sms);
//            }
//        }
//    }
    public void onBackPressed() {
        Intent obj = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
        startActivity(obj);
        finish();

    }
}

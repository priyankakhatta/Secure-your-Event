package event.secure.secureyourevent.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import event.secure.secureyourevent.Activity.Adapter.EventAdapter;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.Activity.Model.Profile;
import event.secure.secureyourevent.Activity.Utils.InternetConnectionDetector;
import event.secure.secureyourevent.Activity.Utils.My_Prefrence_Class;
import event.secure.secureyourevent.R;

/**
 * Created by priyankakhatta on 2018-02-11.
 */

public class ProfileScreen extends AppCompatActivity {

    private EditText firstNameEditText,lastNameEditText, emailEditText,passwordEditText,phoneEditText,addressEditText,aboutMeEditText;
    private RadioGroup radiogroup;
    private RadioButton customerRadioButton, securityCompanyRadioButton, individualGuardRadioButton;
    private Button updateButton;
    private ImageView profileImageView;

    private ProgressDialog pDialog;
    private My_Prefrence_Class getPref;
    private InternetConnectionDetector cd;
    Boolean isInternetPresent = false;
   // private DatabaseReference dbUsers;
    private Uri uriPofileImage;
    private String prefrenceStr ="C", userId;
    private ProgressBar progressBr;
    private String profileUrl;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference dbProfile;
    private FirebaseUser usr;
    private ArrayList<Profile> profileList = new ArrayList<>();
    private String profileId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);



        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        dbProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Profile");


        profileList = new ArrayList<>();

        cd = new InternetConnectionDetector(getApplicationContext());
        getPref = new My_Prefrence_Class(getApplicationContext());
        pDialog = new ProgressDialog(ProfileScreen.this);
        pDialog.setMessage( getResources().getText(R.string.splash_text2));
      //  dbUsers = FirebaseDatabase.getInstance().getReference("Users");

        firstNameEditText =(EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText =(EditText)findViewById(R.id.lastNameEditText);
      //  emailEditText =(EditText)findViewById(R.id.emailEditText);
        phoneEditText =(EditText)findViewById(R.id.phoneEditText);
        addressEditText =(EditText)findViewById(R.id.addressEditText);
        aboutMeEditText =(EditText)findViewById(R.id.aboutMeEditText);
        profileImageView = (ImageView)findViewById(R.id.profileImageView);
        progressBr = (ProgressBar)findViewById(R.id.progressBr);

        radiogroup =(RadioGroup) findViewById(R.id.radiogroup);
        customerRadioButton =(RadioButton)findViewById(R.id.customerRadioButton);
        securityCompanyRadioButton =(RadioButton)findViewById(R.id.securityCompanyRadioButton);
        individualGuardRadioButton =(RadioButton)findViewById(R.id.individualGuardRadioButton);

        updateButton =(Button)findViewById(R.id.updateButton);




//        firstNameEditText.setText("");
//        lastNameEditText.setText("");
//        phoneEditText.setText("");
//        addressEditText.setText("");
//        aboutMeEditText.setText("");
//        profileImageView.setImageResource(R.drawable.thumbnail);


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            saveUserInfo();
            }
        });



        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.customerRadioButton){

                    prefrenceStr = "C";
                }
                else if(checkedId == R.id.securityCompanyRadioButton){

                    prefrenceStr = "S";

                }
                else if(checkedId == R.id.individualGuardRadioButton){

                    prefrenceStr = "I";

                }
            }
        });


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileScreen.this, LoginScreen.class));
                    finish();
                }
                else {

//                    if((dbProfile.child("pid").equals("1")))
//                    {
//                        loadUserInfomation();
//                    }


                }
            }
        };


//        if(firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this, LoginScreen.class));
//        }


    }

    private void loadUserInfomation(Profile post) {


                    firstNameEditText.setText(post.getfName());
                    lastNameEditText.setText(post.getlName());
                    aboutMeEditText.setText(post.getAboutMe());
                    phoneEditText.setText(post.getPhne());
                    addressEditText.setText(post.getAddress());


                  if(post.getPrefrence().equals("C")){

                      customerRadioButton.setChecked(true);

                  }
                  else if(post.getPrefrence().equals("S")) {

                      securityCompanyRadioButton.setChecked(true);
                  }

                  else if(post.getPrefrence().equals("I")) {

                      individualGuardRadioButton.setChecked(true);

                  }

//        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
//
            Glide.with(this)
                    .load(firebaseAuth.getCurrentUser().getPhotoUrl().toString())
                    .into(profileImageView);

//        }
//        else {
//
//           // profileImageView.setImageResource(R.drawable.thumbnail);
//
//        }

//        if (user.getPhotoUrl() != null) {
//
//
//            Glide.with(this)
//                    .load(firebaseAuth.getCurrentUser().toString())
//                    .into(profileImageView);
//        }
//        if (user.getDisplayName() != null) {
//
//
//            firstNameEditText.setText(user.getDisplayName());
//         //   lastNameEditText.setText(user.getDisplayName());
//
//        }
//
//            phoneEditText.setText(user.getPhoneNumber());


    }


    private void saveUserInfo() {

        String  displayFName = firstNameEditText.getText().toString();
        String  displayLName = lastNameEditText.getText().toString();
       // String  displayEmail = emailEditText.getText().toString();
        String  displayPhone = phoneEditText.getText().toString();
        String  displayAddress = addressEditText.getText().toString();
        String  displayAboutMe = aboutMeEditText.getText().toString();
       // String  displayProfileImage = profileImageView.getText().toString();
        String  displayRadiogroup = "";



        if(customerRadioButton.isChecked())
        {
            displayRadiogroup = "C";
        }
        else if(securityCompanyRadioButton.isChecked()){

         displayRadiogroup = "S";
        }

        else if(individualGuardRadioButton.isChecked()){

            displayRadiogroup = "I";
        }


        if(displayFName.isEmpty() || displayLName.isEmpty() || displayPhone.isEmpty() || displayAddress.isEmpty()
                || displayAboutMe.isEmpty()){

               Toast.makeText(ProfileScreen.this,"AllFields Required",Toast.LENGTH_SHORT).show();
                 return;
        }

        else {

//            String abd = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Profile").child("uid").getKey();
//            String abccc =  FirebaseAuth.getInstance().getCurrentUser().getUid();
//            if (dbProfile.getKey().equals(firebaseAuth.getCurrentUser().getEmail())) {

//                if (firebaseAuth.getCurrentUser() != null
//                       // && profileUrl != null
//                        ) {


            Profile updateProf = new Profile("1", userId, profileUrl, displayFName, displayLName, displayPhone, displayAddress,
                            prefrenceStr, displayAboutMe);

                    dbProfile.child(profileId).setValue(updateProf);




//            } else if (firebaseAuth.getCurrentUser() != null && profileUrl != null) {
//
//
//
//                Profile updateProf;
//                updateProf = new Profile(profileId, userId, profileUrl, displayFName, displayLName, displayPhone, displayAddress,
//                        prefrenceStr, displayAboutMe);
//
//                dbProfile.child(profileId).setValue(updateProf);
//
//
//
//
//            }
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayFName+" "+displayLName)
                    .setPhotoUri(Uri.parse(profileUrl))
                    .build();

            firebaseAuth.getCurrentUser().updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileScreen.this,"ProfileScreen Updated",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProfileScreen.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }

    }


//    private void addUser(){
//        String username = firstNameEditText.getText().toString()+" "+ lastNameEditText.getText().toString();
//        String email = emailEditText.getText().toString();
//        if(!TextUtils.isEmpty(username) || (!TextUtils.isEmpty(email))){
//
//           String id = dbUsers.push().getKey();
//
//            Users newUser = new Users(id,username,email);
//
//            dbUsers.child(id).setValue(newUser);
//
//            Toast.makeText(getApplicationContext(),"User Added Successfully", Toast.LENGTH_SHORT).show();
//
//
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"Pleas Enter User name", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public void onBackPressed() {
        Intent obj = new Intent(ProfileScreen.this, LoginScreen.class);
        startActivity(obj);
        finish();

    }

    private void showImageChooser(){


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select ProfileScreen Image"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriPofileImage = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriPofileImage);

                profileImageView.setImageBitmap(bitmap);

                uploadImageToFirbaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirbaseStorage() {

        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");

        if(uriPofileImage != null){

            progressBr.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriPofileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    profileUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                    progressBr.setVisibility(View.GONE);


                }
            })

               .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBr.setVisibility(View.GONE);
                            Toast.makeText(ProfileScreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    @Override
    public void onStart() {
        super.onStart();


        firebaseAuth = FirebaseAuth.getInstance();
        usr = FirebaseAuth.getInstance().getCurrentUser();
        userId = usr.getUid();

        firebaseAuth.addAuthStateListener(authListener);

        dbProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Profile");

        dbProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Profile post = dataSnapshot.getValue(Profile.class);


                loadUserInfomation(post);

                }

//            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

}

package event.secure.secureyourevent.Activity.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import event.secure.secureyourevent.Activity.Adapter.EventAdapter;
import event.secure.secureyourevent.Activity.Model.Bid;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.Activity.Model.Profile;
import event.secure.secureyourevent.R;


/**
 * Created by priyankakhatta on 2018-02-16.
 */

public class EventDetailScreen extends AppCompatActivity {


    private TextView eventTitleDetailTextview, dateDetailTextView, detailEventPeriod, payRangeDetailTextView,detailUniform,
            numberOfGurdsDetailTextView, detailOpenEventTill, addressDetailTextView, descDetailTextView, detailInstructions;
    private Button skipDetailButton, bidDetailButton;

    private ArrayList<Events> eventList;
    private ArrayList<Bid> bidlist;
    private  int pos;
    final Context context = this;
    private FirebaseAuth firebaseAuth;
    private String userId, bidValueStr, eventId, comTxt, strnm;
    private DatabaseReference dbBid;
    private FirebaseAuth.AuthStateListener authListener;
    private  EditText bidEditText, comment;
    private TextView evtName;
    private ImageView like_imageView, fav_imageView;
    private String like = "false", fav = "false", id = "";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_screen);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

       // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Event Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("pos");
        eventList = (ArrayList<Events>) bundle.getSerializable("eventDetail");
        bidlist = new ArrayList<>();
        final int size = eventList.size();

        eventTitleDetailTextview = (TextView)findViewById(R.id.eventTitleDetailTextview);
        dateDetailTextView = (TextView)findViewById(R.id.dateDetailTextView);
        detailEventPeriod = (TextView)findViewById(R.id.detailEventPeriod);
        payRangeDetailTextView = (TextView)findViewById(R.id.payRangeDetailTextView);
        numberOfGurdsDetailTextView = (TextView)findViewById(R.id.numberOfGurdsDetailTextView);
        detailUniform = (TextView)findViewById(R.id.detailUniform);
        addressDetailTextView = (TextView)findViewById(R.id.addressDetailTextView);
        detailOpenEventTill = (TextView)findViewById(R.id.detailOpenEventTill);
        detailInstructions = (TextView)findViewById(R.id.detailInstructions);
        descDetailTextView = (TextView)findViewById(R.id.descDetailTextView);
        skipDetailButton = (Button)findViewById(R.id.skipDetailButton);
        bidDetailButton = (Button)findViewById(R.id.bidDetailButton);

        eventId = eventList.get(pos).getId().toString();
        strnm = eventList.get(pos).getEventTitleEditText().toString();

        eventTitleDetailTextview.setText(eventList.get(pos).getEventTitleEditText().toString());
        dateDetailTextView.setText(eventList.get(pos).getDateEditText().toString());
        detailEventPeriod.setText(eventList.get(pos).getEventPeriodEditText().toString());

        payRangeDetailTextView.setText(eventList.get(pos).getPayEditText().toString());

        numberOfGurdsDetailTextView.setText(eventList.get(pos).getNumberOfGuardsEditText().toString());
        detailUniform.setText(eventList.get(pos).getUniformEditText().toString());
        addressDetailTextView.setText(eventList.get(pos).getAddressOfEventEditText().toString());
        detailOpenEventTill.setText(eventList.get(pos).getOpenEventTillEditText().toString());
        detailInstructions.setText(eventList.get(pos).getInstructionsEditText().toString());
        descDetailTextView.setText(eventList.get(pos).getJobDescriptionEditText().toString());


        skipDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               if(pos < size) {
//                   pos = pos + 1;
//
//                   eventId = eventList.get(pos).getId().toString();
//                   strnm = eventList.get(pos).getEventTitleEditText().toString();
//
//                   eventTitleDetailTextview.setText(eventList.get(pos).getEventTitleEditText().toString());
//                   dateDetailTextView.setText(eventList.get(pos).getDateEditText().toString());
//                   detailEventPeriod.setText(eventList.get(pos).getEventPeriodEditText().toString());
//
//                   payRangeDetailTextView.setText(eventList.get(pos).getPayEditText().toString());
//
//                   numberOfGurdsDetailTextView.setText(eventList.get(pos).getNumberOfGuardsEditText().toString());
//                   detailUniform.setText(eventList.get(pos).getUniformEditText().toString());
//                   addressDetailTextView.setText(eventList.get(pos).getAddressOfEventEditText().toString());
//                   detailOpenEventTill.setText(eventList.get(pos).getOpenEventTillEditText().toString());
//                   detailInstructions.setText(eventList.get(pos).getInstructionsEditText().toString());
//                   descDetailTextView.setText(eventList.get(pos).getJobDescriptionEditText().toString());
//
//               }
//               else {
//                   Toast.makeText(getApplicationContext(),"Last Event",Toast.LENGTH_SHORT).show();
//               }


                Intent intent = new Intent(EventDetailScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bidDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (dbBid.getKey().equals(firebaseAuth.getCurrentUser().getUid()) && dbBid.getKey().equals(eventId)) {
                    Toast.makeText(EventDetailScreen.this, "Bid Already done", Toast.LENGTH_SHORT).show();

                } else {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.bid_pop_up);
                    // dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                     evtName = (TextView)dialog.findViewById(R.id.evtName);
                    comment = (EditText)dialog.findViewById(R.id.comment);
                    bidEditText = (EditText) dialog.findViewById(R.id.bidEditText);
//                    like_imageView = (ImageView) dialog.findViewById(R.id.like_imageView);
//                    fav_imageView = (ImageView) dialog.findViewById(R.id.fav_imageView);

                    Button okButton = (Button) dialog.findViewById(R.id.okButton);
                    Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);



                    evtName.setText(""+strnm);





//                    like_imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if(like.equals("true")){
//                                like_imageView.setImageResource(R.drawable.icons_like2);
//                                like = "false";
//
//
//                            }
//                            else {
//                                like_imageView.setImageResource(R.drawable.icons_like);
//                                like = "true";
//                            }
//                        }
//                    });
//
//                    fav_imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if(fav.equals("true")){
//                                fav_imageView.setImageResource(R.drawable.icons_favourite2);
//                                fav = "false";
//
//                            }
//                            else {
//
//                                fav_imageView.setImageResource(R.drawable.icons_favourite);
//                                fav = "true";
//
//                            }
//                        }
//                    });


                    // if button is clicked, close the custom dialog
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bidValueStr = bidEditText.getText().toString();
                            comTxt = comment.getText().toString();


                            if(bidValueStr.equals("")){

                                Toast.makeText(EventDetailScreen.this,"Please Enter amount Or Cancel", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                for(int i = 0; i < bidlist.size(); i++) {

                                    if(bidlist.get(i).getBUID().equals(userId) && bidlist.get(i).getBEID().equals(eventId)){

                                        id = bidlist.get(i).getId();

                                    }
                                    else{
                                        id = dbBid.push().getKey();

                                    }

                                }

                                if(id.equals("")){

                                    id = dbBid.push().getKey();


                                }

                                Bid bid = new Bid(id, userId, eventId, bidValueStr, eventTitleDetailTextview.getText().toString(),like, comTxt, fav);

                                dbBid.child(id).setValue(bid);

                                Toast.makeText(EventDetailScreen.this, "Bid Successfull", Toast.LENGTH_SHORT).show();



//            }
                            }

                            dialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    dialog.show();

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
                    startActivity(new Intent(EventDetailScreen.this, LoginScreen.class));
                    finish();
                }
                else {
                }
            }
        };

    }

    @Override
    public void onBackPressed(){
        finish();
    }



    private void saveBid() {






    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbBid = FirebaseDatabase.getInstance().getReference().child("Bid");

        dbBid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bidlist.clear();

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){

                    Bid bid = eventSnapshot.getValue(Bid.class);

//                                        bid.setId(id);
//
//                                        bid.setBEID("" + eventId);
//                                        bid.setBUID("" + userId);
//                                        bid.setBidAmmount("" + bidValueStr);
//                                        bid.setEventName("" + eventTitleDetailTextview.getText().toString());
//                                        bid.setComment("" + comTxt);
//                                        bid.setLike("" + like);
//                                        bid.setFavourite("" + fav);

                    bidlist.add(bid);

                }

                bidlist.size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseAuth.addAuthStateListener(authListener);


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

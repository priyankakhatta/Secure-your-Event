package event.secure.secureyourevent.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import event.secure.secureyourevent.Activity.Adapter.EventAdapter;
import event.secure.secureyourevent.Activity.Adapter.MyBidsAdapter;
import event.secure.secureyourevent.Activity.Model.Bid;
import event.secure.secureyourevent.Activity.Model.EventList;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.R;

public class MyEvents extends AppCompatActivity {


    private ListView events_listview;
    private ArrayList<EventList> lst;
    private ArrayList<Events> eventList, eventListFnl;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference dbMyEvents,Users;
    private String userId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        getSupportActionBar().setTitle("My Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            events_listview = (ListView)findViewById(R.id.events_listview);

            userId = getIntent().getStringExtra("userId");

            dbMyEvents = FirebaseDatabase.getInstance().getReference().child("Events");



            eventList = new ArrayList<>();
        eventListFnl = new ArrayList<>();


            events_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent =new Intent(MyBids.this,EventDetailScreen.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("pos",position);
//                bundle.putSerializable("eventDetail",eventList);
//                intent.putExtras(bundle);
//                startActivity(intent);


                }
            });


            mAuth = FirebaseAuth.getInstance();

            //get current user
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(MyEvents.this, LoginScreen.class));
                        finish();
                    }
                }
            };
        }
        @Override
        public void onStart() {
            super.onStart();
            mAuth = FirebaseAuth.getInstance();

            mAuth.addAuthStateListener(authListener);

            final String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            Query myTopPostsQuery = dbMyBids.child("euid").equalTo(myUserId);

            dbMyEvents.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    eventList.clear();
                    eventListFnl.clear();

                    for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){


                        Events event = eventSnapshot.getValue(Events.class);
                        eventList.add(event);


                    }

                    for( int i=0; i<eventList.size(); i++) {

                    String abc = eventList.get(i).getEUID();
                        if( (abc).equals(myUserId) ){
                            eventListFnl.add(eventList.get(i));
                        }
                    }

                    EventAdapter adaptr = new EventAdapter(MyEvents.this,eventListFnl);
                    events_listview.setAdapter(adaptr);
                    adaptr.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
        @Override
        public void onStop() {
            super.onStop();
            if (authListener != null) {
                mAuth.removeAuthStateListener(authListener);
            }
        }

}

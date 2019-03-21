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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import event.secure.secureyourevent.Activity.Adapter.EventAdapter;
import event.secure.secureyourevent.Activity.Adapter.MyBidsAdapter;
import event.secure.secureyourevent.Activity.Model.Bid;
import event.secure.secureyourevent.Activity.Model.EventList;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.R;

public class MyBids extends AppCompatActivity {


    private ListView events_listview;
    private ArrayList<Bid> bidlist, bidlistsorted;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference dbMyBids;
    private String userId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        getSupportActionBar().setTitle("My Bids");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        events_listview = (ListView)findViewById(R.id.events_listview);

        userId = getIntent().getStringExtra("userId");

        dbMyBids = FirebaseDatabase.getInstance().getReference().child("Bid");

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
                    startActivity(new Intent(MyBids.this, LoginScreen.class));
                    finish();
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
        bidlist = new ArrayList<>();
        bidlistsorted = new ArrayList<>();

        dbMyBids.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bidlistsorted.clear();
                bidlist.clear();

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){


                    Bid bid = eventSnapshot.getValue(Bid.class);
                        bidlist.add(bid);


                }

                for( int i=0; i<bidlist.size(); i++) {

                    String abc = bidlist.get(i).getBUID();
                    if( (abc).equals(mAuth.getCurrentUser().getUid()) ){
                        bidlistsorted.add(bidlist.get(i));
                    }
                }
                MyBidsAdapter adaptr = new MyBidsAdapter(MyBids.this,bidlistsorted);
                events_listview.setAdapter(adaptr);
                adaptr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}

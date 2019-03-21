package event.secure.secureyourevent.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import event.secure.secureyourevent.Activity.Adapter.EventAdapter;
import event.secure.secureyourevent.Activity.Model.EventList;
import event.secure.secureyourevent.Activity.Model.Events;
import event.secure.secureyourevent.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView events_listview;
    private ArrayList<EventList> lst;
    private ArrayList<Events> eventList;
    EventList evntlst, evntlst2,evntlst3, evntlst4;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference dbEvent,Users;
    private String userId = "";
    private String user_name ="", user_email = "";
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        events_listview = (ListView)findViewById(R.id.events_listview);

        userId = getIntent().getStringExtra("userId");

        dbEvent = FirebaseDatabase.getInstance().getReference().child("Events");

        user_name = firebaseAuth.getCurrentUser().getDisplayName();

        user_email = firebaseAuth.getCurrentUser().getEmail();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddNewEvent.class);
                startActivity(intent);

                }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View mHeaderView = navigationView.getHeaderView(0);
        TextView header_name = (TextView) mHeaderView.findViewById(R.id.header_name);
        header_name.setText(user_name);
        TextView header_email = (TextView) mHeaderView.findViewById(R.id.header_email);
        header_email.setText(user_email);

        ImageView header_userImage = (ImageView)findViewById(R.id.header_userImage);


        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {

//            Glide.with(this)
//                    .load(firebaseAuth.getCurrentUser().getPhotoUrl().toString())
//                    .into(header_userImage);

        }
        else {

        //  header_userImage.setImageResource(R.drawable.thumbnail);

        }
       // lst = new ArrayList<>();
        eventList = new ArrayList<>();


        events_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(MainActivity.this,EventDetailScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pos",position);
                bundle.putSerializable("eventDetail",eventList);
                intent.putExtras(bundle);
                startActivity(intent);


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
                    startActivity(new Intent(MainActivity.this, LoginScreen.class));
                    finish();
                }
            }
        };

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);





        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {



            Intent intent =new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent =new Intent(MainActivity.this,MyEvents.class);
            startActivity(intent);
            finish();


        } else if (id == R.id.nav_slideshow) {

            Intent intent =new Intent(MainActivity.this,ChangePassword.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_manage) {
            Intent intent =new Intent(MainActivity.this,ProfileScreen.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();

        } else if (id == R.id.my_bids) {


            Intent intent =new Intent(MainActivity.this,MyBids.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.sign_out) {
            signOut();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {


        mAuth.signOut();

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

        dbEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){


                    Events event = eventSnapshot.getValue(Events.class);

                    eventList.add(event) ;

                }

                EventAdapter adaptr = new EventAdapter(MainActivity.this,eventList);
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
}

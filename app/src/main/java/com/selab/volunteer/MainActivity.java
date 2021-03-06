package com.selab.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.selab.volunteer.EventDescription1.databaseReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String profilename1,email1,phno1;
    private ChildEventListener childEventListener;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    TextView pname,pemail,pphno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profilename1 = dataSnapshot.child("name").getValue().toString();
                //Toast.makeText(Profile.this,"hello" + profilename1,Toast.LENGTH_SHORT).show();
                pname = findViewById(R.id.profilename2);
                pname.setText(profilename1);

                email1 = dataSnapshot.child("mail").getValue().toString();
                pemail = findViewById(R.id.profileemail2);
                pemail.setText(email1);

                de.hdodenhof.circleimageview.CircleImageView pro;
                pro =(de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.pro1);


                String imageurl = dataSnapshot.child("url").getValue().toString();
                if(imageurl!=" ")
                    Picasso.with(MainActivity.this).load(imageurl).fit().centerCrop().into(pro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_events);
            NavigationView navigationview = (NavigationView) findViewById(R.id.nav_view);
            View headerview = navigationview.getHeaderView(0);

            ImageView view=(ImageView)headerview.findViewById(R.id.pro1);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(MainActivity.this,Profile.class);
                    startActivity(intent1);

                }
            });
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){

            case R.id.nav_events:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Events()).commit();
                Intent intent = new Intent(MainActivity.this, MyEvents.class);
                startActivity(intent);

                break;
            case R.id.nav_volunteer:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VolunteeringFragment()).commit();
                Intent intent1 = new Intent(MainActivity.this, VolunteerEvents.class);
                startActivity(intent1);
                break;
            case R.id.nav_wallet:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Wallet()).commit();
                Intent intent3 = new Intent(MainActivity.this, Wallet.class);
                startActivity(intent3);
                break;
            case R.id.nav_host:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Hosting()).commit();
                Intent intent2 = new Intent(MainActivity.this, HostAnEvent.class);
                startActivity(intent2);

                break;



            case R.id.nav_share: {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String shareBody = "Hey!!!\nDownload the new cool app for easily organising events!!!\nLink to Download the file \n"+"https://drive.google.com/open?id=1wRGNFWaoEKEkKqHPwm1wWgTuDlfhMoOP";
                String sharesub = "Download The App";
                share.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                share.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(share, "Share Using"));
            }
            break;
            case R.id.nav_feedback:
                //Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
                Intent feedback = new Intent(MainActivity.this,feedbacktab.class);
                startActivity(feedback);
                break;
            case R.id.nav_report:
                Intent report = new Intent(MainActivity.this,Report.class);
                startActivity(report);
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }
}
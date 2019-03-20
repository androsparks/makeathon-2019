package com.selectmakeathon.app.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.NavModel;
import com.selectmakeathon.app.model.UserModel;
import com.selectmakeathon.app.ui.auth.AuthActivity;
import com.selectmakeathon.app.ui.main.aboutteam.AboutTeamBottomSheetFragment;
import com.selectmakeathon.app.ui.main.home.HomeFragment;
import com.selectmakeathon.app.ui.main.idea.AbstractActivity;
import com.selectmakeathon.app.ui.main.info.InfoActivity;
import com.selectmakeathon.app.ui.main.myTeam.MyTeamActivity;
import com.selectmakeathon.app.ui.main.problems.ProblemActivity;
import com.selectmakeathon.app.ui.main.rules.RulesFragment;
import com.selectmakeathon.app.ui.main.scratch.ScratchFragment;
import com.selectmakeathon.app.ui.main.sidenav.SideNavAdapter;
import com.selectmakeathon.app.ui.main.sidenav.SideNavListener;
import com.selectmakeathon.app.ui.main.searchteam.TeamSearchActivity;
import com.selectmakeathon.app.util.Constants;

//import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements SideNavListener {

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private FrameLayout mainContainer;
    private LottieAnimationView loadingAnimation;
    private RelativeLayout loadingContainer;

    private DrawerLayout drawerLayout;
    private TextView buttonSignOut;
    private NavigationView navigationView;
    private RecyclerView rvNav;
    private TextView navUserName;
    private TextView navUserId;

    SideNavAdapter adapter;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public UserModel userModel;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        userName = prefs.getString(Constants.PREF_USER_ID, "");

        initViews();
        initAdapter();

        startAnimation();

        if (!isInternetAvailable(this)) {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

//        updateFragment(HomeFragment.newInstance());

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAuthActivity();
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                updateDrawer();
            }
        });

        reference.child("users").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        stopAnimation();

//        updateFragment(HomeFragment.newInstance());
        updateDrawer();

        navUserName.setText(userModel.getName());
        navUserId.setText(userModel.getRegNo());
    }

    public void startAnimation() {
        mainContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void stopAnimation() {
        mainContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void initAdapter() {

        ArrayList<NavModel> navModels = new ArrayList<>();

        navModels.add(new NavModel(
                R.drawable.ic_home_black_24dp,
                "Home"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_format_list_bulleted_black_24dp,
                "Problem Statements"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_people_black_24dp,
                "Team"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_script_text,
                "Rules"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_info_black_24dp,
                "Info"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_info_black_24dp,
                "Scratch Card"
        ));
        navModels.add(new NavModel(
                R.drawable.ic_info_black_24dp,
                "Developers"
        ));

        adapter = new SideNavAdapter(this, navModels, this);

        initRv();
    }

    private void initRv() {

        rvNav.setHasFixedSize(true);
        rvNav.setLayoutManager(new LinearLayoutManager(this));

        rvNav.setAdapter(adapter);

    }

    private void updateDrawer() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (fragment != null){

            String fragClassName = fragment.getClass().getName();

            if (fragClassName.equals(HomeFragment.class.getName())) {
                adapter.update(0);
            }
        }

    }

    public void startAuthActivity() {
        prefEditor.putBoolean(Constants.PREF_IS_LOGGED_IN, true).apply();

        TaskStackBuilder.create(MainActivity.this)
                .addNextIntentWithParentStack(new Intent(MainActivity.this, AuthActivity.class))
                .startActivities();
        finish();
    }

    private void initViews() {
        mainContainer = findViewById(R.id.container_main);
        loadingContainer = findViewById(R.id.layout_main_loading);

        drawerLayout = findViewById(R.id.drawer_layout);
        buttonSignOut = findViewById(R.id.main_text_signout);
        navigationView = findViewById(R.id.nav_view);
        rvNav = findViewById(R.id.nav_rv);

        navUserId = findViewById(R.id.nav_userid);
        navUserName = findViewById(R.id.nav_username);
    }

    public void updateFragment(Fragment fragment) {

        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container_main, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main);

            if (fragment != null){

                String fragClassName = fragment.getClass().getName();

                if (fragClassName.equals(HomeFragment.class.getName())) {
                    finish();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            updateFragment(HomeFragment.newInstance());
//        } else if (id == R.id.nav_problem_statement) {
//
//        } else if (id == R.id.nav_team) {
//
//        } else if (id == R.id.nav_info) {
//            Intent i = new Intent(this, InfoActivity.class);
//            startActivity(i);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void openSideNav() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragment(HomeFragment.newInstance());
        updateDrawer();
    }

    @Override
    public void onNavItemSelected(int position) {

        if (position == 0) {
            updateFragment(HomeFragment.newInstance());
        } else if (position == 1) {
            Intent i = new Intent(this, ProblemActivity.class);
            i.putExtra("CONTINUE", false);
            startActivity(i);
        } else if (position == 2) {
            Intent intent;
            if (userModel.isJoined()) {
                intent = new Intent(this, MyTeamActivity.class);
            } else {
                intent = new Intent(this, TeamSearchActivity.class);
            }
            startActivity(intent);
        } else if (position == 3) {
            updateFragment(RulesFragment.newInstance());
        } else if (position == 4) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
        } else if (position == 5) {
            updateFragment(ScratchFragment.newInstance(userModel.getRegNo()));
        } else if (position == 6) {
            AboutTeamBottomSheetFragment aboutTeamBottomSheetFragment
                    = new AboutTeamBottomSheetFragment();
            aboutTeamBottomSheetFragment.show(
                    getSupportFragmentManager(),
                    aboutTeamBottomSheetFragment.getTag()
            );
        }

        if (position != 6) {
            adapter.update(position);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public boolean isInternetAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
}

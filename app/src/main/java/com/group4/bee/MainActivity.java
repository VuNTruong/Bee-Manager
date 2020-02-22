package com.group4.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String currentFragment = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            if (currentFragment.equals("Home")) {
                                break;
                            } else {
                                currentFragment = "Home";
                                selectedFragment = new HomeFragment();
                                changeFragment(selectedFragment);
                                break;
                            }
                        case R.id.navigation_dashboard:
                            if (currentFragment.equals("Dashboard")) {
                                break;
                            } else {
                                currentFragment = "Dashboard";
                                selectedFragment = new DashboardFragment();
                                changeFragment(selectedFragment);
                                break;
                            }
                        case R.id.navigation_profile:
                            if (currentFragment.equals("Profile")) {
                                break;
                            } else {
                                currentFragment = "Profile";
                                selectedFragment = new ProfileFragment();
                                changeFragment(selectedFragment);
                                break;
                            }
                        case R.id.navigation_forum:
                            if (currentFragment.equals("About")) {
                                break;
                            } else {
                                currentFragment = "About";
                                selectedFragment = new AboutFragment();
                                changeFragment(selectedFragment);
                                break;
                            }
                        case R.id.navigation_social:
                            if (currentFragment.equals("Social")) {
                                break;
                            } else {
                                currentFragment = "Social";
                                selectedFragment = new SocialFragment();
                                changeFragment(selectedFragment);
                                break;
                            }
                    }

                    return true;
                }
            };

    private void changeFragment (Fragment selectedFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
    }
}

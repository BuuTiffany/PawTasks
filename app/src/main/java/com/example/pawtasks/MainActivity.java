package com.example.pawtasks;
import com.example.pawtasks.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fragments
        TaskPage taskPage = new TaskPage();
        GachaPage gachaPage = new GachaPage();
        PetViewPage petViewPage = new PetViewPage();

        // Set token count on gacha page
        // NEED TO IMPLEMENT WAY OF SAVING DATA
        //gachaPage.setTokenCount(user's token count);

        // Sets to TaskPage on app start
        replaceFragment(new TaskPage());

        // bottom navigation bar logic
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.tasks)
            {
                replaceFragment(taskPage);
            }
            else if (id == R.id.gacha)
            {
                replaceFragment(gachaPage);
            }
            else if (id == R.id.pet)
            {
                replaceFragment(petViewPage);
            }

            return true;
        });
    }

    // To change the fragment to different pages
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navigationMenu_FrameLayout, fragment);
        fragmentTransaction.commit();
    }
}
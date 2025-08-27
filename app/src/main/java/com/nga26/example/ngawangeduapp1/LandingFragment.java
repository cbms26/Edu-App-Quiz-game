package com.nga26.example.ngawangeduapp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/** @noinspection ALL*/
public class LandingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        //set the title for action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("NgawangEduApp - Home");
        }

        Button goButton = view.findViewById(R.id.goButton);
        goButton.setOnClickListener(v -> {
            // Get nameText
            TextInputEditText nameText = view.findViewById(R.id.nameText);
            // Get user name
            String username = Objects.requireNonNull(nameText.getText()).toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a username",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Share username
            SharedPreferences preferences = getActivity().getSharedPreferences("ngawang_items", Context.MODE_PRIVATE);
            preferences.edit().clear().putString("username", username).apply();
            // Navigate to SettingFragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_landingFragment_to_settingFragment);
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem homeItem = menu.findItem(R.id.home);
        if (homeItem != null) {
            // Hide the score menu item
            homeItem.setVisible(false);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.score) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_landingFragment_to_scoreFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

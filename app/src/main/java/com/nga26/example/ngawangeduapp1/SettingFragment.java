package com.nga26.example.ngawangeduapp1;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

/** @noinspection ALL*/
public class SettingFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.background_track);
        mediaPlayer.setLooping(true);

        // Initialize the Switch view
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mySwitch = view.findViewById(R.id.bg_track_toggle);
        // Check if the Switch view is not null before setting the listener
        if (mySwitch != null) {
            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Start background music
                        startBackgroundMusic();
                    } else {
                        // Stop background music
                        stopBackgroundMusic();
                    }
                }
            });
        }


        //set the title for action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("NgawangEduApp - Setting");
        }

        Button playButton = view.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get levelSpinner
                Spinner levelSpinner = view.findViewById(R.id.levelSpinner);
                // Get the selected level
                String levelStr = levelSpinner.getSelectedItem().toString();
                // Create a Bundle to pass the selected level
                Bundle bundle = new Bundle();
                bundle.putString("levelStr", levelStr);
                // Navigate to GameFragment and pass the bundle
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_gameFragment6, bundle);
            }
        });
        return view;
    }

    private void startBackgroundMusic() {
        if (!isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    private void stopBackgroundMusic() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.score) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_settingFragment_to_scoreFragment);
            return true;
        } else if (item.getItemId() == R.id.home) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_settingFragment_to_landingFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
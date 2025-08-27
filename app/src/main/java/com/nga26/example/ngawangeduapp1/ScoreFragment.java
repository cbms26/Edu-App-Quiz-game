package com.nga26.example.ngawangeduapp1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nga26.example.ngawangeduapp1.adapter.ScoreAdapter;
import com.nga26.example.ngawangeduapp1.helper.DBHelper;
import com.nga26.example.ngawangeduapp1.model.User;

import java.util.ArrayList;

/** @noinspection ALL*/
public class ScoreFragment extends Fragment {
    private DBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = new DBHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        //set the title for action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("NgawangEduApp - Scoreboard");
        }

        // Get scoreRecyclerView
        RecyclerView scoreRecyclerView = view.findViewById(R.id.scoreRecyclerView);
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager
        scoreRecyclerView.setAdapter(new ScoreAdapter(User.loadUsers(dbHelper)));

        // Setup clearScoreListBtn click listener
        Button button = view.findViewById(R.id.clearScoreListBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the database
                dbHelper.getWritableDatabase().execSQL("DELETE FROM " + DBHelper.TABLE_NAME);
                // Refresh the RecyclerView
                scoreRecyclerView.setAdapter(new ScoreAdapter(new ArrayList<User>()));
            }
        });

        return view;
    }


    private void insert_2_user_for_testing() {
        // Clear DB
        dbHelper.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_NAME);
        dbHelper.insertPlayer("Tony", 75, "1", 50);
        dbHelper.insertPlayer("Sarah", 60, "3", 70);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem scoreItem = menu.findItem(R.id.score);
        if (scoreItem != null) {
            // Hide the score menu item
            scoreItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_scoreFragment_to_landingFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
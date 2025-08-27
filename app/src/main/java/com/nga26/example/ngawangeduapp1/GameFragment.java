package com.nga26.example.ngawangeduapp1;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.nga26.example.ngawangeduapp1.helper.DBHelper;
import com.nga26.example.ngawangeduapp1.helper.QuestionImageManager;
import com.nga26.example.ngawangeduapp1.helper.SoundManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/** @noinspection ALL*/
public class GameFragment extends Fragment {
    private DBHelper dbHelper;
    private SoundManager soundManager;
    private final int num_of_questions = 6;
    private String levelStr = "1";
    private int duration;
    private int score = 0;
    private int win_sound_id;
    private int lose_sound_id;
    private int game_over;
    private int current_Question_index = 0;

    private TextView questionNumberView;
    private TextView scoreView;
    private TextView durationView;
    private ImageView questionImageView;
    private TextInputEditText answerText;
    private Timer timer;

    private boolean isSoundOn = true; // Assuming sound is on by default
    private int soundId;

    // Arrays for images of each level
    private final ArrayList<Integer> level1ImageList = new ArrayList<>();
    private final ArrayList<Integer> level2ImageList = new ArrayList<>();
    private final ArrayList<Integer> level3ImageList = new ArrayList<>();

    //getting corresponding answer for each image
    private final Map<Integer, String> imageAnswerMap = new HashMap<>();
    private int currentImageResourceID;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Retrieve level from arguments
        if (getArguments() != null) {
            levelStr = getArguments().getString("levelStr", "1");
        }

        // Initialise DBHelper, imageManager, soundManager
        Context context = requireContext();
        dbHelper = new DBHelper(context);
        QuestionImageManager imageManager = new QuestionImageManager(levelStr, context.getAssets());
        soundManager = new SoundManager(context);
        // Load audio file
        win_sound_id = soundManager.addSound(R.raw.win_track);
        lose_sound_id = soundManager.addSound(R.raw.lose_track);
        game_over = soundManager.addSound(R.raw.game_over);

        // Initialize and shuffle the image lists
        initializeImageLists();
        if (savedInstanceState == null){ shuffleImageLists(); }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("duration", duration); //save current duration
        outState.putInt("score", score);
        outState.putInt("currentQuestionIndex", current_Question_index);
        outState.putString("levelStr", levelStr);
        outState.putInt("currentImageResourceID", currentImageResourceID);
        outState.putBoolean("isSoundOn", isSoundOn);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        //set the title for action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle("NgawangEduApp - Now Playing");
        }
        // Retrieve the view elements
        questionNumberView = view.findViewById(R.id.questionNumberView);
        scoreView = view.findViewById(R.id.scoreView);
        durationView = view.findViewById(R.id.durationView);
        questionImageView = view.findViewById(R.id.imageQuestionView);
        answerText = view.findViewById(R.id.answerText);
        ImageButton soundToggle = view.findViewById(R.id.soundOnOff);
        Button answerButton = view.findViewById(R.id.answerButton);

        //set up imagebutton for toggling sound
        soundToggle.setImageResource(isSoundOn ? R.drawable.ic_sound_on : R.drawable.ic_sound_off);
        soundToggle.setOnClickListener(v -> {
            isSoundOn = !isSoundOn; // Toggle the sound state
            if (isSoundOn) {
                soundManager.play(soundId, 1.0f);
                soundToggle.setImageResource(R.drawable.ic_sound_on);
            } else {
                soundManager.stop(soundId);
                soundToggle.setImageResource(R.drawable.ic_sound_off);
            }
        });
        // OnClick method for the answerButton
        answerButton.setOnClickListener(v -> answerProcessing());

        //Retain certain view elements when changing viewscreen
        if (savedInstanceState != null) {
            duration = savedInstanceState.getInt("duration", 0);
            score = savedInstanceState.getInt("score", 0);
            current_Question_index = savedInstanceState.getInt("currentQuestionIndex", 0);
            levelStr = savedInstanceState.getString("levelStr", "1");
            currentImageResourceID = savedInstanceState.getInt("currentImageResourceID");
            questionImageView.setImageResource(currentImageResourceID);
            scoreView.setText("Score: " + score + "/" + 10 * num_of_questions);
            questionNumberView.setText("Question: " + (current_Question_index + 1) + "/" + num_of_questions + " Level: " + levelStr);
            isSoundOn = savedInstanceState.getBoolean("isSoundOn", true);
        } else {
            // Initialize game on first load
            startGame();
        }
        return view;
    }

    private void initializeImageLists() {
        // Initialize the mapping from image resource IDs to retrieve their corresponding answers
        //for level 1
        imageAnswerMap.put(R.drawable.level01_pic01_0, "0");
        imageAnswerMap.put(R.drawable.level01_pic02_21, "21");
        imageAnswerMap.put(R.drawable.level01_pic03_15, "15");
        imageAnswerMap.put(R.drawable.level01_pic04_55, "55");
        imageAnswerMap.put(R.drawable.level01_pic05_6, "6");
        imageAnswerMap.put(R.drawable.level01_pic06_0, "0");
        //for level 2
        imageAnswerMap.put(R.drawable.level02_pic01_31, "31");
        imageAnswerMap.put(R.drawable.level02_pic02_26, "26");
        imageAnswerMap.put(R.drawable.level02_pic03_4, "4");
        imageAnswerMap.put(R.drawable.level02_pic04_2, "2");
        imageAnswerMap.put(R.drawable.level02_pic05_35, "35");
        imageAnswerMap.put(R.drawable.level02_pic06_63, "63");
        //for level 2
        imageAnswerMap.put(R.drawable.level03_pic01_27, "27");
        imageAnswerMap.put(R.drawable.level03_pic02_4, "4");
        imageAnswerMap.put(R.drawable.level03_pic03_5, "5");
        imageAnswerMap.put(R.drawable.level03_pic04_24, "24");
        imageAnswerMap.put(R.drawable.level03_pic05_25, "25");
        imageAnswerMap.put(R.drawable.level03_pic06_4, "4");

        // Add all images for each level to their respective lists
        Collections.addAll(level1ImageList,
                R.drawable.level01_pic01_0, R.drawable.level01_pic02_21, R.drawable.level01_pic03_15,
                R.drawable.level01_pic04_55, R.drawable.level01_pic05_6, R.drawable.level01_pic06_0);
        Collections.addAll(level2ImageList,
                R.drawable.level02_pic01_31, R.drawable.level02_pic02_26, R.drawable.level02_pic03_4,
                R.drawable.level02_pic04_2, R.drawable.level02_pic05_35, R.drawable.level02_pic06_63);
        Collections.addAll(level3ImageList,
                R.drawable.level03_pic01_27, R.drawable.level03_pic02_4, R.drawable.level03_pic03_5,
                R.drawable.level03_pic04_24, R.drawable.level03_pic05_25, R.drawable.level03_pic06_4);
    }

    private void shuffleImageLists() {
        // Shuffle each image list to randomize the order
        Collections.shuffle(level1ImageList);
        Collections.shuffle(level2ImageList);
        Collections.shuffle(level3ImageList);
    }

    @SuppressLint("SetTextI18n")
    private void startGame() {
        //reset the score
        score = 0;
        scoreView.setText("Score: 0/" + 10 * num_of_questions);
        //empty answerText
        answerText.setText("");
        //reset currentQuestionIndex
        current_Question_index = 0;
        //show a question
        showQuestion();
        startTimer();
    }

    @SuppressLint("SetTextI18n")
    private void showQuestion() {
        // Pick a random image based on the current level
        // Pick the next non-repeated image for the current level
        int imageResId = getNextRandomImageForLevel(levelStr);
        if (imageResId == -1) {
            return;
        }
        currentImageResourceID = imageResId;

        // Set the non-repeated random image to the ImageView
        questionImageView.setImageResource(imageResId);
        questionNumberView.setText("Question: "
                + (current_Question_index + 1) + " Level: " + levelStr);
        answerText.setText("");
        answerText.requestFocus();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                duration++;
                String timeStr;
                //allow changing to min with hits 60secs
                if (duration < 60) {
                    timeStr = duration + " sec";
                } else {
                    int minutes = duration / 60;
                    int seconds = duration % 60;
                    timeStr = minutes + " min " + seconds + " sec";
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            durationView.setText("Duration: " + timeStr));
                }
            }
        }, 0, 1000);  // start immediately and update every second
    }

    //onPause stop timer
    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    //onResume start timer
    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @SuppressLint("SetTextI18n")
    private void answerProcessing() {
        String happyEmoji = "\uD83D\uDE00";
        String sadEmoji = "\uD83D\uDE41";
        //get user answer
        String answerStr = Objects.requireNonNull(answerText.getText()).toString().trim();
        //verify if nothing is entered
        if (answerStr.isEmpty()) {
            Toast.makeText(getContext(), "Please provide an answer.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Verify if the input contains only numbers
        if (!answerStr.matches("[0-99]+")) {
            Toast.makeText(getContext(), "Please enter numbers only.", Toast.LENGTH_SHORT).show();
            return;
        }

        String correctAnswer = imageAnswerMap.get(currentImageResourceID);
        //check answer and play a win sound
        //if (answerStr.equals(imageManager.getAnswer(current_Question_index))) {
        //gives correct answer for random images displayed
        if (answerStr.equals(correctAnswer)){
            Toast.makeText(getContext(),happyEmoji + "Correct!", Toast.LENGTH_SHORT).show();
            score += 10;
            if (isSoundOn) {
                soundManager.play(win_sound_id, 1);
            }
            //update scoreView
            scoreView.setText("Score: " + score + "/" + 10 * num_of_questions);
        } else {
            Toast.makeText(getContext(),
                    sadEmoji + "Wrong", Toast.LENGTH_SHORT).show();
            if (isSoundOn) {
                soundManager.play(lose_sound_id, 1);
            }
        }
        //Check if not the last question
        if (current_Question_index < num_of_questions) {
            current_Question_index++;
            //if it is the last question
            if (current_Question_index == num_of_questions){
                stopTimer();
                //save a new user record into DB
                //get username
                String username =
                        requireActivity().getSharedPreferences("ngawang_items", MODE_PRIVATE).
                                getString("username", "Unknown");
                dbHelper.insertPlayer(username, duration, levelStr, score);
                //show a dialog to start new game, exit or viewscoreboard
                showRestartOrExitDialog();
            }
            else {
                //show next question
                showQuestion();
            }
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void showRestartOrExitDialog() {
        //play game_over sound
        soundManager.play(game_over, 1);

        //dialog for game over,exit or view scoreboard
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Game Over")
                .setMessage("Do you want to start a new game?")
                .setPositiveButton("New Game", (dialog, which) -> {
                    assert getParentFragment() != null;
                    NavController navController = NavHostFragment.findNavController(getParentFragment());
                    navController.navigate(R.id.action_gameFragment6_to_settingFragment); // Adjust ID if needed
                })
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setNeutralButton("View Scoreboard", (dialog, which) -> {
                    // Navigate to scoreFragment
                    NavController navController = NavHostFragment.findNavController(getParentFragment());
                    navController.navigate(R.id.action_gameFragment6_to_scoreFragment);
                })
                .setCancelable(false); // prevent dismissing dialog by clicking outside

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Helper method to get a random image for the current level
    private int getNextRandomImageForLevel(String level) {
        ArrayList<Integer> selectedList = getLevelImageList(level);
        if (selectedList.isEmpty()) {
            // No more images to display. Handle according to your game design:
            // Option 1: Show a game over or restart dialog
            showRestartOrExitDialog();
            // Option 2: Return a default or error resource ID, or -1 if handled by the caller
            return -1; // Indicative that no more images are left.
        }
        // Remove and return the first element safely
        return selectedList.remove(0);
    }

    private ArrayList<Integer> getLevelImageList(String level) {
        switch (level) {
            case "1": return level1ImageList;
            case "2": return level2ImageList;
            case "3": return level3ImageList;
            default: throw new IllegalStateException("Unexpected level: " + level);
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
            navController.navigate(R.id.action_gameFragment6_to_scoreFragment);
            return true;
        } else if (item.getItemId() == R.id.home) {
            showLeaveGameDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLeaveGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("You are trying to leave the game. Are you sure?")
                .setPositiveButton("Leave", (dialog, which) -> {
                    NavController navController = NavHostFragment.findNavController(GameFragment.this);
                    navController.navigate(R.id.action_gameFragment6_to_landingFragment);
                })
                //user cancels to continue the game
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .show();
    }
}
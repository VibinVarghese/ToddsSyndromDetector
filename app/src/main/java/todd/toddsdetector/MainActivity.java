package todd.toddsdetector;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView questionString;
    Button positiveButton, negativeButton;
    ArrayList<QuestionItem> questionItems = new ArrayList<>();
    SharedPreferences prefs;
    int currentQuestionDisplayed = 0;

    DiagnosticsFragment diagnosticsFragment;
    PreviousResultsFragment previousResultsFragment;

    //Shared preference key for previous results
    final String PREVIOUS_RESULTS_STORAGE_KEY = "previous_results_storage_key";
    //Shared preference key for previous test dates
    final String PREVIOUS_DATES_STORAGE_KEY = "previous_dates_storage_key";

    ArrayList<String> previousTestDates, previousResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();

        //Retrieving previous results from shared preference storage
        String previousStoredResults = prefs.getString(PREVIOUS_RESULTS_STORAGE_KEY, "");
        String previousStoredDates = prefs.getString(PREVIOUS_DATES_STORAGE_KEY, "");

        if (previousStoredDates.equals(""))
            previousTestDates = new ArrayList<>();
        else
            previousTestDates = gson.fromJson(previousStoredDates, type);

        if (previousStoredResults.equals(""))
            previousResults = new ArrayList<>();
        else
            previousResults = gson.fromJson(previousStoredResults, type);


        initializeQuestions();
        initUI();
        loadNextQuestion();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //committing previous results to local storage
        Gson gson = new Gson();
        String json = gson.toJson(previousResults);
        prefs.edit().putString(PREVIOUS_RESULTS_STORAGE_KEY, json).apply();
        json = gson.toJson(previousTestDates);
        prefs.edit().putString(PREVIOUS_DATES_STORAGE_KEY, json).apply();


    }

    /**
     * Initialize all UI elements
     */
    protected void initUI() {
        questionString = (TextView) findViewById(R.id.question_text);
        positiveButton = (Button) findViewById(R.id.positive_button);
        negativeButton = (Button) findViewById(R.id.negative_button);
        positiveButton.setOnClickListener(responseButtonClickListener);
        negativeButton.setOnClickListener(responseButtonClickListener);
    }

    /**
     * This method is used to initialize the questions with the possible responses
     */
    protected void initializeQuestions() {
        QuestionItem questionItem1 = new QuestionItem();
        questionItem1.setQuestion("Do you experience Migrains?");
        questionItem1.setPositiveResponse("Yes");
        questionItem1.setNegativeResponse("No");
        questionItems.add(questionItem1);

        QuestionItem questionItem2 = new QuestionItem();
        questionItem2.setQuestion("Are you below 16 years of age?");
        questionItem2.setPositiveResponse("Yes");
        questionItem2.setNegativeResponse("No");
        questionItems.add(questionItem2);

        QuestionItem questionItem3 = new QuestionItem();
        questionItem3.setQuestion("Please pick your gender");
        questionItem3.setPositiveResponse("Men");
        questionItem3.setNegativeResponse("Women");
        questionItems.add(questionItem3);

        QuestionItem questionItem4 = new QuestionItem();
        questionItem4.setQuestion("Are you currently using hallucinogenic drugs?");
        questionItem4.setPositiveResponse("Yes");
        questionItem4.setNegativeResponse("No");
        questionItems.add(questionItem4);
    }

    View.OnClickListener responseButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.positive_button) {
                questionItems.get(currentQuestionDisplayed).setAffirmitiveResponse(true);
            } else {
                questionItems.get(currentQuestionDisplayed).setAffirmitiveResponse(false);
            }

            currentQuestionDisplayed++;

            if (currentQuestionDisplayed == questionItems.size())
                launchDiagnosticsFragment();
            else
                loadNextQuestion();

        }
    };

    protected void launchDiagnosticsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        diagnosticsFragment = new DiagnosticsFragment();
        fragmentTransaction.replace(android.R.id.content, diagnosticsFragment);
        fragmentTransaction.commit();
    }

    protected void launchPreviousResultsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        previousResultsFragment = new PreviousResultsFragment();
        fragmentTransaction.add(android.R.id.content, previousResultsFragment);
        fragmentTransaction.commit();
    }

    protected void dismissPreviousResultsFragment() {
        if (previousResultsFragment != null) {
            this.getSupportFragmentManager().beginTransaction()
                    .remove(previousResultsFragment).commit();
            previousResultsFragment = null;
        }
    }

    protected void dismissDiagnosticsFragment() {
        if (diagnosticsFragment != null) {
            this.getSupportFragmentManager().beginTransaction()
                    .remove(diagnosticsFragment).commit();
            diagnosticsFragment = null;
        }
    }

    /**
     * Load next questions and results with animation
     */
    protected void loadNextQuestion() {

        Animation hideAnimation = AnimationUtils.loadAnimation(this, R.anim.center_to_left);
        questionString.startAnimation(hideAnimation);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation showAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.right_to_center);
                questionString.startAnimation(showAnimation);
                QuestionItem currentItem = questionItems.get(currentQuestionDisplayed);
                questionString.setText(currentItem.getQuestion());
                positiveButton.setText(currentItem.getPositiveResponse());
                negativeButton.setText(currentItem.getNegativeResponse());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

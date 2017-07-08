package todd.toddsdetector;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vibinvarghese on 08/07/17.
 */

public class DiagnosticsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.diagnostics_fragment, container, false);
        TextView probabilityText = (TextView) rootView.findViewById(R.id.probability_text);
        TextView previousResultsText = (TextView) rootView.findViewById(R.id.previous_results_text);
        TextView retakeQuestionaire = (TextView) rootView.findViewById(R.id.retake_questionaire);

        int probability = (int) computeProbability();

        probabilityText.setText(probability + "%");

        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, probability);
        animation.setDuration(2000); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        //Storing current result into previous results array list
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date

        ((MainActivity) getActivity()).previousTestDates.add(currentDateTime);
        ((MainActivity) getActivity()).previousResults.add(String.valueOf(probability) + "%");

        previousResultsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).launchPreviousResultsFragment();
            }
        });

        retakeQuestionaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).currentQuestionDisplayed = 0;
                ((MainActivity) getActivity()).loadNextQuestion();
                ((MainActivity) getActivity()).dismissDiagnosticsFragment();
            }
        });

        return rootView;
    }

    /**
     * Computes the probability of Todd's syndrome
     */
    protected double computeProbability() {
        ArrayList<QuestionItem> questionItems = ((MainActivity) getActivity()).questionItems;
        double sum = 0;
        for (int i = 0; i < questionItems.size(); i++) {
            if (questionItems.get(i).isAffirmitiveResponse())
                sum++;
        }
        return ((sum / questionItems.size()) * 100);

    }
}

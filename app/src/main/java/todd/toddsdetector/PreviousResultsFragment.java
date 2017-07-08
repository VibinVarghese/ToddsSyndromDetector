package todd.toddsdetector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vibinvarghese on 08/07/17.
 */

public class PreviousResultsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.previous_results_fragment, container, false);
        LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.parent_main_view);
        ImageView cross = (ImageView) rootView.findViewById(R.id.cross);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).dismissPreviousResultsFragment();
            }
        });

        // Not using a listview as items are not complex and list of items is not too large

        for (int i = 0; i < ((MainActivity) getActivity()).previousResults.size(); i++) {
            View view = (View) layoutInflater.inflate(R.layout.result_item, null);

            ((TextView) view.findViewById(R.id.date)).setText(((MainActivity) getActivity()).previousTestDates.get(i));
            ((TextView) view.findViewById(R.id.result)).setText(((MainActivity) getActivity()).previousResults.get(i));

            mainLayout.addView(view);
        }


        return rootView;
    }
}

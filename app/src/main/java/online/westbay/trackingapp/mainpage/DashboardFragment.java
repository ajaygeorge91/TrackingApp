package online.westbay.trackingapp.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.mqtt.MqttTestActivity;
import online.westbay.trackingapp.trackingstuff.TrackActivity;

public class DashboardFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        int columnCount = 1; // LinearLayout
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Button buttonTrack = (Button) view.findViewById(R.id.buttonTrack);
        buttonTrack.setOnClickListener(view1 -> {
//            Intent intent = new Intent(getActivity(), TrackActivity.class);
            Intent intent = new Intent(getActivity(), MqttTestActivity.class);
            startActivity(intent);
        });

        return view;
    }

}

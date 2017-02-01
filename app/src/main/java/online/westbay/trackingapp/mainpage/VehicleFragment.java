package online.westbay.trackingapp.mainpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.adapters.OrgVehiclesExpandableListAdapter;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserWithDetailsDTO;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.services.UserService;
import online.westbay.trackingapp.utils.RxBus;
import online.westbay.trackingapp.utils.SharedPrefs;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VehicleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    ExpandableListView expandableListView;
    OrgVehiclesExpandableListAdapter vehiclesExpandableListAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleFragment() {
    }

    public static VehicleFragment newInstance() {
        int columnCount = 1; // LinearLayout
        VehicleFragment fragment = new VehicleFragment();
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
        vehiclesExpandableListAdapter = new OrgVehiclesExpandableListAdapter(getActivity());

        RxBus.toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s instanceof UserWithDetailsDTO) {
                        vehiclesExpandableListAdapter.setOrganizationUserDTOList(((UserWithDetailsDTO) s).getOrgList());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(vehiclesExpandableListAdapter);

        return view;
    }

}

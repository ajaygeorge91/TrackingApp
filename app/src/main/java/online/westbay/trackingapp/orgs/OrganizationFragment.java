package online.westbay.trackingapp.orgs;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.OrganizationDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.services.OrganizationService;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.utils.SharedPrefs;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Orgs.
 */
public class OrganizationFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OrganizationService organizationService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrganizationFragment() {
    }

    public static OrganizationFragment newInstance() {
        int columnCount = 1; // LinearLayout
        OrganizationFragment fragment = new OrganizationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        organizationService = ServiceGenerator.createService(OrganizationService.class, SharedPrefs.getToken(getActivity()));
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organization_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            organizationService.listOrgs()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(error -> {
                        Log.d("app", error.getMessage());
                        return new ResponseDTO<List<OrganizationDTO>>(error.getMessage());
                    })
                    .subscribe(s -> {
                        if (s.success) {
                            recyclerView.setAdapter(new MyOrganizationRecyclerViewAdapter(s.data,getActivity()));
                        } else {
                            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return view;
    }


}

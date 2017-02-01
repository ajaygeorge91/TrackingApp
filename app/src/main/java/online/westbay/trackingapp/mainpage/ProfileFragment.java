package online.westbay.trackingapp.mainpage;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import online.westbay.trackingapp.App;
import online.westbay.trackingapp.R;
import online.westbay.trackingapp.adapters.OrgProfilesRecyclerViewAdapter;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserDTO;
import online.westbay.trackingapp.models.UserWithDetailsDTO;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.services.UserService;
import online.westbay.trackingapp.utils.RxBus;
import online.westbay.trackingapp.utils.SharedPrefs;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileFragment extends Fragment {


    TextView textViewFullName;
    TextView textViewContact;
    ImageView imageViewAvatar;
    Button buttonLogout;
    private RecyclerView list_orgs;
    private OrgProfilesRecyclerViewAdapter orgProfilesRecyclerViewAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orgProfilesRecyclerViewAdapter = new OrgProfilesRecyclerViewAdapter(new ArrayList<>(),getActivity());
        RxBus.toObserverable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s instanceof UserWithDetailsDTO) {
                        populateData((UserWithDetailsDTO) s);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewFullName = (TextView) view.findViewById(R.id.textViewFullName);
        textViewContact = (TextView) view.findViewById(R.id.textViewContact);
        imageViewAvatar = (ImageView) view.findViewById(R.id.imageViewAvatar);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(view1 -> App.logout());

        list_orgs = (RecyclerView) view.findViewById(R.id.list_orgs);

        list_orgs.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void populateData(UserWithDetailsDTO userWithDetailsDTO) {
        textViewFullName.setText(userWithDetailsDTO.getUser().fullName);
        textViewContact.setText(userWithDetailsDTO.getUser().email);
        if (userWithDetailsDTO.getUser().avatarURL != null)
            imageViewAvatar.setImageURI(Uri.parse(userWithDetailsDTO.getUser().avatarURL));

        orgProfilesRecyclerViewAdapter.setValues(userWithDetailsDTO.getOrgList());
    }

}

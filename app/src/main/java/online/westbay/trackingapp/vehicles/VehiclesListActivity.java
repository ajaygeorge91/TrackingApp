package online.westbay.trackingapp.vehicles;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.OrganizationDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.VehicleDTO;
import online.westbay.trackingapp.services.OrganizationService;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.utils.SharedPrefs;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VehiclesListActivity extends AppCompatActivity {

    public static final String ARG_organizationDTO = "ARG_organizationDTO";

    private OrganizationService organizationService;
    private OrganizationDTO organizationDTO;
    private RecyclerView list_vehicles;
    private final int mColumnCount = 1;// linear layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        organizationService = ServiceGenerator.createService(OrganizationService.class, SharedPrefs.getToken(this));

        if (getIntent().getSerializableExtra(ARG_organizationDTO) != null) {
            organizationDTO = (OrganizationDTO) getIntent().getSerializableExtra(ARG_organizationDTO);
            getSupportActionBar().setTitle(organizationDTO.name);
        } else {
            Toast.makeText(this, "ERROR: No org as arg", Toast.LENGTH_SHORT).show();
            finish();
        }

        list_vehicles = (RecyclerView) findViewById(R.id.list_vehicles);

        // Set the adapter
        Context context = list_vehicles.getContext();
        if (mColumnCount <= 1) {
            list_vehicles.setLayoutManager(new LinearLayoutManager(context));
        } else {
            list_vehicles.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // get all the list of vehicles along with the organization detail

        organizationService.listVehiclesFromOrg(organizationDTO.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> {
                    Log.d("app", error.getMessage());
                    return new ResponseDTO<List<VehicleDTO>>(error.getMessage());
                })
                .subscribe(s -> {
                    if (s.success) {
                        list_vehicles.setAdapter(new VehiclesRecyclerViewAdapter(s.data, this));
                        list_vehicles.invalidate();
                    } else {
                        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}

package online.westbay.trackingapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.OrganizationUserDTO;
import online.westbay.trackingapp.models.VehicleDTO;
import online.westbay.trackingapp.test1.MapsActivity;
import online.westbay.trackingapp.trackingstuff.TrackActivity;

/**
 * Created by ajayg on 11/10/2016.
 */

public class OrgVehiclesExpandableListAdapter extends BaseExpandableListAdapter {

    private List<OrganizationUserDTO> organizationUserDTOList = new ArrayList<>();
    private Context context;

    public OrgVehiclesExpandableListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return organizationUserDTOList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return organizationUserDTOList.get(i).getVehicles().size();
    }

    @Override
    public OrganizationUserDTO getGroup(int i) {
        return organizationUserDTOList.get(i);
    }

    @Override
    public VehicleDTO getChild(int i, int i1) {
        return organizationUserDTOList.get(i).getVehicles().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(i).getOrganization().name;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.org_list_group, null);
        }
        TextView listTitleTextView = (TextView) view.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return view;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean b, View view, ViewGroup viewGroup) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition).name;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.org_list_vehicle_item, null);
        }
        TextView expandedListTextView = (TextView) view.findViewById(R.id.expandedListItem);
        Button buttonTrack = (Button) view.findViewById(R.id.buttonTrack);
        buttonTrack.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra(MapsActivity.VEHICLE_DTO,getChild(listPosition, expandedListPosition));
            context.startActivity(intent);
        });
        expandedListTextView.setText(expandedListText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setOrganizationUserDTOList(List<OrganizationUserDTO> organizationUserDTOList) {
        this.organizationUserDTOList = organizationUserDTOList;
        notifyDataSetChanged();
    }

}

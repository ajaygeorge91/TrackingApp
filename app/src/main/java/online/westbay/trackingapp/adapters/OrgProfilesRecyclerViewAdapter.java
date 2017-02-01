package online.westbay.trackingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.OrganizationUserDTO;
import online.westbay.trackingapp.vehicles.VehicleDetailActivity;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrganizationUserDTO}
 */
public class OrgProfilesRecyclerViewAdapter extends RecyclerView.Adapter<OrgProfilesRecyclerViewAdapter.ViewHolder> {

    private List<OrganizationUserDTO> mValues;
    private Context context;

    public OrgProfilesRecyclerViewAdapter(List<OrganizationUserDTO> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getOrganization().name);
        holder.mContentView.setText(mValues.get(position).getRole());

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VehicleDetailActivity.class);
            intent.putExtra(VehicleDetailActivity.ARG_vehicleDTO, mValues.get(position).getOrganization());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public OrganizationUserDTO mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void setValues(List<OrganizationUserDTO> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }
}

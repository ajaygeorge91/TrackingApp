package online.westbay.trackingapp.orgs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.OrganizationDTO;
import online.westbay.trackingapp.vehicles.VehiclesListActivity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrganizationDTO}
 */
public class MyOrganizationRecyclerViewAdapter extends RecyclerView.Adapter<MyOrganizationRecyclerViewAdapter.ViewHolder> {

    private final List<OrganizationDTO> mValues;
    private Context context;

    public MyOrganizationRecyclerViewAdapter(List<OrganizationDTO> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_organization, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context,VehiclesListActivity.class);
            intent.putExtra(VehiclesListActivity.ARG_organizationDTO,mValues.get(position));
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
        public OrganizationDTO mItem;

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
}

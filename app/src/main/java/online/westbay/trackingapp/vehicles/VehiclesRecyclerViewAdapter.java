package online.westbay.trackingapp.vehicles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.VehicleDTO;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VehicleDTO}
 */
public class VehiclesRecyclerViewAdapter extends RecyclerView.Adapter<VehiclesRecyclerViewAdapter.ViewHolder> {

    private final List<VehicleDTO> mValues;
    private Context context;

    public VehiclesRecyclerViewAdapter(List<VehicleDTO> items, Context context) {
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
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VehicleDetailActivity.class);
            intent.putExtra(VehicleDetailActivity.ARG_vehicleDTO, mValues.get(position));
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
        public VehicleDTO mItem;

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

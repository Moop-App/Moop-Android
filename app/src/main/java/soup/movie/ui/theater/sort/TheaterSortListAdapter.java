package soup.movie.ui.theater.sort;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.model.TheaterCode;
import soup.movie.util.ListUtil;

class TheaterSortListAdapter extends RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder> {

    private final ArrayList<TheaterCode> selectedItems;

    TheaterSortListAdapter(@NonNull List<TheaterCode> selectedItems) {
        this.selectedItems = new ArrayList<>(selectedItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        return new ViewHolder(LayoutInflater.from(ctx)
                .inflate(R.layout.item_theater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheaterCode theaterItem = selectedItems.get(position);
        holder.bindType(theaterItem);
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(selectedItems);
    }

    public List<TheaterCode> getSelectedTheaters() {
        return new ArrayList<>(selectedItems);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chip_theater)
        Chip theaterChip;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindType(TheaterCode data) {
            theaterChip.setChipText(data.getName());
            theaterChip.setTransitionName(data.getCode());
        }
    }
}

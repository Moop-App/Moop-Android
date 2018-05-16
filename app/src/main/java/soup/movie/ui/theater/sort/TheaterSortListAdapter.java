package soup.movie.ui.theater.sort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.model.Theater;
import soup.movie.util.ListUtil;
import soup.widget.drag.ItemTouchHelperAdapter;
import soup.widget.drag.OnStartDragListener;

class TheaterSortListAdapter extends RecyclerView.Adapter<TheaterSortListAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private final ArrayList<Theater> selectedItems;

    private OnStartDragListener dragStartListener;

    TheaterSortListAdapter(@NonNull List<Theater> selectedItems,
                           OnStartDragListener dragListener) {
        this.selectedItems = new ArrayList<>(selectedItems);
        dragStartListener = dragListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        return new ViewHolder(LayoutInflater.from(ctx)
                .inflate(R.layout.item_theater, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theater theaterItem = selectedItems.get(position);
        holder.bindType(theaterItem);

        holder.dragHandle.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(selectedItems);
    }

    public List<Theater> getSelectedTheaters() {
        return new ArrayList<>(selectedItems);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(selectedItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chip_theater)
        Chip theaterChip;

        @BindView(R.id.drag_handle)
        View dragHandle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindType(Theater data) {
            theaterChip.setChipText(data.getName());
            theaterChip.setTransitionName(data.getCode());
            theaterChip.setTag(data.getCode());
        }
    }
}

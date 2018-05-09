package soup.movie.ui.theater.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.model.TheaterCode;
import soup.movie.util.ListUtil;

class TheaterEditListAdapter extends RecyclerView.Adapter<TheaterEditListAdapter.ViewHolder> {

    private static final int MAX_ITEMS = 3;

    private final ArrayList<TheaterCode> allItems;
    private final HashMap<String, TheaterCode> selectedItemMap;

    TheaterEditListAdapter(@NonNull List<TheaterCode> allItems,
                           @NonNull List<TheaterCode> selectedItems) {
        this.allItems = new ArrayList<>(allItems);
        this.selectedItemMap = createSelectedItemMapFrom(selectedItems);
    }

    private static HashMap<String, TheaterCode> createSelectedItemMapFrom(
            @NonNull List<TheaterCode> selectedItems) {
        HashMap<String, TheaterCode> itemMap = new HashMap<>();
        for (TheaterCode tc : selectedItems) {
            itemMap.put(tc.getCode(), tc);
        }
        return itemMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        ViewHolder holder = new ViewHolder(LayoutInflater.from(ctx)
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false));
        holder.checkedTextView.setOnClickListener(v -> {
            TheaterCode theaterItem = allItems.get(holder.getAdapterPosition());
            CheckedTextView targetView = holder.checkedTextView;
            if (targetView.isChecked()) {
                selectedItemMap.remove(theaterItem.getCode());
                targetView.setChecked(false);
            } else if (selectedItemMap.size() < MAX_ITEMS) {
                selectedItemMap.put(theaterItem.getCode(), theaterItem);
                targetView.setChecked(true);
            } else {
                String message = ctx.getString(R.string.theater_select_limit_description, MAX_ITEMS);
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheaterCode theaterItem = allItems.get(position);
        holder.checkedTextView.setText(theaterItem.getName());
        holder.checkedTextView.setChecked(selectedItemMap.containsKey(theaterItem.getCode()));
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(allItems);
    }

    public List<TheaterCode> getSelectedTheaters() {
        return new ArrayList<>(selectedItemMap.values());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        CheckedTextView checkedTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

package soup.movie.ui.boxoffice;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import soup.movie.R;
import soup.movie.data.BoxOfficeMovie;
import soup.movie.util.ListUtil;

class BoxOfficeListAdapter extends RecyclerView.Adapter<BoxOfficeListAdapter.ViewHolder> {

    private List<BoxOfficeMovie> mItems = new ArrayList<>();

    BoxOfficeListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_with_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BoxOfficeMovie item = mItems.get(position);
        holder.mRankView.setText(item.getRank());
        holder.mTitleView.setText(item.getMovieNm());
        holder.mSubtitleView.setText(item.getAudiCnt());
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(mItems);
    }

    void updateList(List<BoxOfficeMovie> newItems) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return ListUtil.size(mItems);
            }

            @Override
            public int getNewListSize() {
                return ListUtil.size(newItems);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        }, false);
        mItems = newItems;
        result.dispatchUpdatesTo(this);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mRankView;
        final TextView mTitleView;
        final TextView mSubtitleView;
        final View mFavoriteButton;

        ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.preview_card).setOnClickListener(v -> {});
            mRankView = view.findViewById(R.id.txt_rank);
            mTitleView = view.findViewById(R.id.primary_text);
            mSubtitleView = view.findViewById(R.id.sub_text);
            mFavoriteButton = view.findViewById(R.id.favorite_button);
            mFavoriteButton.setOnClickListener(v -> {});
        }
    }
}

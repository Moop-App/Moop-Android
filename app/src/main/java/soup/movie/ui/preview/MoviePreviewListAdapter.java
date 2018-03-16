package soup.movie.ui.preview;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import soup.movie.R;
import soup.movie.data.kobis.model.Movie;
import soup.movie.util.ListUtil;

public class MoviePreviewListAdapter extends RecyclerView.Adapter<MoviePreviewListAdapter.ViewHolder>
        implements MoviePreviewContract.AdapterView {

    private List<Movie> mItems = new ArrayList<>();

    MoviePreviewListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie item = mItems.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mSubtitleView.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(mItems);
    }

    @Override
    public void updateList(List<Movie> newItems) {
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

        //final ImageView mPosterView;
        final TextView mTitleView;
        final TextView mSubtitleView;
        final View mFavoriteButton;
        final View mShareButton;

        ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.preview_card).setOnClickListener(v -> {});
            //mPosterView = view.findViewById(R.id.movie_poster);
            mTitleView = view.findViewById(R.id.primary_text);
            mSubtitleView = view.findViewById(R.id.sub_text);
            mFavoriteButton = view.findViewById(R.id.favorite_button);
            mFavoriteButton.setOnClickListener(v -> {});
            mShareButton = view.findViewById(R.id.share_button);
            mShareButton.setOnClickListener(v -> {});
        }
    }
}

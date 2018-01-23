package soup.movie.ui.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import soup.movie.R;
import soup.movie.data.Movie;

public class MoviePreviewListAdapter extends RecyclerView.Adapter<MoviePreviewListAdapter.ViewHolder>
        implements MoviePreviewContract.AdapterView {

    private List<Movie> mValues = new ArrayList<>();

    MoviePreviewListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie item = mValues.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mSubtitleView.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void updateList(List<Movie> items) {
        //TODO: update using DiffUtil
        mValues = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mPosterView;
        final TextView mTitleView;
        final TextView mSubtitleView;
        final View mFavoriteButton;
        final View mBookmarkButton;
        final View mShareButton;

        ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.preview_card).setOnClickListener(v -> {});
            mPosterView = view.findViewById(R.id.movie_poster);
            mTitleView = view.findViewById(R.id.primary_text);
            mSubtitleView = view.findViewById(R.id.sub_text);
            mFavoriteButton = view.findViewById(R.id.favorite_button);
            mFavoriteButton.setOnClickListener(v -> {});
            mBookmarkButton = view.findViewById(R.id.bookmark_button);
            mBookmarkButton.setOnClickListener(v -> {});
            mShareButton = view.findViewById(R.id.share_button);
            mShareButton.setOnClickListener(v -> {});
        }
    }
}

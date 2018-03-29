package soup.movie.ui.home;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.soup.model.Movie;
import soup.movie.ui.util.ImageUtil;
import soup.movie.ui.util.OnItemClickListener;
import soup.movie.util.ListUtil;
import soup.movie.util.function.Consumer;

class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private final Context mContext;
    private final OnItemClickListener<Movie> mClickListener;

    private List<Movie> mItems = new ArrayList<>();

    HomeListAdapter(Context context, OnItemClickListener<Movie> clickListener) {
        mContext = context;
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_for_home, parent, false);
        return new ViewHolder(view, position -> mClickListener.onItemClick(mItems.get(position)));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie item = mItems.get(position);
        ImageUtil.loadAsync(mContext, holder.mPosterView, item.getThumbnailUrl());
        holder.mTitleView.setText(item.getTitle());
        holder.mAgeView.setText(item.getAge());
        holder.mEggView.setText(item.getEgg());
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(mItems);
    }

    void updateList(List<Movie> newItems) {
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

        @BindView(R.id.movie_poster)
        ImageView mPosterView;
        @BindView(R.id.primary_text)
        TextView mTitleView;
        @BindView(R.id.sub_text1)
        TextView mAgeView;
        @BindView(R.id.sub_text2)
        TextView mEggView;
        @BindView(R.id.favorite_button)
        View mFavoriteButton;
        @BindView(R.id.share_button)
        View mShareButton;

        ViewHolder(View view, Consumer<Integer> positionConsumer) {
            super(view);
            ButterKnife.bind(this, view);
            view.findViewById(R.id.preview_card).setOnClickListener(
                    v -> positionConsumer.accept(getAdapterPosition()));
            mFavoriteButton.setOnClickListener(v -> {});
            mShareButton.setOnClickListener(v -> {});
        }
    }
}

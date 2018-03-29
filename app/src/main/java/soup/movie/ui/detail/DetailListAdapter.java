package soup.movie.ui.detail;

import android.app.Activity;
import android.support.annotation.NonNull;
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
import soup.movie.data.soup.model.Trailer;
import soup.movie.data.utils.YouTubeUtil;
import soup.movie.ui.util.ImageUtil;
import soup.movie.util.ListUtil;

class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private final Activity mHost;

    private List<Trailer> mItems = new ArrayList<>();

    DetailListAdapter(Activity host) {
        mHost = host;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> YouTubeUtil.executeYoutubeApp(
                mHost, mItems.get(holder.getAdapterPosition()).getId()));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Trailer item = mItems.get(position);
        ImageUtil.loadAsync(mHost, holder.mThumbnailView, YouTubeUtil.getThumbnailUrl(item.getId()));
        holder.mTitleView.setText(item.getTitle());
        holder.mAuthorView.setText(item.getAuthor());
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(mItems);
    }

    void updateList(@NonNull List<Trailer> newItems) {
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

        @BindView(R.id.trailer_thumbnail)
        ImageView mThumbnailView;
        @BindView(R.id.primary_text)
        TextView mTitleView;
        @BindView(R.id.sub_text)
        TextView mAuthorView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

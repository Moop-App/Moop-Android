package soup.movie.ui.now;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.soup.model.Movie;
import soup.movie.data.utils.MovieUtil;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.util.ImageUtil;
import soup.movie.util.ListUtil;

import static soup.movie.util.IntentUtil.createShareIntentWithText;

class NowListAdapter extends RecyclerView.Adapter<NowListAdapter.ViewHolder> {

    private final Activity mHost;

    private List<Movie> mItems = new ArrayList<>();

    @BindColor(R.color.green)
    int mGreenColor;

    @BindColor(R.color.blue)
    int mBlueColor;

    @BindColor(R.color.amber)
    int mAmberColor;

    @BindColor(R.color.red)
    int mRedColor;

    NowListAdapter(Activity host) {
        mHost = host;
        ButterKnife.bind(this, host);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_for_home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mHost, DetailActivity.class);
            MovieUtil.saveTo(intent, mItems.get(holder.getAdapterPosition()));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(mHost,
                            Pair.create(holder.mBackground, mHost.getString(R.string.transition_background)),
                            Pair.create(holder.mPosterView, mHost.getString(R.string.transition_poster)),
                            Pair.create(holder.mTitleView, mHost.getString(R.string.transition_title)),
                            Pair.create(holder.mAgeView, mHost.getString(R.string.transition_age)),
                            Pair.create(holder.mSubTextView, mHost.getString(R.string.transition_egg)),
//                            Pair.create(holder.mFavoriteButton, mHost.getString(R.string.transition_favorite)),
                            Pair.create(holder.mShareButton, mHost.getString(R.string.transition_share)));
            mHost.startActivity(intent, options.toBundle());
        });
        holder.mShareButton.setOnClickListener(v -> {
            Movie movie = mItems.get(holder.getAdapterPosition());
            mHost.startActivity(createShareIntentWithText(
                    "공유하기", MovieUtil.createShareDescription(movie)));
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie item = mItems.get(position);
        ImageUtil.loadAsync(mHost, holder.mPosterView, item.getPosterUrl());
        holder.mTitleView.setText(item.getTitle());
        holder.mSubTextView.setText(item.getOpenDate());
        updateAgeText(holder.mAgeBgView, holder.mAgeView, item.getAge());
    }

    private void updateAgeText(View ageBgView, TextView ageTextView, String ageText) {
        int color = Color.TRANSPARENT;
        switch (ageText) {
            case "전체 관람가":
                ageText = "전체";
                color = mGreenColor;
                break;
            case "12세 관람가":
                ageText = "12";
                color = mBlueColor;
                break;
            case "15세 관람가":
                ageText = "15";
                color = mAmberColor;
                break;
            case "청소년관람불가":
                ageText = "청불";
                color = mRedColor;
                break;
            default:
                ageText = null;
        }
        if (TextUtils.isEmpty(ageText)) {
            ageBgView.setVisibility(View.GONE);
            ageTextView.setVisibility(View.GONE);
        } else {
            ageBgView.setBackgroundColor(color);
            ageTextView.setText(ageText);
            ageTextView.setTextColor(color);
            ageBgView.setVisibility(View.VISIBLE);
            ageTextView.setVisibility(View.VISIBLE);
        }
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

        @BindView(R.id.background)
        View mBackground;
        @BindView(R.id.movie_poster)
        ImageView mPosterView;
        @BindView(R.id.primary_text)
        TextView mTitleView;
        @BindView(R.id.age_bg)
        View mAgeBgView;
        @BindView(R.id.age_icon)
        TextView mAgeView;
        @BindView(R.id.sub_text2)
        TextView mSubTextView;
        @BindView(R.id.favorite_button)
        View mFavoriteButton;
        @BindView(R.id.share_button)
        View mShareButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

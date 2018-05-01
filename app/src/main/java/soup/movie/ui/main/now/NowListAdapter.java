package soup.movie.ui.main.now;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import soup.movie.data.model.Movie;
import soup.movie.util.MovieUtil;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.util.ImageUtil;
import soup.movie.util.ListUtil;

import static soup.movie.util.IntentUtil.createShareIntentWithText;

class NowListAdapter extends RecyclerView.Adapter<NowListAdapter.ViewHolder> {

    private final Activity host;

    private List<Movie> items = new ArrayList<>();

    @BindColor(R.color.green)
    int greenColor;

    @BindColor(R.color.blue)
    int blueColor;

    @BindColor(R.color.amber)
    int amberColor;

    @BindColor(R.color.red)
    int redColor;

    @BindColor(R.color.grey)
    int greyColor;

    NowListAdapter(Activity host) {
        this.host = host;
        ButterKnife.bind(this, host);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_horizontal, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(host, DetailActivity.class);
            MovieUtil.saveTo(intent, items.get(holder.getAdapterPosition()));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(host,
                            Pair.create(holder.backgroundView, host.getString(R.string.transition_background)),
                            Pair.create(holder.posterView, host.getString(R.string.transition_poster)),
                            Pair.create(holder.titleView, host.getString(R.string.transition_title)),
                            Pair.create(holder.ageView, host.getString(R.string.transition_age)),
                            Pair.create(holder.ageBgView, host.getString(R.string.transition_age_bg)),
                            Pair.create(holder.subTextView, host.getString(R.string.transition_egg)),
//                            Pair.create(holder.favoriteButton, host.getString(R.string.transition_favorite)),
                            Pair.create(holder.shareButton, host.getString(R.string.transition_share)));
            host.startActivity(intent, options.toBundle());
        });
        holder.shareButton.setOnClickListener(v -> {
            Movie movie = items.get(holder.getAdapterPosition());
            host.startActivity(createShareIntentWithText(
                    "공유하기", MovieUtil.createShareDescription(movie)));
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie item = items.get(position);
        ImageUtil.loadAsync(host, holder.posterView, item.getPosterUrl());
        holder.titleView.setText(item.getTitle());
        holder.subTextView.setText(item.getOpenDate());
        updateAgeText(holder.ageBgView, holder.ageView, item.getAge());
    }

    private void updateAgeText(View ageBgView, TextView ageTextView, String ageText) {
        int color;
        switch (ageText) {
            case "전체 관람가":
                ageText = "전체";
                color = greenColor;
                break;
            case "12세 관람가":
                ageText = "12";
                color = blueColor;
                break;
            case "15세 관람가":
                ageText = "15";
                color = amberColor;
                break;
            case "청소년관람불가":
                ageText = "청불";
                color = redColor;
                break;
            default:
                ageText = "미정";
                color = greyColor;
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
        return ListUtil.size(items);
    }

    void updateList(List<Movie> newItems) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return ListUtil.size(items);
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
        items = newItems;
        result.dispatchUpdatesTo(this);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.background)
        View backgroundView;
        @BindView(R.id.movie_poster)
        ImageView posterView;
        @BindView(R.id.primary_text)
        TextView titleView;
        @BindView(R.id.age_bg)
        View ageBgView;
        @BindView(R.id.age_icon)
        TextView ageView;
        @BindView(R.id.sub_text2)
        TextView subTextView;
        @BindView(R.id.favorite_button)
        View favoriteButton;
        @BindView(R.id.share_button)
        View shareButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

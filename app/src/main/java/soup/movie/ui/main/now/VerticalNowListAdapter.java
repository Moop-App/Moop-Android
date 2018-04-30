package soup.movie.ui.main.now;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.model.Movie;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.util.ImageUtil;
import soup.movie.util.ListUtil;
import soup.movie.util.MovieUtil;

class VerticalNowListAdapter extends RecyclerView.Adapter<VerticalNowListAdapter.ViewHolder> {

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

    VerticalNowListAdapter(Activity host) {
        this.host = host;
        ButterKnife.bind(this, host);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_vertical, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(host, DetailActivity.class);
            MovieUtil.saveTo(intent, items.get(holder.getAdapterPosition()));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(host,
                            Pair.create(holder.backgroundView, host.getString(R.string.transition_background)),
                            Pair.create(holder.posterView, host.getString(R.string.transition_poster)),
                            Pair.create(holder.ageBgView, host.getString(R.string.transition_age)));
            host.startActivity(intent, options.toBundle());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie item = items.get(position);
        ImageUtil.loadAsync(host, holder.posterView, item.getPosterUrl());
        updateAgeView(holder.ageBgView, item.getAge());
    }

    private void updateAgeView(View ageBgView, String ageText) {
        int color = Color.TRANSPARENT;
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
                ageText = null;
        }
        if (TextUtils.isEmpty(ageText)) {
            ageBgView.setVisibility(View.GONE);
        } else {
            ageBgView.setBackgroundTintList(ColorStateList.valueOf(color));
            ageBgView.setVisibility(View.VISIBLE);
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
        @BindView(R.id.age_bg)
        View ageBgView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

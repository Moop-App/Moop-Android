package soup.movie.ui.main.now;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.model.Movie;
import soup.movie.ui.main.OnInternalClickListener;
import soup.movie.ui.main.OnMovieClickListener;
import soup.movie.util.AlwaysDiffCallback;
import soup.movie.util.ImageUtil;

class NowListAdapter extends ListAdapter<Movie, NowListAdapter.ViewHolder> {

    private final OnMovieClickListener clickListener;

    NowListAdapter(OnMovieClickListener clickListener) {
        super(new AlwaysDiffCallback<>());
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_vertical, parent, false),
                (position, sharedElements) ->
                        clickListener.onMovieClick(getItem(position), sharedElements));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.background)
        View backgroundView;
        @BindView(R.id.movie_poster)
        ImageView posterView;
        @BindView(R.id.age_bg)
        View ageBgView;
        @BindView(R.id.age_bg_outer)
        View ageBgOuterView;

        ViewHolder(View view, OnInternalClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            Context ctx = view.getContext();
            view.setOnClickListener(v -> clickListener.onItemClick(
                    getAdapterPosition(),
                    Pair.create(backgroundView, ctx.getString(R.string.transition_background)),
                    Pair.create(posterView, ctx.getString(R.string.transition_poster)),
                    Pair.create(ageBgView, ctx.getString(R.string.transition_age_bg)),
                    Pair.create(ageBgOuterView, ctx.getString(R.string.transition_age_bg_outer))));
        }

        void bindItem(@NonNull Movie movie) {
            Context ctx = itemView.getContext();
            ImageUtil.loadAsync(ctx, posterView, movie.getPoster());
            updateAgeText(ageBgView, movie.getAge());
        }

        private void updateAgeText(View ageBgView, String ageText) {
            if (TextUtils.isEmpty(ageText)) {
                ageBgView.setVisibility(View.GONE);
            } else {
                ageBgView.setBackgroundTintList(
                        ContextCompat.getColorStateList(ageBgView.getContext(), getColorAsAge(ageText)));
                ageBgView.setVisibility(View.VISIBLE);
            }
        }

        @ColorRes
        private int getColorAsAge(String age) {
            switch (age) {
                case "전체 관람가":
                    return R.color.green;
                case "12세 관람가":
                    return R.color.blue;
                case "15세 관람가":
                    return R.color.amber;
                case "청소년관람불가":
                    return R.color.red;
                default:
                    return R.color.grey;
            }
        }
    }
}

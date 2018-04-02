package soup.movie.ui.detail;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import soup.movie.R;
import soup.movie.data.soup.model.Day;
import soup.movie.data.soup.model.TheaterCode;
import soup.movie.data.soup.model.Thumbnail;
import soup.movie.data.soup.model.Thumbnails;
import soup.movie.data.soup.model.TimeTable;
import soup.movie.data.soup.model.Trailer;
import soup.movie.data.utils.MovieAppUtil;
import soup.movie.data.utils.YouTubeUtil;
import soup.movie.ui.util.DialogUtil;
import soup.movie.ui.util.ImageUtil;
import soup.movie.util.ListUtil;
import soup.movie.util.function.Consumer;

class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 3;
    private static final int TYPE_TIMETABLE_NONE = 0;
    private static final int TYPE_TIMETABLE = 1;
    private static final int TYPE_TRAILER = 2;

    private final Activity mHost;
    private final Consumer<List<TheaterCode>> mConsumer;

    private TimeTable mTimeTable;
    private List<Trailer> mItems = new ArrayList<>();

    DetailListAdapter(Activity host, Consumer<List<TheaterCode>> theaterCodeConsumer) {
        mHost = host;
        mConsumer = theaterCodeConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder holder;
        switch (viewType) {
            case TYPE_TIMETABLE_NONE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_timetable_none, parent, false);
                holder = new NoneTimeTableViewHolder(view);
                View.OnClickListener listener = v ->
                        DialogUtil.startDialogToSelectTheaters(mHost, mConsumer::accept);
                holder.itemView.setOnClickListener(listener);
                ((NoneTimeTableViewHolder)holder).mSelect.setOnClickListener(listener);
                break;
            case TYPE_TIMETABLE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_timetable, parent, false);
                holder = new TimeTableViewHolder(view);
                holder.itemView.setOnClickListener(v -> {
                    //TODO: show notification with selected date and time
                    MovieAppUtil.executeCgvApp(mHost);
                });
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_trailer, parent, false);
                holder = new TrailerViewHolder(view);
                holder.itemView.setOnClickListener(v -> YouTubeUtil.executeYoutubeApp(
                        mHost, mItems.get(holder.getAdapterPosition()).getId()));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position == 0) {
            if (holder instanceof TimeTableViewHolder) {
                TimeTableViewHolder holder1 = (TimeTableViewHolder) holder;
                TimeTable item = mTimeTable;
                List<Day> days = item.getDayList();
                if (days != null && !days.isEmpty()) {
                    holder1.empty.setVisibility(View.GONE);
                    int size = Math.min(3, days.size());
                    for (int i = 0; i < size; i++) {
                        Day day = days.get(i);
                        holder1.dates[i].setText(day.getDate());
                        holder1.dates[i].setVisibility(View.VISIBLE);
                        holder1.times[i].setText(StringUtils.join(day.getTimeList(), ", "));
                        holder1.times[i].setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            TrailerViewHolder trailerViewHolder = (TrailerViewHolder)holder;
            Trailer item = mItems.get(position - 1);
            ImageUtil.loadAsync(mHost, trailerViewHolder.mThumbnailView, getThumbnailUrl(item));
            trailerViewHolder.mTitleView.setText(item.getTitle());
            trailerViewHolder.mAuthorView.setText(item.getAuthor());
        }
    }

    private static String getThumbnailUrl(@NonNull Trailer trailer) {
        Thumbnails thumbnails = trailer.getThumbnails();
        if (thumbnails != null) {
            Thumbnail high = thumbnails.getHigh();
            if (high != null && high.getUrl() != null) {
                return high.getUrl();
            }
            Thumbnail medium = thumbnails.getMedium();
            if (medium != null && medium.getUrl() != null) {
                return medium.getUrl();
            }
            Thumbnail low = thumbnails.getLow();
            if (low != null && low.getUrl() != null) {
                return low.getUrl();
            }
        }
        return YouTubeUtil.getThumbnailUrl(trailer.getId());
    }

    @Override
    public int getItemViewType(int position) {
        return position != 0 ? TYPE_TRAILER : mTimeTable.getDayList() == null ? TYPE_TIMETABLE_NONE : TYPE_TIMETABLE;
    }

    @Override
    public int getItemCount() {
        return ListUtil.size(mItems) + calcMoreCount(mTimeTable);
    }

    private int calcMoreCount(TimeTable timeTable) {
        return timeTable != null ? 1 : 0;
    }

    void updateList(@Nullable TimeTable timeTable, @Nullable List<Trailer> newItems) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return ListUtil.size(mItems) + calcMoreCount(mTimeTable);
            }

            @Override
            public int getNewListSize() {
                return ListUtil.size(newItems) + calcMoreCount(timeTable);
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
        mTimeTable = timeTable;
        mItems = newItems;
        result.dispatchUpdatesTo(this);
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
        }
    }
    class TrailerViewHolder extends ViewHolder {

        @BindView(R.id.trailer_thumbnail)
        ImageView mThumbnailView;
        @BindView(R.id.primary_text)
        TextView mTitleView;
        @BindView(R.id.sub_text)
        TextView mAuthorView;

        TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    class TimeTableViewHolder extends ViewHolder {

        @BindViews({
                R.id.time1, R.id.time2, R.id.time3})
        TextView[] times;
        @BindViews({
                R.id.date1, R.id.date2, R.id.date3})
        TextView[] dates;
        @BindView(R.id.empty)
        TextView empty;

        TimeTableViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    class NoneTimeTableViewHolder extends ViewHolder {

        @BindView(R.id.select)
        View mSelect;

        NoneTimeTableViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

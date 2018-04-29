package soup.movie.data;

import java.util.List;

import io.reactivex.Single;
import soup.movie.data.kobis.IKobisDataSource;
import soup.movie.data.kobis.model.DailyBoxOfficeRequest;
import soup.movie.data.kobis.model.DailyBoxOfficeResult;
import soup.movie.data.kobis.model.Movie;
import soup.movie.data.kobis.model.MovieListRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeRequest;
import soup.movie.data.kobis.model.WeeklyBoxOfficeResult;
import soup.movie.data.soup.ISoupDataSource;
import soup.movie.data.soup.model.ArtMovieRequest;
import soup.movie.data.soup.model.ArtMovieResponse;
import soup.movie.data.soup.model.CodeRequest;
import soup.movie.data.soup.model.CodeResponse;
import soup.movie.data.soup.model.NowMovieRequest;
import soup.movie.data.soup.model.NowMovieResponse;
import soup.movie.data.soup.model.PlanMovieRequest;
import soup.movie.data.soup.model.PlanMovieResponse;
import soup.movie.data.soup.model.TimeTableRequest;
import soup.movie.data.soup.model.TimeTableResponse;

public class MovieRepository implements IKobisDataSource, ISoupDataSource {

    private final IKobisDataSource kobis;
    private final ISoupDataSource soup;

    public MovieRepository(IKobisDataSource kobisDataSource, ISoupDataSource soupDataSource) {
        kobis = kobisDataSource;
        soup = soupDataSource;
    }

    @Override
    public Single<List<Movie>> getMovieList(MovieListRequest movieListRequest) {
        return kobis.getMovieList(movieListRequest);
    }

    @Override
    public Single<DailyBoxOfficeResult> getDailyBoxOfficeList(DailyBoxOfficeRequest dailyBoxOfficeRequest) {
        return kobis.getDailyBoxOfficeList(dailyBoxOfficeRequest);
    }

    @Override
    public Single<WeeklyBoxOfficeResult> getWeeklyBoxOfficeList(WeeklyBoxOfficeRequest weeklyBoxOfficeRequest) {
        return kobis.getWeeklyBoxOfficeList(weeklyBoxOfficeRequest);
    }

    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return soup.getNowList(request);
    }

    @Override
    public Single<ArtMovieResponse> getArtList(ArtMovieRequest request) {
        return soup.getArtList(request);
    }

    @Override
    public Single<PlanMovieResponse> getPlanList(PlanMovieRequest request) {
        return soup.getPlanList(request);
    }

    @Override
    public Single<CodeResponse> getCodeList(CodeRequest request) {
        return soup.getCodeList(request);
    }

    @Override
    public Single<TimeTableResponse> getTimeTableList(TimeTableRequest request) {
        return soup.getTimeTableList(request);
    }
}

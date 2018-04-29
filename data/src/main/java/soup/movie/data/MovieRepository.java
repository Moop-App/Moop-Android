package soup.movie.data;

import io.reactivex.Single;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.NowMovieRequest;
import soup.movie.data.model.NowMovieResponse;
import soup.movie.data.model.PlanMovieRequest;
import soup.movie.data.model.PlanMovieResponse;
import soup.movie.data.model.TimeTableRequest;
import soup.movie.data.model.TimeTableResponse;

public class MovieRepository implements ISoupDataSource {

    private final ISoupDataSource soup;

    public MovieRepository(ISoupDataSource soupDataSource) {
        soup = soupDataSource;
    }

    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return soup.getNowList(request);
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

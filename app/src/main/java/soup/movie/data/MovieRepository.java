package soup.movie.data;

import io.reactivex.Single;
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

public class MovieRepository implements ISoupDataSource {

    private final ISoupDataSource soup;

    public MovieRepository(ISoupDataSource soupDataSource) {
        soup = soupDataSource;
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

package soup.movie.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.NowMovieRequest;
import soup.movie.data.model.NowMovieResponse;
import soup.movie.data.model.PlanMovieRequest;
import soup.movie.data.model.PlanMovieResponse;
import soup.movie.data.model.TimeTableRequest;
import soup.movie.data.model.TimeTableResponse;

@Singleton
public class MovieRepository implements IMoobDataSource {

    private final IMoobDataSource remoteDataSource;

    @Inject
    public MovieRepository(IMoobDataSource moobDataSource) {
        remoteDataSource = moobDataSource;
    }

    @Override
    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return remoteDataSource.getNowList(request);
    }

    @Override
    public Single<PlanMovieResponse> getPlanList(PlanMovieRequest request) {
        return remoteDataSource.getPlanList(request);
    }

    @Override
    public Single<CodeResponse> getCodeList(CodeRequest request) {
        return remoteDataSource.getCodeList(request);
    }

    @Override
    public Single<TimeTableResponse> getTimeTableList(TimeTableRequest request) {
        return remoteDataSource.getTimeTableList(request);
    }
}

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

public interface ISoupDataSource {

    Single<NowMovieResponse> getNowList(NowMovieRequest request);
    Single<PlanMovieResponse> getPlanList(PlanMovieRequest request);
    Single<CodeResponse> getCodeList(CodeRequest request);
    Single<TimeTableResponse> getTimeTableList(TimeTableRequest request);
}

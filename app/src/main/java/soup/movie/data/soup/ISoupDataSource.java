package soup.movie.data.soup;

import io.reactivex.Single;
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
import soup.movie.data.soup.model.TrailerRequest;
import soup.movie.data.soup.model.TrailerResponse;

public interface ISoupDataSource {

    Single<NowMovieResponse> getNowList(NowMovieRequest request);
    Single<ArtMovieResponse> getArtList(ArtMovieRequest request);
    Single<PlanMovieResponse> getPlanList(PlanMovieRequest request);
    Single<CodeResponse> getCodeList(CodeRequest request);
    Single<TimeTableResponse> getTimeTableList(TimeTableRequest request);
    Single<TrailerResponse> getTrailerList(TrailerRequest request);
}

package soup.movie.data.soup;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
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
import soup.movie.data.soup.service.SoupApiService;

public class SoupDataSource implements ISoupDataSource {

    private SoupApiService mSoupApi;

    public SoupDataSource(SoupApiService soupApi) {
        mSoupApi = soupApi;
    }

    @Override
    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return mSoupApi.getNowList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<ArtMovieResponse> getArtList(ArtMovieRequest request) {
        return mSoupApi.getArtList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PlanMovieResponse> getPlanList(PlanMovieRequest request) {
        return mSoupApi.getPlanList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<CodeResponse> getCodeList(CodeRequest request) {
        return mSoupApi.getCodeList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<TimeTableResponse> getTimeTableList(TimeTableRequest request) {
        return mSoupApi.getTimeTableList(
                request.getAreaCode(), request.getTheaterCode(), request.getMovieCode())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<TrailerResponse> getTrailerList(TrailerRequest request) {
        return mSoupApi.getTrailerList(request.getMovieCode())
                .subscribeOn(Schedulers.io());
    }
}

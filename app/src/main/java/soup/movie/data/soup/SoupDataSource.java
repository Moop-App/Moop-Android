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
import soup.movie.data.soup.service.SoupApiService;

public class SoupDataSource implements ISoupDataSource {

    private SoupApiService soupApiService;

    public SoupDataSource(SoupApiService soupApi) {
        soupApiService = soupApi;
    }

    @Override
    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return soupApiService.getNowList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<ArtMovieResponse> getArtList(ArtMovieRequest request) {
        return soupApiService.getArtList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PlanMovieResponse> getPlanList(PlanMovieRequest request) {
        return soupApiService.getPlanList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<CodeResponse> getCodeList(CodeRequest request) {
        return soupApiService.getCodeList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<TimeTableResponse> getTimeTableList(TimeTableRequest request) {
        return soupApiService.getTimeTableList(request.getTheaterCode(), request.getMovieCode())
                .subscribeOn(Schedulers.io());
    }
}

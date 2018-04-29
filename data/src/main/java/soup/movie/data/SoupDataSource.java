package soup.movie.data;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import soup.movie.data.model.CodeRequest;
import soup.movie.data.model.CodeResponse;
import soup.movie.data.model.NowMovieRequest;
import soup.movie.data.model.NowMovieResponse;
import soup.movie.data.model.PlanMovieRequest;
import soup.movie.data.model.PlanMovieResponse;
import soup.movie.data.model.TimeTableRequest;
import soup.movie.data.model.TimeTableResponse;
import soup.movie.data.service.SoupApiService;

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

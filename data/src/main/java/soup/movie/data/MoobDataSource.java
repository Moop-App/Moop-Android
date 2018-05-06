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
import soup.movie.data.service.MoobApiService;

public class MoobDataSource implements IMoobDataSource {

    private MoobApiService moobApiService;

    public MoobDataSource(MoobApiService moobApi) {
        moobApiService = moobApi;
    }

    @Override
    public Single<NowMovieResponse> getNowList(NowMovieRequest request) {
        return moobApiService.getNowList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PlanMovieResponse> getPlanList(PlanMovieRequest request) {
        return moobApiService.getPlanList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<CodeResponse> getCodeList(CodeRequest request) {
        return moobApiService.getCodeList()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<TimeTableResponse> getTimeTableList(TimeTableRequest request) {
        return moobApiService.getTimeTableList(request.getTheaterCode(), request.getMovieCode())
                .subscribeOn(Schedulers.io());
    }
}

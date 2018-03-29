package soup.movie.data.soup.service;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import soup.movie.data.soup.model.ArtMovieResponse;
import soup.movie.data.soup.model.CodeResponse;
import soup.movie.data.soup.model.NowMovieResponse;
import soup.movie.data.soup.model.PlanMovieResponse;
import soup.movie.data.soup.model.TimeTableResponse;

public interface SoupApiService {

    String API_BASE_URL = "https://soup-movie.firebaseio.com/";

    // 현재상영작
    @GET("v1/cgv/now.json")
    Single<NowMovieResponse> getNowList();

    // 아트하우스
    @GET("v1/cgv/art.json")
    Single<ArtMovieResponse> getArtList();

    // 개봉예정작
    @GET("v1/cgv/plan.json")
    Single<PlanMovieResponse> getPlanList();

    // 공통코드
    @GET("v1/cgv/code.json")
    Single<CodeResponse> getCodeList();

    // 상영시간표
    @GET("v1/cgv/timetable/asMovie/{ac}/{tc}/{mc}.json")
    Single<TimeTableResponse> getTimeTableList(@Path("ac") String area,
                                               @Path("tc") String theater,
                                               @Path("mc") String movie);

    //TODO: 나중에
    //@GET("v1/cgv/timetable/asMovie/{ac}/{tc}.json")
    //Single<TimeTableResponse> getTimeTableList(@Path("ac") String area,
    //                                           @Path("tc") String theater);
}

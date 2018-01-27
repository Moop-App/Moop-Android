package soup.movie.data.source.remote.service;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import soup.movie.BuildConfig;
import soup.movie.data.MovieListResponse;

// http://www.kobis.or.kr/kobisopenapi/homepg/apiservice/searchServiceInfo.do

public interface KobisApiService {

    String API_BASE_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/";

    String API_KEY = BuildConfig.KOBIS_API_KEY;

    //TODO: 일별 박스오피스
    @GET("boxoffice/searchDailyBoxOfficeList.json")
    Completable getDailyBoxOfficeList();

    //TODO: 주간/주말 박스오피스
    @GET("boxoffice/searchWeeklyBoxOfficeList.json")
    Completable getWeeklyBoxOfficeList();

    //TODO: 공통코드 조회
    @GET("code/searchCodeList.json")
    Completable getCodeList();

    //영화목록
    @GET("movie/searchMovieList.json")
    Single<MovieListResponse> getMovieList(@QueryMap Map<String, String> queryMap);

    //TODO: 영화상세정보
    @GET("movie/searchMovieInfo.json")
    Completable getMovieInfo();

    //TODO: 영화사목록
    @GET("company/searchCompanyList.json")
    Completable getCompanyList();

    //TODO: 영화사 상세정보
    @GET("company/searchCompanyInfo.json")
    Completable getCompanyInfo();

    //TODO: 영화인 목록
    @GET("people/searchPeopleList.json")
    Completable getPeopleList();

    //TODO: 영화인 상세정보
    @GET("people/searchPeopleInfo.json")
    Completable getPeopleInfo();
}

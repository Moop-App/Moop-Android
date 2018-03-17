package soup.movie.data.movist.service;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import soup.movie.BuildConfig;
import soup.movie.data.movist.model.ComingMovieResponse;
import soup.movie.data.movist.model.MovieDetailResponse;
import soup.movie.data.movist.model.NowMovieResponse;

// https://www.datastore.or.kr/product/offererDetail.do?apiGoods=9

public interface MovistApiService {

    String API_BASE_URL = "http://14.49.41.130:8880/movist/";

    String API_KEY = BuildConfig.MOVIST_API_KEY;

    @GET("movies/now")
    Single<NowMovieResponse> getNowMovieList(@QueryMap Map<String, String> queryMap);

    @GET("movies/coming")
    Single<ComingMovieResponse> getComingMovieList(@QueryMap Map<String, String> queryMap);

    @GET("movies/{id}")
    Single<MovieDetailResponse> getMovieDetail(@QueryMap Map<String, String> queryMap);
}

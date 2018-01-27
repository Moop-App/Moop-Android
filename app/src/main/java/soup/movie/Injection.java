package soup.movie;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import soup.movie.data.source.MovieRepository;
import soup.movie.data.source.remote.RemoteDataSource;
import soup.movie.data.source.remote.service.KobisApiService;

public class Injection {

    private final MovieRepository mMovieRepository;

    public Injection() {
        mMovieRepository = provideRecordRepository(
                provideRemoteDataSource(
                        provideServerApiService(
                                provideGsonConverterFactory(),
                                provideRxJava2CallAdapterFactory(),
                                provideOkHttpClient())));
    }

    public MovieRepository getMovieRepository() {
        return mMovieRepository;
    }

    private MovieRepository provideRecordRepository(RemoteDataSource remoteDataSource) {
        return new MovieRepository(remoteDataSource);
    }

    private RemoteDataSource provideRemoteDataSource(KobisApiService kobisApi) {
        return new RemoteDataSource(kobisApi);
    }

    private KobisApiService provideServerApiService(GsonConverterFactory gsonConverterFactory,
                                                     RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                                     OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KobisApiService.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build();
        return retrofit.create(KobisApiService.class);
    }

    private GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    private RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        BuildType.addNetworkInterceptor(builder);
        return builder.build();
    }
}

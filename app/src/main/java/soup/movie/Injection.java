package soup.movie;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import soup.movie.data.MovieRepository;
import soup.movie.data.kobis.KobisDataSource;
import soup.movie.data.kobis.service.KobisApiService;
import soup.movie.data.movist.MovistDataSource;
import soup.movie.data.movist.service.MovistApiService;

public class Injection {

    private final MovieRepository mMovieRepository;

    public Injection() {
        mMovieRepository = provideRepository(
                provideKobisDataSource(
                        provideKobisApiService(
                                provideGsonConverterFactory(),
                                provideRxJava2CallAdapterFactory(),
                                provideOkHttpClient(
                                        null))),
                provideMovistDataSource(
                        provideMovistApiService(
                                provideGsonConverterFactory(),
                                provideRxJava2CallAdapterFactory(),
                                provideOkHttpClient(
                                        provideMovistHeaderInterceptor()))));
    }

    public MovieRepository getMovieRepository() {
        return mMovieRepository;
    }

    // Internal Injection

    private MovieRepository provideRepository(KobisDataSource kobisDataSource,
                                              MovistDataSource movistDataSource) {
        return new MovieRepository(kobisDataSource, movistDataSource);
    }

    private KobisDataSource provideKobisDataSource(KobisApiService kobisApi) {
        return new KobisDataSource(kobisApi);
    }

    private KobisApiService provideKobisApiService(GsonConverterFactory gsonConverterFactory,
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

    private OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        BuildType.addNetworkInterceptor(builder);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    private MovistDataSource provideMovistDataSource(MovistApiService movistApi) {
        return new MovistDataSource(movistApi);
    }

    private MovistApiService provideMovistApiService(GsonConverterFactory gsonConverterFactory,
                                                   RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                                   OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovistApiService.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build();
        return retrofit.create(MovistApiService.class);
    }

    private Interceptor provideMovistHeaderInterceptor() {
        return chain -> chain.proceed(chain.request().newBuilder()
                .addHeader("x-waple-authorization", MovistApiService.API_KEY)
                .build());
    }
}

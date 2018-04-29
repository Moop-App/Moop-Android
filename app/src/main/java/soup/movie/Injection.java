package soup.movie;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import soup.movie.data.MovieRepository;
import soup.movie.data.SoupDataSource;
import soup.movie.data.service.SoupApiService;

public class Injection {

    private interface Singleton {
        Injection INSTANCE = new Injection();
    }

    public static Injection get() {
        return Singleton.INSTANCE;
    }

    private final MovieRepository movieRepository;

    private Injection() {
        movieRepository = provideRepository(
                provideSoupDataSource(
                        provideSoupApiService(
                                provideGsonConverterFactory(),
                                provideRxJava2CallAdapterFactory(),
                                provideOkHttpClient(
                                        provideSoupHeaderInterceptor()))));
    }

    public MovieRepository getMovieRepository() {
        return movieRepository;
    }

    // Internal Injection

    private MovieRepository provideRepository(SoupDataSource soupDataSource) {
        return new MovieRepository(soupDataSource);
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

    private SoupDataSource provideSoupDataSource(SoupApiService soupApi) {
        return new SoupDataSource(soupApi);
    }

    private SoupApiService provideSoupApiService(GsonConverterFactory gsonConverterFactory,
                                                 RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                                 OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SoupApiService.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build();
        return retrofit.create(SoupApiService.class);
    }

    private Interceptor provideSoupHeaderInterceptor() {
        return chain -> chain.proceed(chain.request().newBuilder()
                .build());
    }
}

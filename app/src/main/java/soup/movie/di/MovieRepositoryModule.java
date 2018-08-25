package soup.movie.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import soup.movie.BuildType;
import soup.movie.data.IMoobDataSource;
import soup.movie.data.MoobDataSource;
import soup.movie.data.MoobRepository;
import soup.movie.data.service.MoobApiService;

@Module
public class MovieRepositoryModule {

    @Singleton
    @Provides
    MoobRepository provideMoobRepository(IMoobDataSource remoteDataSource) {
        return new MoobRepository(remoteDataSource);
    }

    @Singleton
    @Provides
    IMoobDataSource provideMoobDataSource(MoobApiService moobApiService) {
        return new MoobDataSource(moobApiService);
    }

    @Singleton
    @Provides
    MoobApiService provideMoobApiService(GsonConverterFactory gsonConverterFactory,
                                         RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                                         OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoobApiService.Companion.getAPI_BASE_URL())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build();
        return retrofit.create(MoobApiService.class);
    }

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    RxJava2CallAdapterFactory provideRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        BuildType.addNetworkInterceptor(builder);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Interceptor provideSoupHeaderInterceptor() {
        return chain -> chain.proceed(chain.request().newBuilder().build());
    }
}

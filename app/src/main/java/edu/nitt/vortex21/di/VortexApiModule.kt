package edu.nitt.vortex21.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import edu.nitt.vortex21.api.AuthApiService
import edu.nitt.vortex21.api.HeaderInterceptor
import edu.nitt.vortex21.api.StoryApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object VortexApiModule {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(context: Context) = HeaderInterceptor(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, interceptor: HeaderInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .callTimeout(10, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        @Named("url") url: String
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit) = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideStoryApiService(retrofit: Retrofit) = retrofit.create(StoryApiService::class.java)
}
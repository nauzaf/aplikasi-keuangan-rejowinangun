package web.id.anproject.rekapkeuangan

import retrofit2.http.GET
import web.id.anproject.rekapkeuangan.model.AnggaranList
import web.id.anproject.rekapkeuangan.model.Dashboard
import web.id.anproject.rekapkeuangan.model.TransaksiList
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface WebService {

    @GET("dashboard")
    fun dashboard(): Observable<Dashboard>

    @GET("anggaran")
    fun anggaran(): Observable<AnggaranList>

    @GET("transaksi")
    fun transaksi(): Observable<TransaksiList>

    companion object Factory {

        // Http Interceptor
        fun getHttpInterCeptor(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            return okHttpClient

        }

        // Http API
        fun getHttpApi(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://andri.anproject.web.id/Api/")
                .client(getHttpInterCeptor())
                .build()
        }

        // Object Service
        fun create(): WebService {
            return getHttpApi().create(WebService::class.java)
        }

    }

}
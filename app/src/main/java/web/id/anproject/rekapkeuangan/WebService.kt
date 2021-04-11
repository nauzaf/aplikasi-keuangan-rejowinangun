package web.id.anproject.rekapkeuangan

import retrofit2.http.GET
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import web.id.anproject.rekapkeuangan.model.*
import java.util.concurrent.TimeUnit

interface WebService {

    @GET("statistic")
    fun statistic(): Observable<Statistic>

    @GET("dashboard")
    fun dashboard(): Observable<Dashboard>

    @GET("anggaran")
    fun anggaran(): Observable<AnggaranList>

    @GET("transaksi")
    fun transaksi(): Observable<TransaksiList>

    @GET("anggaran_bulan/{bulan}")
    fun anggaran(@Path("bulan") bulan: String): Observable<AnggaranList>

    @GET("transaksi_bulan/{bulan}")
    fun transaksi(@Path("bulan") bulan: String): Observable<TransaksiList>

    @GET("tahuns")
    fun tahuns(): Observable<Tahuns>

    @GET("kegiatans")
    fun kegiatans(): Observable<Kegiatans>

    @GET("get_by_id/{id}")
    fun getAnggaranById(@Path("id") id: String): Observable<RAnggaran>

    @GET("rekap/{tahun}/{bulan}")
    fun getRekap(@Path("tahun") tahun: String, @Path("bulan") bulan: String): Observable<RRekap>

    @POST("login")
    fun login(@Body body: BRLogin): Observable<Login>

    @POST("transaksi")
    fun transaksi(@Body body: BRTransaksi): Observable<BRTransaksi>

    @POST("transaksi_update/{id}")
    fun transaksiUpdate(@Path("id") id: String, @Body body: BRTransaksi): Observable<RAnggaran>

    @POST("transaksi_destroy/{id}")
    fun transaksiDelete(@Path("id") id: String): Observable<RDestroy>

    @POST("anggaran")
    fun anggaran(@Body body: BRAnggaran): Observable<RAnggaran>

    @POST("anggaran_update/{id}")
    fun anggaranUpdate(@Path("id") id: String, @Body body: BRAnggaran): Observable<RAnggaran>

    @POST("anggaran_destroy/{id}")
    fun anggaranDelete(@Path("id") id: String): Observable<RDestroy>

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
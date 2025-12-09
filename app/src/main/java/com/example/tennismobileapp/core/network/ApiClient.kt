package com.example.tennismobileapp.core.network

import com.example.tennismobileapp.data.remote.api.TennisCourtApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * object : 싱글톤 패턴 객체
 *
 * - Retrofit, OkHttpClient는 생성 비용이 큼
 * - 앱 내 하나만 필요함
 * - 중복되면, Connection Pool도 중복됨
 */
object ApiClient {

    // 에뮬레이터에서 로컬호스트 접속
    // 컴파일 탐임 상수 -> java의 static final과 유사
    private const val BASE_URL = "http://10.0.2.2:8888/"

    /**
     * 네트워크 디버깅 도구 -> LogCat에 출력
     *
     * - NONE : x
     * - BASIC : 요청/응답 라인만 출력
     * - HEADERS : 헤더까지 출력
     * - BODY : 헤더 + 바디(json) + 요청내용 모두 출력
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 여기서 위에서 만든 loggingInterceptor 사용 by builder 패턴
    // 참고 : 모든 api는 okHttpClient를 통과함
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // 운영에서는 끄는 것이 좋음 -> 보안 + 성능 이슈
        .build()

    // moshi : json <-> kotlin 객체
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Http 클라이언트 구성 by retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    /**
     * TennisCourtApiService 인터페이스를
     * Retrofit이 런타임에서 `실제 구현체`로 만들어 제공
     * lazy 덕분에 처음 사용할 때 딱 한 번만 생성되며 재사용
     */
    val tennisCourtApi: TennisCourtApiService by lazy {
        retrofit.create(TennisCourtApiService::class.java)
    }

}
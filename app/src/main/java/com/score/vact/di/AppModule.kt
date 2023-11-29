package com.score.vact.di


import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.score.vact.api.AppService
import com.score.vact.db.AppDb
import com.score.vact.db.AppointmentDao
import com.score.vact.db.RegistrationFormDao
import com.score.vact.db.SurveyDao
import com.score.vact.vo.ResponseException
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
class AppModule {

    private val BASE_URL = "http://192.168.0.208/vactservice/api/"

    @Singleton
    @Provides
    fun provideAppService(): AppService {

        val requestInterceptor = Interceptor { chain ->

            val url = chain.request()
                .url()
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            Log.d(javaClass.simpleName, "url is $url")
            return@Interceptor chain.proceed(request)

        }

        val responseInterceptor =
            Interceptor { chain ->
                return@Interceptor getResponseInteceptor(chain)
            }


        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(responseInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppService::class.java)
    }

    private fun getResponseInteceptor(chain: Interceptor.Chain): Response? {
        val response = chain.proceed(chain.request())

        when {
            response.code() == 400 -> throw ResponseException(response.code(), "Bad Request")
            response.code() == 401 -> throw ResponseException(
                response.code(),
                "Invalid username or password"
            )
            response.code() == 403 -> throw ResponseException(response.code(), "Forbidden")
            response.code() == 404 -> throw ResponseException(response.code(), "Not Found")
            response.code() == 405 -> throw ResponseException(response.code(), "Method Not Allowed")
            response.code() == 406 -> throw ResponseException(response.code(), "Not Acceptable")
            response.code() == 408 -> throw ResponseException(response.code(), "Request timeout")
            response.code() == 500 -> throw ResponseException(response.code(), "Server Error")
            response.code() == 204 -> throw ResponseException(
                response.code(),
                "No Content exception"
            )
            response.code() != 200 -> throw ResponseException(
                response.code(),
                "Something went wrong"
            )
            else -> return response
        }
    }

    @Singleton
    @Provides
    fun getSharedPreference(application: Application?): SharedPreferences {
        return application!!.applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
    }

    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDb): RegistrationFormDao {
        return db.registrationFormData()
    }

    @Provides
    fun provideAppointmentsDao(db: AppDb): AppointmentDao {
        return db.appointmentDao()
    }

    @Provides
    fun provideSurveyDao(db: AppDb):SurveyDao{
        return db.surveyDao()
    }
}
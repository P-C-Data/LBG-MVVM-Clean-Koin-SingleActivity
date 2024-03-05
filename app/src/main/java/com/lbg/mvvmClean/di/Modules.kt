package com.lbg.mvvmClean.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.lbg.mvvmClean.data.database.LBGDatabase
import com.lbg.mvvmClean.data.repositories.CatDetailsRepositoryImpl
import com.lbg.mvvmClean.data.repositories.CatsRepositoryImpl
import com.lbg.mvvmClean.data.services.CatsService
import com.lbg.mvvmClean.data.services.cats.CatApiServiceHelper
import com.lbg.mvvmClean.data.services.cats.CatApiServiceHelperImpl
import com.lbg.mvvmClean.data.services.cats.CatsDatabaseHelper
import com.lbg.mvvmClean.data.services.cats.CatsDatabaseHelperImpl
import com.lbg.mvvmClean.data.services.catsDetail.CatDetailsApiServiceHelper
import com.lbg.mvvmClean.data.services.catsDetail.CatDetailsApiServiceHelperImpl
import com.lbg.mvvmClean.data.services.catsDetail.CatsDetailsDatabaseHelper
import com.lbg.mvvmClean.data.services.catsDetail.CatsDetailsDatabaseHelperImpl
import com.lbg.mvvmClean.domain.repositories.CatDetailsRepository
import com.lbg.mvvmClean.domain.repositories.CatsRepository
import com.lbg.mvvmClean.domain.usecase.cats.GetCatsUseCase
import com.lbg.mvvmClean.domain.usecase.cats.GetCatsUseCaseImpl
import com.lbg.mvvmClean.domain.usecase.cats.GetFavCatsUseCase
import com.lbg.mvvmClean.domain.usecase.cats.GetFavCatsUseCaseImpl
import com.lbg.mvvmClean.domain.usecase.catsDetail.CheckFavUseCase
import com.lbg.mvvmClean.domain.usecase.catsDetail.CheckFavouriteUseCaseImpl
import com.lbg.mvvmClean.domain.usecase.catsDetail.DeleteFavCatUseCase
import com.lbg.mvvmClean.domain.usecase.catsDetail.DeleteFavCatUseCaseImpl
import com.lbg.mvvmClean.domain.usecase.catsDetail.PostFavCatUseCase
import com.lbg.mvvmClean.domain.usecase.catsDetail.PostFavCatUseCaseImpl
import com.lbg.mvvmClean.network.interceptor.HeaderInterceptor
import com.lbg.mvvmClean.network.interceptor.NetworkConnectionInterceptor
import com.lbg.mvvmClean.presentation.ui.features.SharedViewModel
import com.lbg.mvvmClean.presentation.ui.features.catDetails.viewModel.CatsDetailsViewModel
import com.lbg.mvvmClean.presentation.ui.features.cats.viewModel.CatsViewModel
import com.lbg.mvvmClean.utils.Constants
import com.google.gson.GsonBuilder
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

private val gsonModule = module {
    single { GsonBuilder().create() }
}

private fun getSharedPreferences(androidApplication: Application): SharedPreferences =
    androidApplication.getSharedPreferences(
        Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

private val persistence = module {
    single<EncryptedPreferences> {
        EncryptedPreferences.Builder(get()).withEncryptionPassword(Constants.PREF_PASSWORD).build()
    }
    single {
        getSharedPreferences(androidApplication())
    }
    single<SharedPreferences.Editor> {
        getSharedPreferences(androidApplication()).edit()
    }
}
private val viewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { CatsViewModel(get(), get()) }
    viewModel { CatsDetailsViewModel(get(), get(), get()) }

}
private val serviceHelperModule = module {
    factory<CatApiServiceHelper> { CatApiServiceHelperImpl(get()) }
    factory<CatsDatabaseHelper> { CatsDatabaseHelperImpl(get()) }
    factory<CatDetailsApiServiceHelper> { CatDetailsApiServiceHelperImpl(get()) }
    factory<CatsDetailsDatabaseHelper> { CatsDetailsDatabaseHelperImpl(get()) }
}
private val repoModule = module {
    factory<CatsRepository> { CatsRepositoryImpl(get(), get()) }
    factory<CatDetailsRepository> { CatDetailsRepositoryImpl(get(), get()) }

}

private val useCaseModule = module {
    factory<GetCatsUseCase> { GetCatsUseCaseImpl(get()) }
    factory<GetFavCatsUseCase> { GetFavCatsUseCaseImpl(get()) }
    factory<PostFavCatUseCase> { PostFavCatUseCaseImpl(get()) }
    factory<CheckFavUseCase> { CheckFavouriteUseCaseImpl(get()) }
    factory<DeleteFavCatUseCase> { DeleteFavCatUseCaseImpl(get()) }
}

const val url = "CatUrl"
private val serviceModule = module {
    single { provideOkHttpClient(androidContext()) }
    //Retrofit instances
    single(named(url)) {
        provideCustomRetrofit(
            androidContext(), Constants.baseUrl
        )
    }

    //Service
    single { get<Retrofit>(named(url)).create(CatsService::class.java) }
}

private val dispatchModule = module {
    single(named("io")) { Dispatchers.IO }
    single(named("main")) { Dispatchers.Main }
    single(named("default")) { Dispatchers.Default }
}

private val databaseModule = module {
    single { LBGDatabase.getInstance(androidContext()) }
    single { get<LBGDatabase>().favImageDao() }
}

val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(
        type: Type, annotations: Array<out Annotation>, retrofit: Retrofit
    ) = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter =
            retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

        override fun convert(value: ResponseBody) =
            if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
    }
}

private fun provideCustomRetrofit(context: Context, url: String): Retrofit =
    Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(GsonConverterFactory.create()).baseUrl(url)
        .client(provideOkHttpClient(context)).build()


private fun provideOkHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(HeaderInterceptor())
        .addInterceptor(NetworkConnectionInterceptor(context)).addInterceptor(loggingInterceptor)
        .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS).build()
}

//Add module to allModules for use
val allModules = listOf(
    viewModelModule,
    persistence,
    dispatchModule,
    gsonModule,
    serviceModule,
    serviceHelperModule,
    repoModule,
    useCaseModule,
    databaseModule
)
package com.hexflake.rajutaskevince.di


import android.content.Context
import androidx.room.Room
import com.hexflake.rajutaskevince.conts.APIConstant
import com.hexflake.rajutaskevince.database.AppMainDatabase
import com.hexflake.rajutaskevince.database.dao.UserListDao
import com.hexflake.rajutaskevince.network.ApiMethodsList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
    * @provide Moshi adapter factory to retrofit object
    * */
    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * provide user dao to injencted classes
     * */
    @Provides
    @Singleton
    fun provideUserListDao(db: AppMainDatabase): UserListDao {
        return db.userListDao()
    }


    /**
     * provide retrofit service class in injected files
     * */
    @Provides
    @Singleton
    fun providesAPiService(moshi: Moshi): ApiMethodsList {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(APIConstant.BASEURL)
            .build()
            .create(ApiMethodsList::class.java)
    }

    /**
     * provide room database main object
     * */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppMainDatabase {
        return Room.databaseBuilder(
            context,
            AppMainDatabase::class.java,
            "evince_db"
        ).build()
    }

}
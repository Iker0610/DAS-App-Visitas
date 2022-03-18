package das.omegaterapia.visits.model

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun providesOmegaterapiaVisitsDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            OmegaterapiaVisitsDatabase::class.java,
            "omegaterapia_visits_database"
        ).build()


    @Singleton
    @Provides
    fun provideAuthDao(db: OmegaterapiaVisitsDatabase) = db.authDao()
}
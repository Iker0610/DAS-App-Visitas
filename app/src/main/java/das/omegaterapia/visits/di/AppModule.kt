package das.omegaterapia.visits.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import das.omegaterapia.visits.model.OmegaterapiaVisitsDatabase
import das.omegaterapia.visits.model.dao.AuthenticationDao
import das.omegaterapia.visits.model.dao.VisitsDao
import das.omegaterapia.visits.model.repositories.ILoginRepository
import das.omegaterapia.visits.model.repositories.IVisitsRepository
import das.omegaterapia.visits.model.repositories.LoginRepository
import das.omegaterapia.visits.model.repositories.VisitsRepository
import das.omegaterapia.visits.preferences.ILoginSettings
import das.omegaterapia.visits.preferences.PreferencesRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun providesOmegaterapiaVisitsDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, OmegaterapiaVisitsDatabase::class.java, "omegaterapia_visits_database")
            .createFromAsset("database/omegaterapia_visits_database.db")
            .build()

    @Singleton
    @Provides
    fun provideAuthenticationDao(db: OmegaterapiaVisitsDatabase) = db.authenticationDao()

    @Singleton
    @Provides
    fun provideVisitsDao(db: OmegaterapiaVisitsDatabase) = db.visitsDao()

    @Singleton
    @Provides
    fun provideLoginSettings(@ApplicationContext app: Context): ILoginSettings = PreferencesRepository(app)

    @Singleton
    @Provides
    fun provideLoginRepository(authDao: AuthenticationDao, loginSettings: ILoginSettings): ILoginRepository = LoginRepository(authDao, loginSettings)

    @Singleton
    @Provides
    fun provideVisitsRepository(visitsDao: VisitsDao): IVisitsRepository = VisitsRepository(visitsDao)
}
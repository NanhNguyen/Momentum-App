package com.momentum.app.di

import android.content.Context
import androidx.room.Room
import com.momentum.app.data.local.*
import com.momentum.app.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMomentumDatabase(@ApplicationContext context: Context): MomentumDatabase =
        Room.databaseBuilder(
            context,
            MomentumDatabase::class.java,
            "momentum.db"
        )
            .addMigrations(MomentumDatabase.MIGRATION_2_3, MomentumDatabase.MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db: MomentumDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideHabitDao(db: MomentumDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideHabitLogDao(db: MomentumDatabase): HabitLogDao = db.habitLogDao()

    @Provides
    fun provideReflectionDao(db: MomentumDatabase): ReflectionDao = db.reflectionDao()

    @Provides
    fun provideEntertainmentDao(db: MomentumDatabase): EntertainmentDao = db.entertainmentDao()

    @Provides
    fun provideAppSettingsDao(db: MomentumDatabase): AppSettingsDao = db.appSettingsDao()
}

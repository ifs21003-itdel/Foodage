package com.benha.core.di

import android.content.Context
import androidx.room.Room
import com.benha.core.data.source.local.room.RecipeDao
import com.benha.core.data.source.local.room.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    val passphrase: ByteArray = SQLiteDatabase.getBytes("archico".toCharArray())
    val factory = SupportFactory(passphrase)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipeDatabase =
        Room.databaseBuilder(
            context,
            RecipeDatabase::class.java, "Resep.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao =
        database.recipeDao()
}
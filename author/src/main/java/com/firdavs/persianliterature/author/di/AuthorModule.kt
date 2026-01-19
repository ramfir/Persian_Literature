package com.firdavs.persianliterature.author.di

import androidx.room.Room
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.dao.PoemsDao
import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapper
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapperImpl
import com.firdavs.persianliterature.author.db.migration.MIGRATION_1_2
import com.firdavs.persianliterature.author.repository.AuthorRepositoryImpl
import com.firdavs.persianliterature.author.repository.FavouritesRepositoryImpl
import com.firdavs.persianliterature.author.repository.PoemRepositoryImpl
import com.firdavs.persianliterature.author.repository.WorksRepositoryImpl
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorModule = module {
    single<AuthorRepository> {
        AuthorRepositoryImpl(
            authorsDao = get(),
            authorsEntityToDomainMapper = get(),
            languageManager = get(),
            context = androidContext()
        )
    }
    single<WorksRepository> {
        WorksRepositoryImpl(
            worksDao = get(),
            languageManager = get(),
            context = androidContext()
        )
    }
    singleOf(::FavouritesRepositoryImpl) bind FavouritesRepository::class
    single<AuthorsDb> {
        Room.databaseBuilder(
            androidContext(),
            AuthorsDb::class.java,
            AuthorsDb.DATABASE_NAME
        ).addMigrations(MIGRATION_1_2)
            .build()
    }
    single<AuthorsDao> { get<AuthorsDb>().getAuthorsDao() }
    single<WorksDao> { get<AuthorsDb>().getWorksDao() }
    single<PoemsDao> { get<AuthorsDb>().getPoemsDao() }
    single<PoemRepository> {
        PoemRepositoryImpl(
            poemsDao = get(),
            languageManager = get(),
            context = androidContext()
        )
    }
    factory<AuthorsEntityToDomainMapper> { AuthorsEntityToDomainMapperImpl() }
}

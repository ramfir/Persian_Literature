package com.firdavs.persianliterature.author.di

import androidx.room.Room
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapper
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapperImpl
import com.firdavs.persianliterature.author.repository.AuthorRepositoryImpl
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorModule = module {
    singleOf(::AuthorRepositoryImpl) bind AuthorRepository::class
    single<AuthorsDb> {
        Room.databaseBuilder(
            androidContext(),
            AuthorsDb::class.java,
            AuthorsDb.DATABASE_NAME
        ).build()
    }
    single<AuthorsDao> { get<AuthorsDb>().getDao() }
    factory<AuthorsEntityToDomainMapper> { AuthorsEntityToDomainMapperImpl() }
}

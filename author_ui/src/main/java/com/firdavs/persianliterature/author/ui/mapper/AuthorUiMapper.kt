package com.firdavs.persianliterature.author.ui.mapper

import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.core.mapper.Mapper

interface AuthorUiMapper : Mapper<List<Author>, List<AuthorUiModel>>

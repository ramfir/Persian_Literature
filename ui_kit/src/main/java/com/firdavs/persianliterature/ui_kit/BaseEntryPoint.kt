package com.firdavs.persianliterature.ui_kit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.core.presentation.UiState
import org.koin.compose.currentKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras
import org.koin.viewmodel.resolveViewModel
import kotlin.reflect.KClass

@Composable
fun <S : UiState, VM : BaseViewModel<S>> BaseEntryPoint(
    vmClass: KClass<VM>,
    vararg viewModelParams: Any?,
    content: @Composable (S, VM) -> Unit
) {
    val viewModel = getKoinViewModel(vmClass) { parametersOf(viewModelParams) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onViewResumed()
    }
    content(state, viewModel)
}

@OptIn(KoinInternalApi::class)
@Composable
fun <VM : BaseViewModel<*>> getKoinViewModel(
    clazz: KClass<VM>,
    qualifier: Qualifier? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    extras: CreationExtras = defaultExtras(viewModelStoreOwner),
    scope: Scope = currentKoinScope(),
    parameters: ParametersDefinition? = null
): VM {
    return resolveViewModel(
        vmClass = clazz,
        viewModelStore = viewModelStoreOwner.viewModelStore,
        key = key,
        extras = extras,
        qualifier = qualifier,
        scope = scope,
        parameters = parameters
    )
}

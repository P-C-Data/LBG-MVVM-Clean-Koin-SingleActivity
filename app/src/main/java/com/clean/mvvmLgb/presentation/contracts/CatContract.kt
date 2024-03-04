package com.clean.mvvmLgb.presentation.contracts

import com.clean.mvvmLgb.domain.mappers.CatDataModel

class CatContract {
    data class State(
        val cats: List<CatDataModel> = listOf(),
        val favCatsList: List<CatDataModel> = listOf(),
        val isLoading: Boolean = false
    )
}

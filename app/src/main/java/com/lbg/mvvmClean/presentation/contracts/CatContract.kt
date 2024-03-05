package com.lbg.mvvmClean.presentation.contracts

import com.lbg.mvvmClean.domain.mappers.CatDataModel

class CatContract {
    data class State(
        val cats: List<CatDataModel> = listOf(),
        val favCatsList: List<CatDataModel> = listOf(),
        val isLoading: Boolean = false
    )
}

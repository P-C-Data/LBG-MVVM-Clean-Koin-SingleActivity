package com.lbg.mvvmClean.presentation.contracts


import com.lbg.mvvmClean.domain.mappers.CallSuccessModel


class CatDetailsContract {
    data class State(
        val postFavCatSuccess: CallSuccessModel?,
        val deleteFavCatSuccess: CallSuccessModel?
    )

}
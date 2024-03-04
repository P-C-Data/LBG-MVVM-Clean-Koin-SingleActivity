package com.clean.mvvmLgb.presentation.contracts


import com.clean.mvvmLgb.domain.mappers.CallSuccessModel


class CatDetailsContract {
    data class State(
        val postFavCatSuccess: CallSuccessModel?,
        val deleteFavCatSuccess: CallSuccessModel?
    )

}
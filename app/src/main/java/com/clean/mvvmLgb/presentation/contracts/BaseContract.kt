package com.clean.mvvmLgb.presentation.contracts

open class BaseContract {
    sealed class Effect {
        data object DataWasLoaded : Effect()
        data class Error(val errorMessage: String) : Effect()

    }
}
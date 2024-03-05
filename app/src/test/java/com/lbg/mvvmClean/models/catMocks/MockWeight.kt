package com.lbg.mvvmClean.models.catMocks

import com.lbg.mvvmClean.data.models.catData.BreedWeight


data class MockWeight(
    val imperial: String = "23",
    val metric: String = "25"
)

fun toResponseCatBreedWeight(mockWeight: MockWeight): BreedWeight {
    return BreedWeight(mockWeight.imperial, mockWeight.metric)
}
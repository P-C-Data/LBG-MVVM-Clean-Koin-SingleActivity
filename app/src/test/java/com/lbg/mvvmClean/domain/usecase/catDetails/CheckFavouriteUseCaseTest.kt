package com.lbg.mvvmClean.domain.usecase.catDetails

import com.lbg.mvvmClean.domain.repositories.CatDetailsRepository
import com.lbg.mvvmClean.domain.usecase.catsDetail.CheckFavouriteUseCaseImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CheckFavouriteUseCaseTest {
    @Mock
    private lateinit var catDetailsRepo: CatDetailsRepository

    private lateinit var checkFavouriteUseCase: CheckFavouriteUseCaseImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        checkFavouriteUseCase = CheckFavouriteUseCaseImpl(catDetailsRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit correct value from repository`() =
        runTest(UnconfinedTestDispatcher()) {
            // Given
            val imageId = "123"
            val expectedResult = 1

            Mockito.`when`(catDetailsRepo.fetchIsFavouriteRelation(imageId))
                .thenReturn(expectedResult)

            // When
            val resultFlow: Flow<Int?> = checkFavouriteUseCase.execute(imageId)

            // Then
            resultFlow.collect { result ->
                assertEquals(expectedResult, result)
            }
        }


}

package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

@Suppress("InjectDispatcher")
interface FlowUseCase<in Input, Output> {
    /**
     * Executes the flow on Dispatchers.IO and wraps exceptions inside it into Result
     */
    operator fun invoke(param: Input): Flow<Output> =
        execute(param).flowOn(Dispatchers.IO)

    fun execute(param: Input): Flow<Output>
}
package com.examwarriors.notifiers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class Notifier(val viewModelScope: CoroutineScope) {

    // private val channel = BroadcastChannel<Notify>(Channel.BUFFERED)
    // private val subscription = channel.openSubscription()

    private val flow = MutableStateFlow<Notify>(Notify())

    fun notify(event: Notify, coroutineContext: CoroutineContext = Dispatchers.IO) {
        viewModelScope.launch(coroutineContext) {
            // channel.send(event)
            flow.emit(event)
        }
    }

    fun notify(event: Notify, coroutineContext: CoroutineContext = Dispatchers.IO, delayMillis: Long) {
        viewModelScope.launch(coroutineContext) {
            delay(delayMillis)
            // channel.send(event)
            flow.emit(event)
        }
    }

    fun receive(coroutineContext: CoroutineContext = Dispatchers.Main, callback: (event: Notify) -> Unit) {
        viewModelScope.launch(coroutineContext) {
            /*subscription.consumeEach { event ->
                callback(event)
            }*/
            flow.collect { event ->
                callback(event)
            }
        }
    }

    fun close() {
        // subscription.cancel()
        // channel.cancel()
    }
}

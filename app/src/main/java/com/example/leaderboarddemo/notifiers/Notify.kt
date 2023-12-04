package com.examwarriors.notifiers

open class Notify(val identifier: String = "", vararg val arguments: Any?)
class Loader(val loading: Boolean) : Notify()
class NotifyException(val exception: Exception) : Notify()
class NotifyRetry(val call: () -> Unit) : Notify()

object NotifierIdentifier {
    const val FINISH_ACTIVITY = "FINISH_ACTIVITY"
}

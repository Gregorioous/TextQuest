package com.example.textquest

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class MainViewModel(
    private val communication: Communication.Mutable,
    private val repository: Repository
) : Communication.Observe, ActionCallback {

    private val mapper = ScreenDataToUi(this)

    init {
        moveToScreen("1")
    }
    override fun moveToScreen(id: String) {
        val screenData = repository.nextScreen(id)
        val screenUi = mapper.map(screenData)
        communication.map(screenUi)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ScreenUi>) =
        communication.observe(owner, observer)
}

interface Mapper<S, R> {
    fun map(data: S): R
}

class ScreenDataToUi(private val actionCallback: ActionCallback) : Mapper<ScreenData, ScreenUi> {
    override fun map(data: ScreenData): ScreenUi {
        val actions = data.actionsList.map { actionData ->
            ActionUi(actionCallback, actionData.screenId, actionData.text)
        }
        return ScreenUi(data.text, actions)
    }
}

interface Communication {

    interface Observe {
        fun observe(owner: LifecycleOwner, observer: Observer<ScreenUi>)
    }

    interface SetValue : Mapper<ScreenUi, Unit>

    interface Mutable : Observe, SetValue

    class Base : Mutable {
        private val liveData = MutableLiveData<ScreenUi>()

        override fun map(data: ScreenUi) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<ScreenUi>) =
            liveData.observe(owner, observer)
    }
}
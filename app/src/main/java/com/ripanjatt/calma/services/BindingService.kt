package com.ripanjatt.calma.services

object BindingService {

    private var changeListener: ChangeListener? = null

    fun setChangeListener(changeListener: ChangeListener) {
        this.changeListener = changeListener
    }

    fun received(action: String) {
        changeListener?.onReceive(action)
    }

    interface ChangeListener {
        fun onReceive(action: String)
    }
}
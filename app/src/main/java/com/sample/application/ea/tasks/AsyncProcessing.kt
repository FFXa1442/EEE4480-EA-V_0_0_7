package com.sample.application.ea.tasks

import android.os.Handler
import android.os.Looper
import androidx.core.util.Consumer
import java.util.concurrent.atomic.AtomicBoolean

class AsyncProcessing private constructor() {

    private val steps = HashSet<Step>()

    private val delayMillis = 10L

    private val handler = Handler(Looper.getMainLooper())

    private val isAllFinish: Boolean
        get() {
            for (step in steps)
                if (!step.state.get())
                    return false
            return true
        }

    fun addStep(consumer: Consumer<State>): AsyncProcessing {
        steps.add(Step(consumer))
        return this
    }

    fun run(finish: Runnable) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isAllFinish) {
                    handler.removeCallbacks(this)
                    finish.run()
                } else {
                    handler.postDelayed(this, delayMillis)
                }
            }
        }, delayMillis)

        for (step in steps)
            step.run()
    }

    private class Step(
        private val consumer: Consumer<State>,
    ) {
        private val id = System.identityHashCode(consumer)

        val state: State = State()

        fun run() = consumer.accept(state)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val step = other as Step
            return id == step.id
        }

        override fun hashCode() = id
    }

    class State(
        private val core: AtomicBoolean = AtomicBoolean(false),
    ) {
        fun setFinish() = core.set(true)
        fun get() = core.get()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AsyncProcessing()
    }
}
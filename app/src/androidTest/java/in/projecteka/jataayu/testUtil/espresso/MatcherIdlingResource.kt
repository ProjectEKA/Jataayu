package `in`.projecteka.jataayu.testUtil.espresso

import android.view.View
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import org.hamcrest.Matcher
import java.lang.Thread.sleep

class MatcherIdlingResource(private val waitTimeInMillis: Int, private val viewMatcher: Matcher<in View?>?, val view: View?) :
    IdlingResource {
    private var resourceCallback: ResourceCallback? = null
    private var startTime: Long = 0
    private var thread: Thread? = null
    private var isIdleNowCallCount = 0
    private var matched = false

    override fun getName(): String? = this.javaClass.name

    override fun isIdleNow(): Boolean {
            val timeElapsed = now() - startTime >= waitTimeInMillis
            if (isMatched() || timeElapsed) {
                resourceCallback?.onTransitionToIdle()
            }
            isIdleNowCallCount++
            return isMatched() || timeElapsed
        }

    override fun registerIdleTransitionCallback(resourceCallback: ResourceCallback?) {
        startTime = now()
        this.resourceCallback = resourceCallback
        pollViewUntilMatchesOrTimeout(resourceCallback)
    }

    private fun pollViewUntilMatchesOrTimeout(resourceCallback: ResourceCallback?) {
        thread = Thread(Runnable {
            while (!viewMatcher?.matches(view)!! && (now() - startTime < waitTimeInMillis)) {
                try {
                    sleep(50)
                } catch (ignored: InterruptedException) {
                }
            }
            matched = viewMatcher.matches(view)
            resourceCallback?.onTransitionToIdle()
        })
        thread!!.start()
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }

    fun isMatched(): Boolean {
        return matched
    }
}
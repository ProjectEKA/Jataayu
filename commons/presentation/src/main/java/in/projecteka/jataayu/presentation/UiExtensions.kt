package `in`.projecteka.jataayu.presentation

import android.view.View
import android.view.animation.AnimationUtils

fun View.wobble() {
    val loadAnimation = AnimationUtils.loadAnimation(context, R.anim.wobble)
    this.animation = loadAnimation
    loadAnimation.startNow()
}

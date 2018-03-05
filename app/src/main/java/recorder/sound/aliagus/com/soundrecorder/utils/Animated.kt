package recorder.sound.aliagus.com.soundrecorder.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

/**
 * Created by ali on 26/02/18.
 */
class Animated {
    companion object {
        const val VISIBLE = 1
        fun animatedView(view: View, visibility: Int) {
            val alpha: Float
            val y: Float
            if (visibility == VISIBLE) {
                alpha = 1.0f
                y = 0f
            } else {
                alpha = 0.0f
                y = view.height.toFloat()
            }
            view.animate()
                    .translationY(y)
                    .alpha(alpha)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            if (visibility == VISIBLE) {
                                view.visibility = View.VISIBLE
                            } else {
                                view.visibility = View.GONE
                            }
                        }
                    })
        }
    }



    fun animatedView(view: View) {
        view.animate()
                .translationY(view.height.toFloat())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        view.visibility = View.GONE
                    }
                })
    }
}

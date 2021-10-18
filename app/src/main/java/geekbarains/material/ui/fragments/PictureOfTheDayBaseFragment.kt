package geekbarains.material.ui.fragments

import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import geekbarains.material.util.EquilateralImageView

open class PictureOfTheDayBaseFragment: Fragment() {
    private var isExpandedPicture = false

    protected fun changePictureSize(imageViewContainer : ViewGroup,imageView : EquilateralImageView){
        isExpandedPicture = !isExpandedPicture

        TransitionManager.beginDelayedTransition(
            imageViewContainer, TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
        )

        val params: ViewGroup.LayoutParams = imageView.layoutParams
        params.height =
            if (isExpandedPicture) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        imageView.layoutParams = params
        imageView.scaleType =
            if (isExpandedPicture) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
}
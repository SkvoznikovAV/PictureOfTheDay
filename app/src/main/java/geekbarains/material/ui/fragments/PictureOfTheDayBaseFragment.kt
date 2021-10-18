package geekbarains.material.ui.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import geekbarains.material.R
import geekbarains.material.ui.entities.PictureOfTheDayData
import geekbarains.material.util.EquilateralImageView
import kotlinx.android.synthetic.main.picture_of_the_day_description.*

open class PictureOfTheDayBaseFragment: Fragment() {
    private var isExpandedPicture = false

    private fun changePictureSize(imageViewContainer: ViewGroup, imageView: EquilateralImageView){
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

    private fun getDescriptionText(explanation: String,copyRight: String) : SpannableString {
        val copyRightLoc = copyRight.replace("\n","")

        val prefExplanation="Explanation:"
        val prefCopyRight="Image Credit & Copyright:"
        val descriptionText = SpannableString("$prefExplanation $explanation\n$prefCopyRight $copyRightLoc")
        descriptionText.setSpan(StyleSpan(Typeface.BOLD),0,prefExplanation.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        descriptionText.setSpan(ForegroundColorSpan(Color.BLUE),0,prefExplanation.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        descriptionText.setSpan(StyleSpan(Typeface.BOLD),0,prefExplanation.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        descriptionText.setSpan(ForegroundColorSpan(Color.BLUE),0,prefExplanation.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        descriptionText.setSpan(StyleSpan(Typeface.BOLD),
            prefExplanation.length+explanation.length+2,
            prefExplanation.length+explanation.length+prefCopyRight.length+2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        descriptionText.setSpan(ForegroundColorSpan(Color.BLUE),
            prefExplanation.length+explanation.length+2,
            prefExplanation.length+explanation.length+prefCopyRight.length+2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return descriptionText
    }

    open fun renderData(data: PictureOfTheDayData,
                             pictureOfTheDayLoadingLayout: View,
                             pictureOfTheDayErrorLayout: View,
                             pictureOfTheDayLayout: View,
                             pictureOfTheDayDescriptionContainer: View,
                             imageView: EquilateralImageView,
                             pictureMotionLayout: ViewGroup,
                             owner: LifecycleOwner?,
                             txtError: TextView) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                pictureOfTheDayLoadingLayout.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.GONE
                pictureOfTheDayLayout.visibility = View.VISIBLE
                pictureOfTheDayDescriptionContainer.visibility = View.VISIBLE

                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (!url.isNullOrEmpty()) {
                    imageView.load(url) {
                        lifecycle(owner)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = getDescriptionText(serverResponseData.explanation?:"",serverResponseData.copyright?:"")

                    imageView.setOnClickListener {
                        changePictureSize(pictureMotionLayout, imageView)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                pictureOfTheDayLayout.visibility = View.GONE
                pictureOfTheDayDescriptionContainer.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.GONE
                pictureOfTheDayLoadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                pictureOfTheDayLayout.visibility = View.GONE
                pictureOfTheDayDescriptionContainer.visibility = View.GONE
                pictureOfTheDayLoadingLayout.visibility = View.GONE
                pictureOfTheDayErrorLayout.visibility = View.VISIBLE

                txtError.text= String.format("%s %s",getString(R.string.str_prefix_error),data.error.message)
            }
        }
    }
}
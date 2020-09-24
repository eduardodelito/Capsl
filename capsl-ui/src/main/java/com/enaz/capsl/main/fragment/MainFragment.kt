package com.enaz.capsl.main.fragment

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.MainFragmentBinding
import com.enaz.capsl.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : RtcBaseFragment<MainFragmentBinding, MainViewModel>() {

    private var listener: MainFragmentListener? = null

    private val TAG = LiveStreamFragment::class.java.simpleName

    companion object {
        private const val MIN_INPUT_METHOD_HEIGHT = 200
        private const val ANIM_DURATION = 200

        // Permission request code of any integer value
        private const val PERMISSION_REQ_CODE = 1 shl 4

        fun newInstance() = MainFragment()
    }

    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val mVisibleRect = Rect()
    private var mLastVisibleHeight = 0
    private var mBodyDefaultMarginTop = 0

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun createLayout() = R.layout.main_fragment

    override fun getBindingVariable() = BR.mainViewModel

    private val mLogoAnimListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
            // Do nothing
        }

        override fun onAnimationEnd(animator: Animator) {
            main_logo.visibility = View.VISIBLE
        }

        override fun onAnimationCancel(animator: Animator) {
            main_logo.visibility = View.VISIBLE
        }

        override fun onAnimationRepeat(animator: Animator) {
            // Do nothing
        }
    }

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            // Do nothing
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            // Do nothing
        }

        override fun afterTextChanged(editable: Editable) {
            start_broadcast_button.isEnabled = !TextUtils.isEmpty(editable)
        }
    }

    private val mLayoutObserverListener =
        ViewTreeObserver.OnGlobalLayoutListener { checkInputMethodWindowState() }

    private fun checkInputMethodWindowState() {
        activity?.window?.decorView?.rootView?.getWindowVisibleDisplayFrame(mVisibleRect)
        val visibleHeight = mVisibleRect.bottom - mVisibleRect.top
        if (visibleHeight == mLastVisibleHeight) return
        val inputShown: Boolean =
            mDisplayMetrics.heightPixels - visibleHeight > MIN_INPUT_METHOD_HEIGHT
        mLastVisibleHeight = visibleHeight

        // Log.i(TAG, "onGlobalLayout:" + inputShown +
        //        "|" + getWindow().getDecorView().getRootView().getViewTreeObserver());

        // There is no official way to determine whether the
        // input method dialog has already shown.
        // This is a workaround, and if the visible content
        // height is significantly less than the screen height,
        // we should know that the input method dialog takes
        // up some screen space.
        if (inputShown) {
            if (main_logo.visibility == View.VISIBLE) {
                main_logo.measuredHeight.toFloat().let {
                    middle_layout.animate()?.translationYBy(-it)
                        ?.setDuration(ANIM_DURATION.toLong())?.setListener(null)?.start()
                }
                main_logo.visibility = View.INVISIBLE
            }
        } else if (main_logo.visibility != View.VISIBLE) {
            main_logo.measuredHeight.toFloat().let {
                middle_layout.animate()?.translationYBy(it)
                    ?.setDuration(ANIM_DURATION.toLong())?.setListener(mLogoAnimListener)
                    ?.start()
            }
        }
    }

    override fun initViews() {
        start_broadcast_button.setOnClickListener { checkPermission() }
        topic_edit.addTextChangedListener(mTextWatcher)

        if (TextUtils.isEmpty(topic_edit.text)) start_broadcast_button.isEnabled = false
    }

    override fun subscribeUi() {
    }

    //    @Override
//    protected void onGlobalLayoutCompleted() {
//        adjustViewPositions();
//    }

//    private fun adjustViewPositions() {
//        // Setting btn move downward away the status bar
//        var param = setting_button.layoutParams as RelativeLayout.LayoutParams
//        param.topMargin += mStatusBarHeight
//        setting_button.layoutParams = param
//
//        // Logo is 0.48 times the screen width
//        param = main_logo.layoutParams as RelativeLayout.LayoutParams
//        val size = (mDisplayMetrics.widthPixels * 0.48) as Int
//        param.width = size
//        param.height = size
//        main_logo.layoutParams = param
//
//        // Bottom margin of the main body should be two times it's top margin.
//        param = middle_layout.layoutParams as RelativeLayout.LayoutParams
//        param.topMargin = (mDisplayMetrics.heightPixels -
//                middle_layout.measuredHeight - mStatusBarHeight) / 3
//        middle_layout.layoutParams = param
//        mBodyDefaultMarginTop = param.topMargin
//
//        // The width of the start button is roughly 0.72
//        // times the width of the screen
//        param = start_broadcast_button.layoutParams as RelativeLayout.LayoutParams
//        param.width = (mDisplayMetrics.widthPixels * 0.72).toInt()
//        start_broadcast_button.layoutParams = param
//    }

//    fun onSettingClicked(view: View) {
//
//    }

    private fun checkPermission() {
        var granted = true
        for (per in permissions) {
            if (!permissionGranted(per)) {
                granted = false
                break
            }
        }
        if (granted) {
            resetLayoutAndForward()
        } else {
            requestPermissions()
        }
    }

    private fun permissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQ_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQ_CODE) {
            var granted = true
            for (result in grantResults) {
                granted = result == PackageManager.PERMISSION_GRANTED
                if (!granted) break
            }
            if (granted) {
                resetLayoutAndForward()
            } else {
                toastNeedPermissions()
            }
        }
    }

    private fun resetLayoutAndForward() {
        closeImeDialogIfNeeded()
        gotoRoleActivity()
    }

    private fun closeImeDialogIfNeeded() {
        val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            topic_edit.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun gotoRoleActivity() {
//        Intent intent = new Intent(MainActivity.this, RoleActivity.class);
//        String room = mTopicEdit.getText().toString();
//        config().setChannelName(room);
//        startActivity(intent);
        listener?.navigateToRoleScreen(requireView())
    }

    private fun toastNeedPermissions() {
        Toast.makeText(context, R.string.need_necessary_permissions, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        resetUI()
        registerLayoutObserverForSoftKeyboard()
    }

    private fun resetUI() {
        resetLogo()
        closeImeDialogIfNeeded()
    }

    private fun resetLogo() {
        main_logo.visibility = View.VISIBLE
        middle_layout.y = mBodyDefaultMarginTop.toFloat()
    }

    private fun registerLayoutObserverForSoftKeyboard() {
        val view: View? = activity?.window?.decorView?.rootView
        val observer = view?.viewTreeObserver
        observer?.addOnGlobalLayoutListener(mLayoutObserverListener)
    }

    override fun onPause() {
        super.onPause()
        removeLayoutObserverForSoftKeyboard()
    }

    private fun removeLayoutObserverForSoftKeyboard() {
        val view: View? = activity?.window?.decorView?.rootView
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(mLayoutObserverListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface MainFragmentListener {
        fun navigateToRoleScreen(view: View)
    }
}

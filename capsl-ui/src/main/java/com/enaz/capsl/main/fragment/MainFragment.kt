package com.enaz.capsl.main.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.common.util.afterTextChanged
import com.enaz.capsl.common.util.hideKeyboard
import com.enaz.capsl.common.util.reObserve
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.MainFragmentBinding
import com.enaz.capsl.main.model.MainFormState
import com.enaz.capsl.main.model.MainLogoForm
import com.enaz.capsl.main.model.TextWatcherForm
import com.enaz.capsl.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : RtcBaseFragment<MainFragmentBinding, MainViewModel>() {

    private var listener: MainFragmentListener? = null

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

    private val mLayoutObserverListener = ViewTreeObserver.OnGlobalLayoutListener { checkInputMethodWindowState() }

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
                    ?.setDuration(ANIM_DURATION.toLong())?.setListener(viewModel.mLogoAnimListener)
                    ?.start()
            }
        }
    }

    override fun initViews() {
        start_broadcast_button.setOnClickListener { checkPermission() }
        topic_edit.afterTextChanged {
            viewModel.topicEditTextChanged(it)
        }

        if (TextUtils.isEmpty(topic_edit.text)) start_broadcast_button.isEnabled = false
    }

    override fun subscribeUi() {
        with(viewModel) {
            reObserve(getMainLiveData(), ::onMainStateChanged)
        }
    }

    private fun onMainStateChanged(state: MainFormState?) {
        when(state) {
            is TextWatcherForm -> start_broadcast_button.isEnabled = state.isEnabled
            is MainLogoForm -> main_logo.visibility = state.mainLogoVisible
        }
    }

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
        hideKeyboard()
        gotoRoleActivity()
    }

    private fun gotoRoleActivity() {
        mGlobalConfig.setChannelName(topic_edit.text.toString())
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
        hideKeyboard()
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

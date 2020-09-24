package com.enaz.capsl.common.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.enaz.capsl.common.R
import com.enaz.capsl.common.viewmodel.BaseViewModel
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import io.agora.rtc.video.VideoEncoderConfiguration.VideoDimensions
import java.lang.reflect.ParameterizedType

/**
 * Created by eduardo.delito on 9/24/20.
 */
abstract class RtcBaseFragment<T : ViewDataBinding, V : BaseViewModel> : BaseFragment() {
    private lateinit var viewDataBinding: T

    protected abstract val viewModel: V

    /**
     * Abstract function to set fragment layout
     *
     * @return the layout id
     */
    abstract fun createLayout(): Int

    /**
     * Abstract function to set binding variable
     *
     * @return the binding variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * Abstract function to initialize views
     */
    abstract fun initViews()

    /**
     * Abstract function to subscribe to live data of view model
     */
    abstract fun subscribeUi()

    /**
     * Function to get the data binding of the current fragment
     *
     * @return the data binding of the current fragment
     */
    fun getBinding() = viewDataBinding

    /**
     * Function to get the viewModel class
     *
     * @return the viewModel class
     */
    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<V>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        subscribeUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (::viewDataBinding.isInitialized.not()) {
            viewDataBinding = DataBindingUtil.inflate(inflater, createLayout(), container, false)
            viewDataBinding.apply {
                setVariable(getBindingVariable(), viewModel)
                lifecycleOwner = viewLifecycleOwner
                executePendingBindings()
            }
        }
        return viewDataBinding.root
    }

    fun initLiveStream() {
        registerRtcEventHandler(this)
        configVideo()
        joinChannel()
    }

    private fun configVideo() {
        val configuration = VideoEncoderConfiguration(
            VIDEO_DIMENSIONS.get(mGlobalConfig.getVideoDimenIndex()),
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        )
        configuration.mirrorMode = VIDEO_MIRROR_MODES[mGlobalConfig.getMirrorEncodeIndex()]
        mRtcEngine.setVideoEncoderConfiguration(configuration)
    }

    private fun joinChannel() {
        // Initialize token, extra info here before joining channel
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name and uid that
        // you use to generate this token.
        var token: String? = getString(R.string.agora_access_token)
        if (TextUtils.isEmpty(token)) {
            token = null // default, no token
        }
        mRtcEngine.joinChannel(token, mGlobalConfig.getChannelName(), "", 0)
    }

    protected open fun prepareRtcVideo(uid: Int, local: Boolean): SurfaceView? {
        // Render local/remote video on a SurfaceView
        val surface = RtcEngine.CreateRendererView(requireContext())
        if (local) {
            mRtcEngine.setupLocalVideo(
                VideoCanvas(
                    surface,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    0,
                    VIDEO_MIRROR_MODES[mGlobalConfig.getMirrorLocalIndex()]
                )
            )
        } else {
            mRtcEngine.setupRemoteVideo(
                VideoCanvas(
                    surface,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    uid,
                    VIDEO_MIRROR_MODES[mGlobalConfig.getMirrorRemoteIndex()]
                )
            )
        }
        return surface
    }

    protected open fun removeRtcVideo(uid: Int, local: Boolean) {
        if (local) {
            mRtcEngine.setupLocalVideo(null)
        } else {
            mRtcEngine.setupRemoteVideo(VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        }
    }

    fun destroy() {
        removeRtcEventHandler(this)
        mRtcEngine.leaveChannel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    companion object {
        var VIDEO_DIMENSIONS = arrayOf(
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_480x360,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            VideoDimensions(960, 540),
            VideoEncoderConfiguration.VD_1280x720
        )

        var VIDEO_MIRROR_MODES = intArrayOf(
            Constants.VIDEO_MIRROR_MODE_AUTO,
            Constants.VIDEO_MIRROR_MODE_ENABLED,
            Constants.VIDEO_MIRROR_MODE_DISABLED
        )
    }
}
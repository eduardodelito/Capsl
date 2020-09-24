package com.enaz.capsl.main.fragment

import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.LiveStreamFragmentBinding
import com.enaz.capsl.main.viewmodel.LiveStreamViewModel
import javax.inject.Inject

class LiveStreamFragment : RtcBaseFragment<LiveStreamFragmentBinding, LiveStreamViewModel>() {

    companion object {
        fun newInstance() = LiveStreamFragment()
    }

    @Inject
    override lateinit var viewModel: LiveStreamViewModel

    override fun createLayout() = R.layout.live_stream_fragment

    override fun getBindingVariable() = BR.liveStreamViewModel

    override fun initViews() {
        initLiveStream()
    }

    override fun subscribeUi() {
        
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }
}
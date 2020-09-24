package com.enaz.capsl.main.fragment

import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.SupportFragmentBinding
import com.enaz.capsl.main.viewmodel.SupportViewModel
import javax.inject.Inject

class SupportFragment : RtcBaseFragment<SupportFragmentBinding, SupportViewModel>() {

    companion object {
        fun newInstance() = SupportFragment()
    }

    @Inject
    override lateinit var viewModel: SupportViewModel

    override fun createLayout() = R.layout.support_fragment

    override fun getBindingVariable() = BR.supportViewModel

    override fun initViews() {
    }

    override fun subscribeUi() {
    }
}
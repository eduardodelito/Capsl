package com.enaz.capsl.main.fragment

import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.SettingsFragmentBinding
import com.enaz.capsl.main.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : RtcBaseFragment<SettingsFragmentBinding, SettingsViewModel>() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @Inject
    override lateinit var viewModel: SettingsViewModel

    override fun createLayout() = R.layout.settings_fragment

    override fun getBindingVariable() = BR.settingsViewModel

    override fun initViews() {
    }

    override fun subscribeUi() {
    }
}

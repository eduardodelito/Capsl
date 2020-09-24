package com.enaz.capsl.main.fragment

import android.content.Context
import android.view.View
import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.RoleFragmentBinding
import com.enaz.capsl.main.viewmodel.RoleViewModel
import io.agora.rtc.Constants
import kotlinx.android.synthetic.main.role_fragment.*
import javax.inject.Inject

class RoleFragment : RtcBaseFragment<RoleFragmentBinding, RoleViewModel>() {

    private var listener: RoleFragmentListener? = null

    companion object {
        fun newInstance() = RoleFragment()
    }

    @Inject
    override lateinit var viewModel: RoleViewModel

    override fun createLayout() = R.layout.role_fragment

    override fun getBindingVariable() = BR.roleViewModel

    override fun initViews() {
        broadcaster_layout.setOnClickListener {
            gotoLiveScreen(it, Constants.CLIENT_ROLE_BROADCASTER)
        }

        audience_layout.setOnClickListener {
            gotoLiveScreen(it, Constants.CLIENT_ROLE_AUDIENCE)
        }
    }

    override fun subscribeUi() {

    }

    private fun gotoLiveScreen(view: View, role: Int) {
        listener?.navigateToLiveScreen(view, role)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RoleFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface RoleFragmentListener {
        fun navigateToLiveScreen(view: View, role: Int)
    }
}
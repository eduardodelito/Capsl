package com.enaz.capsl.main.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.enaz.capsl.main.fragment.MainFragment
import com.enaz.capsl.main.fragment.MainFragmentDirections
import com.enaz.capsl.main.fragment.RoleFragment
import com.enaz.capsl.main.fragment.RoleFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), MainFragment.MainFragmentListener,
    RoleFragment.RoleFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.navigation_main,
                R.id.navigation_users,
                R.id.navigation_support,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration.build())
        navView.setupWithNavController(navController)
    }

    override fun navigateToRoleScreen(view: View) {
        val action = MainFragmentDirections.actionNavigationMainToNavigationRole()
        view.findNavController().navigate(action)
    }

    override fun navigateToLiveScreen(view: View, role: Int) {
        val action = RoleFragmentDirections.actionNavigationRoleToNavigationLiveStream()
        view.findNavController().navigate(action)
    }
}

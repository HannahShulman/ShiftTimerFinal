package com.shift.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.shift.timer.databinding.ActivityMainBinding
import com.shift.timer.ui.CurrentShiftFragment
import com.shift.timer.ui.SettingsFragment
import com.shift.timer.ui.ShiftListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val tag: String = menuItem.fragmentTagById()

            val currentFragment = supportFragmentManager.primaryNavigationFragment
            currentFragment?.let {
                fragmentTransaction.hide(it)
            }

            var fragment = supportFragmentManager.findFragmentByTag(tag)

            if (fragment == null) { // No existing fragment, create one
                fragment = menuItem.fragmentById()

                fragment?.let {
                    fragmentTransaction.setCustomAnimations(R.anim.main_fragment_transition, 0)
                    fragmentTransaction.add(R.id.fragment_container, fragment, tag)
                }
            } else { // Found existing fragment
                fragmentTransaction.setCustomAnimations(R.anim.main_fragment_transition, 0)
                fragmentTransaction.show(fragment)
            }

            if (fragment != currentFragment) {
                fragmentTransaction.setPrimaryNavigationFragment(fragment)
                fragmentTransaction.commit()
            }

            return@setOnNavigationItemSelectedListener true
        }

        binding.bottomNavigation.selectedItemId = R.id.current_shift
    }

    private fun MenuItem.fragmentTagById(): String = when (itemId) {
        R.id.current_shift -> "Current Shift"
        else -> ""
    }

    private fun MenuItem.fragmentById(): Fragment? = when(itemId) {
        R.id.settings -> SettingsFragment()
        R.id.current_shift -> CurrentShiftFragment()
        R.id.shifts -> ShiftListFragment()
        else -> null
    }
}
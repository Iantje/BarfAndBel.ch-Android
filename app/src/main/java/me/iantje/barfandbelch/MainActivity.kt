package me.iantje.barfandbelch

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.TextView
import me.iantje.barfandbelch.fragments.AllQuotesFragment
import me.iantje.barfandbelch.fragments.HomeFragment
import me.iantje.barfandbelch.fragments.UserFragment
import me.iantje.barfandbelch.R.id.fragment
import me.iantje.barfandbelch.widgets.StaticNotification

class MainActivity : AppCompatActivity() {

    private val homeFragment: HomeFragment = HomeFragment()
    private val allQuotesFragment: AllQuotesFragment = AllQuotesFragment()
    private val userFragment: UserFragment = UserFragment()

    private var currentFragment: Fragment = homeFragment

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if(currentFragment == homeFragment) {
                    homeFragment.showNewQuote()
                }
                currentFragment = homeFragment
            }
            R.id.navigation_dashboard -> {
                currentFragment = allQuotesFragment
            }
            R.id.navigation_notifications -> {
                currentFragment = userFragment
            }
        }

        changeFragment(currentFragment)
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        changeFragment(currentFragment)
    }

    fun changeFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(fragment, frag).commit()
    }
}

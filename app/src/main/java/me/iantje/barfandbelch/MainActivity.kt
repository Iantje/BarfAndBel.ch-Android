package me.iantje.barfandbelch

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
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

    private val notification = StaticNotification()

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(allQuotesFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(userFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        changeFragment(homeFragment)

        //notification.pushNotification(this)
    }

    fun changeFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(fragment, frag).commit()
    }
}

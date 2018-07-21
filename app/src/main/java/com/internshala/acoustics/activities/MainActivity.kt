package com.internshala.acoustics.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.SeekBar
import com.internshala.acoustics.R
import com.internshala.acoustics.adapters.NavigationDrawerAdapter
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.internshala.acoustics.fragments.MainScreenFragment
import com.internshala.acoustics.fragments.SongPlayingFragment
import kotlinx.android.synthetic.main.fragment_song_playing.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){

    var trackNotificationBuilder:  Notification? = null

    var navigationDrawerIconsList: ArrayList<String> = arrayListOf()
    var images_for_navdrawer = intArrayOf(R.drawable.navigation_allsongs, R.drawable.navigation_favorites
            , R.drawable.navigation_settings, R.drawable.navigation_aboutus)
    object  Statified{
        var drawerLayout: DrawerLayout? = null
        var notificationManager: NotificationManager? = null
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Statified.drawerLayout = findViewById(R.id.drawer_layout)

        navigationDrawerIconsList.add("All Songs")
        navigationDrawerIconsList.add("Favorites")
        navigationDrawerIconsList.add("Settings")
        navigationDrawerIconsList.add("About Us")



        val toggle = ActionBarDrawerToggle(this@MainActivity, Statified.drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragnent = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragnent, "MainScreenFragment")
                .commit()

        var _navigationAdapter = NavigationDrawerAdapter(navigationDrawerIconsList, images_for_navdrawer, this)
        _navigationAdapter.notifyDataSetChanged()

        var navigation_recycler_view = findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recycler_view.layoutManager = LinearLayoutManager(this)
        navigation_recycler_view.itemAnimator = DefaultItemAnimator()
        navigation_recycler_view.adapter = _navigationAdapter
        navigation_recycler_view.setHasFixedSize(true)
        val intent = Intent(this@MainActivity, MainActivity :: class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity, System.currentTimeMillis().toInt(),
                 intent, 0)
        trackNotificationBuilder = Notification.Builder(this)
                .setContentTitle("A Track is playing in background")
                .setSmallIcon(R.drawable.preview)
                .setContentIntent(pIntent)

                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1978)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                 Statified.notificationManager?.notify(1978, trackNotificationBuilder)
            }

        }catch(e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
          Statified.notificationManager?.cancel(1978)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}

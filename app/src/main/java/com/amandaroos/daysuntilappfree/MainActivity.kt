package com.amandaroos.daysuntilappfree

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amandaroos.daysuntilappfree.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val MILLISECONDS_IN_DAY = 86400000
    val MILLISECONDS_IN_HOUR = 3600000
    val MILLISECONDS_IN_MINUTE = 60000
    val MILLISECONDS_IN_SECOND = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000) //3600000 1 hour
                        runOnUiThread {
                            val c = Calendar.getInstance()
                            var now = c.timeInMillis

                            var savedDate =
                                sharedPref.getLong(
                                    getString(R.string.date_key),
                                    Calendar.getInstance().timeInMillis
                                )

                            binding.dateText.text =
                                DateFormat.getDateFormat(getApplicationContext()).format(savedDate)
                            binding.timeText.text =
                                DateFormat.getTimeFormat(getApplicationContext()).format(savedDate)

                            var days = (savedDate - now) / MILLISECONDS_IN_DAY

                            if (days < 1) {
                                binding.daysText.text = getString(R.string.days)
                                binding.daysUntilNumber.text = "0"
                            } else if (days < 2) {
                                binding.daysText.text = getString(R.string.day)
                                binding.daysUntilNumber.text = "1"
                            } else if (days > 0) {
                                binding.daysText.text = getString(R.string.days)
                                binding.daysUntilNumber.text = days.toInt().toString()
                            }

                            var hours =
                                ((savedDate - now) % MILLISECONDS_IN_DAY) / MILLISECONDS_IN_HOUR
                            if (hours < 1) {
                                binding.hoursText.text = getString(R.string.hours)
                                binding.hoursUntilNumber.text = "0"
                            } else if (hours < 2) {
                                binding.hoursText.text = getString(R.string.hour)
                                binding.hoursUntilNumber.text = "1"
                            } else if (hours > 0) {
                                binding.hoursText.text = getString(R.string.hours)
                                binding.hoursUntilNumber.text = hours.toInt().toString()
                            }

                            var minutes =
                                (((savedDate - now) % MILLISECONDS_IN_DAY) % MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTE
                            if (minutes < 1) {
                                binding.minutesText.text = getString(R.string.minutes)
                                binding.minutesUntilNumber.text = "0"
                            } else if (minutes < 2) {
                                binding.minutesText.text = getString(R.string.minute)
                                binding.minutesUntilNumber.text = "1"
                            } else if (minutes > 0) {
                                binding.minutesText.text = getString(R.string.minutes)
                                binding.minutesUntilNumber.text = minutes.toInt().toString()
                            }
                            var seconds =
                                ((((savedDate - now) % MILLISECONDS_IN_DAY) % MILLISECONDS_IN_HOUR) % MILLISECONDS_IN_MINUTE) / MILLISECONDS_IN_SECOND
                            if (seconds < 1) {
                                binding.secondsText.text = getString(R.string.seconds)
                                binding.secondsUntilNumber.text = "0"
                            } else if (seconds < 2) {
                                binding.secondsText.text = getString(R.string.second)
                                binding.secondsUntilNumber.text = "1"
                            } else if (seconds > 0) {
                                binding.secondsText.text = getString(R.string.seconds)
                                binding.secondsUntilNumber.text = seconds.toInt().toString()
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        thread.start()
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_upgrade -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.play_store_direct_link_upgrade))
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.play_store_browser_link_upgrade))
                        )
                    )
                }
                true
            }
            R.id.action_donate -> {
                val intent2 = Intent(this, DonateActivity::class.java)
                startActivity(intent2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
package com.amandaroos.daysuntilappfree

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val MILLISECONDS_IN_DAY = 86400000
    val MILLISECONDS_IN_HOUR = 3600000
    val MILLISECONDS_IN_MINUTE = 60000
    val MILLISECONDS_IN_SECOND = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                            date_text.text =
                                DateFormat.getDateFormat(getApplicationContext()).format(savedDate)
                            time_text.text =
                                DateFormat.getTimeFormat(getApplicationContext()).format(savedDate)

                            var days = (savedDate - now) / MILLISECONDS_IN_DAY

                            if (days < 1) {
                                days_text.text = getString(R.string.days)
                                days_until_number.text = "0"
                            } else if (days < 2) {
                                days_text.text = getString(R.string.day)
                                days_until_number.text = "1"
                            } else if (days > 0) {
                                days_text.text = getString(R.string.days)
                                days_until_number.text = days.toInt().toString()
                            }

                            var hours =
                                ((savedDate - now) % MILLISECONDS_IN_DAY) / MILLISECONDS_IN_HOUR
                            if (hours < 1) {
                                hours_text.text = getString(R.string.hours)
                                hours_until_number.text = "0"
                            } else if (hours < 2) {
                                hours_text.text = getString(R.string.hour)
                                hours_until_number.text = "1"
                            } else if (hours > 0) {
                                hours_text.text = getString(R.string.hours)
                                hours_until_number.text = hours.toInt().toString()
                            }

                            var minutes =
                                (((savedDate - now) % MILLISECONDS_IN_DAY) % MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTE
                            if (minutes < 1) {
                                minutes_text.text = getString(R.string.minutes)
                                minutes_until_number.text = "0"
                            } else if (minutes < 2) {
                                minutes_text.text = getString(R.string.minute)
                                minutes_until_number.text = "1"
                            } else if (minutes > 0) {
                                minutes_text.text = getString(R.string.minutes)
                                minutes_until_number.text = minutes.toInt().toString()
                            }
                            var seconds =
                                ((((savedDate - now) % MILLISECONDS_IN_DAY) % MILLISECONDS_IN_HOUR) % MILLISECONDS_IN_MINUTE) / MILLISECONDS_IN_SECOND
                            if (seconds < 1) {
                                seconds_text.text = getString(R.string.seconds)
                                seconds_until_number.text = "0"
                            } else if (seconds < 2) {
                                seconds_text.text = getString(R.string.second)
                                seconds_until_number.text = "1"
                            } else if (seconds > 0) {
                                seconds_text.text = getString(R.string.seconds)
                                seconds_until_number.text = seconds.toInt().toString()
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> {
//                val intent = Intent(this, SettingsActivity::class.java).apply {
//                    //putExtra(getString(R.string.context_key),this@MainActivity)
//                }
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}
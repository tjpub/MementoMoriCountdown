package zak0.github.calendarcountdown.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import zak0.github.calendarcountdown.R
import zak0.github.calendarcountdown.data.CountdownSettings


class IncludeExcludeOnlyCustomDaysActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var settings: CountdownSettings
    private var radio_btn_id: Int = 0
    private lateinit var Monday_btn: CheckBox
    private lateinit var Tuesday_btn: CheckBox
    private lateinit var Wednesday_btn: CheckBox
    private lateinit var Thursday_btn: CheckBox
    private lateinit var Friday_btn: CheckBox
    private lateinit var Saturday_btn: CheckBox
    private lateinit var Sunday_btn: CheckBox

    private lateinit var include_btn: RadioButton
    private lateinit var exclude_btn: RadioButton
    private var include_flag: Boolean = false
    private var exclude_flag: Boolean = false

    private var Monday_flag: Boolean = false
    private var Tuesday_flag: Boolean = false
    private var Wednesday_flag: Boolean = false
    private var Thursday_flag: Boolean = false
    private var Friday_flag: Boolean = false
    private var Saturday_flag: Boolean = false
    private var Sunday_flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_include_exclude_only_custom_days)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Monday_btn = findViewById(R.id.Monday_radio_btn)
        Monday_btn.setOnClickListener(this)
        Tuesday_btn = findViewById(R.id.Tuesday_radio_btn)
        Tuesday_btn.setOnClickListener(this)
        Wednesday_btn = findViewById(R.id.Wednesday_radio_btn)
        Wednesday_btn.setOnClickListener(this)
        Thursday_btn = findViewById(R.id.Thursday_radio_btn)
        Thursday_btn.setOnClickListener(this)
        Friday_btn = findViewById(R.id.Friday_radio_btn)
        Friday_btn.setOnClickListener(this)
        Saturday_btn = findViewById(R.id.Saturday_radio_btn)
        Saturday_btn.setOnClickListener(this)
        Sunday_btn = findViewById(R.id.Sunday_radio_btn)
        Sunday_btn.setOnClickListener(this)

        include_btn = findViewById(R.id.include_radio_btn)
        include_btn.setOnClickListener(this)
        exclude_btn = findViewById(R.id.exclude_radio_btn)
        exclude_btn.setOnClickListener(this)

//         CountdownSettings should be in the extras, if not, then a crash is justified...
        settings = intent.getSerializableExtra(CountdownSettings.extraName) as CountdownSettings
        if((settings.include_only_days_list.size > 0) && (settings.include_only_days_list_str !="")) {
            include_flag = true
            include_btn.isChecked = true
            if(settings.include_only_days_list.contains("Monday")) {
                Monday_flag = true
                Monday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Tuesday")) {
                Tuesday_flag = true
                Tuesday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Wednesday")){
                Wednesday_flag = true
                Wednesday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Thursday")) {
                Thursday_flag = true
                Thursday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Friday")) {
                Friday_flag = true
                Friday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Saturday")) {
                Saturday_flag = true
                Saturday_btn.isChecked = true
            }
            if(settings.include_only_days_list.contains("Sunday")) {
                Sunday_flag= true
                Sunday_btn.isChecked = true
            }
        }
        else if((settings.exclude_only_days_list.size > 0) &&(settings.exclude_only_days_list_str !="")) {
            exclude_flag = true
            exclude_btn.isChecked = true
            if(settings.exclude_only_days_list.contains("Monday")) {
                Monday_flag = true
                Monday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Tuesday")) {
                Tuesday_flag = true
                Tuesday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Wednesday")){
                Wednesday_flag = true
                Wednesday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Thursday")) {
                Thursday_flag = true
                Thursday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Friday")) {
                Friday_flag = true
                Friday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Saturday")) {
                Saturday_flag = true
                Saturday_btn.isChecked = true
            }
            if(settings.exclude_only_days_list.contains("Sunday")) {
                Sunday_flag= true
                Sunday_btn.isChecked = true
            }
        }

    }

    /**
     * Pressing back will finish this activity and pass current CountdownSettings as params
     */
    override fun onBackPressed() {
        if(include_flag) {
            if(Monday_flag && (!settings.include_only_days_list.contains("Monday"))) settings.include_only_days_list.add("Monday")
            else {
                if(!Monday_flag && settings.include_only_days_list.contains("Monday")) settings.include_only_days_list.remove("Monday")
            }

            if(Tuesday_flag && (!settings.include_only_days_list.contains("Tuesday"))) settings.include_only_days_list.add("Tuesday")
            else{
                if(!Tuesday_flag &&settings.include_only_days_list.contains("Tuesday")) settings.include_only_days_list.remove("Tuesday")
            }

            if(Wednesday_flag && (!settings.include_only_days_list.contains("Wednesday"))) settings.include_only_days_list.add("Wednesday")
            else {
                if(!Wednesday_flag && settings.include_only_days_list.contains("Wednesday")) settings.include_only_days_list.remove("Wednesday")
            }

            if(Thursday_flag && (!settings.include_only_days_list.contains("Thursday"))) settings.include_only_days_list.add("Thursday")
            else {
                if(!Thursday_flag && settings.include_only_days_list.contains("Thursday")) settings.include_only_days_list.remove("Thursday")
            }
            if(Friday_flag && (!settings.include_only_days_list.contains("Friday"))) settings.include_only_days_list.add("Friday")
            else {
                if(!Friday_flag && settings.include_only_days_list.contains("Friday")) settings.include_only_days_list.remove("Friday")
            }

            if(Saturday_flag && (!settings.include_only_days_list.contains("Saturday"))) settings.include_only_days_list.add("Saturday")
            else {
                if(!Saturday_flag && settings.include_only_days_list.contains("Saturday")) settings.include_only_days_list.remove("Saturday")
            }

            if(Sunday_flag && (!settings.include_only_days_list.contains("Sunday"))) settings.include_only_days_list.add("Sunday")
            else {
                if(!Sunday_flag && settings.include_only_days_list.contains("Sunday")) settings.include_only_days_list.remove("Sunday")
            }
        }

        if(exclude_flag) {
            if(Monday_flag && (!settings.exclude_only_days_list.contains("Monday"))) settings.exclude_only_days_list.add("Monday")
            else {
                if(!Monday_flag && settings.exclude_only_days_list.contains("Monday")) settings.exclude_only_days_list.remove("Monday")
            }

            if(Tuesday_flag && (!settings.exclude_only_days_list.contains("Tuesday"))) settings.exclude_only_days_list.add("Tuesday")
            else{
                if(!Tuesday_flag &&settings.exclude_only_days_list.contains("Tuesday")) settings.exclude_only_days_list.remove("Tuesday")
            }

            if(Wednesday_flag && (!settings.exclude_only_days_list.contains("Wednesday"))) settings.exclude_only_days_list.add("Wednesday")
            else {
                if(!Wednesday_flag && settings.exclude_only_days_list.contains("Wednesday")) settings.exclude_only_days_list.remove("Wednesday")
            }

            if(Thursday_flag && (!settings.exclude_only_days_list.contains("Thursday"))) settings.exclude_only_days_list.add("Thursday")
            else {
                if(!Thursday_flag && settings.exclude_only_days_list.contains("Thursday")) settings.exclude_only_days_list.remove("Thursday")
            }
            if(Friday_flag && (!settings.exclude_only_days_list.contains("Friday"))) settings.exclude_only_days_list.add("Friday")
            else {
                if(!Friday_flag && settings.exclude_only_days_list.contains("Friday")) settings.exclude_only_days_list.remove("Friday")
            }

            if(Saturday_flag && (!settings.exclude_only_days_list.contains("Saturday"))) settings.exclude_only_days_list.add("Saturday")
            else {
                if(!Saturday_flag && settings.exclude_only_days_list.contains("Saturday")) settings.exclude_only_days_list.remove("Saturday")
            }

            if(Sunday_flag && (!settings.exclude_only_days_list.contains("Sunday"))) settings.exclude_only_days_list.add("Sunday")
            else {
                if(!Sunday_flag && settings.exclude_only_days_list.contains("Sunday")) settings.exclude_only_days_list.remove("Sunday")
            }
        }


        val data = Intent()
        data.putExtra(CountdownSettings.extraName, settings)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val REQUEST_CODE_INCLUDE_ONLY_CUSTOM_DAYS = 200
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.Monday_radio_btn -> {
                Monday_flag = Monday_btn.isChecked
            }
            R.id.Tuesday_radio_btn -> {
                Tuesday_flag = Tuesday_btn.isChecked
            }
            R.id.Wednesday_radio_btn -> {
                Wednesday_flag = Wednesday_btn.isChecked
            }
            R.id.Thursday_radio_btn -> {
                Thursday_flag = Thursday_btn.isChecked
            }
            R.id.Friday_radio_btn -> {
                Friday_flag = Friday_btn.isChecked
            }
            R.id.Saturday_radio_btn -> {
                Saturday_flag = Saturday_btn.isChecked
            }
            R.id.Sunday_radio_btn -> {
                Sunday_flag = Sunday_btn.isChecked
            }
            R.id.include_radio_btn -> {
                include_flag = include_btn.isChecked
                if(include_flag) {
                    exclude_flag = false
                    settings.exclude_only_days_list_str = ""
                    settings.exclude_only_days_list.clear()
                }
            }
            R.id.exclude_radio_btn -> {
                exclude_flag = exclude_btn.isChecked
                if(exclude_flag) {
                    include_flag = false
                    settings.include_only_days_list.clear()
                    settings.include_only_days_list_str = ""
                }
            }

        }
    }
}
package com.cleaner.cybercleanerapp.ui.cpu_cooler

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cleaner.cybercleanerapp.ui.MainActivity
import java.io.File

class AsyncTaskList(private var activity: MainActivity, var fragment: CPUCoolerFragment) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        activity.isOptimizationActive = true
        val pm: PackageManager = activity.packageManager
        val apps = pm.getInstalledApplications(0)
        Log.d("ASYNC", "start")
        for (app in apps) {
            val appsSizeFile = File(pm.getApplicationInfo(app.packageName, PackageManager.GET_META_DATA).publicSourceDir).length()
//            val label = pm.getApplicationLabel(app)
            val icon: Drawable = pm.getApplicationIcon(app)
            if (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {

            } else if (app.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            } else {
                val apps_installed = InstalledAppsInfoModel((appsSizeFile / 1000000 + 20).toString() + "MB", icon)
                fragment.installedAppsInfo.add(apps_installed)
            }
        }
        return null
    }

    override fun onCancelled() {
        super.onCancelled()
        Log.d("ASYNC", "cancel")
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Log.d("ASYNC", "loaded")
        if (fragment.installedAppsInfo.size > 1) {
            fragment.setAppsList()
        }
        activity.isOptimizationActive = false
    }
}
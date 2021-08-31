package com.cleaner.cybercleanerapp.util;

import android.content.Context;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static android.content.Context.BATTERY_SERVICE;

public class Util {
    public static final String SHARED_BATTERY = "cyber_cleaner_battery";
    public static final String SHARED_STORAGE = "cyber_cleaner_storage";
    public static final String SHARED_CPU = "cyber_cleaner_cpu";
    public static final String SHARED_JUNK = "cyber_cleaner_junk";
    public static final float MAX_CPU_TEMP = 60.0f;

    public static int getBatteryPercentage(Context context) {
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static float cpuTemperature() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            } else {
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
}

package com.cleaner.cybercleanerapp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import eu.chainfire.libsuperuser.Shell;

import static android.content.Context.ACTIVITY_SERVICE;

public class PhoneTaskCleanerUtil {
    public static void clearBackgroundTasks(Context context) {
        PackageManager pm = context.getPackageManager();
        List<String> stdout = Shell.SH.run("ps");
        List<String> packages = new ArrayList<>();
        for (String line : stdout) {
            String[] arr = line.split("\\s+");
            String processName = arr[arr.length - 1].split(":")[0];
            packages.add(processName);
        }
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

        for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
            if (!packages.contains(it.next().packageName)) {
                it.remove();
            }
        }
        List<String> packages_all = new ArrayList<>();
        for (ApplicationInfo app : apps) {
            packages_all.add(app.processName);
        }
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (String name : packages_all
        ) {
            try {
                if (!name.equals("com.cleaner.cybercleanerapp")) {
                    am.killBackgroundProcesses(name);
                }
            } catch (Exception e) {
            }
            final MemStat memStat = new MemStat(context);
            SingletonClassApp.getInstance().UsedMemory = String.valueOf((int) memStat.getUsedMemory());
            SingletonClassApp.getInstance().TotalMemory = String.valueOf(memStat.getTotalMemory());
            if (SingletonClassApp.getInstance().procentMemory <= 100 - memStat.getProcentMemory()) {
                SingletonClassApp.getInstance().procentMemory = 100 - memStat.getProcentMemory();
            } else {
                final int min = 5;
                final int max = 10;
                final int random = new Random().nextInt((max - min) + 1) + min;
                SingletonClassApp.getInstance().procentMemory = SingletonClassApp.getInstance().procentMemory - random;
            }
        }
    }
}

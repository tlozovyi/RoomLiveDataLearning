package com.levor.roomwordssample

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import java.io.File

object AppSecurityManager {

    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    // possibly check https://github.com/scottyab/rootbeer for more options
    fun isRooted(context: Context): Boolean {
        return checkBuildTags()
                || checkRootApps()
                || checkSuFiles()
                || checkSuInSystemPath()
                || checkRootAppsPackages(context)
                || checkDangerousAppsPackages(context)
                || checkRootCloaingAppsPackages(context)
    }

    private fun checkBuildTags(): Boolean {
        val buildTags = Build.TAGS
        return !isEmulator() && buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootApps(): Boolean {
        val commonRootAppsPaths = listOf(
            "/system/app/Superuser.apk",
            "/system/etc/init.d/99SuperSUDaemon",
            "/dev/com.koushikdutta.superuser.daemon/",
            "/system/xbin/daemonsu"
        )

        return commonRootAppsPaths.any { File(it).exists() }
    }

    private fun checkSuFiles(): Boolean {
        val commonSuFilePaths = listOf(
            "/sbin/su",
            "/system/bin/su",
            "/system/bin/failsafe/su",
            "/system/xbin/su",
            "/system/sd/xbin/su",
            "/data/local/su",
            "/data/local/xbin/su",
            "/data/local/bin/su"
        )

        return !isEmulator() && commonSuFilePaths.any { File(it).exists() }
    }

    private fun checkSuInSystemPath(): Boolean {
        var result = false
        System.getenv("PATH").orEmpty()
            .split(":")
            .dropLastWhile { it.isEmpty() }
            .forEach { pathDir ->
                if (File(pathDir, "su").exists()) {
                    result = true
                }
            }
        return !isEmulator() && result
    }

    private fun checkRootAppsPackages(context: Context): Boolean {
        val allPackages = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            .map { it.packageName }

        val knownRootAppPackages = listOf(
            "com.thirdparty.superuser",
            "eu.chainfire.supersu",
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "com.koushikdutta.superuser",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine",
            "com.topjohnwu.magisk",
            "com.yellowes.su",
            "com.kingroot.kinguser",
            "com.kingo.root",
            "com.smedialink.oneclickroot",
            "com.zhiqupk.root.global",
            "com.alephzain.framaroot"
        )

        return allPackages.intersect(knownRootAppPackages).isNotEmpty()
    }

    private fun checkDangerousAppsPackages(context: Context): Boolean {
        val allPackages = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            .map { it.packageName }

        val knownDangerousAppPackages = listOf(
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro",
            "com.android.vending.billing.InAppBillingService.COIN",
            "com.chelpus.luckypatcher"
        )

        return allPackages.intersect(knownDangerousAppPackages).isNotEmpty()
    }

    private fun checkRootCloaingAppsPackages(context: Context): Boolean {
        val allPackages = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            .map { it.packageName }

        val knownCloakingApps = listOf(
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot"
        )

        return allPackages.intersect(knownCloakingApps).isNotEmpty()
    }

    fun isReverseEngineeringToolsInstalled(context: Context): Boolean {
        val allPackages = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            .map { it.packageName }

        val toolsPackages = listOf(
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.mwr.dz",
            "asvid.github.io.fridaapp",
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "com.android.SSLTrustKiller"
        )

        return allPackages.intersect(toolsPackages).isNotEmpty()
    }

    fun isDebuggerAttached(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()

    }

    fun isAppDebuggable(context: Context) =
        (context.applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}
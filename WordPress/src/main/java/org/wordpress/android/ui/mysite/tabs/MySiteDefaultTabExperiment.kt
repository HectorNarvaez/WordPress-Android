package org.wordpress.android.ui.mysite.tabs

import org.wordpress.android.analytics.AnalyticsTracker.Stat
import org.wordpress.android.ui.prefs.AppPrefsWrapper
import org.wordpress.android.util.analytics.AnalyticsTrackerWrapper
import org.wordpress.android.util.config.MySiteDashboardTabsFeatureConfig
import org.wordpress.android.util.config.MySiteDefaultTabExperimentFeatureConfig
import org.wordpress.android.util.config.MySiteDefaultTabExperimentVariationDashboardFeatureConfig
import javax.inject.Inject

class MySiteDefaultTabExperiment @Inject constructor(
    private val mySiteDefaultTabExperimentFeatureConfig: MySiteDefaultTabExperimentFeatureConfig,
    private val mySiteDefaultTabExperimentVariationDashboardFeatureConfig:
    MySiteDefaultTabExperimentVariationDashboardFeatureConfig,
    private val mySiteDashboardTabsFeatureConfig: MySiteDashboardTabsFeatureConfig,
    private val appPrefsWrapper: AppPrefsWrapper,
    private val analyticsTrackerWrapper: AnalyticsTrackerWrapper
) {
    fun checkAndSetVariantIfNeeded() {
        if (isExperimentRunning()) {
            if (!isVariantAssigned()) {
                setVariantAssigned()
                when (mySiteDefaultTabExperimentVariationDashboardFeatureConfig.isDashboardVariant()) {
                    true -> setExperimentVariant(MySiteTabType.DASHBOARD.trackingLabel)
                    false -> setExperimentVariant(MySiteTabType.SITE_MENU.trackingLabel)
                }
                analyticsTrackerWrapper.setInjectExperimentProperties(getVariantMapForTracking())
                analyticsTrackerWrapper.track(Stat.MY_SITE_DEFAULT_TAB_EXPERIMENT_VARIANT_ASSIGNED)
            }
        }
    }

    fun checkAndSetTrackingPropertiesIfNeeded() {
        if (isExperimentRunning()) {
            analyticsTrackerWrapper.setInjectExperimentProperties(getVariantMapForTracking())
        }
    }

    fun changeExperimentVariantAssignmentIfNeeded(toVariant: String) {
        if (isExperimentRunning() && isVariantAssigned()) {
            setExperimentVariant(toVariant)
            analyticsTrackerWrapper.setInjectExperimentProperties(getVariantMapForTracking())
            analyticsTrackerWrapper.track(Stat.MY_SITE_DEFAULT_TAB_EXPERIMENT_VARIANT_ASSIGNED)
        }
    }

    private fun isExperimentRunning() =
            mySiteDashboardTabsFeatureConfig.isEnabled() && mySiteDefaultTabExperimentFeatureConfig.isEnabled()

    private fun isVariantAssigned() = appPrefsWrapper.isMySiteDefaultTabExperimentVariantAssigned()

    private fun setVariantAssigned() = appPrefsWrapper.setMySiteDefaultTabExperimentVariantAssigned()

    private fun setExperimentVariant(variant: String) =
            appPrefsWrapper.setInitialScreenFromMySiteDefaultTabExperimentVariant(variant)

    private fun getVariantMapForTracking() = mapOf(DEFAULT_TAB_EXPERIMENT to getVariantTrackingLabel())

    private fun getVariantTrackingLabel(): String {
        if (!isVariantAssigned()) return NONEXISTENT
        return if (appPrefsWrapper.getMySiteInitialScreen() == MySiteTabType.DASHBOARD.label) {
            MySiteTabType.DASHBOARD.trackingLabel
        } else {
            MySiteTabType.SITE_MENU.trackingLabel
        }
    }

    companion object {
        private const val DEFAULT_TAB_EXPERIMENT = "default_tab_experiment"
        private const val NONEXISTENT = "nonexistent"
    }
}

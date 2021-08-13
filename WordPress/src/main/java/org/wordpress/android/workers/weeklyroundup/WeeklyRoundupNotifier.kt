package org.wordpress.android.workers.weeklyroundup

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import org.wordpress.android.fluxc.store.AccountStore
import org.wordpress.android.fluxc.store.SiteStore
import org.wordpress.android.push.NotificationPushIds.WEEKLY_ROUNDUP_NOTIFICATION_ID
import org.wordpress.android.ui.stats.StatsTimeframe.WEEK
import org.wordpress.android.ui.stats.refresh.StatsActivity
import org.wordpress.android.util.SiteUtils
import org.wordpress.android.viewmodel.ContextProvider
import javax.inject.Inject

class WeeklyRoundupNotifier @Inject constructor(
    private val accountStore: AccountStore,
    private val siteStore: SiteStore,
    private val contextProvider: ContextProvider
) {
    fun shouldShowNotifications() = accountStore.hasAccessToken() && siteStore.hasSitesAccessedViaWPComRest()

    fun buildNotifications(): List<WeeklyRoundupNotification> {
        val context = contextProvider.getContext()
        val site = siteStore.sitesAccessedViaWPComRest[0]
        val siteId = site.id
        val siteName = SiteUtils.getSiteNameOrHomeURL(site)
        val notificationId = WEEKLY_ROUNDUP_NOTIFICATION_ID + siteId

        val weeklyRoundupNotification = WeeklyRoundupNotification(
                id = notificationId,
                contentIntentBuilder = {
                    PendingIntent.getActivity(
                            context,
                            notificationId,
                            StatsActivity.buildIntent(context, site, WEEK),
                            FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE
                    )
                },
                contentTitle = "Weekly Roundup: $siteName",
                contentText = "Your site got X views, Y likes, Z comments.",
        )

        return listOf(weeklyRoundupNotification)
    }
}

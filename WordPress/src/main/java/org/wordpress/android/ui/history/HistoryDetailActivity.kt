package org.wordpress.android.ui.history

import android.os.Bundle
import org.wordpress.android.R
import org.wordpress.android.analytics.AnalyticsTracker
import org.wordpress.android.analytics.AnalyticsTracker.Stat
import org.wordpress.android.databinding.HistoryDetailActivityBinding
import org.wordpress.android.ui.LocaleAwareActivity
import org.wordpress.android.ui.history.HistoryListItem.Revision

class HistoryDetailActivity : LocaleAwareActivity() {
    companion object {
        const val KEY_HISTORY_DETAIL_FRAGMENT = "history_detail_fragment"
    }

    private var binding: HistoryDetailActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(HistoryDetailActivityBinding.inflate(layoutInflater)) {
            setContentView(root)
            binding = this

            setSupportActionBar(toolbarMain)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = requireNotNull(intent.extras)
        val revision = extras.getParcelable<Revision>(HistoryDetailContainerFragment.EXTRA_REVISION)
        val revisions = extras.getParcelableArrayList<Revision>(HistoryDetailContainerFragment.EXTRA_REVISIONS)

        var historyDetailContainerFragment = supportFragmentManager.findFragmentByTag(KEY_HISTORY_DETAIL_FRAGMENT)

        if (historyDetailContainerFragment == null) {
            historyDetailContainerFragment = HistoryDetailContainerFragment.newInstance(
                    revision,
                    revisions as ArrayList<Revision>
            )
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, historyDetailContainerFragment, KEY_HISTORY_DETAIL_FRAGMENT)
                    .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        AnalyticsTracker.track(Stat.REVISIONS_DETAIL_CANCELLED)
        finish()
        return true
    }

    override fun onBackPressed() {
        AnalyticsTracker.track(Stat.REVISIONS_DETAIL_CANCELLED)
        super.onBackPressed()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

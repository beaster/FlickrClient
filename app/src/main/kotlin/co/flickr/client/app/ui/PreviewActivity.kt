package co.flickr.client.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.flickr.client.R
import co.flickr.client.util.unsafeLazy
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.fitCenterTransform
import kotlinx.android.synthetic.main.activity_preview.*


class PreviewActivity : AppCompatActivity() {

    private val url by unsafeLazy { intent.getStringExtra(EXTRA_URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        setupToolbar()
        showImagePreview()
    }

    private fun setupToolbar() {
        supportActionBar?.setTitle(R.string.preview_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showImagePreview() {
        Glide.with(this).load(url).apply(fitCenterTransform()).into(preview)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private const val EXTRA_URL = "url"

        fun start(context: Context, url: String) {
            context.startActivity(Intent(context, PreviewActivity::class.java).putExtra(EXTRA_URL, url))
        }
    }
}
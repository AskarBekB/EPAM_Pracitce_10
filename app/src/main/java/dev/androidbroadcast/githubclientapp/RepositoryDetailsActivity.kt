package dev.androidbroadcast.githubclientapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.androidbroadcast.githubclientapp.databinding.ActivityRepositoryDetailsBinding
import dev.androidbroadcast.githubclientapp.model.RepositoryDetailsViewModel

class RepositoryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepositoryDetailsBinding
    private val viewModel: RepositoryDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val owner = intent.getStringExtra(EXTRA_OWNER) ?: return
        val repo = intent.getStringExtra(EXTRA_REPO) ?: return

        viewModel.loadRepositoryDetails(owner, repo)

        viewModel.repositoryDetails.observe(this) { details ->
            binding.repoName.text = details.name
            binding.repoDescription.text = details.description ?: getString(R.string.no_description)
            binding.repoForks.text = getString(R.string.forks, details.forks_count)
            binding.repoWatchers.text = getString(R.string.watchers, details.watchers_count)
            binding.repoOpenIssues.text = getString(R.string.open_issues, details.open_issues_count)
            details.parent?.let {
                binding.repoParent.text = getString(R.string.parent, it.full_name)
            }
        }

        viewModel.error.observe(this) {
            it?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val EXTRA_OWNER = "EXTRA_OWNER"
        private const val EXTRA_REPO = "EXTRA_REPO"

        fun start(context: Context, owner: String, repo: String) {
            val intent = Intent(context, RepositoryDetailsActivity::class.java)
            intent.putExtra(EXTRA_OWNER, owner)
            intent.putExtra(EXTRA_REPO, repo)
            context.startActivity(intent)
        }
    }
}


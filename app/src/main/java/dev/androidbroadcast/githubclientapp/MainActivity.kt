package dev.androidbroadcast.githubclientapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.androidbroadcast.githubclientapp.databinding.ActivityMainBinding
import dev.androidbroadcast.githubclientapp.model.RepositoryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RepositoryViewModel by viewModels()
    private val adapter = RepositoryAdapter { repo ->
        RepositoryDetailsActivity.start(this, repo.owner.login, repo.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchButton.setOnClickListener {
            val orgName = binding.searchInput.text.toString().trim()
            if (orgName.isNotEmpty()) {
                viewModel.loadRepositories(orgName)
            }
        }

        viewModel.repositories.observe(this) {
            adapter.submitList(it)
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
}

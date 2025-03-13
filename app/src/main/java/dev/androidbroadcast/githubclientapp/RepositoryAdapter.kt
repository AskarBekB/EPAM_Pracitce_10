package dev.androidbroadcast.githubclientapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.androidbroadcast.githubclientapp.databinding.ItemRepositoryBinding
import dev.androidbroadcast.githubclientapp.network.Repository

class RepositoryAdapter(private val onClick: (Repository) -> Unit) :
RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>(){

    private var repositories: List<Repository> = emptyList()

    fun submitList(list: List<Repository>) {
        repositories = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    inner class RepositoryViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repository) {
            binding.repoName.text = repo.name
            binding.repoDescription.text = repo.description?.take(100) ?: "No description available"
            binding.root.setOnClickListener { onClick(repo) }
        }
    }
}
package org.cosmicide.rewrite.treeview

import android.content.DialogInterface
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Space
import androidx.annotation.MenuRes
import androidx.core.view.updateLayoutParams
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.dingyi222666.view.treeview.DataSource
import io.github.dingyi222666.view.treeview.TreeNode
import io.github.dingyi222666.view.treeview.TreeNodeEventListener
import io.github.dingyi222666.view.treeview.TreeView
import io.github.dingyi222666.view.treeview.TreeViewBinder
import org.cosmicide.rewrite.R
import org.cosmicide.rewrite.databinding.TreeviewContextActionDialogItemBinding
import org.cosmicide.rewrite.databinding.TreeviewItemDirBinding
import org.cosmicide.rewrite.databinding.TreeviewItemFileBinding
import org.cosmicide.rewrite.model.FileViewModel
import org.jetbrains.kotlin.incremental.createDirectory
import java.io.File


class ViewBinder(var layoutInflater: LayoutInflater, val fileViewModel: FileViewModel) : TreeViewBinder<DataSource<File>>(),
    TreeNodeEventListener<DataSource<File>> {

    override fun createView(parent: ViewGroup, viewType: Int): View {
        return if (viewType == 1) {
            TreeviewItemDirBinding.inflate(layoutInflater, parent, false).root
        } else {
            TreeviewItemFileBinding.inflate(layoutInflater, parent, false).root
        }
    }

    override fun getItemViewType(node: TreeNode<DataSource<File>>): Int {
        if (node.isChild) {
            return 1
        }
        return 0
    }

    override fun bindView(
        holder: TreeView.ViewHolder,
        node: TreeNode<DataSource<File>>,
        listener: TreeNodeEventListener<DataSource<File>>
    ) {
        if (node.isChild) {
            applyDir(holder, node)
        } else {
            applyFile(holder, node)
        }

        val itemView = holder.itemView.findViewById<Space>(R.id.space)

        itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            width = node.depth * 22.dp
        }

    }

    private fun applyFile(holder: TreeView.ViewHolder, node: TreeNode<DataSource<File>>) {
        val binding = TreeviewItemFileBinding.bind(holder.itemView)
        binding.textView.text = node.name.toString()
    }

    private fun applyDir(holder: TreeView.ViewHolder, node: TreeNode<DataSource<File>>) {
        val binding = TreeviewItemDirBinding.bind(holder.itemView)
        binding.textView.text = node.name.toString()

        binding
            .imageView
            .animate()
            .rotation(if (node.expand) 90f else 0f)
            .setDuration(200)
            .start()
    }

    override fun onLongClick(
        node: TreeNode<DataSource<File>>,
        holder: TreeView.ViewHolder
    ): Boolean {
        showMenu(holder.itemView, R.menu.treeview_menu, node.data!!.data!!)
        return super<TreeViewBinder>.onLongClick(node, holder)
    }

    override fun onClick(node: TreeNode<DataSource<File>>, holder: TreeView.ViewHolder) {
        if (node.isChild) {
            applyDir(holder, node)
        } else {
            fileViewModel.addFile(node.data!!.data!!)
        }
    }

    override fun onToggle(
        node: TreeNode<DataSource<File>>,
        isExpand: Boolean,
        holder: TreeView.ViewHolder
    ) {
        applyDir(holder, node)
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, file: File) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.create_kotlin_class -> {
                    val binding = TreeviewContextActionDialogItemBinding.inflate(layoutInflater)
                    MaterialAlertDialogBuilder(v.context)
                        .setTitle("Create kotlin class")
                        .setView(binding.root)
                        .setPositiveButton("Create") { _, _ ->
                            file.absolutePath
                            var name = binding.edittext.text.toString()
                            val file = File(file, "$name.kt")
                            file.createNewFile()
                            fileViewModel.addFile(file)
                        }
                        .setNegativeButton("Cancel"){ dialog ,_ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                R.id.create_java_class -> {
                    val binding = TreeviewContextActionDialogItemBinding.inflate(layoutInflater)
                    MaterialAlertDialogBuilder(v.context)
                        .setTitle("Create java class")
                        .setView(binding.root)
                        .setPositiveButton("Create") { _, _ ->
                            file.absolutePath
                            var name = binding.edittext.text.toString()
                            File(file, "$name.java").createNewFile()
                        }
                        .setNegativeButton("Cancel"){ dialog ,_ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                R.id.create_folder -> {
                    val binding = TreeviewContextActionDialogItemBinding.inflate(layoutInflater)
                    MaterialAlertDialogBuilder(v.context)
                        .setTitle("Create folder")
                        .setView(binding.root)
                        .setPositiveButton("Create") { _, _ ->
                            file.absolutePath
                            var name = binding.edittext.text.toString()
                            File(file, "$name").createDirectory()
                        }
                        .setNegativeButton("Cancel"){ dialog ,_ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                R.id.rename -> {
                    val binding = TreeviewContextActionDialogItemBinding.inflate(layoutInflater)
                    MaterialAlertDialogBuilder(v.context)
                        .setTitle("Rename")
                        .setView(binding.root)
                        .setPositiveButton("Create") { _, _ ->
                            var name = binding.edittext.text.toString()
                            File(file.absolutePath).renameTo(File("${file.parent}, $name"))
                        }
                        .setNegativeButton("Cancel"){ dialog ,_ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                R.id.delete -> {
                    val binding = TreeviewContextActionDialogItemBinding.inflate(layoutInflater)
                    MaterialAlertDialogBuilder(v.context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this file")
                        .setPositiveButton("Create") { _, _ ->
                            file.absolutePath
                            binding.edittext.text.toString()
                            File(file.absolutePath).deleteRecursively()
                        }
                        .setNegativeButton("Cancel"){ dialog ,_ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            true
        }
        popup.show()
    }
}

inline val Int.dp: Int
    get() = (Resources.getSystem().displayMetrics.density * this + 0.5f).toInt()

package me.iamsahil.multiitemrvexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import me.iamsahil.multiitemrvexample.databinding.ActivityMainBinding
import me.iamsahil.multiitemrvexample.databinding.ItemTypeALayoutBinding
import me.iamsahil.multiitemrvexample.databinding.ItemTypeBLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = GenericMultiViewRvAdapter<YourItemType, ViewBinding>(
            inflater = { layoutInflater, parent, attachToParent, viewType ->
                // Inflation logic based on the view type
                when (viewType) {
                    VIEW_TYPE_A -> ItemTypeALayoutBinding.inflate(layoutInflater, parent, attachToParent)
                    VIEW_TYPE_B -> ItemTypeBLayoutBinding.inflate(layoutInflater, parent, attachToParent)
                    else -> throw IllegalArgumentException("Unknown item type")
                }
            },
            viewHolderBinder = { item ->
                // Bind data based on the item type
                when (item) {
                    is ItemTypeA -> {
                        // Bind data for ItemTypeA
                        // Example: textView.text = item.text
                        val binding = this as ItemTypeALayoutBinding
                        binding.titleTypeAItem.text = item.text
                    }
                    is ItemTypeB -> {
                        // Bind data for ItemTypeB
                        // Example: textView.text = item.number.toString()
                        val binding = this as ItemTypeBLayoutBinding
                        binding.titleTypeBItem.text = item.number.toString()
                    }
                    else -> throw IllegalArgumentException("Unknown item type")
                }
            },
            viewTypeProvider = { item ->
                // Determine the view type based on the item type
                when (item) {
                    is ItemTypeA -> VIEW_TYPE_A
                    is ItemTypeB -> VIEW_TYPE_B
                    else -> throw IllegalArgumentException("Unknown item type")
                }
            }
        )

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        
        adapter.submitList(listOf(
            ItemTypeA(text = "Sahil"),
            ItemTypeB(number = 342342),
        ))

    }

    companion object{
        const val VIEW_TYPE_A = 0
        const val VIEW_TYPE_B = 1
    }
}


/**
 * Base class representing the different item types in the RecyclerView.
 */
abstract class YourItemType

/**
 * Data class representing an item type with text content.
 * @property text The text content for ItemTypeA.
 */
data class ItemTypeA(val text: String) : YourItemType()

/**
 * Data class representing an item type with a numeric content.
 * @property number The numeric content for ItemTypeB.
 */
data class ItemTypeB(val number: Int) : YourItemType()
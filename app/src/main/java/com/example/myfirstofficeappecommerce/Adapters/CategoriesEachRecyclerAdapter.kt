package com.example.myfirstofficeappecommerce.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.CategoriesEachViewpagerGridRowlayoutBinding
import com.example.myfirstofficeappecommerce.fragments.ProductFragment
import kotlinx.coroutines.*

class CategoriesEachRecyclerAdapter(

    var fragment: Fragment,    //fragments may be main fragment or categoryEachViewPagerFragment
    var displayType: String = "grid"
) :
    ListAdapter<CategoriesModelClass, CategoriesEachRecyclerAdapter.CategoryViewHolder>(
        CategoriesModelClass.diffUtil
    ) {




    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:CategoriesEachViewpagerGridRowlayoutBinding=CategoriesEachViewpagerGridRowlayoutBinding.bind(itemView)
        val itemImage: ImageView = itemView.findViewById(R.id.ItemImageView)
        val itemName: TextView = itemView.findViewById(R.id.ItemNameTextView)
        val itemDescription: TextView = itemView.findViewById(R.id.ItemDescriptionTextView)
        val itemGrossweight: TextView = itemView.findViewById(R.id.GrossWeightTextView)
        val itemNetWeight: TextView = itemView.findViewById(R.id.NetWeightTextView)
        val realmrp: TextView = itemView.findViewById(R.id.originalPriceTextView)
        val addToCart: Button = itemView.findViewById(R.id.addToCartButton)
        val addOrRemoveItemLinearLayout: LinearLayout =
            itemView.findViewById(R.id.addorremoveitemslinearlayout)
        val removeItemsImageButton: ImageButton = itemView.findViewById(R.id.removeitemsImageButton)
        val addItemsImageButton: ImageButton = itemView.findViewById(R.id.additemsImageButton)
        val quantityOfItemAddaedToCartTextView: TextView =
            itemView.findViewById(R.id.cartitemquantitiytextview)
        val shareImageView: ImageView = itemView.findViewById(R.id.categories_row_share_imageview)
        val favouritesImageView =
            itemView.findViewById<ImageView>(R.id.categories_row_favouritesImageView)




        init {
            addToCart.setOnClickListener {
                var modelClass = currentList[adapterPosition]
                var modelClassTemp =
                    ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }
                if (modelClassTemp == null) {
                    modelClass.quantityOfItem++
                    (ApplicationClass.selectedItemsList as MutableList).add(modelClass)
                    notifyItemChanged(adapterPosition)
                    //callback()
                }

            }

            removeItemsImageButton.setOnClickListener {
                var modelClass = currentList[adapterPosition]
                if (modelClass.quantityOfItem > 0) {
                    modelClass.quantityOfItem--
                    ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =
                        modelClass.quantityOfItem
                }


                if (modelClass.quantityOfItem == 0 && ApplicationClass.selectedItemsList?.contains(
                        modelClass
                    )!!
                ) {

                    (ApplicationClass.selectedItemsList as MutableList).remove(modelClass)
                }

                notifyItemChanged(adapterPosition)
              //  callback()
            }

            addItemsImageButton.setOnClickListener {
                var modelClass = currentList[adapterPosition]
                modelClass.quantityOfItem++

                ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =
                    modelClass.quantityOfItem

                notifyItemChanged(adapterPosition)
                //callback()
            }

            itemImage.setOnClickListener {
                fragment.activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ProductFragment(currentList[adapterPosition]))
                    .addToBackStack(null).commit()

            }

            shareImageView.setOnClickListener {
                var s = currentList[adapterPosition].itemName
                var intent: Intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/html";
                intent.putExtra(Intent.EXTRA_TEXT, s)
                fragment.startActivity(intent)
            }

            favouritesImageView.setOnClickListener {
              var variant:VariantsModelClass? =  currentList[adapterPosition].variantsList!![0]
                if (currentList[adapterPosition].isFav) {

                    if (variant != null) {
                        variant.isfav=false
                        CoroutineScope(Dispatchers.IO).launch {
                            ApplicationClass.mydb!!.dao().update(variant)
                            createToastforFavItems("Removed from favourites")
                        }

                    }
                    else throw Throwable("Variant is null")
                    currentList[adapterPosition].isFav = false

                    notifyItemChanged(adapterPosition)


                } else {

                    if (variant != null) {
                        variant.isfav=true
                        CoroutineScope(Dispatchers.IO).launch {
                            ApplicationClass.mydb!!.dao().insert(variant)
                            createToastforFavItems(fragment.getString(R.string.fav_items_added_toast))
                        }
                    }
                    else throw Throwable("Variant is null")
                    currentList[adapterPosition].isFav = true

                    notifyItemChanged(adapterPosition)
                }
            }
        }

        private suspend fun createToastforFavItems(s:String) {
          withContext(Dispatchers.Main){
              Toast.makeText(fragment.context,s,Toast.LENGTH_SHORT).show()
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_each_viewpager_grid_rowlayout, parent, false)
        return CategoryViewHolder(view)
    }


    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        var modelClass = currentList[position]
        Log.d("finding", modelClass.itemName + " " + modelClass.quantityOfItem)
//        if (modelClass.quantityOfItem > 0) {
//            if (displayType != "grid") {
//                holder.addToCart.visibility = View.GONE
//                holder.addOrRemoveItemLinearLayout.visibility = View.VISIBLE
//            }
//            holder.quantityOfItemAddaedToCartTextView.text = modelClass.quantityOfItem.toString()
//        } else {
//            if (displayType != "grid") {
//                holder.addToCart.visibility = View.VISIBLE
//                holder.addOrRemoveItemLinearLayout.visibility = View.GONE
//            }
//        }
        holder.itemDescription.text = modelClass.itemDescriptionText
        holder.realmrp.text =
            " MRP : ${fragment.getString(R.string.Rs)} ${
                modelClass.variantsList?.getOrNull(
                    0
                )?.price
            }"

        holder.itemName.text = modelClass.itemName
        holder.itemNetWeight.text = modelClass.itemNetWeight
        Glide.with(fragment).load(modelClass.imageSrcOfVariants.getOrNull(0)?.imageUrl)
            .into(holder.itemImage)

        if (modelClass.isFav)
            Glide.with(fragment.context!!)
                .load(R.drawable.ic_baseline_favorite_24).into(holder.favouritesImageView)
        else
            Glide.with(fragment.context!!)
                .load(R.drawable.ic_baseline_favorite_border_24).into(holder.favouritesImageView)
    }


}
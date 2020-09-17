package com.example.myfirstofficeappecommerce.Adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.ProductColorModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.ProductFragment
import java.util.*

class ProductColorRecyclerViewAdapter(
    var fragment: ProductFragment,
    var callback: (color: String) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<VariantsModelClass, ProductColorRecyclerViewAdapter.ProductColorViewHolder>
        (VariantsModelClass.diffUtil) {


    inner class ProductColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.productcolorrowTextview)
        var cardView:CardView=itemView.findViewById(R.id.cardviewcolor)

        init {
            cardView.setOnClickListener  {
                if(adapterPosition>=0)
                    callback(currentList[adapterPosition].color.toString())

                currentList.filter { it.isSelected=false
                    return@filter true} as MutableList<VariantsModelClass>
                currentList[adapterPosition].isSelected=true
                notifyDataSetChanged()


           }
        }

    }

    override fun submitList(list: MutableList<VariantsModelClass>?) {
        super.submitList(list?.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductColorViewHolder {
        return ProductColorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_color_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductColorViewHolder, position: Int) {

        var modelClass: VariantsModelClass = currentList[position]

        if(modelClass.isSelected)
            holder.cardView.cardElevation=20f
        else
            holder.cardView.cardElevation=0f

        Log.d("modelclasscolor",modelClass.color.toString())
        when {
            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
                holder.imageView.setBackgroundColor(Color.RED)


            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "b" -> {
                Log.d("modelclasscolorr",  modelClass.color!!.toLowerCase(Locale.ROOT))
                if (modelClass.color!!.toLowerCase(Locale.ROOT) == "black")
                    holder.imageView.setBackgroundColor(Color.BLACK)
                else
                holder.imageView.setBackgroundColor(Color.BLUE)

            }
            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "g" -> {
                if (modelClass.color!!.toLowerCase(Locale.ROOT) == "gre")
                    holder.imageView.setBackgroundColor(Color.GREEN)
                else holder.imageView.setBackgroundColor(Color.GRAY)
            }



            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "w" ->
                holder.imageView.setBackgroundColor(Color.WHITE)


            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "p" ->
                holder.imageView.setBackgroundColor(Color.rgb(255, 192, 203))


            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "y" -> holder.imageView.setBackgroundColor(
                Color.YELLOW
            )
            else ->{
                holder.imageView.visibility=View.GONE
            }

//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
//            modelClass.color!!.toLowerCase(Locale.ROOT)[0].toString() == "r" ->
//                holder.imageView.setBackgroundColor(Color.RED)
        }
    }


}
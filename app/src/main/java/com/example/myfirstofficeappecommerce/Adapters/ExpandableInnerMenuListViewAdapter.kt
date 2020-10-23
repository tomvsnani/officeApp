package com.example.myfirstofficeappecommerce.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myfirstofficeappecommerce.*

class ExpandableInnerMenuListViewAdapter(
    var groupNameList: List<Item>,
    var hashmap: HashMap<Item, List<Nesteditem>>, var context: MainActivity
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return groupNameList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {

        return groupNameList[groupPosition].nesteditems.size
    }

    override fun getGroup(groupPosition: Int): Item {
        return groupNameList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Nesteditem {
        return groupNameList[groupPosition].nesteditems[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        if (convertView == null) {
            var view =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.menu_expandablelist_child_layout,
                    null,
                    false
                )
            view.findViewById<TextView>(R.id.expandablelistchildlayouttextview).text =
                getGroup(groupPosition).name
            view.findViewById<ImageView>(R.id.expandablelistchildlayoutimageview).visibility=View.VISIBLE
            return view
        } else
            return convertView

    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        var view: View? = null

        view =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.menu_expandablelist_child_layout,
                null,
                false
            )
        view.findViewById<View>(R.id.childdivider).visibility=View.GONE
        view.findViewById<TextView>(R.id.expandablelistchildlayouttextview).text =
            getChild(groupPosition, childPosition).name
        view.findViewById<ImageView>(R.id.expandablelistchildlayoutimageview).visibility=View.INVISIBLE


        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
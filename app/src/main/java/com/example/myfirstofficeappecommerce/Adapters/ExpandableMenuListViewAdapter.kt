package com.example.myfirstofficeappecommerce.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.Item
import com.example.myfirstofficeappecommerce.Models.Menu
import com.example.myfirstofficeappecommerce.Models.Nesteditem
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment
import com.example.myfirstofficeappecommerce.fragments.WebViewFragment

class ExpandableMenuListViewAdapter(
    var groupNameList: List<Menu>,
    var hashmap: HashMap<Menu, List<Item>>, var context: MainActivity
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return groupNameList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {

        return groupNameList[groupPosition].items.size
    }

    override fun getGroup(groupPosition: Int): Menu {
        return groupNameList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Item {
        return groupNameList[groupPosition].items[childPosition]
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
                    R.layout.menu_expandable_list_parent_layout,
                    null,
                    false
                )
            view.findViewById<TextView>(R.id.expandablelistparentlayouttextview).text =
                getGroup(groupPosition).groupname
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

        if (groupNameList[groupPosition].items[childPosition].type != "nested") {
            view =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.menu_expandablelist_child_layout,
                    null,
                    false
                )

            view.findViewById<TextView>(R.id.expandablelistchildlayouttextview).text =
                getChild(groupPosition, childPosition).name
            if (groupNameList[groupPosition].items[childPosition].icon.isNotEmpty())
                Glide.with(context).load(groupNameList[groupPosition].items[childPosition].icon)
                    .into(view.findViewById<ImageView>(R.id.expandablelistchildlayoutimageview))

            if (isLastChild) {
                view.findViewById<View>(R.id.childdivider).visibility = View.VISIBLE
                Log.d("islast", isLastChild.toString())
            } else
                view.findViewById<View>(R.id.childdivider).visibility = View.GONE

        } else {
            view =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.innermenu_expandable_listview_layout,
                    parent,
                    false
                )
//
            var expandablelist =
                view!!.findViewById<ExpandableListView>(R.id.innermenuexpandablelist)

            var hashmap: HashMap<Item, List<Nesteditem>> = HashMap()
            var nesteditems = getChild(groupPosition, childPosition).nesteditems
            var adapter = ExpandableInnerMenuListViewAdapter(
                listOf(getChild(groupPosition, childPosition)),
                hashmap, context
            )
            hashmap[getChild(groupPosition, childPosition)] =
                getChild(groupPosition, childPosition).nesteditems
            expandablelist.setAdapter(
                adapter

            )


            expandablelist.setOnGroupExpandListener {
                expandablelist.layoutParams.height =
                    getChild(groupPosition, childPosition).nesteditems.size * 200
            }
            expandablelist.setOnGroupCollapseListener {
                if (expandablelist.childCount > 0)
                    expandablelist.layoutParams.height =
                        expandablelist.getChildAt(0).measuredHeight
            }

            expandablelist.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

                context.opencloseDrawerLayout()

                when (nesteditems[childPosition].type) {
                    "collection" -> {

                        if (nesteditems[childPosition].typeid.isNotEmpty()) {
                            context.supportFragmentManager
                                .beginTransaction()
                                .replace(
                                    R.id.container,
                                    CategoryEachViewPagerFragment(
                                        CategoriesModelClass(
                                            id = nesteditems[childPosition].typeid
                                        )
                                    ) {}
                                ).addToBackStack(null)
                                .commit()

                        } else {


                            context.supportFragmentManager
                                .beginTransaction()
                                .replace(
                                    R.id.container,
                                    CategoryEachViewPagerFragment(ApplicationClass.menucategorylist[0]) {}
                                ).addToBackStack(null)
                                .commit()


                        }
                    }
                    "blog" -> {

                        context.supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                WebViewFragment(nesteditems[childPosition].url, "")
                            ).addToBackStack(null)
                            .commit()
                    }
                    "page" -> {
                        context.supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                WebViewFragment(nesteditems[childPosition].url, "")
                            ).addToBackStack(null)
                            .commit()
                    }
                }


                return@setOnChildClickListener true
            }


        }
        return view!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
package com.dypcet.g1.batterysaversystem.appdetail

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.dypcet.g1.batterysaversystem.R

class MyExpandableListAdapter(
    private val titleList: List<String>,
    private val detailList: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return titleList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return detailList[titleList[groupPosition]]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return titleList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return detailList[titleList[groupPosition]]!![childPosition]
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
        val listTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            return LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_group, parent, false)
                .apply {
                    findViewById<TextView>(R.id.list_title).text = listTitle
                }
        }
        convertView.apply {
            findViewById<TextView>(R.id.list_title).apply {
                text = listTitle
                setTypeface(null, Typeface.BOLD)
            }
        }
        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val expandedListText = getChild(groupPosition, childPosition) as String
        if (convertView == null) {
            return LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_item, parent, false)
                .apply {
                    this.findViewById<TextView>(R.id.expandable_list_item).text = expandedListText
                }
        }
        convertView.apply {
            findViewById<TextView>(R.id.expandable_list_item).text = expandedListText
        }
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
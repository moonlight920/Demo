package com.okay.demo

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yuefeng.lib.BubblePopupWindow
import com.yuefeng.lib.XGravity
import com.yuefeng.lib.YGravity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Demo1Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            param1 = getString(ARG_PARAM1)
            param2 = getString(ARG_PARAM2)
        }
    }

    var xGravity = XGravity.LEFT
    var yGravity = YGravity.ALIGN_BOTTOM
    var mXOff = 0
    var mYOff = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_demo1, container, false)

        view.findViewById<Button>(R.id.btnCenter).setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.example_list, null)
            val listView = view.findViewById<ListView>(R.id.listView)

            listView.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf("ABCDEFG", "HIJKLMN", "OPQ RST","UVW XYZ"))
            setListViewBasedOnChildren(listView)
            val pop = BubblePopupWindow.create(context!!, view)
            pop.showAtAnchorView(it, xGravity, yGravity, mXOff, mYOff)
        }

        view.findViewById<RadioGroup>(R.id.xRadioGroup).setOnCheckedChangeListener { group, checkedId ->
            xGravity = when (checkedId) {
                R.id.x_left -> XGravity.LEFT
                R.id.x_right -> XGravity.RIGHT
                R.id.x_center -> XGravity.CENTER
                R.id.x_align_left -> XGravity.ALIGN_LEFT
                R.id.x_align_right -> XGravity.ALIGN_RIGHT
                else -> XGravity.CENTER
            }
        }
        view.findViewById<RadioGroup>(R.id.yRadioGroup).setOnCheckedChangeListener { group, checkedId ->
            yGravity = when (checkedId) {
                R.id.y_above -> YGravity.ABOVE
                R.id.y_below -> YGravity.BELOW
                R.id.y_align_bottom -> YGravity.ALIGN_BOTTOM
                R.id.y_align_top -> YGravity.ALIGN_TOP
                R.id.y_center -> YGravity.CENTER
                else -> YGravity.CENTER
            }
        }
        view.findViewById<SeekBar>(R.id.xOff).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mXOff = (progress - 50) * 10
            }

        })
        view.findViewById<SeekBar>(R.id.yOff).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mYOff = (progress - 50) * 10
            }

        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            Demo1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun setListViewBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        var maxWidth = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
            val width = listItem.measuredWidth
            if (width > maxWidth) maxWidth = width
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        params.width = maxWidth
        listView.layoutParams = params
    }
}

package com.okay.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yuefeng.lib.XGravity
import com.yuefeng.lib.YGravity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Demo1Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnCenter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            param1 = getString(ARG_PARAM1)
            param2 = getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_demo1, container, false)

        view.findViewById<Button>(R.id.btnCenter).setOnClickListener {
            var pop = BubblePopupWindow.create(context!!).build()
//            pop.showAtAnchorView(it, XGravity.ALIGN_RIGHT, YGravity.BELOW, 30, 0)
            pop.showAtAnchorView(it, XGravity.ALIGN_LEFT, YGravity.BELOW, 30, 0)
        }


        view.findViewById<Button>(R.id.btnLeftTop).setOnClickListener {
            var pop = BubblePopupWindow.create(context!!).build()
            pop.showAtAnchorView(it, XGravity.CENTER, YGravity.BELOW, 0, 0)

        }
        view.findViewById<Button>(R.id.btnLeftBottom).setOnClickListener {
            var pop = BubblePopupWindow.create(context!!).build()
            pop.showAtAnchorView(it, XGravity.CENTER, YGravity.BELOW, 0, 0)

        }
        view.findViewById<Button>(R.id.btnRightTop).setOnClickListener {
            var pop = BubblePopupWindow.create(context!!).build()
            pop.showAtAnchorView(it, XGravity.CENTER, YGravity.BELOW, 0, 0)

        }
        view.findViewById<Button>(R.id.btnRightBottom).setOnClickListener {
            var pop = BubblePopupWindow.create(context!!).build()
            pop.showAtAnchorView(it, XGravity.CENTER, YGravity.BELOW, 0, 0)

        }

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
}

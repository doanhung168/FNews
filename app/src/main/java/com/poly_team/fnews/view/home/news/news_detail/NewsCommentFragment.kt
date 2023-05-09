package com.poly_team.fnews.view.home.news.news_detail

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poly_team.fnews.databinding.LayoutCommentBinding
import com.poly_team.fnews.utility.text
import javax.inject.Inject


class NewsCommentFragment @Inject constructor(
    private val mNewsContentViewModel: NewsContentViewModel
) : BottomSheetDialogFragment() {

    private val TAG = "NewsCommentFragment"

    private lateinit var mBinding: LayoutCommentBinding

    private var mCommentAdapter: NewsCommentAdapter? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { layout ->
                val behaviour = BottomSheetBehavior.from(layout)
                setupFullHeight(layout)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        val window: Window? = dialog.window

        if (window != null) {
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LayoutCommentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCommentAdapter = NewsCommentAdapter()
        mCommentAdapter?.setListener {
            Log.i(TAG, "onClick: $it")
        }

        with(mBinding) {
            rcvComments.adapter = mCommentAdapter
            tvInterestingComment.setOnClickListener {
                mNewsContentViewModel.getCommentOfNews(0)
            }

            tvNewComment.setOnClickListener {
                mNewsContentViewModel.getCommentOfNews(1)
            }

            edtCommentContent.let {
                it.setEndIconOnClickListener {
                    val commentContent = edtCommentContent.text()
                    if (commentContent.isNotEmpty()) {
                        mNewsContentViewModel.sendComment(commentContent, 0)
                    } else {
                        Toast.makeText(
                            context,
                            "Vui lòng nhập nội dung bình luận!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        mNewsContentViewModel._mCurrentNewsComment.observe(viewLifecycleOwner) {
            Log.i(TAG, " current news comment: ${it.size}")
            mCommentAdapter?.submitList(it)
        }
    }


}


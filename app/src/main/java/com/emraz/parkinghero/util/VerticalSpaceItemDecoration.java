package com.emraz.parkinghero.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Yousuf on 7/28/2017.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

  private final int mVerticalSpaceHeight;

  public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
    this.mVerticalSpaceHeight = mVerticalSpaceHeight;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
      outRect.bottom = mVerticalSpaceHeight;
    }

  }

}

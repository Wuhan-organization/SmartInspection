package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {

	public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshLinearLayout(Context context, Mode mode, AnimationStyle animStyle) {
		super(context, mode, animStyle);
	}

	public PullToRefreshLinearLayout(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
	}

	public PullToRefreshLinearLayout(Context context) {
		super(context);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected LinearLayout createRefreshableView(Context context,
			AttributeSet attrs) {
		LinearLayout linearLayout = new LinearLayout(context, attrs);

		linearLayout.setId(R.id.linearLayout);
		return linearLayout;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullStart() {
		View Child = mRefreshableView.getChildAt(0);
		if (null != Child) {
			return mRefreshableView.getScrollY() >= (Child.getHeight() - getHeight());
		}
		return false;
	}

}

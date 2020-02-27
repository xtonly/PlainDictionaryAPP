/*
 * Copyright (C) 2016 Tobias Rohloff
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.knziha.plod.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.knziha.plod.PlainDict.CMN;

/**
 * https://github.com/tobiasrohloff/NestedScrollWebView/edit/master/lib/src/main/java/com/tobiasrohloff/view/NestedScrollWebView.java
 */
public class AdvancedNestScrollView extends ScrollViewmy implements NestedScrollingChild {
	int mLastMotionY;
	boolean mNestedScrollEnabled;

	private final int[] mScrollOffset = new int[2];
	private final int[] mScrollConsumed = new int[2];

	private int mNestedYOffset;

	private NestedScrollingChildHelper mChildHelper;

	public AdvancedNestScrollView(Context context) {
		super(context);
	}

	public AdvancedNestScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AdvancedNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mNestedScrollEnabled) {
			MotionEvent trackedEvent = MotionEvent.obtain(event);

			final int action = event.getActionMasked();

			if (action == MotionEvent.ACTION_DOWN) {
				mNestedYOffset = 0;
			}

			int y = (int) event.getY();

			event.offsetLocation(0, mNestedYOffset);

			switch (action) {
				case MotionEvent.ACTION_DOWN:
					mLastMotionY = y;
					startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
					break;
				case MotionEvent.ACTION_MOVE:
					//CMN.Log("...ACTION_MOVE");
					int deltaY = mLastMotionY - y;

					if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
						deltaY -= mScrollConsumed[1];
						trackedEvent.offsetLocation(0, mScrollOffset[1]);
						mNestedYOffset += mScrollOffset[1];
					}

					mLastMotionY = y - mScrollOffset[1];

					int oldY = getScrollY();
					int newScrollY = Math.max(0, oldY + deltaY);
					int dyConsumed = newScrollY - oldY;
					int dyUnconsumed = deltaY - dyConsumed;

					if (dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, mScrollOffset)) {
						mLastMotionY -= mScrollOffset[1];
						trackedEvent.offsetLocation(0, mScrollOffset[1]);
						mNestedYOffset += mScrollOffset[1];
					}
					trackedEvent.recycle();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					//CMN.Log("ACTION_UP");
					stopNestedScroll();
					break;
			}
			//if(OrgTop-getTop()==0)
			super.onTouchEvent(event);
			return true;
		}
		return super.onTouchEvent(event);
	}

	// NestedScrollingChild

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		mNestedScrollEnabled=enabled;
		if(mChildHelper==null){
			mChildHelper = new NestedScrollingChildHelper(this);
			mChildHelper.setNestedScrollingEnabled(enabled);
		} else {
			mChildHelper.setNestedScrollingEnabled(enabled);
		}
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		return mNestedScrollEnabled&&mChildHelper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes) {
		return mNestedScrollEnabled&&mChildHelper.startNestedScroll(axes);
	}

	@Override
	public void stopNestedScroll() {
		if(mChildHelper!=null)
			mChildHelper.stopNestedScroll();
	}

	@Override
	public boolean hasNestedScrollingParent() {
		return mNestedScrollEnabled&&mChildHelper.hasNestedScrollingParent();
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
		return mNestedScrollEnabled&&mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
		return mNestedScrollEnabled&&mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		return mNestedScrollEnabled&&mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		return mNestedScrollEnabled&&mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
	}
}

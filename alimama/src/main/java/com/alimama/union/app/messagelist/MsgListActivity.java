package com.alimama.union.app.messagelist;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.moon.view.BackToTopView;
import com.alimama.moon.view.design.PageStatusView;
import com.alimama.union.app.messagelist.network.MessageListRequest;
import com.alimama.union.app.messagelist.network.MessageListResponse;
import com.alimama.union.app.pagerouter.IUTPage;
import com.alimama.union.app.toolCenter.view.SimpleItemDecoration;
import com.alimama.unionwl.uiframe.views.base.ISPtrFrameLayout;
import com.alimama.unionwl.uiframe.views.base.ISPtrHeaderView;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import com.alimamaunion.common.listpage.EndlessRecyclerOnScrollListener;
import in.srain.cube.ptr.PtrDefaultHandler;
import in.srain.cube.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class MsgListActivity extends BaseActivity implements IUTPage {
    private CommonRecyclerAdapter mAdapter;
    /* access modifiers changed from: private */
    public List<CommonItemInfo> mList = new ArrayList();
    /* access modifiers changed from: private */
    public PageStatusView mMsgPageView;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    private final View.OnClickListener mRefreshListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (MsgListActivity.this.messageListRequest != null) {
                MsgListActivity.this.mMsgPageView.onLoading();
                MsgListActivity.this.messageListRequest.queryFirstPage();
                MsgListActivity.this.mList.clear();
            }
        }
    };
    /* access modifiers changed from: private */
    public MessageListRequest messageListRequest;
    private ISPtrFrameLayout ptrFrameLayout;

    public String getCurrentPageName() {
        return UTHelper.PAGE_NAME_MSGLIST_ACTIVITY;
    }

    public String getCurrentSpmCnt() {
        return UTHelper.SPM_CNT_MSGLIST_ACTIVITY;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        initView();
        this.mMsgPageView.onLoading();
        this.messageListRequest = new MessageListRequest();
        this.messageListRequest.queryFirstPage();
        this.mList.clear();
    }

    private void initView() {
        setContentView((int) R.layout.activty_new_messagelist_layout);
        this.mMsgPageView = (PageStatusView) findViewById(R.id.message_layout_container);
        this.mMsgPageView.setRefreshListener(this.mRefreshListener);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), true);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.message_container);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, 1, false);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        this.mAdapter = new CommonRecyclerAdapter();
        this.mAdapter.setEnableFooter(true);
        this.mAdapter.setFooterProcesser(new CommonFooterProcess());
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new SimpleItemDecoration(LocalDisplay.dp2px(12.0f)));
        this.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            public void onLoadMore(int i) {
                if (MsgListActivity.this.messageListRequest != null) {
                    MsgListActivity.this.messageListRequest.queryNextPage();
                }
            }
        });
        ((BackToTopView) findViewById(R.id.back_to_top)).bindRecyclerView(this.mRecyclerView);
        this.ptrFrameLayout = (ISPtrFrameLayout) findViewById(R.id.pull_refresh_layout);
        ISPtrHeaderView iSPtrHeaderView = new ISPtrHeaderView(this);
        this.ptrFrameLayout.initView(iSPtrHeaderView, iSPtrHeaderView);
        this.ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                return super.checkCanDoRefresh(ptrFrameLayout, MsgListActivity.this.mRecyclerView, view2);
            }

            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                MsgListActivity.this.messageListRequest.queryFirstPage();
                MsgListActivity.this.mList.clear();
            }
        });
    }

    @Subscribe
    public void onMessageListDataEvent(MessageListDataEvent messageListDataEvent) {
        this.ptrFrameLayout.refreshComplete();
        if (messageListDataEvent.isSuccess || !this.messageListRequest.isFirstPage()) {
            if (!messageListDataEvent.isSuccess && !this.messageListRequest.isFirstPage()) {
                this.mAdapter.setFinish(true);
                this.mAdapter.notifyDataSetChanged();
            } else if (!this.mList.isEmpty() || (messageListDataEvent.dataResult != null && !messageListDataEvent.dataResult.messageListItems.isEmpty())) {
                this.messageListRequest.clearLoadingState();
                MessageListResponse messageListResponse = messageListDataEvent.dataResult;
                this.messageListRequest.setHasMore(messageListDataEvent.hasMore());
                this.mAdapter.setFinish(true ^ messageListDataEvent.hasMore());
                if (messageListResponse.messageListItems != null && !messageListResponse.messageListItems.isEmpty()) {
                    this.mList.addAll(messageListDataEvent.dataResult.messageListItems);
                    this.mAdapter.notifyResult(this.messageListRequest.isFirstPage(), messageListDataEvent.dataResult.messageListItems);
                    this.mAdapter.notifyDataSetChanged();
                }
                this.mMsgPageView.onContentLoaded();
            } else {
                this.mMsgPageView.onEmptyData(R.string.msg_center_empty);
            }
        } else if (messageListDataEvent.isEmptyData()) {
            this.mMsgPageView.onEmptyData(R.string.msg_center_empty);
        } else if (messageListDataEvent.isNetworkError()) {
            this.mMsgPageView.onNetworkError();
        } else {
            this.mMsgPageView.onServerError();
        }
    }
}

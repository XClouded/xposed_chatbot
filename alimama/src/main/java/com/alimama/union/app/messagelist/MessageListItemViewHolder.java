package com.alimama.union.app.messagelist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.union.app.messagelist.network.MessageListItem;
import com.alimama.union.app.messagelist.network.MessageListReadRequest;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;
import java.util.HashMap;

public class MessageListItemViewHolder implements CommonBaseViewHolder<MessageListItem> {
    private static final String MESSAGE_READ = "2";
    /* access modifiers changed from: private */
    public EtaoDraweeView arrowImg;
    private TextView mCategory;
    /* access modifiers changed from: private */
    public Context mContext;
    private EtaoDraweeView mIconImage;
    private View mMessageItemContainer;
    private TextView mTime;
    /* access modifiers changed from: private */
    public TextView mTitle;
    private View mView;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.mContext = viewGroup.getContext();
        this.mView = layoutInflater.inflate(R.layout.messagecenter_tab_item_layout, viewGroup, false);
        this.mIconImage = (EtaoDraweeView) this.mView.findViewById(R.id.message_icon);
        this.mCategory = (TextView) this.mView.findViewById(R.id.message_category);
        this.mTitle = (TextView) this.mView.findViewById(R.id.message_title);
        this.arrowImg = (EtaoDraweeView) this.mView.findViewById(R.id.message_arrow);
        this.mTime = (TextView) this.mView.findViewById(R.id.message_time);
        this.mMessageItemContainer = this.mView.findViewById(R.id.message_item_container);
        return this.mView;
    }

    public void onBindViewHolder(int i, final MessageListItem messageListItem) {
        this.mIconImage.setAnyImageUrl(messageListItem.msgIcon);
        this.mCategory.setText(messageListItem.topCategoryName);
        this.mTitle.setText(messageListItem.msgTitle);
        this.mTime.setText(messageListItem.gmtCreate);
        if (!TextUtils.isEmpty(messageListItem.msgUrl)) {
            this.arrowImg.setVisibility(0);
        } else {
            this.arrowImg.setVisibility(8);
        }
        if ("2".equals(messageListItem.readStatus)) {
            this.arrowImg.setAnyImageRes(this.mContext, R.drawable.message_read_arrow);
            this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.gray_text_color));
        } else {
            this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.light_black));
            this.arrowImg.setAnyImageRes(this.mContext, R.drawable.message_unread_arrow);
        }
        this.mMessageItemContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!"2".equals(messageListItem.readStatus)) {
                    MessageListItemViewHolder.this.arrowImg.setAnyImageRes(MessageListItemViewHolder.this.mContext, R.drawable.message_read_arrow);
                    MessageListItemViewHolder.this.mTitle.setTextColor(MessageListItemViewHolder.this.mContext.getResources().getColor(R.color.gray_text_color));
                    new MessageListReadRequest().appendParam("timestamp", messageListItem.timestamp).sendRequest();
                    messageListItem.readStatus = "2";
                }
                if (!TextUtils.isEmpty(messageListItem.msgUrl)) {
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(SpmProcessor.appendSpm(messageListItem.msgUrl, UTHelper.SPM_CNT_MSGLIST_ACTIVITY));
                }
                MessageListItemViewHolder.this.addSpmClick(messageListItem.topCategoryName);
            }
        });
    }

    /* access modifiers changed from: private */
    public void addSpmClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("category", str);
        UTHelper.sendControlHit(UTHelper.PAGE_NAME_MSGLIST_ACTIVITY, UTHelper.SPM_CLICK_MSG, hashMap);
    }
}

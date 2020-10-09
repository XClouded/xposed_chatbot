package com.alimama.union.app.taotokenConvert;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.union.app.network.response.TaoCodeItemInfo;

public class TaoCodeTransferEvent {
    @Nullable
    private String mClipboardContent;
    @Nullable
    private String mErrorMessage;
    @Nullable
    private TaoCodeItemInfo mItemInfo;
    @Nullable
    private String mItemRawUrl;
    @NonNull
    private final TransferResult mTransferResult;

    public enum TransferResult {
        TAO_CODE_ITEM,
        TAO_CODE_ITEM_NOT_FOUND,
        TAO_CODE_NOT_SUPPORTED,
        TAO_CODE_ITEM_TITLE,
        TAO_CODE_PERMISSION_DENIED,
        TAO_CODE_WORSHIP_INVITE
    }

    private TaoCodeTransferEvent(@NonNull TransferResult transferResult) {
        this.mTransferResult = transferResult;
    }

    public static TaoCodeTransferEvent itemTitle(String str) {
        TaoCodeTransferEvent taoCodeTransferEvent = new TaoCodeTransferEvent(TransferResult.TAO_CODE_ITEM_TITLE);
        taoCodeTransferEvent.mClipboardContent = str;
        return taoCodeTransferEvent;
    }

    public static TaoCodeTransferEvent permissionDenied() {
        return new TaoCodeTransferEvent(TransferResult.TAO_CODE_PERMISSION_DENIED);
    }

    public static TaoCodeTransferEvent itemNotFound(String str, String str2) {
        TaoCodeTransferEvent taoCodeTransferEvent = new TaoCodeTransferEvent(TransferResult.TAO_CODE_ITEM_NOT_FOUND);
        taoCodeTransferEvent.mErrorMessage = str2;
        taoCodeTransferEvent.mItemRawUrl = str;
        return taoCodeTransferEvent;
    }

    public static TaoCodeTransferEvent taoCodeNotSupported(String str) {
        TaoCodeTransferEvent taoCodeTransferEvent = new TaoCodeTransferEvent(TransferResult.TAO_CODE_NOT_SUPPORTED);
        taoCodeTransferEvent.mErrorMessage = str;
        return taoCodeTransferEvent;
    }

    public static TaoCodeTransferEvent success(String str, TaoCodeItemInfo taoCodeItemInfo) {
        TaoCodeTransferEvent taoCodeTransferEvent = new TaoCodeTransferEvent(TransferResult.TAO_CODE_ITEM);
        taoCodeTransferEvent.mClipboardContent = str;
        taoCodeTransferEvent.mItemInfo = taoCodeItemInfo;
        return taoCodeTransferEvent;
    }

    public static TaoCodeTransferEvent worshipInviteTaoCode(TaoCodeItemInfo taoCodeItemInfo) {
        TaoCodeTransferEvent taoCodeTransferEvent = new TaoCodeTransferEvent(TransferResult.TAO_CODE_WORSHIP_INVITE);
        taoCodeTransferEvent.mItemInfo = taoCodeItemInfo;
        return taoCodeTransferEvent;
    }

    @Nullable
    public TaoCodeItemInfo getItemInfo() {
        return this.mItemInfo;
    }

    @Nullable
    public String getClipboardContent() {
        return this.mClipboardContent;
    }

    @Nullable
    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    @Nullable
    public String getItemRawUrl() {
        return this.mItemRawUrl;
    }

    public TransferResult getTransferResult() {
        return this.mTransferResult;
    }
}

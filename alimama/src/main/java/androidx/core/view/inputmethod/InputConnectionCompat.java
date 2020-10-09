package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean handlePerformPrivateCommand(@androidx.annotation.Nullable java.lang.String r8, @androidx.annotation.NonNull android.os.Bundle r9, @androidx.annotation.NonNull androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener r10) {
        /*
            r0 = 0
            if (r9 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.String r1 = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT"
            boolean r1 = android.text.TextUtils.equals(r1, r8)
            r2 = 1
            if (r1 == 0) goto L_0x000f
            r8 = 0
            goto L_0x0018
        L_0x000f:
            java.lang.String r1 = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT"
            boolean r8 = android.text.TextUtils.equals(r1, r8)
            if (r8 == 0) goto L_0x0088
            r8 = 1
        L_0x0018:
            r1 = 0
            if (r8 == 0) goto L_0x0021
            java.lang.String r3 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER"
            goto L_0x0023
        L_0x001e:
            r8 = move-exception
            r3 = r1
            goto L_0x0082
        L_0x0021:
            java.lang.String r3 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER"
        L_0x0023:
            android.os.Parcelable r3 = r9.getParcelable(r3)     // Catch:{ all -> 0x001e }
            android.os.ResultReceiver r3 = (android.os.ResultReceiver) r3     // Catch:{ all -> 0x001e }
            if (r8 == 0) goto L_0x0030
            java.lang.String r4 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI"
            goto L_0x0032
        L_0x002e:
            r8 = move-exception
            goto L_0x0082
        L_0x0030:
            java.lang.String r4 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI"
        L_0x0032:
            android.os.Parcelable r4 = r9.getParcelable(r4)     // Catch:{ all -> 0x002e }
            android.net.Uri r4 = (android.net.Uri) r4     // Catch:{ all -> 0x002e }
            if (r8 == 0) goto L_0x003d
            java.lang.String r5 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION"
            goto L_0x003f
        L_0x003d:
            java.lang.String r5 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION"
        L_0x003f:
            android.os.Parcelable r5 = r9.getParcelable(r5)     // Catch:{ all -> 0x002e }
            android.content.ClipDescription r5 = (android.content.ClipDescription) r5     // Catch:{ all -> 0x002e }
            if (r8 == 0) goto L_0x004a
            java.lang.String r6 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI"
            goto L_0x004c
        L_0x004a:
            java.lang.String r6 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI"
        L_0x004c:
            android.os.Parcelable r6 = r9.getParcelable(r6)     // Catch:{ all -> 0x002e }
            android.net.Uri r6 = (android.net.Uri) r6     // Catch:{ all -> 0x002e }
            if (r8 == 0) goto L_0x0057
            java.lang.String r7 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"
            goto L_0x0059
        L_0x0057:
            java.lang.String r7 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"
        L_0x0059:
            int r7 = r9.getInt(r7)     // Catch:{ all -> 0x002e }
            if (r8 == 0) goto L_0x0062
            java.lang.String r8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS"
            goto L_0x0064
        L_0x0062:
            java.lang.String r8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS"
        L_0x0064:
            android.os.Parcelable r8 = r9.getParcelable(r8)     // Catch:{ all -> 0x002e }
            android.os.Bundle r8 = (android.os.Bundle) r8     // Catch:{ all -> 0x002e }
            if (r4 == 0) goto L_0x0078
            if (r5 == 0) goto L_0x0078
            androidx.core.view.inputmethod.InputContentInfoCompat r9 = new androidx.core.view.inputmethod.InputContentInfoCompat     // Catch:{ all -> 0x002e }
            r9.<init>(r4, r5, r6)     // Catch:{ all -> 0x002e }
            boolean r8 = r10.onCommitContent(r9, r7, r8)     // Catch:{ all -> 0x002e }
            goto L_0x0079
        L_0x0078:
            r8 = 0
        L_0x0079:
            if (r3 == 0) goto L_0x0081
            if (r8 == 0) goto L_0x007e
            r0 = 1
        L_0x007e:
            r3.send(r0, r1)
        L_0x0081:
            return r8
        L_0x0082:
            if (r3 == 0) goto L_0x0087
            r3.send(r0, r1)
        L_0x0087:
            throw r8
        L_0x0088:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.view.inputmethod.InputConnectionCompat.handlePerformPrivateCommand(java.lang.String, android.os.Bundle, androidx.core.view.inputmethod.InputConnectionCompat$OnCommitContentListener):boolean");
    }

    public static boolean commitContent(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull InputContentInfoCompat inputContentInfoCompat, int i, @Nullable Bundle bundle) {
        boolean z;
        ClipDescription description = inputContentInfoCompat.getDescription();
        String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        int length = contentMimeTypes.length;
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                z = false;
                break;
            } else if (description.hasMimeType(contentMimeTypes[i2])) {
                z = true;
                break;
            } else {
                i2++;
            }
        }
        if (!z) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo) inputContentInfoCompat.unwrap(), i, bundle);
        }
        switch (EditorInfoCompat.getProtocol(editorInfo)) {
            case 2:
                z2 = true;
                break;
            case 3:
            case 4:
                break;
            default:
                return false;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable(z2 ? COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY : COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfoCompat.getContentUri());
        bundle2.putParcelable(z2 ? COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY : COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfoCompat.getDescription());
        bundle2.putParcelable(z2 ? COMMIT_CONTENT_LINK_URI_INTEROP_KEY : COMMIT_CONTENT_LINK_URI_KEY, inputContentInfoCompat.getLinkUri());
        bundle2.putInt(z2 ? COMMIT_CONTENT_FLAGS_INTEROP_KEY : COMMIT_CONTENT_FLAGS_KEY, i);
        bundle2.putParcelable(z2 ? COMMIT_CONTENT_OPTS_INTEROP_KEY : COMMIT_CONTENT_OPTS_KEY, bundle);
        return inputConnection.performPrivateCommand(z2 ? COMMIT_CONTENT_INTEROP_ACTION : COMMIT_CONTENT_ACTION, bundle2);
    }

    @NonNull
    public static InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (Build.VERSION.SDK_INT >= 25) {
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
                    if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), i, bundle)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, i, bundle);
                }
            };
        } else {
            if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                return inputConnection;
            }
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean performPrivateCommand(String str, Bundle bundle) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(str, bundle, onCommitContentListener)) {
                        return true;
                    }
                    return super.performPrivateCommand(str, bundle);
                }
            };
        }
    }
}

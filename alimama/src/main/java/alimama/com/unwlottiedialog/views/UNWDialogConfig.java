package alimama.com.unwlottiedialog.views;

public class UNWDialogConfig {
    private String content;
    private int contentGravity = 17;
    private boolean isVertical;
    private String jumpUrl;
    private CallBack leftCallback;
    private String leftText;
    private CallBack rightCallback;
    private String rightText;
    private CallBack showCallback;
    private String spanStr;
    private String title;

    public interface CallBack {
        void callback();
    }

    public UNWDialogConfig setTitle(String str) {
        this.title = str;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public UNWDialogConfig setContent(String str) {
        this.content = str;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public UNWDialogConfig setVertical(boolean z) {
        this.isVertical = z;
        return this;
    }

    public boolean isVertical() {
        return this.isVertical;
    }

    public UNWDialogConfig setShowCallback(CallBack callBack) {
        this.showCallback = callBack;
        return this;
    }

    public CallBack getShowCallback() {
        return this.showCallback;
    }

    public UNWDialogConfig setLeftCallback(CallBack callBack) {
        this.leftCallback = callBack;
        return this;
    }

    public UNWDialogConfig setRightCallback(CallBack callBack) {
        this.rightCallback = callBack;
        return this;
    }

    public UNWDialogConfig setRightText(String str) {
        this.rightText = str;
        return this;
    }

    public UNWDialogConfig setSpanStr(String str) {
        this.spanStr = str;
        return this;
    }

    public UNWDialogConfig setJumpUrl(String str) {
        this.jumpUrl = str;
        return this;
    }

    public UNWDialogConfig setLeftText(String str) {
        this.leftText = str;
        return this;
    }

    public String getSpanStr() {
        return this.spanStr;
    }

    public String getJumpUrl() {
        return this.jumpUrl;
    }

    public CallBack getLeftCallback() {
        return this.leftCallback;
    }

    public String getLeftText() {
        return this.leftText;
    }

    public CallBack getRightCallback() {
        return this.rightCallback;
    }

    public String getRightText() {
        return this.rightText;
    }

    public UNWDialogConfig setContentGravity(int i) {
        this.contentGravity = i;
        return this;
    }

    public int getContentGravity() {
        return this.contentGravity;
    }
}

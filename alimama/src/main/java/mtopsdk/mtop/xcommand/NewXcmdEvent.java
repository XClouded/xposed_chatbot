package mtopsdk.mtop.xcommand;

public class NewXcmdEvent {
    String value;

    public NewXcmdEvent(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }
}

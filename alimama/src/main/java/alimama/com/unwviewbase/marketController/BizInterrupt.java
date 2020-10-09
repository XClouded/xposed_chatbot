package alimama.com.unwviewbase.marketController;

import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwviewbase.marketController.UNWDialogController;
import java.util.Map;

public interface BizInterrupt {
    UNWDialogController.InterruptStyle isInterrupt(Map<String, String> map, UNWAbstractDialog uNWAbstractDialog);
}

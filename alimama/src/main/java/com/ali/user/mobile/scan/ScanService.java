package com.ali.user.mobile.scan;

import com.ali.user.mobile.scan.model.CommonScanParam;
import com.ali.user.mobile.scan.model.CommonScanResponse;
import com.ali.user.mobile.scan.model.ScanParam;
import com.ali.user.mobile.scan.model.ScanResponse;

public interface ScanService {
    ScanResponse cancel(ScanParam scanParam);

    CommonScanResponse commonCancel(CommonScanParam commonScanParam);

    CommonScanResponse commonConfirm(CommonScanParam commonScanParam);

    CommonScanResponse commonScan(CommonScanParam commonScanParam);

    ScanResponse confirm(ScanParam scanParam);

    ScanResponse scan(ScanParam scanParam);
}

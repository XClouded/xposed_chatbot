package com.taobao.android.dinamicx.template.loader;

import android.content.Context;
import android.util.Log;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.loader.binary.DXCodeReader;
import com.taobao.android.dinamicx.template.loader.binary.DXEnumLoader;
import com.taobao.android.dinamicx.template.loader.binary.DXExprCodeLoader;
import com.taobao.android.dinamicx.template.loader.binary.DXStringLoader;
import com.taobao.android.dinamicx.template.loader.binary.DXUiCodeLoader;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.List;
import java.util.Stack;

public class DXBinaryLoader {
    private static final int DEFAULT_AUTO_ID = 1000;
    private static final String FILE_START_TAG = "ALIDX";
    public static final int MAJOR_VERSION = 3;
    public static final int MINOR_VERSION_0 = 0;
    public static final int MINOR_VERSION_1 = 1;
    public static final int STATE_continue = 0;
    public static final int STATE_failed = 2;
    public static final int STATE_successful = 1;
    private static final String TAG = "BinaryLoader_TMTEST";
    public static final short TYPE_ADAPTIVE_UNIT = 32;
    public static final short TYPE_COLOR = 16;
    public static final short TYPE_DOUBLE = 4;
    public static final short TYPE_ENUM = 512;
    public static final short TYPE_INT = 1;
    public static final short TYPE_LIST = 128;
    public static final short TYPE_LONG = 2;
    public static final short TYPE_MAP = 256;
    public static final short TYPE_NATIVE_UNIT = 64;
    public static final short TYPE_OBJECT = 1024;
    public static final short TYPE_STRING = 8;
    private int autoId = 1000;
    private DXEnumLoader enumLoader = new DXEnumLoader(this.varStringLoader);
    private DXExprCodeLoader exprCodeLoader = new DXExprCodeLoader(this.varStringLoader);
    private int minorVersion;
    private Stack<DXWidgetNode> nodeStack = new Stack<>();
    private DXStringLoader stringLoader = new DXStringLoader();
    private DXUiCodeLoader uiCodeLoader = new DXUiCodeLoader();
    private DXStringLoader varStringLoader = new DXStringLoader();

    public DXWidgetNode loadFromBuffer(byte[] bArr, DXRuntimeContext dXRuntimeContext, Context context) {
        if (bArr != null) {
            return createWidgetTree(loadFromBufferInternally(bArr, dXRuntimeContext), dXRuntimeContext, context);
        }
        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_EMPTY));
        return null;
    }

    private DXCodeReader loadFromBufferInternally(byte[] bArr, DXRuntimeContext dXRuntimeContext) {
        int i;
        byte[] bArr2 = bArr;
        DXRuntimeContext dXRuntimeContext2 = dXRuntimeContext;
        DXCodeReader dXCodeReader = new DXCodeReader();
        int length = FILE_START_TAG.length();
        String str = new String(bArr2, 0, length);
        if (!FILE_START_TAG.equals(str)) {
            Log.e(TAG, "loadFromBuffer failed tag is invalidate:" + str);
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_TAG_ERROR));
            return null;
        }
        dXCodeReader.setCode(bArr2);
        dXCodeReader.seekBy(length);
        if (dXCodeReader.readByte() != 3) {
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_MAJOR_VERSION_ERROR));
            return null;
        }
        this.minorVersion = dXCodeReader.readShort();
        short readShort = dXCodeReader.readShort();
        String str2 = new String(dXCodeReader.getCode(), dXCodeReader.getPos(), readShort);
        dXCodeReader.seekBy(readShort);
        dXCodeReader.setVersion(dXCodeReader.readShort());
        int readInt = dXCodeReader.readInt();
        int readInt2 = dXCodeReader.readInt();
        int readInt3 = dXCodeReader.readInt();
        int readInt4 = dXCodeReader.readInt();
        int readInt5 = dXCodeReader.readInt();
        int readInt6 = dXCodeReader.readInt();
        int readInt7 = dXCodeReader.readInt();
        int readInt8 = dXCodeReader.readInt();
        int readInt9 = dXCodeReader.readInt();
        int readInt10 = dXCodeReader.readInt();
        if (dXCodeReader.seek(readInt)) {
            this.uiCodeLoader.loadFromBuffer(str2, readInt2, dXCodeReader);
            if (dXCodeReader.getPos() == readInt3) {
                if (!this.stringLoader.loadFromBuffer(readInt4, dXCodeReader, dXRuntimeContext2)) {
                    DXRemoteLog.remoteLoge("string loadFromBuffer error!");
                }
                i = readInt;
            } else {
                List<DXError.DXErrorInfo> list = dXRuntimeContext.getDxError().dxErrorInfoList;
                StringBuilder sb = new StringBuilder();
                i = readInt;
                sb.append("string pos error:");
                sb.append(readInt3);
                sb.append("  read pos:");
                sb.append(dXCodeReader.getPos());
                list.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_STRING_LOADER_POSITION_ERROR, sb.toString()));
            }
            if (dXCodeReader.getPos() != readInt5) {
                List<DXError.DXErrorInfo> list2 = dXRuntimeContext.getDxError().dxErrorInfoList;
                list2.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_POSITION_ERROR, "var string pos error:" + readInt3 + "  read pos:" + dXCodeReader.getPos()));
            } else if (!this.varStringLoader.loadFromBuffer(readInt6, dXCodeReader, dXRuntimeContext2)) {
                DXRemoteLog.remoteLoge("var string loadFromBuffer error!");
            }
            if (dXCodeReader.getPos() != readInt7) {
                List<DXError.DXErrorInfo> list3 = dXRuntimeContext.getDxError().dxErrorInfoList;
                list3.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_POSITION_ERROR, "expr pos error:" + readInt7 + "  read pos:" + dXCodeReader.getPos()));
            } else if (!this.exprCodeLoader.loadFromBuffer(readInt8, dXCodeReader, dXRuntimeContext2)) {
                DXRemoteLog.remoteLoge("expr loadFromBuffer error!");
            }
            if (dXCodeReader.getPos() != readInt9) {
                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_POSITION_ERROR, "enum pos error:" + readInt7 + "  read pos:" + dXCodeReader.getPos()));
            } else if (!this.enumLoader.loadFromBuffer(readInt10, dXCodeReader, dXRuntimeContext2)) {
                DXRemoteLog.remoteLoge("enum loadFromBuffer error!");
            }
        } else {
            i = readInt;
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_UI_LOADER_POSITION_ERROR));
        }
        dXCodeReader.seek(i);
        return dXCodeReader;
    }

    /* JADX WARNING: Removed duplicated region for block: B:132:0x02b8 A[Catch:{ Exception -> 0x0451 }] */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x038d A[Catch:{ Exception -> 0x0451 }] */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0448 A[SYNTHETIC, Splitter:B:199:0x0448] */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x045c  */
    /* JADX WARNING: Removed duplicated region for block: B:210:0x0405 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x007c A[Catch:{ Exception -> 0x0451 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ad A[Catch:{ Exception -> 0x0451 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b4 A[Catch:{ Exception -> 0x0451 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0112 A[Catch:{ Exception -> 0x0451 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.android.dinamicx.widget.DXWidgetNode createWidgetTree(com.taobao.android.dinamicx.template.loader.binary.DXCodeReader r22, com.taobao.android.dinamicx.DXRuntimeContext r23, android.content.Context r24) {
        /*
            r21 = this;
            r1 = r21
            r2 = r24
            r3 = 0
            if (r22 != 0) goto L_0x0008
            return r3
        L_0x0008:
            java.util.Stack<com.taobao.android.dinamicx.widget.DXWidgetNode> r0 = r1.nodeStack     // Catch:{ Exception -> 0x0453 }
            r0.clear()     // Catch:{ Exception -> 0x0453 }
            r0 = 1000(0x3e8, float:1.401E-42)
            r1.autoId = r0     // Catch:{ Exception -> 0x0453 }
            byte r0 = r22.readByte()     // Catch:{ Exception -> 0x0453 }
            r6 = r3
            r7 = 0
        L_0x0017:
            r9 = 1
            switch(r0) {
                case 0: goto L_0x0041;
                case 1: goto L_0x0022;
                default: goto L_0x001b;
            }
        L_0x001b:
            r13 = r23
            java.lang.String r2 = "BinaryLoader_TMTEST"
            r3 = 1
            goto L_0x03e9
        L_0x0022:
            java.util.Stack<com.taobao.android.dinamicx.widget.DXWidgetNode> r0 = r1.nodeStack     // Catch:{ Exception -> 0x0453 }
            int r0 = r0.size()     // Catch:{ Exception -> 0x0453 }
            if (r0 <= 0) goto L_0x003b
            java.util.Stack<com.taobao.android.dinamicx.widget.DXWidgetNode> r0 = r1.nodeStack     // Catch:{ Exception -> 0x0453 }
            java.lang.Object r0 = r0.pop()     // Catch:{ Exception -> 0x0453 }
            com.taobao.android.dinamicx.widget.DXWidgetNode r0 = (com.taobao.android.dinamicx.widget.DXWidgetNode) r0     // Catch:{ Exception -> 0x0453 }
            r0.addChild(r6)     // Catch:{ Exception -> 0x0453 }
            r13 = r23
            r6 = r0
        L_0x0038:
            r3 = 0
            goto L_0x0403
        L_0x003b:
            r13 = r23
            r3 = 0
            r7 = 1
            goto L_0x0403
        L_0x0041:
            long r10 = r22.readLong()     // Catch:{ Exception -> 0x0453 }
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r23.getWidgetNodeMap()     // Catch:{ Exception -> 0x0453 }
            if (r0 != 0) goto L_0x004c
            return r3
        L_0x004c:
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r23.getWidgetNodeMap()     // Catch:{ Exception -> 0x0453 }
            java.lang.Object r0 = r0.get(r10)     // Catch:{ Exception -> 0x0453 }
            com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode r0 = (com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode) r0     // Catch:{ Exception -> 0x0453 }
            if (r0 == 0) goto L_0x0081
            com.taobao.android.dinamicx.widget.DXWidgetNode r12 = r0.build(r2)     // Catch:{ Exception -> 0x0072 }
            int r0 = r1.autoId     // Catch:{ Exception -> 0x006e }
            int r13 = r0 + 1
            r1.autoId = r13     // Catch:{ Exception -> 0x006e }
            r12.setAutoId(r0)     // Catch:{ Exception -> 0x006e }
            r13 = r23
            r12.setDXRuntimeContext(r13)     // Catch:{ Exception -> 0x006c }
            r5 = 1
            goto L_0x00ab
        L_0x006c:
            r0 = move-exception
            goto L_0x0076
        L_0x006e:
            r0 = move-exception
            r13 = r23
            goto L_0x0076
        L_0x0072:
            r0 = move-exception
            r13 = r23
            r12 = r3
        L_0x0076:
            boolean r14 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ Exception -> 0x0451 }
            if (r14 == 0) goto L_0x007f
            r0.printStackTrace()     // Catch:{ Exception -> 0x0451 }
        L_0x007f:
            r5 = 0
            goto L_0x00ab
        L_0x0081:
            r13 = r23
            com.taobao.android.dinamicx.DXError$DXErrorInfo r0 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r12 = "Pipeline"
            java.lang.String r14 = "Pipeline_Stage_Load_Binary"
            r15 = 70024(0x11188, float:9.8125E-41)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r8.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = "创建原型树找不到注册的widgetNode nodeId"
            r8.append(r5)     // Catch:{ Exception -> 0x0451 }
            r8.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = r8.toString()     // Catch:{ Exception -> 0x0451 }
            r0.<init>(r12, r14, r15, r5)     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError r5 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r5 = r5.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            r5.add(r0)     // Catch:{ Exception -> 0x0451 }
            r12 = r3
            goto L_0x007f
        L_0x00ab:
            if (r6 == 0) goto L_0x00b2
            java.util.Stack<com.taobao.android.dinamicx.widget.DXWidgetNode> r0 = r1.nodeStack     // Catch:{ Exception -> 0x0451 }
            r0.push(r6)     // Catch:{ Exception -> 0x0451 }
        L_0x00b2:
            if (r5 != 0) goto L_0x0109
            java.util.Stack<com.taobao.android.dinamicx.widget.DXWidgetNode> r0 = r1.nodeStack     // Catch:{ Exception -> 0x0451 }
            int r0 = r0.size()     // Catch:{ Exception -> 0x0451 }
            if (r0 != 0) goto L_0x00e3
            com.taobao.android.dinamicx.DXError$DXErrorInfo r0 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Load_Binary"
            r5 = 70016(0x11180, float:9.8113E-41)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r6.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r7 = "创建原型树root节点失败 !getNodeResult nodeStack.size() nodeId"
            r6.append(r7)     // Catch:{ Exception -> 0x0451 }
            r6.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0451 }
            r0.<init>(r2, r4, r5, r6)     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError r2 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r2.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            r2.add(r0)     // Catch:{ Exception -> 0x0451 }
            return r3
        L_0x00e3:
            com.taobao.android.dinamicx.DXError$DXErrorInfo r0 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = "Pipeline"
            java.lang.String r6 = "Pipeline_Stage_Load_Binary"
            r8 = 70017(0x11181, float:9.8115E-41)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r14.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r15 = "创建节点失败 !getNodeResult nodeId"
            r14.append(r15)     // Catch:{ Exception -> 0x0451 }
            r14.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r14 = r14.toString()     // Catch:{ Exception -> 0x0451 }
            r0.<init>(r5, r6, r8, r14)     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError r5 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r5 = r5.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            r5.add(r0)     // Catch:{ Exception -> 0x0451 }
        L_0x0109:
            byte r5 = r22.readByte()     // Catch:{ Exception -> 0x0451 }
            r6 = 0
        L_0x010e:
            r0 = 512(0x200, float:7.175E-43)
            if (r6 >= r5) goto L_0x02a4
            short r8 = r22.readShort()     // Catch:{ Exception -> 0x0451 }
            r14 = 4
            if (r8 == r14) goto L_0x0292
            r14 = 8
            if (r8 == r14) goto L_0x0248
            r14 = 16
            if (r8 == r14) goto L_0x023a
            r14 = 32
            if (r8 == r14) goto L_0x0227
            r14 = 64
            if (r8 == r14) goto L_0x0213
            r14 = 128(0x80, float:1.794E-43)
            if (r8 == r14) goto L_0x01c4
            r14 = 256(0x100, float:3.59E-43)
            if (r8 == r14) goto L_0x0165
            if (r8 == r0) goto L_0x0156
            switch(r8) {
                case 1: goto L_0x0147;
                case 2: goto L_0x0138;
                default: goto L_0x0136;
            }     // Catch:{ Exception -> 0x0451 }
        L_0x0136:
            goto L_0x029f
        L_0x0138:
            long r14 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setLongAttribute(r14, r3)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0147:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            int r0 = r22.readInt()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setIntAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0156:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            int r0 = r22.readInt()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setIntAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0165:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            long r14 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.template.loader.binary.DXStringLoader r0 = r1.varStringLoader     // Catch:{ Exception -> 0x0178 }
            java.lang.String r0 = r0.getString(r14)     // Catch:{ Exception -> 0x0178 }
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r0)     // Catch:{ Exception -> 0x0178 }
            goto L_0x0183
        L_0x0178:
            r0 = move-exception
            boolean r8 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ Exception -> 0x0451 }
            if (r8 == 0) goto L_0x0182
            r0.printStackTrace()     // Catch:{ Exception -> 0x0451 }
        L_0x0182:
            r0 = 0
        L_0x0183:
            if (r0 != 0) goto L_0x01bd
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = "Pipeline"
            java.lang.String r6 = "Pipeline_Stage_Load_Binary"
            r7 = 70020(0x11184, float:9.8119E-41)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r8.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r9 = "nodeId"
            r8.append(r9)     // Catch:{ Exception -> 0x0451 }
            r8.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r9 = " key "
            r8.append(r9)     // Catch:{ Exception -> 0x0451 }
            r8.append(r3)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r3 = " value "
            r8.append(r3)     // Catch:{ Exception -> 0x0451 }
            r8.append(r14)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r3 = r8.toString()     // Catch:{ Exception -> 0x0451 }
            r2.<init>(r5, r6, r7, r3)     // Catch:{ Exception -> 0x0451 }
            r0.add(r2)     // Catch:{ Exception -> 0x0451 }
            r2 = 0
            return r2
        L_0x01bd:
            if (r12 == 0) goto L_0x029f
            r12.setMapAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x01c4:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            long r14 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.template.loader.binary.DXStringLoader r0 = r1.varStringLoader     // Catch:{ Exception -> 0x01d7 }
            java.lang.String r0 = r0.getString(r14)     // Catch:{ Exception -> 0x01d7 }
            com.alibaba.fastjson.JSONArray r0 = com.alibaba.fastjson.JSON.parseArray(r0)     // Catch:{ Exception -> 0x01d7 }
            goto L_0x01e2
        L_0x01d7:
            r0 = move-exception
            boolean r8 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ Exception -> 0x0451 }
            if (r8 == 0) goto L_0x01e1
            r0.printStackTrace()     // Catch:{ Exception -> 0x0451 }
        L_0x01e1:
            r0 = 0
        L_0x01e2:
            if (r0 != 0) goto L_0x020c
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r3 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Load_Binary"
            r5 = 70019(0x11183, float:9.8118E-41)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r6.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r7 = "nodeId"
            r6.append(r7)     // Catch:{ Exception -> 0x0451 }
            r6.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0451 }
            r2.<init>(r3, r4, r5, r6)     // Catch:{ Exception -> 0x0451 }
            r0.add(r2)     // Catch:{ Exception -> 0x0451 }
            r2 = 0
            return r2
        L_0x020c:
            if (r12 == 0) goto L_0x029f
            r12.setListAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0213:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            double r14 = r22.readDouble()     // Catch:{ Exception -> 0x0451 }
            float r0 = (float) r14     // Catch:{ Exception -> 0x0451 }
            int r0 = com.taobao.android.dinamicx.widget.utils.DXScreenTool.dip2px(r2, r0)     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setIntAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0227:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            double r14 = r22.readDouble()     // Catch:{ Exception -> 0x0451 }
            float r0 = (float) r14     // Catch:{ Exception -> 0x0451 }
            int r0 = com.taobao.android.dinamicx.widget.utils.DXScreenTool.ap2px(r2, r0)     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setIntAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x023a:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            int r0 = r22.readInt()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setIntAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0248:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            long r14 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            com.taobao.android.dinamicx.template.loader.binary.DXStringLoader r0 = r1.varStringLoader     // Catch:{ Exception -> 0x0451 }
            java.lang.String r0 = r0.getString(r14)     // Catch:{ Exception -> 0x0451 }
            boolean r8 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0451 }
            if (r8 == 0) goto L_0x028e
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r3 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Load_Binary"
            r5 = 70018(0x11182, float:9.8116E-41)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r6.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r7 = "nodeId"
            r6.append(r7)     // Catch:{ Exception -> 0x0451 }
            r6.append(r10)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r7 = " value "
            r6.append(r7)     // Catch:{ Exception -> 0x0451 }
            r6.append(r14)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0451 }
            r2.<init>(r3, r4, r5, r6)     // Catch:{ Exception -> 0x0451 }
            r0.add(r2)     // Catch:{ Exception -> 0x0451 }
            r2 = 0
            return r2
        L_0x028e:
            r12.setStringAttribute(r3, r0)     // Catch:{ Exception -> 0x0451 }
            goto L_0x029f
        L_0x0292:
            long r3 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            double r14 = r22.readDouble()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x029f
            r12.setDoubleAttribute(r3, r14)     // Catch:{ Exception -> 0x0451 }
        L_0x029f:
            int r6 = r6 + 1
            r3 = 0
            goto L_0x010e
        L_0x02a4:
            byte r3 = r22.readByte()     // Catch:{ Exception -> 0x0451 }
            if (r3 <= 0) goto L_0x02b5
            if (r12 == 0) goto L_0x02b5
            androidx.collection.LongSparseArray r4 = r12.getDataParsersExprNode()     // Catch:{ Exception -> 0x0451 }
            if (r4 != 0) goto L_0x02b5
            r12.newDataParsersExprNode(r3)     // Catch:{ Exception -> 0x0451 }
        L_0x02b5:
            r4 = 0
        L_0x02b6:
            if (r4 >= r3) goto L_0x0378
            short r5 = r22.readShort()     // Catch:{ Exception -> 0x0451 }
            int r6 = r1.minorVersion     // Catch:{ Exception -> 0x0451 }
            r14 = 0
            if (r6 < r9) goto L_0x02c9
            long r16 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            r18 = r10
            goto L_0x02cd
        L_0x02c9:
            r18 = r10
            r16 = r14
        L_0x02cd:
            long r9 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            int r6 = (r16 > r14 ? 1 : (r16 == r14 ? 0 : -1))
            if (r6 != 0) goto L_0x02d7
            r14 = r9
            goto L_0x02d9
        L_0x02d7:
            r14 = r16
        L_0x02d9:
            long r0 = r22.readLong()     // Catch:{ Exception -> 0x0373 }
            if (r12 == 0) goto L_0x035f
            r20 = r3
            r2 = r0
            r1 = r21
            com.taobao.android.dinamicx.template.loader.binary.DXExprCodeLoader r0 = r1.exprCodeLoader     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.expression.DXExprNode r0 = r0.get(r2)     // Catch:{ Exception -> 0x0451 }
            if (r0 != 0) goto L_0x031e
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r4 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = "Pipeline"
            java.lang.String r6 = "Pipeline_Stage_Load_Binary"
            r7 = 70022(0x11186, float:9.8122E-41)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r8.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r9 = "nodeId"
            r8.append(r9)     // Catch:{ Exception -> 0x0451 }
            r9 = r18
            r8.append(r9)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r9 = " 创建原型树expr失败 value"
            r8.append(r9)     // Catch:{ Exception -> 0x0451 }
            r8.append(r2)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = r8.toString()     // Catch:{ Exception -> 0x0451 }
            r4.<init>(r5, r6, r7, r2)     // Catch:{ Exception -> 0x0451 }
            r0.add(r4)     // Catch:{ Exception -> 0x0451 }
            r2 = 0
            return r2
        L_0x031e:
            r2 = r18
            r0.setDataType(r5)     // Catch:{ Exception -> 0x0451 }
            androidx.collection.LongSparseArray r6 = r12.getDataParsersExprNode()     // Catch:{ Exception -> 0x0451 }
            r6.put(r9, r0)     // Catch:{ Exception -> 0x0451 }
            r0 = r5 & 512(0x200, float:7.175E-43)
            r5 = 512(0x200, float:7.175E-43)
            if (r0 != r5) goto L_0x0367
            com.taobao.android.dinamicx.template.loader.binary.DXEnumLoader r0 = r1.enumLoader     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r0.getEnumMap()     // Catch:{ Exception -> 0x0451 }
            if (r0 == 0) goto L_0x0367
            com.taobao.android.dinamicx.template.loader.binary.DXEnumLoader r0 = r1.enumLoader     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r0.getEnumMap()     // Catch:{ Exception -> 0x0451 }
            java.lang.Object r0 = r0.get(r14)     // Catch:{ Exception -> 0x0451 }
            if (r0 == 0) goto L_0x0367
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r12.getEnumMap()     // Catch:{ Exception -> 0x0451 }
            if (r0 != 0) goto L_0x034d
            r12.newEnumMap()     // Catch:{ Exception -> 0x0451 }
        L_0x034d:
            com.taobao.android.dinamicx.model.DXLongSparseArray r0 = r12.getEnumMap()     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.template.loader.binary.DXEnumLoader r6 = r1.enumLoader     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.model.DXLongSparseArray r6 = r6.getEnumMap()     // Catch:{ Exception -> 0x0451 }
            java.lang.Object r6 = r6.get(r14)     // Catch:{ Exception -> 0x0451 }
            r0.put(r9, r6)     // Catch:{ Exception -> 0x0451 }
            goto L_0x0367
        L_0x035f:
            r20 = r3
            r2 = r18
            r1 = r21
            r5 = 512(0x200, float:7.175E-43)
        L_0x0367:
            int r4 = r4 + 1
            r10 = r2
            r3 = r20
            r0 = 512(0x200, float:7.175E-43)
            r2 = r24
            r9 = 1
            goto L_0x02b6
        L_0x0373:
            r0 = move-exception
            r1 = r21
            goto L_0x0456
        L_0x0378:
            r2 = r10
            byte r0 = r22.readByte()     // Catch:{ Exception -> 0x0451 }
            if (r0 <= 0) goto L_0x038a
            if (r12 == 0) goto L_0x038a
            androidx.collection.LongSparseArray r4 = r12.getEventHandlersExprNode()     // Catch:{ Exception -> 0x0451 }
            if (r4 != 0) goto L_0x038a
            r12.newEventHandlersExprNode(r0)     // Catch:{ Exception -> 0x0451 }
        L_0x038a:
            r4 = 0
        L_0x038b:
            if (r4 >= r0) goto L_0x03e6
            long r5 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            long r8 = r22.readLong()     // Catch:{ Exception -> 0x0451 }
            if (r12 == 0) goto L_0x03e3
            com.taobao.android.dinamicx.template.loader.binary.DXExprCodeLoader r10 = r1.exprCodeLoader     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.expression.DXExprNode r10 = r10.get(r8)     // Catch:{ Exception -> 0x0451 }
            if (r10 != 0) goto L_0x03dc
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r4 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r7 = "Pipeline"
            java.lang.String r10 = "Pipeline_Stage_Load_Binary"
            r11 = 70023(0x11187, float:9.8123E-41)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r12.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r14 = "nodeId"
            r12.append(r14)     // Catch:{ Exception -> 0x0451 }
            r12.append(r2)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = " 创建原型树event失败 "
            r12.append(r2)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = " key "
            r12.append(r2)     // Catch:{ Exception -> 0x0451 }
            r12.append(r5)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = " value "
            r12.append(r2)     // Catch:{ Exception -> 0x0451 }
            r12.append(r8)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r2 = r12.toString()     // Catch:{ Exception -> 0x0451 }
            r4.<init>(r7, r10, r11, r2)     // Catch:{ Exception -> 0x0451 }
            r0.add(r4)     // Catch:{ Exception -> 0x0451 }
            r2 = 0
            return r2
        L_0x03dc:
            androidx.collection.LongSparseArray r8 = r12.getEventHandlersExprNode()     // Catch:{ Exception -> 0x0451 }
            r8.put(r5, r10)     // Catch:{ Exception -> 0x0451 }
        L_0x03e3:
            int r4 = r4 + 1
            goto L_0x038b
        L_0x03e6:
            r6 = r12
            goto L_0x0038
        L_0x03e9:
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0451 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0451 }
            r3.<init>()     // Catch:{ Exception -> 0x0451 }
            java.lang.String r5 = "invalidate tag type:"
            r3.append(r5)     // Catch:{ Exception -> 0x0451 }
            r3.append(r0)     // Catch:{ Exception -> 0x0451 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x0451 }
            r3 = 0
            r4[r3] = r0     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.log.DXLog.e(r2, r4)     // Catch:{ Exception -> 0x0451 }
            r7 = 2
        L_0x0403:
            if (r7 == 0) goto L_0x0448
            r2 = 1
            if (r2 != r7) goto L_0x040a
            r3 = r6
            goto L_0x040b
        L_0x040a:
            r3 = 0
        L_0x040b:
            int r0 = r22.getPos()     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.template.loader.binary.DXUiCodeLoader r2 = r1.uiCodeLoader     // Catch:{ Exception -> 0x0451 }
            int r2 = r2.getEndPos()     // Catch:{ Exception -> 0x0451 }
            if (r0 == r2) goto L_0x041a
            r2 = 2
            r7 = 2
            goto L_0x041b
        L_0x041a:
            r2 = 2
        L_0x041b:
            if (r7 != r2) goto L_0x0433
            com.taobao.android.dinamicx.DXError r0 = r23.getDxError()     // Catch:{ Exception -> 0x0451 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList     // Catch:{ Exception -> 0x0451 }
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo     // Catch:{ Exception -> 0x0451 }
            java.lang.String r3 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Load_Binary"
            r5 = 70015(0x1117f, float:9.8112E-41)
            r2.<init>(r3, r4, r5)     // Catch:{ Exception -> 0x0451 }
            r0.add(r2)     // Catch:{ Exception -> 0x0451 }
            r3 = 0
        L_0x0433:
            if (r3 == 0) goto L_0x0447
            com.taobao.android.dinamicx.DXRuntimeContext r0 = r3.getDXRuntimeContext()
            if (r0 == 0) goto L_0x0447
            com.taobao.android.dinamicx.DXRuntimeContext r0 = r3.getDXRuntimeContext()
            r0.setWidgetNode(r3)
            int r0 = r1.autoId
            r3.setLastAutoId(r0)
        L_0x0447:
            return r3
        L_0x0448:
            byte r0 = r22.readByte()     // Catch:{ Exception -> 0x0451 }
            r2 = r24
            r3 = 0
            goto L_0x0017
        L_0x0451:
            r0 = move-exception
            goto L_0x0456
        L_0x0453:
            r0 = move-exception
            r13 = r23
        L_0x0456:
            boolean r2 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()
            if (r2 == 0) goto L_0x045f
            r0.printStackTrace()
        L_0x045f:
            com.taobao.android.dinamicx.DXError r2 = r23.getDxError()
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r2.dxErrorInfoList
            com.taobao.android.dinamicx.DXError$DXErrorInfo r3 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r4 = "Pipeline"
            java.lang.String r5 = "Pipeline_Stage_Load_Binary"
            r6 = 70021(0x11185, float:9.812E-41)
            java.lang.String r0 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r0)
            r3.<init>(r4, r5, r6, r0)
            r2.add(r3)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.loader.DXBinaryLoader.createWidgetTree(com.taobao.android.dinamicx.template.loader.binary.DXCodeReader, com.taobao.android.dinamicx.DXRuntimeContext, android.content.Context):com.taobao.android.dinamicx.widget.DXWidgetNode");
    }

    public DXEnumLoader getEnumLoader() {
        return this.enumLoader;
    }

    public DXExprCodeLoader getExprCodeLoader() {
        return this.exprCodeLoader;
    }

    public DXStringLoader getStringLoader() {
        return this.stringLoader;
    }

    public DXStringLoader getVarStringLoader() {
        return this.varStringLoader;
    }
}

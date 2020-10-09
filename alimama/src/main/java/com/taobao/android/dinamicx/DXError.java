package com.taobao.android.dinamicx;

import android.text.TextUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DXError {
    public static final int BINDINGX_BINDINGX_CALL_BACK_CRASH = 121005;
    public static final int BINDINGX_INIT_CRASH = 121001;
    public static final int BINDINGX_POST_DETTACH_FROM_WINDOW_CRASH = 121003;
    public static final int BINDINGX_POST_MSG_CRASH = 121002;
    public static final int BINDINGX_ROOTVIEW_DISAPPEAR_CRASH = 121004;
    public static final int DXERROR_DETAIL_FAST_TEXT_CALCULATE_CRASH = 80005;
    public static final int DXERROR_DETAIL_ON_LAYOUT_ERROR = 80007;
    public static final int DXERROR_DETAIL_ON_MEASURE_ERROR = 80006;
    public static final int DXERROR_DETAIL_ON_RENDER_ERROR = 90002;
    public static final int DXERROR_ENGINE_CREATE_VIEW_IN_GLOBAL_CENTER = 30009;
    public static final int DXERROR_ENGINE_CREATE_VIEW_IN_GLOBAL_CENTER_MAP = 30010;
    public static final int DXERROR_ENGINE_DESTROY_EXCEPTION = 30007;
    public static final int DXERROR_ENGINE_INIT_EXCEPTION_TIMER_ERROR = 30013;
    public static final int DXERROR_GENERIC_EXCEPTION = 10000;
    public static final int DXERROR_GET_ORIGIN_TREE_FAIL = 40002;
    public static final int DXERROR_GET_ORIGIN_TREE_FAIL_ASYNC = 40006;
    public static final int DXERROR_PIPELINE_BINARY_FILE_EMPTY = 70001;
    public static final int DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_COUNT_ERROR = 70012;
    public static final int DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_ERROR = 70011;
    public static final int DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_POSITION_ERROR = 70013;
    public static final int DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_COMMON_ERROR = 70025;
    public static final int DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_COUNT_ERROR = 70009;
    public static final int DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_ERROR = 70008;
    public static final int DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_POSITION_ERROR = 70010;
    public static final int DXERROR_PIPELINE_BINARY_FILE_STRING_LOADER_POSITION_ERROR = 70004;
    public static final int DXERROR_PIPELINE_BINARY_FILE_TAG_ERROR = 70003;
    public static final int DXERROR_PIPELINE_BINARY_FILE_UI_LOADER_POSITION_ERROR = 70014;
    public static final int DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_COUNT_ERROR = 70006;
    public static final int DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_ERROR = 70005;
    public static final int DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_POSITION_ERROR = 70007;
    public static final int DXERROR_PIPELINE_BINARY_MAJOR_VERSION_ERROR = 70002;
    public static final int DXERROR_PIPELINE_CATCH = 40003;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_ERROR = 70017;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_EVENT_ERROR = 70023;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_EXPR_ERROR = 70022;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_LIST_ERROR = 70019;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_MAP_ERROR = 70020;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_ONSETATTR_ERROR = 70021;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_STRING_ERROR = 70018;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_NODE_WIDGET_NOT_FOUND = 70024;
    public static final int DXERROR_PIPELINE_CREATE_WT_CREATE_ROOT_ERROR = 70016;
    public static final int DXERROR_PIPELINE_CREATE_WT_READ_ERROR = 70015;
    public static final int DXERROR_PIPELINE_DETAIL_FLATTEN_CATCH = 80004;
    public static final int DXERROR_PIPELINE_DETAIL_LAYOUT_CATCH = 80003;
    public static final int DXERROR_PIPELINE_DETAIL_PERFORM_MEASURE_CATCH = 80002;
    public static final int DXERROR_PIPELINE_DETAIL_PERFORM_MEASURE_WT_IS_NULL_OR_NOT_LAYOUT = 80001;
    public static final int DXERROR_PIPELINE_DETAIL_RENDER_CATCH = 90001;
    public static final int DXERROR_PIPELINE_EXCEPTION_RUNTIMECONTEXT_NULL = 40001;
    public static final int DXERROR_PIPELINE_GET_CACHE_ERROR = 40005;
    public static final int DXERROR_PIPELINE_PARSE_WT_COLOR_EXCEPTION = 71007;
    public static final int DXERROR_PIPELINE_PARSE_WT_DOUBLE_EXCEPTION = 71006;
    public static final int DXERROR_PIPELINE_PARSE_WT_ERROR = 40004;
    public static final int DXERROR_PIPELINE_PARSE_WT_EXCEPTION = 71001;
    public static final int DXERROR_PIPELINE_PARSE_WT_EXPR_EVALUEATE_EXCEPTION = 71002;
    public static final int DXERROR_PIPELINE_PARSE_WT_EXPR_SET_VALUE = 71003;
    public static final int DXERROR_PIPELINE_PARSE_WT_INT_EXCEPTION = 71004;
    public static final int DXERROR_PIPELINE_PARSE_WT_LIST_EXCEPTION = 71008;
    public static final int DXERROR_PIPELINE_PARSE_WT_LONG_EXCEPTION = 71005;
    public static final int DXERROR_PIPELINE_PARSE_WT_MAP_EXCEPTION = 71009;
    public static final int DXERROR_REGISTER_NOTIFICATION_CRASH = 30012;
    public static final int DXERROR_RENDER_DOWNGRADE = 90004;
    public static final int DXERROR_RENDER_FLATTEN = 90003;
    public static final int DXERROR_ROUTER_CREATE_VIEW_EXCEPTION = 20005;
    public static final int DXERROR_ROUTER_CREATE_VIEW_EXCEPTION_TEMPLATE_NULL = 20012;
    public static final int DXERROR_ROUTER_CREATE_VIEW_EXCEPTION_V2_FAIL = 20013;
    public static final int DXERROR_ROUTER_DESTROY_EXCEPTION = 20009;
    public static final int DXERROR_ROUTER_DOWNLOAD_TEMPLATE_EXCEPTION = 20008;
    public static final int DXERROR_ROUTER_FETCH_TEMPLATE_EXCEPTION = 20007;
    public static final int DXERROR_ROUTER_RENDER_VIEW_EXCEPTION = 20006;
    public static final int DXERROR_ROUTER_RENDER_VIEW_EXCEPTION_CATCH = 200014;
    public static final int DXERROR_ROUTER_TRASFORM_TEMPLATE_TOV2_EXCEPTION = 20010;
    public static final int DXERROR_ROUTER_TRASFORM_TEMPLATE_TOV3_EXCEPTION = 20011;
    public static final int DXError_EngineDownloadException = 30005;
    public static final int DXError_EngineFetchException = 30006;
    public static final int DXError_EngineGenericException = 30000;
    public static final int DXError_EngineInitContextNUll = 30011;
    public static final int DXError_EngineInitEnvException = 30001;
    public static final int DXError_EngineInitException = 30002;
    public static final int DXError_EngineRenderException = 30004;
    public static final int DXError_EngineRenderException_NULL = 30008;
    public static final int DXError_EngineSizeException = 30003;
    public static final int DXError_PipelineGenericException = 40000;
    public static final int DX_DB_CLOSE_ERROR = 60010;
    public static final int DX_DB_DELETE_ALL_ERROR = 60012;
    public static final int DX_DB_DELETE_ERROR = 60013;
    public static final int DX_DB_NULL = 60016;
    public static final int DX_DB_OPEN_ERROR = 60011;
    public static final int DX_DB_QUERY_ERROR = 60015;
    public static final int DX_DB_STORE_ERROR = 60014;
    public static final int DX_ERROR_BIZ_CODE = 150000;
    public static final int DX_ERROR_CODE_AST_EVENT_EXECUTE_EXCEPTION = 100004;
    public static final int DX_ERROR_CODE_AST_EVENT_HANDLER_NOT_FOUND = 100003;
    public static final int DX_ERROR_CODE_CONTROL_EVENT_CENTER_EXCEPTION_CRASH = 111001;
    public static final int DX_ERROR_CODE_METHOD_NODE_EXECUTE_EXCEPTION = 100002;
    public static final int DX_ERROR_CODE_PARSE_NOT_FOUND = 100001;
    public static final int DX_ERROR_CODE_SIGNAL_EXCEPTION_CRASH = 110001;
    public static final int DX_SIMPLE_PIPELINE_CRASH = 400010;
    public static final int DX_TEMPLATE_DOWNLOAD_EMPTY_ERROR = 60000;
    public static final int DX_TEMPLATE_IO_READ_ERROR = 60020;
    public static final int DX_TEMPLATE_IO_WRITE_ERROR = 60021;
    public static final int DX_TEMPLATE_LOAD_ERROR = 60022;
    public static final int DX_TEMPLATE_UNZIP_ERROR = 60001;
    public static final int DX_TEMPLATE_UNZIP_REQUIRED_PARAMS_MISSING_ERROR = 60002;
    public static final int ENGINE_POST_MSG_CRASH = 30014;
    public static final int EVENT_DXEXPRNODE_CAST_ERROR = 122000;
    public static final int GET_EXPAND_TREE_CRASH = 40007;
    public static final int ONEVENT_CRASH = 40009;
    public static final int RESET_ANIMATION_CRASH = 40008;
    public static final int V2_PRE_RENDER_CRASH = 120002;
    public static final int V2_PRE_RENDER_FAIL = 120001;
    public static final int V3_ASYNC_RENDER_INIT_CRASH = 120004;
    public static final int V3_PRE_RENDER_CRASH = 120003;
    public String biztype;
    public List<DXErrorInfo> dxErrorInfoList;
    public DXTemplateItem dxTemplateItem;
    private String errorId;

    public DXError(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.biztype = str;
        } else {
            this.biztype = "NULL";
        }
        this.dxErrorInfoList = new ArrayList();
        this.errorId = "" + System.nanoTime();
    }

    public String toString() {
        return "DXError{biztype='" + this.biztype + '\'' + ", dxTemplateItem=" + this.dxTemplateItem + ", dxErrorInfoList=" + this.dxErrorInfoList + '}';
    }

    public String getErrorId() {
        return this.errorId;
    }

    public static class DXErrorInfo {
        public int code;
        public Map<String, String> extraParams;
        public String featureType;
        public String reason;
        public String serviceId;
        public long timeStamp = System.currentTimeMillis();

        public DXErrorInfo(String str, String str2, int i) {
            this.featureType = str;
            this.serviceId = str2;
            this.code = i;
        }

        public DXErrorInfo(String str, String str2, int i, String str3) {
            this.featureType = str;
            this.serviceId = str2;
            this.code = i;
            this.reason = str3;
        }

        public String toString() {
            return "DXErrorInfo{timeStamp=" + this.timeStamp + ", serviceId='" + this.serviceId + '\'' + ", featureType='" + this.featureType + '\'' + ", code=" + this.code + ", reason='" + this.reason + '}';
        }
    }
}

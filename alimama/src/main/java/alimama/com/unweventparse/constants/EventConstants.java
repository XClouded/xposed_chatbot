package alimama.com.unweventparse.constants;

public class EventConstants {
    public static final String ETAO_HEADER = "etao://";
    public static final String ETAO_SCHEMA = "etao";
    public static final String EVENTID = "eventId";
    public static final String EVENT_DATA = "data";
    public static final String EVENT_HEADER = "event://";
    public static final String EVENT_ID = "event_id";
    public static final String EVENT_INFO = "event_info";
    public static final String EVENT_LIST = "event_list";
    public static final String EVENT_ROOT_ID = "root_event_id";
    public static final String EVENT_SCHEMA = "event";
    public static final String EVENT_SCHEME = "event_scheme";
    public static final String EVENT_TYPE = "event_type";
    public static final String HTTP_SCHEMA = "http";
    public static final String NEXT_EVENTS = "next_events";
    public static final String POP_SCHEMA = "poplayer";
    public static final String TYPE = "type";

    public static class CLOSE {
        public static final String NAME = "close";
        public static final String SCHEMA = "event://close";
    }

    public static class FULLLink {
        public static final String ERRORCODE = "errorCode";
        public static final String FILENAME = "fileName";
        public static final String INFOMAP = "infoMap";
        public static final String LOGGERNAME = "loggerName";
        public static final String LOG_TYPE = "log_type";
        public static final String MSG = "msg";
        public static final String NAME = "fullLink";
        public static final String POINT = "point";
        public static final String SUBTYPE = "subType";
        public static final String TYPE = "type";
    }

    public static class Mtop {
        public static final String API = "api";
        public static final String DATA = "data";
        public static final String ERROR_HANDLER = "error_handler";
        public static final String EVENT_KEY = "event_key";
        public static final String HEADERS = "headers";
        public static final String NAME = "mtop";
        public static final String NEED_ECODE = "needEcode";
        public static final String NEED_SESSION = "needSession";
        public static final String PARAMS = "params";
        public static final String PARSEKEY = "parseKey";
        public static final String RETRYCOUNT = "retryCount";
        public static final String SCHEMA = "event://mtop";
        public static final String SUCCESS_HANDLER = "success_handler";
        public static final String VERSION = "version";
    }

    public static class POPUP {
        public static final String NAME = "popup";
        public static final String SCHEMA = "event://popup";
    }

    public static class ROUTER {
        public static final String NAME = "close";
        public static final String SCHEMA = "event://close";
    }

    public static class SHARE {
        public static final String NAME = "share";
        public static final String SHARETYPE = "share_type";
    }

    public static class UT {
        public static final String ARG1 = "arg1";
        public static final String ARGS = "args";
        public static final String NAME = "userTrack";
        public static final String PAGE_NAME = "page_name";
        public static final String UT_EVENTID = "ut_eventId";
    }

    public static class pageRouter {
        public static final String ANIMATED = "animated";
        public static final String NAME = "pageRouter";
        public static final String NEEDLOGIN = "needLogin";
        public static final String PARAMS = "params";
        public static final String SPM = "spm";
        public static final String URL = "url";
    }
}

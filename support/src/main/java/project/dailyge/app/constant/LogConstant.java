package project.dailyge.app.constant;

public interface LogConstant {
    String ORDER = "order";
    String LAYER = "layer";
    String DURATION = "duration";
    String VISITOR = "visitor";
    String CONTEXT = "context";
    String ARGS = "args";
    String RESULT = "result";
    String TIME = "time";

    String INFO = "INFO";
    String ERROR = "ERROR";

    String[] IP_HEADERS = {
        "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };
    String USER_ID = "userId";
    String IP = "ip";
    String TRACE_ID = "traceId";
    String PATH = "path";
    String METHOD = "method";
    String LOG_ORDER_INI = "0";
    String LOG_ORDER = "logOrder";
    String ENTRANCE_LAYER = "Entrance";
    String IN_COMING = "(IN)";
    String OUT_GOING = "(OUT)";
}

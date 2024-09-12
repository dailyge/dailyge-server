package project.dailyge.app.constant;

public interface LogConstant {
    String[] IP_HEADERS = {
        "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };
    String IP = "ip";
    String TRACE_ID = "traceId";
    String LAST_LAYER = "lastLayer";
    String LOG_ORDER = "logOrder";
    String ENTRANCE_LAYER = "ENTRANCE";
    String PRESENTATION = "PRESENTATION";
    String FACADE = "FACADE";
    String APPLICATION = "APPLICATION";
    String EXTERNAL = "EXTERNAL";
}

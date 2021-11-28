package com.notayessir.rpc.api.bean;

/**
 * 自定义 rpc 异常
 */
public class RPCException extends Throwable{


    private final int code;


    public RPCException(Message message) {
        super(message.getMessage());
        this.code = message.getCode();
    }

    public RPCException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isBusiness(){
        return code == Message.SERVER_BUSINESS_ERROR.getCode();
    }


    public enum Message {


        SERVER_THREAD_POOL_BUSY(500, "queue of server thread pool is full."),

        SERVER_BUSINESS_ERROR(501, "server business exception."),

        SERVER_OR_CLIENT_TIME_OUT(502, "server timeout."),

        UNKNOWN_RESPONSE_STATUS(503, "unknown response status."),



        CLIENT_THREAD_POOL_BUSY(400, "queue of client thread pool is full."),

        CLIENT_NOT_SUPPORT_METHOD(401, "un support method invoke."),

        CLIENT_UNAVAILABLE_INVOKER(402, "unavailable invoker."),

        CLIENT_EMPTY_INVOKERS_AFTER_SELECT(403, "empty invoker list after select."),

        CLIENT_EMPTY_INVOKERS_BEFORE_SELECT(404, "empty invoker list before select."),

        CLIENT_FAILOVER_CLUSTER_FAIL(405, "fail to apply failover cluster."),

        UN_CLASSIFY_EXCEPTION(600, ""),

        ;

        private final int code;

        private final String message;


        Message(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}

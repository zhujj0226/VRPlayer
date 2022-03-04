package com.zhujj.vrplayer.http;

public interface IHttpClient {
    class Response {
        int code;
        String msg;
        String body;

        public Response(int code, String msg, String body) {
            this.code = code;
            this.msg = msg;
            this.body = body;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public String getBody() {
            return body;
        }

        public boolean isSuccessful() {
            return code >= 200 && code < 300;
        }
    }
}

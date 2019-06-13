package top.r2ys.meshx.admin.vo;

import java.io.Serializable;

/**
 * @program:
 * @description:
 * @author: HU
 * @create: 2019-06-13 22:14
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String CODE_SUCCEED = "000000";
    private static final String MSG_SUCCEED = "SUCCEED";

    private static final String CODE_FAIL = "100001";
    private static final String MSG_FAIL = "FAILED";


    private String code;
    private String msg;
    private T data;

    public R() {
        this.code = CODE_SUCCEED;
        this.msg = MSG_SUCCEED;
    }

    public R(T data) {
        this.code = CODE_SUCCEED;
        this.msg = MSG_SUCCEED;
        this.data = data;
    }

    public R(T data, String msg) {
        this.code = CODE_SUCCEED;
        this.msg = MSG_SUCCEED;
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        this.msg = e.getMessage();
        this.code = CODE_FAIL;
    }

    public static <T> R.RBuilder<T> builder() {
        return new R.RBuilder();
    }

    @Override
    public String toString() {
        return "R(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public R<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public R<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static class RBuilder<T> {
        private String code;
        private String msg;
        private T data;

        RBuilder() {
        }

        public R.RBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public R.RBuilder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public R.RBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public R<T> build() {
            return new R(this.code, this.msg, this.data);
        }

        @Override
        public String toString() {
            return "R.RBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
        }
    }
}
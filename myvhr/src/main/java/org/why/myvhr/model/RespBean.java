package org.why.myvhr.model;
public class RespBean {
    private Integer status;
    private String msg;
    private Object obj;

    public static org.why.myvhr.model.RespBean build() {
        return new org.why.myvhr.model.RespBean();
    }

    public static org.why.myvhr.model.RespBean ok(String msg) {
        return new org.why.myvhr.model.RespBean(200, msg, null);
    }

    public static org.why.myvhr.model.RespBean ok(String msg, Object obj) {
        return new org.why.myvhr.model.RespBean(200, msg, obj);
    }

    public static org.why.myvhr.model.RespBean error(String msg) {
        return new org.why.myvhr.model.RespBean(500, msg, null);
    }

    public static org.why.myvhr.model.RespBean error(String msg, Object obj) {
        return new org.why.myvhr.model.RespBean(500, msg, obj);
    }

    private RespBean() {
    }

    private RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public org.why.myvhr.model.RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public org.why.myvhr.model.RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public org.why.myvhr.model.RespBean setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}

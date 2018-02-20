package ru.bmstu.posterminalstub.model;

import org.springframework.stereotype.Component;

/**
 * Created by fruit on 10.12.2017.
 */

@Component
public class ModelView {
    private String rq;
    private String rs;

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }
}

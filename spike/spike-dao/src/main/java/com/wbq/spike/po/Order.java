package com.wbq.spike.po;

import java.io.Serializable;
import java.util.Date;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 8508616091013359853L;

    private Integer id;

    private Integer sid;

    private String name;

    private Date createTime;

    private Order(Builder builder) {
        setSid(builder.sid);
        setName(builder.name);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final class Builder {
        private Integer sid;
        private String name;

        public Builder() {
        }

        public Builder sid(Integer val) {
            sid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
package com.wbq.spike.po;

import java.io.Serializable;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 17 九月 2018
 *  
 */
public class Stock implements Serializable {
    private static final long serialVersionUID = 1968621192695272522L;

    private Integer id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;

    public Stock() {
    }

    private Stock(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setCount(builder.count);
        setSale(builder.sale);
        setVersion(builder.version);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private Integer count;
        private Integer sale;
        private Integer version;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder count(Integer val) {
            count = val;
            return this;
        }

        public Builder sale(Integer val) {
            sale = val;
            return this;
        }

        public Builder version(Integer val) {
            version = val;
            return this;
        }

        public Stock build() {
            return new Stock(this);
        }
    }
}
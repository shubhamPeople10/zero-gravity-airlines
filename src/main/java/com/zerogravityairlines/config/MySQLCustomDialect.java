package com.zerogravityairlines.config;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQLCustomDialect extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB";
    }
}
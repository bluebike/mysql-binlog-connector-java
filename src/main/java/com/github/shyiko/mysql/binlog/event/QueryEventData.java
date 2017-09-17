/*
 * Copyright 2013 Stanley Shyiko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shyiko.mysql.binlog.event;

/**
 * @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
public class QueryEventData implements EventData {

    public enum QueryStatusType {
       FLAGS2,
       SQL_MODE,
       CATALOG_CODE,
       AUTO_INCREMENT,
       CHARSET_CODE;
    }


    public static class QueryStatus {
        public QueryStatusType type;
    }

    public static class QueryStatusFlags2 extends QueryStatus {
        public int flags2;
        public QueryStatusFlags2(int flags2) {
            this.flags2 = flags2;
        }
        public String toString() {
            return "Q_FLAGS2(" + flags2 + ")";
        }
    }

    public static class QueryStatusSqlMode extends QueryStatus {
        public long sql_mode;
        public QueryStatusSqlMode(long sql_mode) {
            this.sql_mode = sql_mode;
        }
        public String toString() {
            return "Q_SQL_MODE(" + sql_mode + ")";
        }
    }

    public static class QueryStatusCatalog extends QueryStatus {
        String catalog;
        public QueryStatusCatalog(String catalog) {
            this.catalog = catalog;
        }
        public String toString() {
            return "Q_CATALOG(" + catalog + ")";
        }
    }

    public static class QueryStatusAutoIncrement extends QueryStatus {
        public int auto_increment_increment;
        public int auto_increment_offset;
        public QueryStatusAutoIncrement(int increment, int offset) {
            this.auto_increment_increment = increment;
            this.auto_increment_offset = offset;
        }
        public String toString() {
            return "Q_AUTO_INCREMENT(" + auto_increment_increment + "," + auto_increment_offset + ")";
        }
    }

    public static class QueryStatusCharset extends QueryStatus {
        public String charset;        
        public QueryStatusCharset(String charset) {
            this.charset = charset;
        }
        public String toString() {
            return "Q_CHARSET(" + charset + ")";
        }
    }

    
    private long threadId;
    private long executionTime;
    private int errorCode;
    private String database;
    private String sql;

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QueryEventData");
        sb.append("{threadId=").append(threadId);
        sb.append(", executionTime=").append(executionTime);
        sb.append(", errorCode=").append(errorCode);
        sb.append(", database='").append(database).append('\'');
        sb.append(", sql='").append(sql).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

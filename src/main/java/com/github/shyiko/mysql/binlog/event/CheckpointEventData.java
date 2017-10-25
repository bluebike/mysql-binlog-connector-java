
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
public class CheckpointEventData implements EventData {

    private String binlog_file_name;
    private int    binlog_file_len;

    public int getFilenLen() {
        return binlog_file_len;
    }
    
    public void setFileLen(int len) {
        this.binlog_file_len = len;
    }

    public String getFilenName() {
        return binlog_file_name;
    }
    
    public void setFileName(String name) {
        this.binlog_file_name = name;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CheckpointEventData");
        sb.append("{file=").append(binlog_file_name);
        sb.append(",len=").append(binlog_file_len);
        sb.append('}');
        return sb.toString();
    }
}

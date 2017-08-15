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
public class FormatDescriptionEventData implements EventData {

    private int binlogVersion;
    private String serverVersion;
    private int headerLength;
    private byte[] postHeaderLengths;

    public int getBinlogVersion() {
        return binlogVersion;
    }

    public void setBinlogVersion(int binlogVersion) {
        this.binlogVersion = binlogVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public void setPostHeaderLengths(byte[] lengths) {
        this.postHeaderLengths = lengths;
    }

    public byte[] getPostHeaderLengths() {
        return this.postHeaderLengths;
    }

    public int getPostHeaderLength(int eventNumber) {
        if(this.postHeaderLengths == null) {
            return 0;
        }
        if(this.postHeaderLengths.length <= eventNumber) {
            return 0;
        }
        return  this.postHeaderLengths[eventNumber] & 0xff;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FormatDescriptionEventData");
        sb.append("{binlogVersion=").append(binlogVersion);
        sb.append(", serverVersion='").append(serverVersion).append('\'');
        sb.append(", headerLength=").append(headerLength);
        if(postHeaderLengths != null) {
            sb.append(", postHeaders=").append(postHeaderLengths != null ).append(",");
            for(int i=0; i < postHeaderLengths.length; i++) {
                if(postHeaderLengths[i] > 0) {
                    sb.append('[');
                    sb.append(EventType.getByEventNumber(i));
                    sb.append('(').append(i).append(')');
                    sb.append('=').append(postHeaderLengths[i] & 0xff);
                    sb.append(']');
                }
            }
        } else {
            sb.append(", postHeaders=null");
        }
        sb.append('}');
        return sb.toString();
    }
}

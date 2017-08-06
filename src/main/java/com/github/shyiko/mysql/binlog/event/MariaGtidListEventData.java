
package com.github.shyiko.mysql.binlog.event;

import com.github.shyiko.mysql.binlog.MariaGtid;

/**
 * @author 
 */
public class MariaGtidListEventData implements EventData {

    public static final byte COMMIT_FLAG = 1;

    private int flags;
    private MariaGtid list[];

    public MariaGtid getGtid(int off) {
        return list[off];
    }

    public int getCount() {
        return list != null ? list.length : 0;
    }

    public void setGtids(MariaGtid list[]) {
        this.list = list;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MariaGtidEventData");
        sb.append("{flags=").append(flags).append(", gtids=");
        for(MariaGtid gtid: list) {
            sb.append("[" + gtid + "]");
        }
        sb.append('}');
        return sb.toString();
    }

}

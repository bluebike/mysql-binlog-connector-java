
package com.github.shyiko.mysql.binlog;

public class MariaGtid {

    private int domain_id;
    private int server_id;
    private long seq_no;
    private long commit_id;

    public MariaGtid(int domain_id, int server_id, long seq_no) {
        this.domain_id = domain_id;
        this.server_id = server_id;
        this.seq_no = seq_no;
    }
    

    public MariaGtid(int domain_id, int server_id, long seq_no, long commit_id) {
        this.domain_id = domain_id;
        this.server_id = server_id;
        this.seq_no = seq_no;
        this.commit_id = commit_id;
    }


    public int getDomainId() {
        return domain_id;
    }

    public int getServerId() {
        return server_id;
    }

    public long getSeqNo() {
        return seq_no;
    }

    public long getCommitId() {
        return commit_id;
    }
    
    public String toString() {
        return "MariaGtid{" + domain_id + "-" + server_id + "-" + seq_no + "}";
    }
    
}

/*
 * Copyright 2014 Patrick Prasse
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

public class MariaGtidEventData implements EventData {

    public static int STANDALONE = 1;
    public static int GROUP_COMMIT_ID = 2;
    public static int TRANSACTIONAL = 4;
    public static int ALLOW_PARALLEL = 8;
    public static int WAITED = 16;
    public static int DDL = 32;

    private int flags;
    private long commit_id;
    private int  domain_id;
    private long seq_no;

    public MariaGtidEventData() {
    }

    public MariaGtidEventData(int flags, int domain_id, long seq_no, long commit_id) {
        this.flags = flags;
        this.domain_id = domain_id;
        this.seq_no = seq_no;
        this.commit_id = commit_id;
    }

    public void setDomainId(int domainId) {
        this.domain_id = domainId;
    }

    public int getDomainId() {
        return this.domain_id;
    }

    public long getCommitId() {
        return this.commit_id;
    }

    public void setSeqNo(long seqNo) {
        this.seq_no = seqNo;
    }

    public long getSeqNo() {
        return this.seq_no;
    }



    public void setCommitId(int commitId) {
        this.commit_id = commitId;
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
        sb.append("{flags=").append(flags);
        sb.append(", domain_id=").append(domain_id);
        sb.append(", seq_no=").append(seq_no);
        sb.append(", commit_id=").append(commit_id);
        sb.append('}');
        return sb.toString();
    }
}

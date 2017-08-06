/*
 * Copyright 2013 Patrick Prasse
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
package com.github.shyiko.mysql.binlog.event.deserialization;

import com.github.shyiko.mysql.binlog.MariaGtid;
import com.github.shyiko.mysql.binlog.event.MariaGtidListEventData;
import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:pprasse@actindo.de">Patrick Prasse</a>
 */
public class MariaGtidListEventDataDeserializer implements EventDataDeserializer<MariaGtidListEventData> {

    @Override
    public MariaGtidListEventData deserialize(ByteArrayInputStream inputStream) throws IOException {


        int val = inputStream.readInteger(4);
        int count = val & ((1 << 28)-1);
        int flags = val & (0x0f << 28);
        MariaGtid list[] = new MariaGtid[count];

        for(int i=0; i < count; i++) {
            int domain_id = inputStream.readInteger(4);
            int server_id = inputStream.readInteger(4);
            long seq_no = inputStream.readLong(8);
            list[i] = new MariaGtid(domain_id, server_id, seq_no);
        }
        MariaGtidListEventData eventData = new MariaGtidListEventData();
        eventData.setFlags(flags);
        eventData.setGtids(list);
        return eventData;
        
    }
}

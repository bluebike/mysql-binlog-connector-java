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

import com.github.shyiko.mysql.binlog.event.MariaGtidEventData;
import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:pprasse@actindo.de">Patrick Prasse</a>
 */
public class MariaGtidEventDataDeserializer implements EventDataDeserializer<MariaGtidEventData> {

    @Override
    public MariaGtidEventData deserialize(ByteArrayInputStream inputStream) throws IOException {

        long seq_no = inputStream.readLong(8);
        int domain_id = inputStream.readInteger(4);
        int flags = inputStream.readInteger(1);
        long commit_id = 0;
        if((flags & MariaGtidEventData.GROUP_COMMIT_ID) != 0) {
            seq_no = 0;
            commit_id = inputStream.readLong(8);
        }
        MariaGtidEventData eventData = new MariaGtidEventData(flags, domain_id, seq_no, commit_id);
        return eventData;
    }
}

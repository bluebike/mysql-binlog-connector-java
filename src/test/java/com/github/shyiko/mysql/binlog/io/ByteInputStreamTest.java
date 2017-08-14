/*
 * Copyright 2015 Stanley Shyiko
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
package com.github.shyiko.mysql.binlog.io;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * @author jukka pihl</a>
 */
public class ByteInputStreamTest {

    @Test
    public void testCorrectness() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream( new byte[]{
                0x01,                 // readInteger(1) => 01
                0x01,0x02,            // readInteger(2) => 0x0201
                0x01,0x02,0x03,       // readInteger(3) => 0x030201
                0x01,0x02,0x03,0x04,  // readInteger(3) => 0x04030201
                0x11,0x22,            // readInteger2() => 0x2211
                0x11,0x22,0x33,       // readInteger3() => 0x332211
                0x11,0x22,0x33,0x44,  // readInteger4() => 0x44332211
                (byte)200,                  // readPackedInteger() => 200
                (byte)252,0x11,0x22,         // readPackedInteger() => 0x2211
                (byte)253,0x11,0x22,0x33,    // readPackedInteger() => 0x332211
                (byte)254,0x01,0x02,0x03,0x04,0x00,0x00,0x00,0x00,    // readPackedInteger() => 0x04030201
                0x57                  // read()  => 0x57

            });

        assertEquals(in.readInteger(1), 0x01);
        assertEquals(in.readInteger(2), 0x0201);
        assertEquals(in.readInteger(3), 0x030201);
        assertEquals(in.readInteger(4), 0x04030201);
        assertEquals(in.readInteger2(), 0x2211);
        assertEquals(in.readInteger3(), 0x332211);
        assertEquals(in.readInteger4(), 0x44332211);
        assertEquals(in.readPackedInteger(), 200);
        assertEquals(in.readPackedInteger(),  0x2211);
        assertEquals(in.readPackedInteger(),  0x332211);
        assertEquals(in.readPackedInteger(),  0x04030201);
        assertEquals(in.readInteger(1),  0x57);

    }
}

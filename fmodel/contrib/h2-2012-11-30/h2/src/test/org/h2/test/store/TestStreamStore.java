/*
 * Copyright 2004-2011 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.test.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.h2.mvstore.StreamStore;
import org.h2.test.TestBase;
import org.h2.util.IOUtils;
import org.h2.util.New;
import org.h2.util.StringUtils;
// import org.junit.Test;
// import static org.junit.Assert.*;

/**
 * Test the stream store.
 */
public class TestStreamStore extends TestBase {

    /**
     * Run just this test.
     *
     * @param a ignored
     */
    public static void main(String... a) throws Exception {
        TestBase.createCaller().init().test();
    }

    @Override
    public void test() throws IOException {
        testDetectIllegalId();
        testTreeStructure();
        testFormat();
        testWithExistingData();
        testWithFullMap();
        testLoop();
    }

    private void testDetectIllegalId() throws IOException {
        Map<Long, byte[]> map = New.hashMap();
        StreamStore store = new StreamStore(map);
        try {
            store.length(new byte[]{3, 0, 0});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            store.remove(new byte[]{3, 0, 0});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        map.put(0L, new byte[]{3, 0, 0});
        InputStream in = store.get(new byte[]{2, 1, 0});
        try {
            in.read();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private void testTreeStructure() throws IOException {

        final AtomicInteger reads = new AtomicInteger();
        Map<Long, byte[]> map = new HashMap<Long, byte[]>() {

            private static final long serialVersionUID = 1L;

            public byte[] get(Object k) {
                reads.incrementAndGet();
                return super.get(k);
            }

        };

        StreamStore store = new StreamStore(map);
        store.setMinBlockSize(10);
        store.setMaxBlockSize(100);
        byte[] id = store.put(new ByteArrayInputStream(new byte[10000]));
        InputStream in = store.get(id);
        assertEquals(0, in.read());
        assertEquals(3, reads.get());
    }

    private void testFormat() throws IOException {
        Map<Long, byte[]> map = New.hashMap();
        StreamStore store = new StreamStore(map);
        store.setMinBlockSize(10);
        store.setMaxBlockSize(20);
        store.setNextKey(123);

        byte[] id;

        id = store.put(new ByteArrayInputStream(new byte[200]));
        assertEquals(200, store.length(id));
        assertEquals("02c8018801", StringUtils.convertBytesToHex(id));

        id = store.put(new ByteArrayInputStream(new byte[0]));
        assertEquals("", StringUtils.convertBytesToHex(id));

        id = store.put(new ByteArrayInputStream(new byte[1]));
        assertEquals("000100", StringUtils.convertBytesToHex(id));

        id = store.put(new ByteArrayInputStream(new byte[3]));
        assertEquals("0003000000", StringUtils.convertBytesToHex(id));

        id = store.put(new ByteArrayInputStream(new byte[10]));
        assertEquals("010a8901", StringUtils.convertBytesToHex(id));

        byte[] combined = StringUtils.convertHexToBytes("0001aa0002bbcc");
        assertEquals(3, store.length(combined));
        InputStream in = store.get(combined);
        assertEquals(1, in.skip(1));
        assertEquals(0xbb, in.read());
        assertEquals(1, in.skip(1));
    }

    private void testWithExistingData() throws IOException {

        final AtomicInteger tests = new AtomicInteger();
        Map<Long, byte[]> map = new HashMap<Long, byte[]>() {

            private static final long serialVersionUID = 1L;

            public boolean containsKey(Object k) {
                tests.incrementAndGet();
                return super.containsKey(k);
            }

        };
        StreamStore store = new StreamStore(map);
        store.setMinBlockSize(10);
        store.setMaxBlockSize(20);
        store.setNextKey(0);
        for (int i = 0; i < 10; i++) {
            store.put(new ByteArrayInputStream(new byte[20]));
        }
        assertEquals(10, map.size());
        assertEquals(10, tests.get());
        for (int i = 0; i < 10; i++) {
            map.containsKey(i);
        }
        assertEquals(20, tests.get());
        store = new StreamStore(map);
        store.setMinBlockSize(10);
        store.setMaxBlockSize(20);
        store.setNextKey(0);
        assertEquals(0, store.getNextKey());
        for (int i = 0; i < 5; i++) {
            store.put(new ByteArrayInputStream(new byte[20]));
        }
        assertEquals(88, tests.get());
        assertEquals(15, store.getNextKey());
        assertEquals(15, map.size());
        for (int i = 0; i < 15; i++) {
            map.containsKey(i);
        }
    }

    private void testWithFullMap() throws IOException {
        final AtomicInteger tests = new AtomicInteger();
        Map<Long, byte[]> map = new HashMap<Long, byte[]>() {

            private static final long serialVersionUID = 1L;

            public boolean containsKey(Object k) {
                tests.incrementAndGet();
                if (((Long) k) < Long.MAX_VALUE / 2) {
                    // simulate a *very* full map
                    return true;
                }
                return super.containsKey(k);
            }

        };
        StreamStore store = new StreamStore(map);
        store.setMinBlockSize(20);
        store.setMaxBlockSize(100);
        store.setNextKey(0);
        store.put(new ByteArrayInputStream(new byte[100]));
        assertEquals(1, map.size());
        assertEquals(64, tests.get());
        assertEquals(Long.MAX_VALUE / 2 + 1, store.getNextKey());
    }

    private void testLoop() throws IOException {
        Map<Long, byte[]> map = New.hashMap();
        StreamStore store = new StreamStore(map);
        assertEquals(256 * 1024, store.getMaxBlockSize());
        assertEquals(256, store.getMinBlockSize());
        store.setNextKey(0);
        assertEquals(0, store.getNextKey());
        test(store, 10, 20, 1000);
        for (int i = 0; i < 20; i++) {
            test(store, 0, 128, i);
            test(store, 10, 128, i);
        }
        for (int i = 20; i < 200; i += 10) {
            test(store, 0, 128, i);
            test(store, 10, 128, i);
        }
    }

    private void test(StreamStore store, int minBlockSize, int maxBlockSize, int length) throws IOException {
        store.setMinBlockSize(minBlockSize);
        assertEquals(minBlockSize, store.getMinBlockSize());
        store.setMaxBlockSize(maxBlockSize);
        assertEquals(maxBlockSize, store.getMaxBlockSize());
        long next = store.getNextKey();
        Random r = new Random(length);
        byte[] data = new byte[length];
        r.nextBytes(data);
        byte[] id = store.put(new ByteArrayInputStream(data));
        if (length > 0 && length >= minBlockSize) {
            assertFalse(store.isInPlace(id));
        } else {
            assertTrue(store.isInPlace(id));
        }
        long next2 = store.getNextKey();
        if (length > 0 && length >= minBlockSize) {
            assertTrue(next2 > next);
        } else {
            assertEquals(next, next2);
        }
        if (length == 0) {
            assertEquals(0, id.length);
        }

        assertEquals(length, store.length(id));

        InputStream in = store.get(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        assertTrue(Arrays.equals(data, out.toByteArray()));

        in = store.get(id);
        in.close();
        assertEquals(-1, in.read());

        in = store.get(id);
        assertEquals(0, in.skip(0));
        if (length > 0) {
            assertEquals(1, in.skip(1));
            if (length > 1) {
                assertEquals(data[1] & 255, in.read());
                if (length > 2) {
                    assertEquals(1, in.skip(1));
                    if (length > 3) {
                        assertEquals(data[3] & 255, in.read());
                    }
                } else {
                    assertEquals(0, in.skip(1));
                }
            } else {
                assertEquals(-1, in.read());
            }
        } else {
            assertEquals(0, in.skip(1));
        }

        if (length > 12) {
            in = store.get(id);
            assertEquals(12, in.skip(12));
            assertEquals(data[12] & 255, in.read());
            long skipped = 0;
            while (true) {
                long s = in.skip(Integer.MAX_VALUE);
                if (s == 0) {
                    break;
                }
                skipped += s;
            }
            assertEquals(length - 13, skipped);
            assertEquals(-1, in.read());
        }

        store.remove(id);
        assertEquals(0, store.getMap().size());
    }

}

package com.protonail.bolt.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class BoltCursorTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void move_first() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {
                    try (BoltCursor cursor = boltTransaction.createCursor()) {
                        BoltKeyValue first = cursor.first();

                        Assert.assertNotNull(first);
                        Assert.assertArrayEquals(bucketName, first.getKey());
                        Assert.assertNull(first.getValue());
                    }
                }
            });
        }
    }

    @Test
    public void move_first_when_not_exists() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltCursor cursor = boltTransaction.createCursor()) {
                    BoltKeyValue first = cursor.first();

                    Assert.assertNull(first);
                }
            });
        }
    }

    @Test
    public void move_last() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {
                    try (BoltCursor cursor = boltTransaction.createCursor()) {
                        BoltKeyValue last = cursor.last();

                        Assert.assertNotNull(last);
                        Assert.assertArrayEquals(bucketName, last.getKey());
                        Assert.assertNull(last.getValue());
                    }
                }
            });
        }
    }

    @Test
    public void move_last_when_not_exists() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltCursor cursor = boltTransaction.createCursor()) {
                    BoltKeyValue last = cursor.last();

                    Assert.assertNull(last);
                }
            });
        }
    }

    @Test
    public void move_next() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored1 = boltTransaction.createBucket("test1".getBytes())) {
                    try (BoltBucket ignored2 = boltTransaction.createBucket("test2".getBytes())) {
                        try (BoltCursor cursor = boltTransaction.createCursor()) {
                            BoltKeyValue next = cursor.next();

                            Assert.assertNull(next);
                        }
                    }
                }
            });
        }
    }

    @Test
    public void move_from_first_to_next() throws IOException {
        byte[] bucketName1 = "test1".getBytes();
        byte[] bucketName2 = "test2".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored1 = boltTransaction.createBucket(bucketName1)) {
                    try (BoltBucket ignored2 = boltTransaction.createBucket(bucketName2)) {
                        try (BoltCursor cursor = boltTransaction.createCursor()) {
                            cursor.first();

                            BoltKeyValue next = cursor.next();

                            Assert.assertNotNull(next);
                            Assert.assertArrayEquals(bucketName2, next.getKey());
                            Assert.assertNull(next.getValue());
                        }
                    }
                }
            });
        }
    }

    @Test
    public void move_from_first_to_next_when_not_exists() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {
                    try (BoltCursor cursor = boltTransaction.createCursor()) {
                        cursor.first();

                        BoltKeyValue next = cursor.next();

                        Assert.assertNull(next);
                    }
                }
            });
        }
    }

    @Test
    public void move_prev() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored1 = boltTransaction.createBucket("test1".getBytes())) {
                    try (BoltBucket ignored2 = boltTransaction.createBucket("test2".getBytes())) {
                        try (BoltCursor cursor = boltTransaction.createCursor()) {
                            BoltKeyValue prev = cursor.prev();

                            Assert.assertNull(prev);
                        }
                    }
                }
            });
        }
    }

    @Test
    public void move_from_last_to_prev() throws IOException {
        byte[] bucketName1 = "test1".getBytes();
        byte[] bucketName2 = "test2".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored1 = boltTransaction.createBucket(bucketName1)) {
                    try (BoltBucket ignored2 = boltTransaction.createBucket(bucketName2)) {
                        try (BoltCursor cursor = boltTransaction.createCursor()) {
                            cursor.last();

                            BoltKeyValue prev = cursor.prev();

                            Assert.assertNotNull(prev);
                            Assert.assertArrayEquals(bucketName1, prev.getKey());
                            Assert.assertNull(prev.getValue());
                        }
                    }
                }
            });
        }
    }

    @Test
    public void move_from_last_to_prev_when_not_exists() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {
                    try (BoltCursor cursor = boltTransaction.createCursor()) {
                        cursor.last();

                        BoltKeyValue prev = cursor.prev();

                        Assert.assertNull(prev);
                    }
                }
            });
        }
    }

    @Test
    public void seek() throws IOException {
        byte[] bucketName1 = "test1".getBytes();
        byte[] bucketName2 = "test2".getBytes();
        byte[] bucketName3 = "test3".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try(BoltBucket ignored1 = boltTransaction.createBucket(bucketName1)) {
                    try(BoltBucket ignored2 = boltTransaction.createBucket(bucketName2)) {
                        try(BoltBucket ignored3 = boltTransaction.createBucket(bucketName3)) {
                            try (BoltCursor cursor = boltTransaction.createCursor()) {
                                BoltKeyValue seek = cursor.seek(bucketName2);

                                Assert.assertNotNull(seek);
                                Assert.assertArrayEquals(bucketName2, seek.getKey());
                            }
                        }
                    }
                }
            });
        }
    }

    @Test
    public void seek_to_unknown_position() throws IOException {
        byte[] bucketName1 = "test1".getBytes();
        byte[] bucketName2 = "test2".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try(BoltBucket ignored = boltTransaction.createBucket(bucketName1)) {
                    try (BoltCursor cursor = boltTransaction.createCursor()) {
                        BoltKeyValue seek = cursor.seek(bucketName2);

                        Assert.assertNull(seek);
                    }
                }
            });
        }
    }

    @Test
    public void delete() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try(BoltBucket bucket = boltTransaction.createBucket(bucketName)) {
                    byte[] key = "key".getBytes();
                    byte[] value = "value".getBytes();

                    bucket.put(key, value);

                    try (BoltCursor cursor = bucket.createCursor()) {
                        cursor.first();

                        cursor.delete();
                    }
                }
            });
        }
    }
}

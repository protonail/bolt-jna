package com.protonail.bolt.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class BoltBucketTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void put_and_get() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    byte[] key = "foo".getBytes();
                    byte[] value = "bar".getBytes();

                    bucket.put(key, value);
                    byte[] getValue = bucket.get(key);

                    Assert.assertArrayEquals(value, getValue);
                }
            });
        }
    }

    @Test
    public void delete() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try(BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    byte[] key = "foo".getBytes();
                    byte[] value = "bar".getBytes();

                    bucket.put(key, value);
                    bucket.delete(key);

                    byte[] getValue = bucket.get(key);

                    Assert.assertNull(getValue);
                }
            });
        }
    }


    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try(BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try(BoltBucket ignored = parentBucket.createBucket("test".getBytes())) {}
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    public void create_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    parentBucket.createBucket("".getBytes());
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucket(bucketName)) {}

                    parentBucket.createBucket(bucketName);
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_if_not_exists() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucketIfNotExists(bucketName)) {}
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    public void create_bucket_if_not_exists_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    parentBucket.createBucketIfNotExists("".getBytes());
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_if_not_exists_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucketIfNotExists(bucketName)) {}
                    try (BoltBucket ignored = parentBucket.createBucketIfNotExists(bucketName)) {}
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void delete_bucket() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucket(bucketName)) {}

                    parentBucket.deleteBucket(bucketName);
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    public void delete_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    parentBucket.deleteBucket("".getBytes());
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    @SuppressWarnings("EmptyTryBlock")
    public void delete_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucket(bucketName)) {
                    }

                    parentBucket.deleteBucket(bucketName);

                    parentBucket.deleteBucket(bucketName);
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void get_bucket() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucket(bucketName)) {}

                    try (BoltBucket ignored = parentBucket.getBucket(bucketName)) {}
                }
            });
        }
    }

    @Test(expected = BoltException.class)
    public void get_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    parentBucket.getBucket("".getBytes());
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void get_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket parentBucket = boltTransaction.createBucket("parent".getBytes())) {
                    try (BoltBucket ignored = parentBucket.createBucket(bucketName)) {}

                    try (BoltBucket ignored = parentBucket.getBucket(bucketName)) {}
                    try (BoltBucket ignored = parentBucket.getBucket(bucketName)) {}
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_cursor() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    try (BoltCursor ignored = bucket.createCursor()) {}
                }
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void iterating_over_buckets_and_keys() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    try (BoltBucket ignored = bucket.createBucket("sub-bucket".getBytes())) {}

                    bucket.put("key".getBytes(), "key-value".getBytes());

                    try (BoltCursor cursor = bucket.createCursor()) {
                        int iteration = 0;

                        BoltKeyValue entry = cursor.first();
                        while (entry != null) {
                            iteration += 1;

                            if (entry.isKey()) {
                                Assert.assertArrayEquals("key".getBytes(), entry.getKey());
                                Assert.assertArrayEquals("key-value".getBytes(), entry.getValue());
                            } else if (entry.isBucket()) {
                                Assert.assertArrayEquals("sub-bucket".getBytes(), entry.getKey());
                                Assert.assertNull(entry.getValue());
                            } else {
                                Assert.fail("Unknown key type.");
                            }

                            entry = cursor.next();
                        }

                        Assert.assertEquals(2, iteration);
                    }
                }
            });
        }
    }

    @Test
    public void get_sequence_ids() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    Assert.assertEquals(1, bucket.nextSequenceId());
                    Assert.assertEquals(2, bucket.nextSequenceId());
                }
            });
        }
    }

    @Test
    public void get_stats() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test1".getBytes())) {
                    bucket.put("key1.1".getBytes(), "value1.1".getBytes());

                    bucket.createBucket("test1.test1".getBytes()).close();
                }
                try (BoltBucket bucket = boltTransaction.createBucket("test2".getBytes())) {
                    bucket.put("key2.1".getBytes(), "value2.1".getBytes());
                    bucket.put("key2.2".getBytes(), "value2.2".getBytes());

                    bucket.createBucket("test2.test1".getBytes()).close();
                    bucket.createBucket("test2.test2".getBytes()).close();
                }
            });

            bolt.view(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.getBucket("test2".getBytes())) {
                    BoltBucketStats stats = bucket.getStats();

                    Assert.assertNotNull(stats);
                    Assert.assertEquals(4, stats.getKeyN());
                    Assert.assertEquals(2, stats.getBucketN() - 1);
                }
            });
        }
    }
}

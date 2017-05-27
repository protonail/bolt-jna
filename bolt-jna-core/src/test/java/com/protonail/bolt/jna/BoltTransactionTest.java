package com.protonail.bolt.jna;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class BoltTransactionTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void commit_transaction() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            BoltTransaction boltTransaction = bolt.begin(true);
            boltTransaction.commit();
        }
    }

    @Test
    public void rollback_transaction() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            BoltTransaction boltTransaction = bolt.begin(true);
            boltTransaction.rollback();
        }
    }

    @Test
    public void get_transaction_id() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> Assert.assertTrue(boltTransaction.getId() > 0));
        }
    }

    @Test
    public void get_database_size() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> Assert.assertTrue(boltTransaction.getDatabaseSize() > 0));
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket("test".getBytes())) {}
            });
        }
    }

    @Test(expected = BoltException.class)
    public void create_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> boltTransaction.createBucket("".getBytes()));
        }
    }

    @Test(expected = BoltException.class)
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {}

                boltTransaction.createBucket(bucketName);
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_if_not_exists() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucketIfNotExists(bucketName)) {}
            });
        }
    }

    @Test(expected = BoltException.class)
    public void create_bucket_if_not_exists_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> boltTransaction.createBucketIfNotExists("".getBytes()));
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_bucket_if_not_exists_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucketIfNotExists(bucketName)) {}
                try (BoltBucket ignored = boltTransaction.createBucketIfNotExists(bucketName)) {}
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void delete_bucket() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {}

                boltTransaction.deleteBucket(bucketName);
            });
        }
    }

    @Test(expected = BoltException.class)
    public void delete_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> boltTransaction.deleteBucket("".getBytes()));
        }
    }

    @Test(expected = BoltException.class)
    @SuppressWarnings("EmptyTryBlock")
    public void delete_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {}

                boltTransaction.deleteBucket(bucketName);

                boltTransaction.deleteBucket(bucketName);
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void get_bucket() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {}

                try (BoltBucket ignored = boltTransaction.getBucket(bucketName)) {}
            });
        }
    }

    @Test(expected = BoltException.class)
    public void get_bucket_with_empty_name() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> boltTransaction.getBucket("".getBytes()));
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void get_bucket_twice() throws IOException {
        byte[] bucketName = "test".getBytes();

        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket ignored = boltTransaction.createBucket(bucketName)) {}

                try (BoltBucket ignored = boltTransaction.getBucket(bucketName)) {}
                try (BoltBucket ignored = boltTransaction.getBucket(bucketName)) {}
            });
        }
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    public void create_cursor() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltCursor ignored = boltTransaction.createCursor()) {}
            });
        }
    }

    @Test
    public void get_stats() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    bucket.put("key".getBytes(), "value".getBytes());
                }

                BoltTransactionStats stats = boltTransaction.getStats();

                Assert.assertNotNull(stats);
                Assert.assertTrue(stats.getCursorCount() > 0);
            });
        }
    }
}

package com.protonail.bolt.jna;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class BoltTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void open_and_close() throws IOException {
        Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath());
        bolt.close();
    }

    @Test
    public void open_from_other_thread() {
        Thread thread = new Thread(() -> {
            try {
                Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath());
                bolt.close();
            } catch (Throwable e) {
                Assert.fail("Bolt adapter can't be created in other thread.");
            }
        });
        thread.run();
    }

    @Test
    @Ignore
    public void open_and_close_with_file_mode() throws IOException {
        Path databaseFileName = Paths.get(testFolder.getRoot().getAbsolutePath(), "bd.bolt");
        EnumSet<BoltFileMode> fileMode = EnumSet.of(BoltFileMode.USER_READ, BoltFileMode.USER_WRITE, BoltFileMode.GROUP_READ, BoltFileMode.GROUP_WRITE);

        Bolt bolt = new Bolt(databaseFileName.toString(), fileMode);
        bolt.close();

        Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(databaseFileName);
        Assert.assertEquals(4, permissions.size());
        Assert.assertTrue(permissions.containsAll(Arrays.asList(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE)));
    }

    @Test
    public void open_and_close_with_options() throws IOException {
        try(BoltOptions boltOptions = new BoltOptions()) {
            Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath(), BoltFileMode.DEFAULT, boltOptions);
            bolt.close();
        }
    }

    @Test(expected = BoltException.class)
    @SuppressWarnings("EmptyTryBlock")
    public void open_database_twice() throws IOException {
        String databaseFileName = testFolder.newFile().getAbsolutePath();
        try(BoltOptions boltOptions = new BoltOptions()) {
            try (Bolt ignored = new Bolt(databaseFileName, BoltFileMode.DEFAULT, boltOptions)) {
                try (Bolt ignored2 = new Bolt(databaseFileName, BoltFileMode.DEFAULT, boltOptions)) {}
            }
        }
    }

    @Test
    public void open_update_transaction() throws IOException {
        String databaseFileName = testFolder.newFile().getAbsolutePath();
        try(Bolt bolt = new Bolt(databaseFileName)) {
            final boolean[] wasInTransaction = {false};

            bolt.update(boltTransaction -> {
                Assert.assertNotNull(boltTransaction);

                wasInTransaction[0] = true;
            });

            Assert.assertTrue(wasInTransaction[0]);
        }
    }

    @Test
    public void open_view_transaction() throws IOException {
        String databaseFileName = testFolder.newFile().getAbsolutePath();
        try(Bolt bolt = new Bolt(databaseFileName)) {
            final boolean[] wasInTransaction = {false};

            bolt.view(boltTransaction -> {
                Assert.assertNotNull(boltTransaction);

                wasInTransaction[0] = true;
            });

            Assert.assertTrue(wasInTransaction[0]);
        }
    }

    @Test
    public void get_stats() throws IOException {
        try(Bolt bolt = new Bolt(testFolder.newFile().getAbsolutePath())) {
            bolt.update(boltTransaction -> {
                try (BoltBucket bucket = boltTransaction.createBucket("test".getBytes())) {
                    bucket.put("key".getBytes(), "value".getBytes());
                }
            });

            BoltStats stats = bolt.getStats();

            Assert.assertNotNull(stats);
            Assert.assertTrue(stats.getFreeAlloc() > 0);
        }
    }
}

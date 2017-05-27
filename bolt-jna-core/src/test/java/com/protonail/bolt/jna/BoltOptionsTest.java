package com.protonail.bolt.jna;

import org.junit.Test;

public class BoltOptionsTest {
    @Test
    public void create_and_free() {
        BoltOptions boltOptions = new BoltOptions();
        boltOptions.close();
    }
}

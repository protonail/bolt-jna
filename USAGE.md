# Usage

## Maven dependencies

Add Maven dependencies to your pom.xml:

```xml
<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-core</artifactId>
    <version>x.x.x</version>
</dependency>

<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>osx</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>windows-x86_64</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>windows-x86</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>linux-x86_64</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.bolt-jna</groupId>
    <artifactId>bolt-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>linux-x86</classifier>
</dependency>
```

If you don't want to use native libraries for some platforms (see `classifier` tag) then just remove theirs dependencies.

If you want to use snapshot version (with `-SNAPSHOR` suffix) then just add snapshot repository to your pom.xml:

```xml
<repositories>
    ...
    <repository>
        <id>sonatype</id>
        <url>https://oss.sonatype.org/content/groups/public</url>
    </repository>
    ...
</repositories>
```

## Initialize Go runtime

Go runtime should be initialized straight after Java application start. It it can help to avoid
"could not obtain pthread_keys" error (details [here](https://github.com/golang/go/issues/17200)).

```java
public static void main() {
    Bolt.init() // Hack for fix error: could not obtain pthread_keys
    ...
}
```

## Create/Open LevelDB database

```java
try(Bolt bolt = new Bolt(databaseFile, options)) {
    // Work with database here
}
```

or with options

```java
try(BoltOptions options = new BoltOptions(5000 /* timeout in ms */)) {
    try(Bolt bolt = new Bolt(databaseFile, BoltFileMode.DEFAULT, options)) {
        // Work with database here
    }
}
```

## Create bucket

```java
bolt.update(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.createBucket("my-bucket".getBytes())) { // 'my-bucket' must not be exists
        // Work with bucket here
    }
});
```

or user `createBucketIfNotExists`

```java
bolt.update(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.createBucketIfNotExists("my-bucket".getBytes())) { // 'my-bucket' must not be exists
        // Work with bucket here
    }
});
```

## Delete bucket

```java
bolt.update(boltTransaction -> {
    boltTransaction.deleteBucket("my-bucket".getBytes()); // 'my-bucket' must be exists
});
```

## Get value by key

```java
bolt.view(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.getBucket("my-bucket".getBytes())) { // 'my-bucket' must be exists
        byte[] value = bucket.get("key".getBytes());
    }
});
```

## Put value by key

```java
bolt.update(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.getBucket("my-bucket".getBytes())) { // 'my-bucket' must be exists
        bucket.put("key".getBytes(), "value".getBytes());
    }
});
```

## Delete value by key

```java
bolt.update(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.getBucket("my-bucket".getBytes())) { // 'my-bucket' must be exists
        bucket.delete("key".getBytes());
    }
});
```

## Iterate by keys and values

```java
bolt.view(boltTransaction -> {
    try(BoltBucket bucket = boltTransaction.getBucket("my-bucket".getBytes())) { // 'my-bucket' must be exists
        try(BoltCursor cursor = bucket.createCursor()) {
            BoltKeyValue currentKeyValue = cursor.first();
            while (currentKeyValue != null) {
                byte[] key = currentKeyValue.getKey();
                byte[] value = currentKeyValue.getValue();

                currentKeyValue = cursor.next();
            }
        }
    }
});
```
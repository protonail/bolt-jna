package main

// #include "types.h"
import "C"

import (
	"github.com/boltdb/bolt"
	"log"
	"unsafe"
)

var boltDbBucketsObjectPool *ObjectPool = NewObjectPool()

//export BoltDBBucket_Free
func BoltDBBucket_Free(objectId C.ObjectId) {
	if _, ok := boltDbBucketsObjectPool.Release(ObjectId(objectId)); !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", objectId)
	}
}

//export BoltDBBucket_Get
func BoltDBBucket_Get(bucketObjectId C.ObjectId, keyPointer unsafe.Pointer, keyLength C.int) C.Value {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	key := C.GoBytes(keyPointer, keyLength)
	value := bucket.(*bolt.Bucket).Get(key)
	if value == nil {
		return C.Value{value: nil, valueLength: 0}
	}

	return C.Value{value: C.CBytes(value), valueLength: C.int(len(value))}
}

//export BoltDBBucket_Put
func BoltDBBucket_Put(bucketObjectId C.ObjectId, keyPointer unsafe.Pointer, keyLength C.int, valuePointer unsafe.Pointer, valueLength C.int) C.Error {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	key := C.GoBytes(keyPointer, keyLength)
	value := C.GoBytes(valuePointer, valueLength)
	err := bucket.(*bolt.Bucket).Put(key, value)
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBBucket_Delete
func BoltDBBucket_Delete(bucketObjectId C.ObjectId, keyPointer unsafe.Pointer, keyLength C.int) C.Error {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	key := C.GoBytes(keyPointer, keyLength)
	err := bucket.(*bolt.Bucket).Delete(key)
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBBucket_CreateBucket
func BoltDBBucket_CreateBucket(bucketObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	parentBucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket, err := parentBucket.(*bolt.Bucket).CreateBucket(bucketName)
	if err != nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(err.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBBucket_CreateBucketIfNotExists
func BoltDBBucket_CreateBucketIfNotExists(bucketObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	parentBucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket, err := parentBucket.(*bolt.Bucket).CreateBucketIfNotExists(bucketName)
	if err != nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(err.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBBucket_Bucket
func BoltDBBucket_Bucket(bucketObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	parentBucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket := parentBucket.(*bolt.Bucket).Bucket(bucketName)
	if bucket == nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(bolt.ErrBucketNotFound.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBBucket_DeleteBucket
func BoltDBBucket_DeleteBucket(bucketObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Error {
	parentBucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	err := parentBucket.(*bolt.Bucket).DeleteBucket(bucketName)
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBBucket_Cursor
func BoltDBBucket_Cursor(bucketObjectId C.ObjectId) C.ObjectId {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	cursor := bucket.(*bolt.Bucket).Cursor()

	return C.ObjectId(boltDbCursorsObjectPool.Put(cursor))
}

func BoltDBBucket_ForEachBucket(bucketObjectId C.ObjectId /*, callback */) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

//export BoltDBBucket_NextSequence
func BoltDBBucket_NextSequence(bucketObjectId C.ObjectId) C.Sequence {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	id, err := bucket.(*bolt.Bucket).NextSequence()
	if err != nil {
		return C.Sequence{id: C.ulonglong(0), error: C.CString(err.Error())}
	}

	return C.Sequence{id: C.ulonglong(id), error: nil}
}

//export BoltDBBucket_Stats
func BoltDBBucket_Stats(bucketObjectId C.ObjectId) C.BucketStats {
	bucket, ok := boltDbBucketsObjectPool.Get(ObjectId(bucketObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Bucket object from object pool by objectId == %d", bucketObjectId)
	}

	stats := bucket.(*bolt.Bucket).Stats()

	return C.BucketStats{
		branchPageN:     C.int(stats.BranchPageN),
		branchOverflowN: C.int(stats.BranchOverflowN),
		leafPageN:       C.int(stats.LeafPageN),
		leafOverflowN:   C.int(stats.LeafOverflowN),

		keyN:  C.int(stats.KeyN),
		depth: C.int(stats.Depth),

		branchAlloc: C.int(stats.BranchAlloc),
		branchInUse: C.int(stats.BranchInuse),
		leafAlloc:   C.int(stats.LeafAlloc),
		leafInUse:   C.int(stats.LeafInuse),

		bucketN:           C.int(stats.BucketN),
		inlineBucketN:     C.int(stats.InlineBucketN),
		inlineBucketInUse: C.int(stats.InlineBucketInuse),
	}
}

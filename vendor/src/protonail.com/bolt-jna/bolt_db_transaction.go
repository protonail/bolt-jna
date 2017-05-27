package main

// #include "types.h"
import "C"

import (
	"github.com/boltdb/bolt"
	"log"
	"unsafe"
)

var boltDbTransactionsObjectPool *ObjectPool = NewObjectPool()

//export BoltDBTransaction_Commit
func BoltDBTransaction_Commit(transactionObjectId C.ObjectId) C.Error {
	transaction, ok := boltDbTransactionsObjectPool.Release(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	err := transaction.(*bolt.Tx).Commit()
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBTransaction_Rollback
func BoltDBTransaction_Rollback(transactionObjectId C.ObjectId) C.Error {
	transaction, ok := boltDbTransactionsObjectPool.Release(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	err := transaction.(*bolt.Tx).Rollback()
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBTransaction_GetId
func BoltDBTransaction_GetId(transactionObjectId C.ObjectId) C.int {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	return C.int(transaction.(*bolt.Tx).ID())
}

//export BoltDBTransaction_Size
func BoltDBTransaction_Size(transactionObjectId C.ObjectId) C.longlong {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	return C.longlong(transaction.(*bolt.Tx).Size())
}

//export BoltDBTransaction_CreateBucket
func BoltDBTransaction_CreateBucket(transactionObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket, err := transaction.(*bolt.Tx).CreateBucket(bucketName)
	if err != nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(err.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBTransaction_CreateBucketIfNotExists
func BoltDBTransaction_CreateBucketIfNotExists(transactionObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket, err := transaction.(*bolt.Tx).CreateBucketIfNotExists(bucketName)
	if err != nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(err.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBTransaction_Bucket
func BoltDBTransaction_Bucket(transactionObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Result {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	bucket := transaction.(*bolt.Tx).Bucket(bucketName)
	if bucket == nil {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(bolt.ErrBucketNotFound.Error())}
	}

	return C.Result{objectId: C.ObjectId(boltDbBucketsObjectPool.Put(bucket)), error: nil}
}

//export BoltDBTransaction_DeleteBucket
func BoltDBTransaction_DeleteBucket(transactionObjectId C.ObjectId, name unsafe.Pointer, nameLength C.int) C.Error {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	bucketName := C.GoBytes(name, nameLength)
	err := transaction.(*bolt.Tx).DeleteBucket(bucketName)
	if err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

//export BoltDBTransaction_Cursor
func BoltDBTransaction_Cursor(transactionObjectId C.ObjectId) C.ObjectId {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	cursor := transaction.(*bolt.Tx).Cursor()

	return C.ObjectId(boltDbCursorsObjectPool.Put(cursor))
}

func BoltDBTransaction_ForEachBucket(transactionObjectId C.ObjectId /* callback */) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

func BoltDBTransaction_CopyFile(transactionObjectId C.ObjectId, fileName C.String, fileMode C.FileMode) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

func BoltDBTransaction_Check(transactionObjectId C.ObjectId) /* chan <- error */ {
	//TODO: Not Implemented Yet
}

//export BoltDBTransaction_Stats
func BoltDBTransaction_Stats(transactionObjectId C.ObjectId) C.TxStats {
	transaction, ok := boltDbTransactionsObjectPool.Get(ObjectId(transactionObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Tx object from object pool by objectId == %d", transactionObjectId)
	}

	stats := transaction.(*bolt.Tx).Stats()

	return C.TxStats{
		pageCount: C.int(stats.PageCount),
		pageAlloc: C.int(stats.PageAlloc),

		cursorCount: C.int(stats.CursorCount),

		nodeCount: C.int(stats.NodeCount),
		nodeDeref: C.int(stats.NodeDeref),

		rebalance:     C.int(stats.Rebalance),
		rebalanceTime: C.longlong(stats.RebalanceTime),

		split:     C.int(stats.Split),
		spill:     C.int(stats.Spill),
		spillTime: C.longlong(stats.SpillTime),

		write:     C.int(stats.Write),
		writeTime: C.longlong(stats.WriteTime),
	}
}

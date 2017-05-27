package main

// #include "types.h"
import "C"

import (
	"github.com/boltdb/bolt"
	"log"
	"os"
)

var boltDbObjectPool *ObjectPool = NewObjectPool()

//export BoltDB_Open
func BoltDB_Open(databaseFileName C.String, mode C.FileMode, optionsObjectId C.ObjectId) C.Result {
	var options *bolt.Options = nil
	if rawOptions, ok := boltDbOptionsObjectPool.Get(ObjectId(optionsObjectId)); ok {
		options = rawOptions.(*bolt.Options)
	}

	if db, err := bolt.Open(C.GoString(databaseFileName), os.FileMode(mode), options); err == nil {
		return C.Result{objectId: C.ObjectId(boltDbObjectPool.Put(db)), error: nil}
	} else {
		return C.Result{objectId: 0, error: C.CString(err.Error())}
	}
}

//export BoltDB_Close
func BoltDB_Close(boltDBObjectId C.ObjectId) {
	db, ok := boltDbObjectPool.Release(ObjectId(boltDBObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.DB object from object pool by objectId == %d", boltDBObjectId)
	}

	db.(*bolt.DB).Close()
}

//export BoltDB_GetPath
func BoltDB_GetPath(boltDBObjectId C.ObjectId) C.String {
	db, ok := boltDbObjectPool.Get(ObjectId(boltDBObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.DB object from object pool by objectId == %d", boltDBObjectId)
	}

	return C.CString(db.(*bolt.DB).Path())
}

//export BoltDB_IsReadOnly
func BoltDB_IsReadOnly(boltDBObjectId C.ObjectId) C.Bool {
	db, ok := boltDbObjectPool.Get(ObjectId(boltDBObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.DB object from object pool by objectId == %d", boltDBObjectId)
	}

	if db.(*bolt.DB).IsReadOnly() { //TODO: how to improve it?
		return C.Bool(1)
	} else {
		return C.Bool(0)
	}
}

//export BoltDB_Begin
func BoltDB_Begin(boltDBObjectId C.ObjectId, writeable C.Bool) C.Result {
	db, ok := boltDbObjectPool.Get(ObjectId(boltDBObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.DB object from object pool by objectId == %d", boltDBObjectId)
	}

	if transaction, err := db.(*bolt.DB).Begin(writeable != 0); err == nil {
		return C.Result{objectId: C.ObjectId(boltDbTransactionsObjectPool.Put(transaction)), error: nil}
	} else {
		return C.Result{objectId: C.ObjectId(0), error: C.CString(err.Error())}
	}
}

func BoltDB_Update(objectId C.ObjectId /*, callback*/) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

func BoltDB_View(objectId C.ObjectId /*, callback*/) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

func BoltDB_Batch(objectId C.ObjectId /*, callback*/) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

func BoltDB_Sync(objectId C.ObjectId) C.Error {
	//TODO: Not Implemented Yet
	return C.Error{error: C.CString("Not Implemented Yet")}
}

//export BoltDB_Stats
func BoltDB_Stats(boltDBObjectId C.ObjectId) C.Stats {
	db, ok := boltDbObjectPool.Get(ObjectId(boltDBObjectId))
	if !ok {
		log.Fatalf("Can't get bolt.DB object from object pool by objectId == %d", boltDBObjectId)
	}

	stats := db.(*bolt.DB).Stats()

	return C.Stats{
		freePageN:     C.int(stats.FreePageN),
		pendingPageN:  C.int(stats.PendingPageN),
		freeAlloc:     C.int(stats.FreeAlloc),
		freelistInUse: C.int(stats.FreelistInuse),

		txN:     C.int(stats.TxN),
		openTxN: C.int(stats.OpenTxN),
		txStats: C.TxStats{
			pageCount: C.int(stats.TxStats.PageCount),
			pageAlloc: C.int(stats.TxStats.PageAlloc),

			cursorCount: C.int(stats.TxStats.CursorCount),

			nodeCount: C.int(stats.TxStats.NodeCount),
			nodeDeref: C.int(stats.TxStats.NodeDeref),

			rebalance:     C.int(stats.TxStats.Rebalance),
			rebalanceTime: C.longlong(stats.TxStats.RebalanceTime),

			split:     C.int(stats.TxStats.Split),
			spill:     C.int(stats.TxStats.Spill),
			spillTime: C.longlong(stats.TxStats.SpillTime),

			write:     C.int(stats.TxStats.Write),
			writeTime: C.longlong(stats.TxStats.WriteTime),
		},
	}
}

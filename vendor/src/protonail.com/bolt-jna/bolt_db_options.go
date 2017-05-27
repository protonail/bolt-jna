package main

// #include "types.h"
import "C"

import (
	"github.com/boltdb/bolt"
	"log"
	"time"
)

var boltDbOptionsObjectPool *ObjectPool = NewObjectPool()

//export BoltDBOptions_Create
func BoltDBOptions_Create(timeout C.longlong, noGrowSync C.Bool, readOnly C.Bool, mmapFlags C.int, initialMmapSize C.int) C.ObjectId {
	options := &bolt.Options{
		Timeout:         time.Duration(timeout),
		NoGrowSync:      noGrowSync != 0,
		ReadOnly:        readOnly != 0,
		MmapFlags:       int(mmapFlags),
		InitialMmapSize: int(initialMmapSize),
	}

	return C.ObjectId(boltDbOptionsObjectPool.Put(options))
}

//export BoltDBOptions_Free
func BoltDBOptions_Free(objectId C.ObjectId) {
	if _, ok := boltDbOptionsObjectPool.Release(ObjectId(objectId)); !ok {
		log.Fatalf("Can't get *bolt.Options object from object pool by objectId == %d", objectId)
	}
}

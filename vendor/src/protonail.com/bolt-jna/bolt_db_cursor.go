package main

// #include "types.h"
import "C"

import (
	"github.com/boltdb/bolt"
	"log"
	"unsafe"
)

var boltDbCursorsObjectPool *ObjectPool = NewObjectPool()

//export BoltDBCursor_Free
func BoltDBCursor_Free(cursorObjectId C.ObjectId) {
	if _, ok := boltDbCursorsObjectPool.Release(ObjectId(cursorObjectId)); !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}
}

//export BoltDBCursor_First
func BoltDBCursor_First(cursorObjectId C.ObjectId) C.KeyValue {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	key, value := cursor.(*bolt.Cursor).First()
	return buildKeyValueResult(key, value)
}

//export BoltDBCursor_Last
func BoltDBCursor_Last(cursorObjectId C.ObjectId) C.KeyValue {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	key, value := cursor.(*bolt.Cursor).Last()
	return buildKeyValueResult(key, value)
}

//export BoltDBCursor_Next
func BoltDBCursor_Next(cursorObjectId C.ObjectId) C.KeyValue {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	key, value := cursor.(*bolt.Cursor).Next()
	return buildKeyValueResult(key, value)
}

//export BoltDBCursor_Prev
func BoltDBCursor_Prev(cursorObjectId C.ObjectId) C.KeyValue {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	key, value := cursor.(*bolt.Cursor).Prev()
	return buildKeyValueResult(key, value)
}

//export BoltDBCursor_Seek
func BoltDBCursor_Seek(cursorObjectId C.ObjectId, seekPointer unsafe.Pointer, seekLength C.int) C.KeyValue {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	seek := C.GoBytes(seekPointer, seekLength)
	key, value := cursor.(*bolt.Cursor).Seek(seek)
	return buildKeyValueResult(key, value)
}

//export BoltDBCursor_Delete
func BoltDBCursor_Delete(cursorObjectId C.ObjectId) C.Error {
	cursor, ok := boltDbCursorsObjectPool.Get(ObjectId(cursorObjectId))
	if !ok {
		log.Fatalf("Can't get *bolt.Cursor object from object pool by objectId == %d", cursorObjectId)
	}

	if err := cursor.(*bolt.Cursor).Delete(); err != nil {
		return C.Error{error: C.CString(err.Error())}
	}

	return C.Error{error: nil}
}

func buildKeyValueResult(key []byte, value []byte) C.KeyValue {
	var cKey unsafe.Pointer = nil
	var cKeyLength C.int = C.int(-1)
	if key != nil {
		cKey = C.CBytes(key)
		cKeyLength = C.int(len(key))
	}

	var cValue unsafe.Pointer = nil
	var cValueLength C.int = C.int(-1)
	if value != nil {
		cValue = C.CBytes(value)
		cValueLength = C.int(len(value))
	}

	return C.KeyValue{key: cKey, keyLength: cKeyLength, value: cValue, valueLength: cValueLength}
}

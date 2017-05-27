package main

import "C"

import (
	"sync"
)

type ObjectId uint32

type ObjectPool struct {
	lock       *sync.RWMutex
	pool       map[ObjectId]interface{}
	sequenceId ObjectId
}

func NewObjectPool() *ObjectPool {
	return &ObjectPool{
		lock:       new(sync.RWMutex),
		pool:       make(map[ObjectId]interface{}),
		sequenceId: 0,
	}
}

func (this *ObjectPool) Put(object interface{}) ObjectId {
	this.lock.Lock()
	this.sequenceId += 1
	objectId := this.sequenceId
	this.pool[objectId] = object
	this.lock.Unlock()

	return objectId
}

func (this *ObjectPool) Get(objectId ObjectId) (interface{}, bool) {
	this.lock.RLock()
	object, ok := this.pool[objectId]
	this.lock.RUnlock()

	return object, ok
}

func (this *ObjectPool) Release(objectId ObjectId) (interface{}, bool) {
	this.lock.Lock()
	object, ok := this.pool[objectId]
	delete(this.pool, objectId)
	this.lock.Unlock()

	return object, ok
}

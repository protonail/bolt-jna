package main

import "testing"

func TestPut(t *testing.T) {
	objectPool := NewObjectPool()
	objectId := objectPool.Put(42)

	if objectId == 0 {
		t.Errorf("Expected objectId > 0, got %d", objectId)
	}

	poolLen := len(objectPool.pool)
	if poolLen != 1 {
		t.Errorf("Expected len(pool) == 1, got %d", poolLen)
	}

	object := objectPool.pool[objectId]
	if object != 42 {
		t.Errorf("Expect object == 42, got %d", object)
	}
}

func TestSeveralPut(t *testing.T) {
	objectPool := NewObjectPool()
	objectId1 := objectPool.Put(42)
	objectId2 := objectPool.Put(43)

	if objectId1 == objectId2 {
		t.Errorf("Expect objectId1 != objectId2, got objectId1 == %d and objectId2 == %d", objectId1, objectId2)
	}

	object1 := objectPool.pool[objectId1]
	if object1 != 42 {
		t.Errorf("Expect 42, got %d", object1)
	}

	object2 := objectPool.pool[objectId2]
	if object2 != 43 {
		t.Errorf("Expect 43, got %d", object2)
	}
}

func TestGet(t *testing.T) {
	objectPool := NewObjectPool()
	objectId := objectPool.Put(42)

	object, ok := objectPool.Get(objectId)
	if !ok {
		t.Errorf("Expect true, got %t", ok)
	}
	if object != 42 {
		t.Errorf("Expect 42, got %d", object)
	}
}

func TestSeveralGet(t *testing.T) {
	objectPool := NewObjectPool()
	objectId1 := objectPool.Put(42)
	objectId2 := objectPool.Put(43)

	object1, ok := objectPool.Get(objectId1)
	if !ok {
		t.Errorf("Expect true, got %t", ok)
	}
	if object1 != 42 {
		t.Errorf("Expect 42, got %d", object1)
	}

	object2, ok := objectPool.Get(objectId2)
	if !ok {
		t.Errorf("Expect true, got %t", ok)
	}
	if object2 != 43 {
		t.Errorf("Expect 43, got %d", object2)
	}
}

func TestGetByUnknownObjectId(t *testing.T) {
	objectPool := NewObjectPool()
	unknownObjectId := ObjectId(42)

	object, ok := objectPool.Get(unknownObjectId)
	if ok {
		t.Errorf("Expect false, got %t", ok)
	}
	if object != nil {
		t.Errorf("Expect nil, got %d", object)
	}
}

func TestRelease(t *testing.T) {
	objectPool := NewObjectPool()
	objectId := objectPool.Put(42)

	object, ok := objectPool.Release(objectId)
	if !ok {
		t.Errorf("Expect true, got %t", ok)
	}
	if object != 42 {
		t.Errorf("Expect 42, got %d", object)
	}

	poolLen := len(objectPool.pool)
	if poolLen != 0 {
		t.Errorf("Expected len(pool) == 0, got %d", poolLen)
	}
}

func TestSeveralRelease(t *testing.T) {
	objectPool := NewObjectPool()
	objectId := objectPool.Put(42)

	object1, ok := objectPool.Release(objectId)
	if !ok {
		t.Errorf("Expect true, got %t", ok)
	}
	if object1 != 42 {
		t.Errorf("Expect 42, got %d", object1)
	}

	object2, ok := objectPool.Release(objectId)
	if ok {
		t.Errorf("Expect false, got %t", ok)
	}
	if object2 != nil {
		t.Errorf("Expect nil, got %d", object2)
	}

	poolLen := len(objectPool.pool)
	if poolLen != 0 {
		t.Errorf("Expected len(pool) == 0, got %d", poolLen)
	}
}

func TestParallelPutGetRelease(t *testing.T) {
	//TODO: how to write parallel test?
}

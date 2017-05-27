#ifndef _BOLT_JNA_TYPES_H_
#define _BOLT_JNA_TYPES_H_

typedef char* String;
typedef char Bool;

typedef unsigned long long ObjectId;

typedef unsigned int FileMode;

extern void Free(void* ptr);

typedef struct {
	ObjectId objectId;
	String error;
} Result;

extern void Result_Free(Result result);

typedef struct {
	String error;
} Error;

extern void Error_Free(Error error);

typedef struct {
	void* key;
	int keyLength;
	void* value;
	int valueLength;
} KeyValue;

typedef struct {
	void* value;
	int valueLength;
} Value;

typedef struct {
    unsigned long long id;
    String error;
} Sequence;

extern void Sequence_Free(Sequence sequence);

typedef struct {
    int pageCount;
    int pageAlloc;

    int cursorCount;

    int nodeCount;
    int nodeDeref;

    int rebalance;
    long long rebalanceTime;

    int split;
    int spill;
    long long spillTime;

    int write;
    long long writeTime;
} TxStats;

typedef struct {
    int branchPageN;
    int branchOverflowN;
    int leafPageN;
    int leafOverflowN;

    int keyN;
    int depth;

    int branchAlloc;
    int branchInUse;
    int leafAlloc;
    int leafInUse;

    int bucketN;
    int inlineBucketN;
    int inlineBucketInUse;
} BucketStats;

typedef struct {
    int freePageN;
    int pendingPageN;
    int freeAlloc;
    int freelistInUse;

    int txN;
    int openTxN;
    TxStats txStats;
} Stats;

#endif // _BOLT_JNA_TYPES_H_

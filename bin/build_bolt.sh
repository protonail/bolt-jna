#!/bin/bash

set -e

export ROOT_HOME=$(cd `dirname "$0"` && cd .. && pwd)

export GOPATH=$ROOT_HOME/vendor

export BOLT_HOME=$GOPATH/src/github.com/boltdb/bolt

if [[ "$GOROOT" == "" ]]; then
  GO=go
else
  GO=$GOROOT/bin/go
fi

if [[ "$1" == "clean" ]]; then
  echo --------------------
  echo Clean
  echo --------------------

  rm -rf $GOPATH/pkg
fi

echo --------------------
echo Build Bolt
echo --------------------

cd $GOPATH

if [[ "$OSTYPE" == "darwin"* ]]; then
  BOLT_FILE=libbolt.dylib
  BOLT_ARCH=darwin
  OUTPUT_LEVELDB_FILE=

  $GO build -o pkg/protonail.com/bolt-jna/$BOLT_FILE -buildmode=c-shared protonail.com/bolt-jna
elif [[ "$OSTYPE" == "linux"* ]]; then
  BOLT_FILE=libbolt.so
  if [[ $(uname -m) == "x86_64" ]]; then
    BOLT_ARCH=linux-x86-64
  else
    BOLT_ARCH=linux-x86
  fi
  OUTPUT_LEVELDB_FILE=

  $GO build -o pkg/protonail.com/bolt-jna/$BOLT_FILE -buildmode=c-shared protonail.com/bolt-jna
elif [[ "$OSTYPE" == "msys" ]]; then
  BOLT_FILE=bolt.dll
  if [[ "$MSYSTEM" == "MINGW64" ]]; then
    BOLT_ARCH=win32-x86-64
  else
    BOLT_ARCH=win32-x86
  fi
  OUTPUT_LEVELDB_FILE=bolt.dll

  PKG_DIR=$GOPATH/pkg/protonail.com/bolt-jna
  SRC_DIR=$GOPATH/src/protonail.com/bolt-jna

  $GO build -o $PKG_DIR/bolt-jna.a -buildmode=c-archive protonail.com/bolt-jna
  gcc -shared -pthread -static-libgcc -o $PKG_DIR/$BOLT_FILE $ROOT_HOME/patches/WinDLL.c -I$PKG_DIR -I$SRC_DIR $PKG_DIR/bolt-jna.a -lWinMM -lntdll -lWS2_32
fi


echo --------------------
echo Copy Bolt library
echo --------------------

mkdir -p $ROOT_HOME/bolt-jna-native/src/main/resources/$BOLT_ARCH/
cp $GOPATH/pkg/protonail.com/bolt-jna/$BOLT_FILE $ROOT_HOME/bolt-jna-native/src/main/resources/$BOLT_ARCH/$BOLT_FILE

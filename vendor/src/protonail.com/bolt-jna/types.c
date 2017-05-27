#include <stdlib.h>
#include "types.h"

void Free(void* ptr) {
  free(ptr);
}

void Result_Free(Result result) {
  free(result.error);
}

void Error_Free(Error error) {
  free(error.error);
}

void Sequence_Free(Sequence sequence) {
  free(sequence.error);
}
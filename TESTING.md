# Testing

## How to run test?

```
mvn test
```

## How to run specific test only?

```
mvn test -Dtest=BoltTest#open_and_close -DfailIfNoTests=false
```

or if a shell interpret `#` as part of glob expression:

```
noglob mvn test -Dtest=BoltTest#open_and_close -DfailIfNoTests=false
```

More examples [here](http://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html).
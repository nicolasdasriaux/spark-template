## Compile the project

```
sbt clean compile
```

## Run tests

```
sbt test
```

## Package fat JAR

```
sbt assembly
```

Fat JAR will be created in `target/scala-2.11` and will be named something like `spark-template-assembly-1.0.jar`.

## Package deployable ZIP

```
sbt universal:packageBin
```

A ZIP file containing the fat JAR and other configuration files will be created in `target/universal` directory and will be named something like `spark-template-1.0.zip`.

## Publish deployable ZIP

```
sbt universal:publish
```

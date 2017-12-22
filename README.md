# akka-menus

A basic rest api to query restaurants

## Requirements

Scala v2.12.4
Sbt v0.13.16

## Installation

```bash
git clone https://github.com/mie00/menus-akka.git
cd menus-akka
```

update the configuration file [src/main/resources/application.json](./src/main/resources/application.json).

To store the data in the `assets/sample-restaurant-data.json` file in the database you can run `sbt "runMain com.mie00.restaurants.migration.Migration"`

```bash
sbt "runMain com.mie00.restaurants.QuickstartServer"
```

## Testing

Please make sure that you change the collection to an empty collection, you can do this in the configuration file.

```bash
sbt test
```

## License

[Apache License 2.0](./LICENSE) © 2017 Mohamed Elawadi

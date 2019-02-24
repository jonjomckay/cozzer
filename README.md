Cozzer
======

[![Build Status](https://travis-ci.org/jonjomckay/cozzer.svg?branch=master)](https://travis-ci.org/jonjomckay/cozzer)

Cozzer is one of those tools that you can use to store and analyze results from your build system, including:

* Test results
* Code coverage (soon)
* Custom metrics (soon)

## Running

To run Cozzer, you'll need a PostgreSQL database, something that can run a Docker image (unless you fancy building it
yourself, and not really much else.

The following environment variables need to exist:

* `DATABASE_URL`: A JDBC URL to your database, like `jdbc:postgresql://localhost:5433/cozzer`
* `DATABASE_USERNAME`: The username to access the given database
* `DATABASE_PASSWORD`: The password to access the given database

Then running is as simple as the following, which will start it on port 8080:

```bash
$ docker run -e "DATABASE_URL=jdbc:postgresql://localhost:5433/cozzer" \
             -e "DATABASE_USERNAME=cozzer" \
             -e "DATABASE_PASSWORD=password" \
             -p 8080:8080
             jonjomckay/cozzer
```

## License

This project is licensed under the MIT License.

## Inspiration

This project was inspired by SonarQube, Codecov and all those other tools that do some of the same things.

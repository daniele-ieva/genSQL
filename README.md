# genSQL: A generic database Command Line Tool

genSQL is a simple command line tool to access, fetch, and modify data in various types of database using SQL queries.

## Supported Databases

- mySQL
- postgreSQL
- mariaDB
- sqlite
- oracleSQL
- msSQL
## Usage

To use this program simply run:
```
java -jar genSQL.jar -p [password] -u [username] [your_database_url]
```

the url depends of the database provider, for example, a mysql database will look something like this: `"mysql://127.0.0.1/mydb?serverTimezone=UTC"`.

Do note the usage of quotation marks when using GET parameters, as most common shells, like zsh and bash, treat `?` it as a wildcard.

By default, the credentials are `root` and `password`, and no database is provided.

To print a guide simply use the `-h` or `--help` flag.

## How to Build

### With System Maven

If you have maven already installed, run:

```mvn package shade:shade```

the executable jar will be in the target directory and be called `genSQL-version.jar`

### With Maven Wrapper
If you do not have maven installed, you can use the following command (depending on your OS):

Windows: ```.\mvnw.cmd package shade:shade```

Others: ```./mvnw package shade:shade```

the executable jar will be in the target directory and be called `genSQL-version.jar`

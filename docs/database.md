# Database Access in QuizSystem
## Configuration
The database layer needs to know a few things to actually connect to the database. **Copy** (not move!)
`config.properties.example` to `config.properties`, then adjust the values:

- `DATABASE_HOST`: Database server name (without port.)
- `DATABASE_PORT`: Self explanatory.
- `DATABASE_SCHEMA`: The name of the database to connect to (this is what you specified in `CREATE DATABASE` on the
server.)
- `DATABASE_USER`: The user to connect to the database server as.
- `DATABASE_PASSWORD`: The password for the above user.

If you know how, or feel like learning, these can also be specified as Environment Variables. If specified, an
Environment Variable has priority over a config file. This can be handy if you need to temporarily override your config.

## Accessing The Database (JDBC/SQL)
If you like raw SQL, you can get a plain-old JDBC connection to use. This is documented in the official Java Platform
documentation, or many articles on the internet. To get a connection, call `Database.getConnection()` - but remember to
**close** the connection when you're done with it! Try-with-resources is worth looking up to aid in closing your
connections.

## Accessing the Database In Style (jOOQ)
If like me you really can't stand SQL in its textual glory, you can try out the jOOQ framework included in the project.
This provides a more friendly, Java-based way to generate and execute SQL queries. Documentation is available on the
jOOQ website, and is linked from the Javadocs in Database. To get a jOOQ DSL object (usually called `create` in the
docs), call `Database.getJooq()`. As far as I know, you do _not_ need to close jOOQ connections.

## BUGS!
Report any problems getting connection objects to the group chat, so I can fix it. The initial implementation was thrown
together before I had to go catch a train.

-- RT
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

## HELP! I need a MySQL Server to connect to! (MySQL on Docker)
Keep calm and install Docker for your platform. On Linux, get it from your package manager. On Windows 10 Pro, use the
new Docker for Windows installer, available from the Docker website. _If you run Windows 10 Home, or an earlier version
of Windows, you'll have to find another solution - Docker only runs on Win10 Pro._

Follow the install documentation (either from your Linux distro wiki, or the official Docker guidance for Windows).

Once installed, you can run `docker` from the command line. On Linux, run it in a root terminal (`sudo` is your friend).
On Windows, you can right-click the Docker tray icon and select "Open Kitematic" - once Kitematic is open, there's a
"Docker CLI" button in the bottom left. Click that to open a terminal you can run `docker` commands in.

To spawn your new container, you can simply run this command (as root or with sudo on Linux):
```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=banana -p 127.0.0.1:3306:3306 -d mysql:5
```

Once the container has been pulled and created, you can stop it with `docker stop mysql`, start it again with `docker
start mysql` and finally destroy it (when stopped) with `docker rm mysql`. Note that your container may not autostart
when your PC is booted, so you may have to start it manually after a reboot.

Now you can connect to your server with the host `localhost`, port 3306, user `root` and password `banana`. I suggest
using either IDEA's built in Database view or the standalone Datagrip from Jetbrains to log in and apply the
`quizsys.sql` file to your MySQL server. MySQL Workbench should work too - but I don't know how to do it in that.

This setup is also very secure, as MySQL is only ever exposed on _your_ machine, not to the outside world. And yes, you
can change the password to something other than `banana` - just change the `MYSQL_ROOT_PASSWORD` field in the `docker
run` command. But why would you? I'm off to get a banana...

## BUGS!
Report any problems getting connection objects to the group chat, so I can fix it. The initial implementation was thrown
together before I had to go catch a train.

-- RT
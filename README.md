VulnEx
======

Building and running
--------------------

* Requires Docker (https://www.docker.com/)
* In the `.env` file configure your the Eclipse Steady backend or set `DEMO_MODE=true`
* Run `docker-compose up`
* The to use the tool go to http://localhost:4001/vulnex

Demo mode
---------

The demo mode allows you to explore VulnEx with a demo dataset.

Components
----------

* client: The web-client of the VA tool, served by the server component
* db-connector: The database connector module for the db-importer and server
* db-importer: `db-importer.jar` creates the database used for the VA tool
* server: The server of the VA tool, serving the static content and REST API

Data
----

### Requirements

* Docker (`docker` and `docker-compose`)

### Setup **Eclipse Steady**

* Clone `https://github.com/eclipse/steady.git`
* See `https://eclipse.github.io/steady/admin/tutorials/docker/` for deployment information

### Execute

You can run the Eclipse Steady process as usual.

License
-------------

Author: Frederik Dennig

Released under a Apache License 2.0. See the [LICENSE](LICENSE) file for details.

Acknowledgement
-------------
This work was funded by the the European Union’s Horizon 2020 research and innovation programme under grant agreement No. 830892 - see [SPARTA](https://sparta.eu/).

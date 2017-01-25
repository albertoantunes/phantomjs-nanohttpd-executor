# phantomjs-nanohttpd-executor
Proof of concept of running phantomjs on docker swarm mode

#Setting up the docker-swarm environment (Docker-Engine 1.13)
1) Build the docker image in the docker directory, making sure the phantomjs binary is executable (chmod +x phantomjs).
The built image is also available in [DockerHub](https://hub.docker.com/r/albertomartins/phantomjs-nanohttpd-executor/).

2) Setup a swarm in docker [swarm-mode](https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/) and [add nodes](https://docs.docker.com/engine/swarm/swarm-tutorial/add-nodes/) to it

3) For using the compose file, in docker-engine 1.13 it is necessary to enable experimental features. This can be done by either specifying the --experimental flag when starting docker engine or by editing the /etc/docker/daemon.json and adding the following:
```
{
    "experimental": true
}
```

4) Run the following command to spin up the service in docker swarm:
```
docker deploy --compose-file docker-compose.yml my_cluster
```
Where my_cluster is the name you want to give to the swarm

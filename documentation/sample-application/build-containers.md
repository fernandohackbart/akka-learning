# Biosphere base container on Docker in the openSuSE	

Install Docker on openSuSE
```
zypper install docker
```

Enable and start the Docker service
```
systemctl enable docker
systemctl start docker
systemctl status docker
```

Stop Docker service
```
systemctl stop docker
```

Fix root only access to docker command 
```
cat > /etc/sudoers.d/docker <<EOF
fernando ALL=(ALL) NOPASSWD:/usr/bin/docker
EOF
```

Create alias file
```
cat > ~/.alias <<EOF
alias docker='sudo /usr/bin/docker'
EOF
```

Run a test container 
```
docker run --name hello-test hello-world
docker stop hello-test
docker rm hello-test
docker rmi -f  docker.io/hello-world
```

# Prepare docker container (BASE IMAGE)
 
* https://hub.docker.com/_/centos/
* https://store.docker.com/images/centos
* https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/#entrypoint
* https://docs.docker.com/engine/reference/builder/

Create the Docker file
* Installs Java
* Create biosphere group and user
* Create /u01 for application installation (and grant access to biosphere user and group)
```
export BIOSPHERE_DOCKER_VERSION=base
export BIOSPHERE_DOCKER=/opt/docker/Biosphere/docker-images/Biosphere/dockerfiles/${BIOSPHERE_DOCKER_VERSION}

mkdir -p ${BIOSPHERE_DOCKER}

cat > ${BIOSPHERE_DOCKER}/Dockerfile <<EOF
FROM centos:7
MAINTAINER Fernando Hackbart<fhackbart@gmail.com>
ENV JAVA_HOME /usr/lib/jvm/java
RUN yum install -y java-1.8.0-openjdk.x86_64 java-1.8.0-openjdk-devel.x86_64 && \
    yum clean all && \
    groupadd biosphere && \
    useradd -d /home/biosphere -g biosphere -s /bin/bash biosphere && \ 
    echo biosphere:biosphere | chpasswd && \
    mkdir -p /u01 && \
    chmod a+xr /u01 && \ 
    chown biosphere:biosphere -R /u01    
EOF
```

Build it
```
docker build -f ${BIOSPHERE_DOCKER}/Dockerfile -t biosphere/biosphere:${BIOSPHERE_DOCKER_VERSION} ${BIOSPHERE_DOCKER}
```

Run it with a terminal to check how the container is
```
docker run -t -i biosphere/biosphere:${BIOSPHERE_DOCKER_VERSION} /bin/bash
```

# Check if the container is running 

```
docker info
docker ps
docker ps -a
```

# Push the container to the Docker store/hub

* http://store.docker.com
* http://hub.docker.com

```
docker login
docker push biosphere/biosphere:base
```

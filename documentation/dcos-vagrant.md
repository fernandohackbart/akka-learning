# Running the akka-learning application on DC/OS (Vagrant)

## Windows 10
Using Windows 10 with Cygwin (https://www.cygwin.com)

## CentOS 7
Using CentOS 7 running on VirtualBox 

## Vagrant
* https://www.vagrantup.com/docs/virtualbox/boxes.html
* https://blog.engineyard.com/2014/building-a-vagrant-box

```
mkdir -p /cygdrive/c/Biosphere/config
cd /cygdrive/c/Biosphere/config
vagrant plugin install vagrant-hostmanager
git clone https://github.com/dcos/dcos-vagrant
cd dcos-vagrant
cp VagrantConfig-1m-1a-1p.yaml VagrantConfig.yaml
vagrant up
```

To maintain the vagrant environment (shutting down the Desktop to go home for example)
```
cd /cygdrive/c/Biosphere/config/dcos-vagrant
vagrant suspend

cd /cygdrive/c/Biosphere/config/dcos-vagrant
vagrant resume
```

If the server gets too messy
```
cd /cygdrive/c/Biosphere/config/dcos-vagrant
vagrant destroy -f a1 m1 p1 boot 
```

## DC/OS Client OpenID token

Consoles URLS:
* http://m1.dcos
* http://m1.dcos/marathon
* http://m1.dcos/mesos


## DC/OS Client Windows
* https://github.com/dcos/dcos-vagrant/blob/master/README.md
	
Fix the /etc/hosts
```
192.168.65.111  a1.dcos
192.168.65.50   boot.dcos
192.168.65.90   m1.dcos
192.168.65.60   p1.dcos
192.168.65.60   spring.acme.org oinker.acme.org
```
Get the client and login to DC/OS
```
mkdir -p /cygdrive/c/Biosphere/config/dcos-client
cd /cygdrive/c/Biosphere/config/dcos-client
curl https://downloads.dcos.io/binaries/cli/windows/x86-64/dcos-1.9/dcos.exe -o dcos.exe
chmod +x /cygdrive/c/Biosphere/config/dcos-client/dcos.exe
export PATH=$PATH:/cygdrive/c/Biosphere/config/dcos-client

dcos config set core.dcos_url http://m1.dcos
dcos auth login
dcos
```

## DC/OS Client Centos 7 

Fix the /etc/hosts
 ```
192.168.65.111  a1.dcos
192.168.65.50   boot.dcos
192.168.65.90   m1.dcos
192.168.65.60   p1.dcos
192.168.65.60   spring.acme.org oinker.acme.org
```
Get the client and login to DC/OS
```	
curl https://downloads.dcos.io/binaries/cli/linux/x86-64/dcos-1.9/dcos -o dcos
mv dcos /usr/local/bin
chmod +x /usr/local/bin/dcos

dcos config set core.dcos_url http://m1.dcos
dcos auth login
dcos	
```

## Docker app to the DC/OS instance
* https://dcos.io/docs/1.7/usage/tutorials/docker-app/

```
export BIOSPHERE_DCOS=/cygdrive/c/Biosphere/config/dcos-service
export PATH=$PATH:/cygdrive/c/Biosphere/config/dcos-client

mkdir -p ${BIOSPHERE_DCOS}
```

```
cat > ${BIOSPHERE_DCOS}/akka-learning-0.0.1.json <<EOF
{
    "id": "akka-learning-0.0.1",
    "container": {
    "type": "DOCKER",
    "docker": {
          "image": "biosphere/akka-learning:0.0.1",
          "network": "BRIDGE",
          "portMappings": [
            { "hostPort": 9000, "containerPort": 9000, "protocol": "tcp"}
          ]
        }
    },
    "acceptedResourceRoles": ["slave_public"],
    "instances": 1,
    "cpus": 0.1,
    "mem": 60
}
EOF
```

```
cd /cygdrive/c/Biosphere/config/dcos-service

cat ${BIOSPHERE_DCOS}/akka-learning-0.0.1.json
dcos marathon app add akka-learning-0.0.1.json
dcos marathon app list
dcos marathon app stop /akka-learning-0.0.1
dcos marathon app remove /akka-learning-0.0.1
dcos task
```

## Test the akka-learning application
```
curl -XGET  http://192.168.65.60:9000/product
curl -XGET  http://192.168.65.60:9000/status
curl -XPOST -H "Content-Type:application/json" -d '{"brand":"ACME","name":"RoadRunner"}' http://92.168.65.60:9000/product
curl -XPOST -H "Content-Type:application/json" -d '{"messageBody":"Greetings!"}' http://92.168.65.60:9000/product
```

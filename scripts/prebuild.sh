#!/bin/bash
sudo yum update -y
sudo amazon-linux-extras install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo docker rm -f $(sudo docker ps -aq)
docker rmi $(docker images)
echo 'Done with prebuild'
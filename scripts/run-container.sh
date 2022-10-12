#!/bin/bash

CONTAINER_NAME="application"

docker container stop $CONTAINER_NAME
docker container rm $CONTAINER_NAME
docker run --name $CONTAINER_NAME -p 8085:8085 -d $IMAGE_NAME

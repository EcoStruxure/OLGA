#!/bin/bash

DOCKER_IMAGE_NAME=${OLGA_DOCKER_TAG:-ecostruxure/olga:latest}

cat << EOF

Using $DOCKER_IMAGE_NAME as the Docker tag

EOF

if [[ "$(docker images -q ${DOCKER_IMAGE_NAME} 2> /dev/null)" != "" ]]; then
  docker run -ti -p9090:9090 ${DOCKER_IMAGE_NAME}
fi

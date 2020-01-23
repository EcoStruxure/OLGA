#!/bin/bash

GIT_REPO_URL=${OLGA_REPO_URL:-https://github.com/EcoStruxure/OLGA.git}
GIT_BRANCH=${OLGA_GIT_BRANCH:-master}
PROJECT=${OLGA_PROJECT_NAME:-OLGA}
SUBPROJECTS=${OLGA_SUBPROJECTS:-OLGA-Core,OLGA-Ws}
MAVEN_ARTIFACT_ID=${OLGA_ARTIFACTID:-OLGA-Ws}
MAVEN_PROJECT_VERSION=${OLGA_VERSION:-0.0.4}
DOCKER_TAG=${OLGA_DOCKER_TAG:-ecostruxure/olga:latest}

cat << EOF

Using $GIT_REPO_URL as git repo URL
Using $GIT_BRANCH as git branch
Using $PROJECT as project
Using $SUBPROJECTS as subprojects
Using $MAVEN_ARTIFACT_ID as Maven artifact ID
Using $MAVEN_PROJECT_VERSION as version
Using $DOCKER_TAG as the Docker tag

EOF

docker build --build-arg url=${GIT_REPO_URL} \
  --build-arg branch=${GIT_BRANCH} \
  --build-arg project=${PROJECT} \
  --build-arg subprojects=${SUBPROJECTS} \
  --build-arg artifactid=${MAVEN_ARTIFACT_ID} \
  --build-arg version=${MAVEN_PROJECT_VERSION} \
  -t ${DOCKER_TAG} - < Dockerfile

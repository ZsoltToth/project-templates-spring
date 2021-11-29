#!/usr/bin/env bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

#echo $SCRIPT_DIR

DOCKER_RUN_ARGS=()
DOCKER_RUN_ARGS+=(--env MYSQL_ROOT_PASSWORD=secret)
DOCKER_RUN_ARGS+=(--env MYSQL_DATABASE=library)
DOCKER_RUN_ARGS+=(--name mysql)
DOCKER_RUN_ARGS+=(--publish 3306:3306)
DOCKER_RUN_ARGS+=(--rm)
DOCKER_RUN_ARGS+=(--volume $SCRIPT_DIR/src/main/resources/sql/create_database.sql:/docker-entrypoint-initdb.d/create_database.sql:ro)
# shellcheck disable=SC2068
#echo ${DOCKER_RUN_ARGS[@]}
docker run ${DOCKER_RUN_ARGS[@]} mysql:8

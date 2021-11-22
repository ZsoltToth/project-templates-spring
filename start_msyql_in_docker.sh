#!/usr/bin/env bash

DOCKER_RUN_ARGS=()
DOCKER_RUN_ARGS+=(--env MYSQL_ROOT_PASSWORD=secret)
DOCKER_RUN_ARGS+=(--env MYSQL_DATABASE=library)
DOCKER_RUN_ARGS+=(--name mysql)
DOCKER_RUN_ARGS+=(--publish 3306:3306)
DOCKER_RUN_ARGS+=(--rm)
# shellcheck disable=SC2068
docker run ${DOCKER_RUN_ARGS[@]} mysql:8

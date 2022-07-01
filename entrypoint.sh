#!/bin/bash -x

mvn -o -Dmaven.repo.local=./repository --batch-mode surefire:test ${DTEST} $@

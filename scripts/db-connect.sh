#!/bin/bash

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

function prop {
    grep "^${1}" ../src/main/resources/database.properties | cut -d '=' -f 2-
}

mongo $(prop 'mongodb.connectionString')

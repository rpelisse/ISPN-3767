#!/bin/bash
readonly NODE_ID=$(basename ${0} | sed -e 's;[A-Za-z\.];;g')
readonly JDG_REPO=/home/rpelisse/Products/jdg-610-tesco.git/lib

java -cp target/classes:target/dependency/*:${JDG_REPO}/* \
        -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1 \
         "org.infinispan.quickstart.clusteredcache.distribution.Node${NODE_ID}"

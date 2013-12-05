#!/bin/bash
readonly NODE_ID=$(basename ${0} | sed -e 's;[A-Za-z\.];;g')

java -cp target/classes:target/dependency/* \
        -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1 \
         "org.infinispan.quickstart.clusteredcache.distribution.Node${NODE_ID}"

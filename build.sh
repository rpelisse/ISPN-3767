#!/bin/bash
mvn clean compile dependency:copy-dependencies -DstripVersion
if [ ${?} -ne 0 ]; then
  cowsay "Failed !"
fi

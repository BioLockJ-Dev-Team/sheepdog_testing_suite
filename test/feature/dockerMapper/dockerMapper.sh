# # docker command
# docker run --rm -it -e SHEP=$SHEP \
# -v /var/run/docker.sock:/var/run/docker.sock \
# -v $SHEP:/shep \
# -v /Users/ieclabau/git/sheepdog_testing_suite/MockMain/pipelines:/mnt/efs/pipelines \
# biolockjdevteam/biolockj_controller /bin/bash

export CONFIG_DIR=$SHEP/test/bash/configFile

#
echo ""
echo "Use default"
echo "Expected: "
echo "[ DEBUG ] Using docker mapper class [biolockj.util.paths.CommonDockerMapper]"
echo "Result:   "
docker run --rm -e SHEP=$SHEP \
-v /var/run/docker.sock:/var/run/docker.sock \
-v $CONFIG_DIR:/configDir \
-v $SHEP/MockMain/pipelines:/mnt/efs/pipelines \
biolockjdevteam/biolockj_controller:v1.3.18 \
java -cp /app/biolockj/dist/BioLockJ.jar biolockj.BioLockJ \
-docker -projectDir /shep/MockMain/pipelines -precheck -systemOut \
-config $CONFIG_DIR/noneUnused.properties  2>&1 | grep "docker mapper class"

echo ""
echo "Use CommonDockerMapper"
echo "Expected: "
echo "[ DEBUG ] Using docker mapper class [biolockj.util.paths.CommonDockerMapper]"
echo "Result:   "
docker run --rm -e SHEP=$SHEP \
-v /var/run/docker.sock:/var/run/docker.sock \
-v $CONFIG_DIR:/configDir \
-v $SHEP/MockMain/pipelines:/mnt/efs/pipelines \
biolockjdevteam/biolockj_controller:v1.3.18 \
java -cp /app/biolockj/dist/BioLockJ.jar biolockj.BioLockJ \
-docker -projectDir /shep/MockMain/pipelines -precheck -systemOut \
-docker-mapper biolockj.util.paths.CommonDockerMapper \
-config $CONFIG_DIR/noneUnused.properties  2>&1 | grep "docker mapper class"

echo ""
echo "Use SimpleDockerMapper"
echo "Expected: "
echo "[ DEBUG ] Using docker mapper class [biolockj.util.paths.SimpleDockerMapper]"
echo "Result:   "
docker run --rm -e SHEP=$SHEP \
-v /var/run/docker.sock:/var/run/docker.sock \
-v $CONFIG_DIR:/configDir \
-v $SHEP/MockMain/pipelines:/mnt/efs/pipelines \
biolockjdevteam/biolockj_controller:v1.3.18 \
java -cp /app/biolockj/dist/BioLockJ.jar biolockj.BioLockJ \
-docker -projectDir /shep/MockMain/pipelines -precheck -systemOut \
-docker-mapper biolockj.util.paths.SimpleDockerMapper \
-config $CONFIG_DIR/noneUnused.properties  2>&1 | grep "docker mapper class"



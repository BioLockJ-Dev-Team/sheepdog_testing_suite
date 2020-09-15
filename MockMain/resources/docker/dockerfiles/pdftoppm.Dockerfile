# suggested build command:
# name=pdftoppm
# cd ${BLJ}
# docker build -t biolockjdevteam/${name} . -f resources/docker/dockerfiles/${name}.Dockerfile 

FROM ubuntu
ARG DEBIAN_FRONTEND=noninteractive

#1.) set shell to bash
SHELL ["/bin/bash", "-c"]

#2.) Copy script that has the BioLockJ assumptions
#COPY resources/docker/docker_build_scripts/imageForBioLockJ.sh /root/.

#3.) Build Standard Directories and varibles and assumed software
#RUN . /root/imageForBioLockJ.sh ~/.bashrc

RUN apt-get update && \
    apt-get install -y poppler-utils

#5.) Cleanup
RUN	rm -rf /tmp/* && rm -rf /usr/games && rm -rf /var/log/*

#6.) Set Default Command
#CMD /bin/bash $COMPUTE_SCRIPT

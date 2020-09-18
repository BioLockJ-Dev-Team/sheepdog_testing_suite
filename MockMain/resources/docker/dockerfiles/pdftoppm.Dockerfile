# suggested build command:
# name=pdftoppm
# cd ${SHEP/MockMain}
# docker build -t biolockjdevteam/${name} . -f resources/docker/dockerfiles/${name}.Dockerfile 

FROM ubuntu
ARG DEBIAN_FRONTEND=noninteractive

#1.) set shell to bash
SHELL ["/bin/bash", "-c"]

#2.) add poppler-utils, including pdftoppm
RUN apt-get update && \
    apt-get install -y poppler-utils

#3.) Cleanup
RUN	rm -rf /tmp/* && rm -rf /usr/games && rm -rf /var/log/*

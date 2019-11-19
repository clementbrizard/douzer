FROM maven:3-jdk-8

RUN wget https://github.com/frekele/oracle-java/releases/download/8u212-b10/jdk-8u212-linux-x64.tar.gz && \
    tar zxvf jdk-8u212-linux-x64.tar.gz && \
    rm jdk-8u212-linux-x64.tar.gz

ENV JAVA_HOME="/jdk1.8.0_212"



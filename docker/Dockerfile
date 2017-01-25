FROM openjdk

COPY PhantomExecutor.jar /jar/
COPY phantomjs /usr/bin/

RUN apt update \
    && apt install -y fontconfig

CMD ["java", "-jar", "/jar/PhantomExecutor.jar"]

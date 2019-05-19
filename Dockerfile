FROM hseeberger/scala-sbt

WORKDIR /scalablecomputing

COPY ./target/universal/scalablecomputing-1.0-SNAPSHOT.zip /scalablecomputing

RUN unzip scalablecomputing-1.0-SNAPSHOT.zip 

EXPOSE 9000

CMD ["./scalablecomputing-1.0-SNAPSHOT/bin/scalablecomputing"]


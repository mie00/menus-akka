FROM hseeberger/scala-sbt

ADD build.sbt /root/

RUN sbt update

ADD . /root/

CMD sbt run

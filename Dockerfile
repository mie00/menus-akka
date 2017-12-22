FROM hseeberger/scala-sbt

ADD build.sbt /root/

RUN sbt update

ADD . /root/

CMD sbt "runMain com.mie00.restaurants.QuickstartServer"

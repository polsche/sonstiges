FROM nexus2.bafw.iirzi.de:5050/tec32/rhel7-jre18:latest

LABEL maintainer="_BA-IT-Systemhaus-SEP52-InfoSysBuB <IT-Systemhaus.SEP52-InfoSysBuB@arbeitsagentur.de>"

RUN echo "kopiere studisu-server"
COPY target/studisu-server*.jar /app/
COPY container/launch.sh  /app/

# take config not from target but from pacify template
# COPY ../studisu-uebergabe/config-rest/infosysbub-studisu-server.properties /app/
# results in :          Step 6 : COPY ../studisu-uebergabe/config-rest/infosysbub-studisu-server.properties /app/
#                       Forbidden path outside the build context: ../studisu-uebergabe/config-rest/infosysbub-studisu-server.properties ()

# trying to copy via maven plugin to get it into docker build context
COPY target/classes/infosysbub-studisu-server.properties /app/

RUN chmod +x /app/launch.sh
RUN chmod a+r /app/infosysbub-studisu-server.properties

EXPOSE 8080
WORKDIR /app

ENV _JAVA_OPTIONS=-Xmx2g
ENV STUDISU_LOG4J_TYPE=container
ENV STUDISU_LOG4J_LOGLEVEL=info

# startet studisu backend server
# CMD java -jar /app/studisu-server*.jar
CMD ./launch.sh

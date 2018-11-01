FROM nexus2.bafw.iirzi.de:5050/tec32/rhel7-rh-nginx112:latest

LABEL maintainer="_BA-IT-Systemhaus-SEP52-InfoSysBuB <IT-Systemhaus.SEP52-InfoSysBuB@arbeitsagentur.de>"

RUN echo "kopiere studisu-ui und nginx-Konfiguration"
#COPY dist /srv/
COPY nginx.conf /srv/
COPY dist/ /usr/share/nginx/html/

COPY ./container/launch.sh  /srv/

RUN echo "setze Rechte"
# TODO Anzahl an RUN commands durch verknuepfung mehrerer shellcommands reduzieren && \
RUN chmod a+w /var/run
RUN chown -R nginx /srv
RUN chmod g+rwx /var/opt/rh/rh-nginx112/lib/nginx/tmp
RUN chmod +x /srv/launch.sh
RUN chmod -R a+r /usr/share/nginx/html
# Alle Verzeichnisse fuer alle lesbar machen
RUN chmod a+x $(find /usr/share/nginx/html -type d)

EXPOSE 8080
WORKDIR /srv

# startet nginx server
#CMD /opt/rh/rh-nginx110/root/usr/sbin/nginx -c /srv/nginx.conf
CMD ./launch.sh

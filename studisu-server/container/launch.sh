

echo "launch.sh begin"
env

### string ersetzung
# declare an array variable
# this is a whitelist of environment variables which will be replaced in files
# with their values set at container runtime
# they are injected via app.json env
declare -a arr=("studisu_ui_url"
    "studisu_cors_active"
    "logging_level_org_springframework_web"
    "logging_level_org_hibernate"
    "studisu_caching_http_static_content"
    "studisu_caching_http_studienangebote"
    "studisu_wcc_url"
    "studisu_wcc_endpoint"
    "studisu_wcc_service"
    "bildungsangebotservice_ws_service_location"
    "bildungsangebotservice_ws_wsdl_name"
    "bildungsangebotservice_ws_auth_type"
    "bildungsangebotservice_ws_policy_name"
    "dkzservice_ws_service_location"
    "dkzservice_ws_wsdl_name"
    "dkzservice_ws_auth_type"
    "dkzservice_ws_policy_name")

# now loop through the above array
for i in "${arr[@]}"
do
   # work on copied infosysbub-studisu-server.properties
   # ! to access parameter with the name
   # {{ as beginn token and }} as end token
   echo "replacing {{$i}} with " ${!i}
   sed -i "s|{{$i}}|${!i}|" infosysbub-studisu-server.properties
done

cat infosysbub-studisu-server.properties
# prepare move by making directory
mkdir BOOT-INF && mkdir BOOT-INF/classes

# put file to correct relative path, to update it in jar
mv infosysbub-studisu-server.properties BOOT-INF/classes/

# update jar to include substituted config
jar -uf studisu-server-1.0.0-SNAPSHOT.jar BOOT-INF/classes/infosysbub-studisu-server.properties
rm BOOT-INF -rf

# Entfernen der yum-Paketmanager-Konfiguration
rm /etc/yum.repos.d/ba.repo  

# Setzen der ProxyEinstellungen aus der app.json env
# Diese werden dann von diversen JavaComponenten per Konvention verwendet
# Fuer den Contentclient haben wir sie noch einmal explizit aktivieren muessen (wegen jersey)
# Java Networking and proxies: https://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html
java -Dhttp.proxyHost=$HTTP_PROXY_HOST -Dhttp.proxyPort=$HTTP_PROXY_PORT -jar studisu-server*.jar

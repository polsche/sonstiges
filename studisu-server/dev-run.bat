rem fuer fork=false muss logroot global fuer die maven-jvm gesetzt werden
rem bei fork=true, wird logroot ueber jvmarguments in der pom.xml gesetzt
set MAVEN_OPTS=%MAVEN_OPTS% -Dlogroot=log

rem fork=false fuer effizientes frontendentwicklung (backend wartet nicht auf verbinden eines remote debuggers)
rem fork=true  fuer debugging des backend (ueber port 5005)
mvn -P local-springboot -P install clean install spring-boot:repackage spring-boot:run -Dfork=false
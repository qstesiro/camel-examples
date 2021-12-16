alias exec='mvn exec:java -Dexec.mainClass=org.apache.camel.example.basic.CamelBasic'

alias jdbs='mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -agentlib:jdwp=transport=dt_socket,address=localhost:1025,server=y,suspend=y org.apache.camel.example.basic.CamelBasic"'
alias jdbc='jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=1025'


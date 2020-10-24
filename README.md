# READE

Netty client /server sample applications

## Environment
 
* JDK 8
* Maven 3.5.2


## How to build

`mvn clean install -DskipTests -Dhttps.protocols=TLSv1.2`


## How to Release 

use following command to tag the project on gitBD

    mvn release:prepare -Dmaven.test.skip=true -Darguments="-Dmaven.test.skip=true" -Dresume=false
    

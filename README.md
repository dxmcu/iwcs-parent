# iwcs


iwcs管理系统



#### 把socket依赖加到本地仓库
```shell
mvn install:install-file -Dfile=libs/socketio-1.0.0.jar -Dpackaging=jar -DpomFile=libs/pom.xml
```



#### 把license依赖加到本地仓库
```shell
mvn install:install-file -Dfile=libs/license-manage-1.0.0.jar -Dpackaging=jar -DpomFile=libs/license-manage-1.0.0.pom
```
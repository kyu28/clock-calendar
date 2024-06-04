
## 背景

## 依赖
### 项目外部依赖
必需：  
+ [Maven](https://maven.apache.org/download.cgi)  
+ [webview/webview_java](https://github.com/webview/webview_java)  

### 额外软件包依赖
Windows:  
无  
Linux:
```
# Debian
apt install libwebkit2gtk-4.0-37
```
## 构建项目
```
mvn clean package
```

## 运行项目
```
java -jar target/clockcalendar-版本号-jar-with-dependencies.jar
```

在以下环境进行过测试  
+ Windows 11 Professional 22H2 AMD64  
Oracle Java JDK 17.0.11  
Maven 3.9.7  
+ Debian GNU/Linux 12 (bookworm) WSL2 on Windows 11 Professional 22H2 AMD64  
WSL 2.1.5.0  
WSLg 1.0.60  
OpenJDK 17.0.11  
Maven 3.8.7  
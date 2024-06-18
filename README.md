# 时钟⽇历
## 背景
2024春 面向对象高级编程 期末大作业
1. ⽀持显⽰时钟⽇历功能  
⽀持显⽰时钟和⽇历功能，允许⼿动修改时钟和⽇历显⽰的时间，以及通过连接时间服务器同步到不同时区时钟的操作。
2. 待办项功能  
⽀持在⽇历中添加待办项，允许⽤⼾在⽇历中的某⼀天中插⼊⼀定数量的待办项，⽽后⽤⼾可以勾选这些待办项是否完成，以及增删待办项。
3. 假期与特殊⽇期功能  
除周六⽇外，各国都有相应的法定假⽇，如我国的劳动节与国庆节。⽽且每⼈的也有各⾃的具有纪念意义的某⼀天。要求⽀持导⼊假⽇与特殊⽇期，这些与周六⽇在⽇历中⾼亮显⽰。由于⻓假⼀般伴随着调休，要求⽀持取消掉特定周六⽇的⾼亮功能。
4. 计时与番茄钟功能  
要求时钟附加倒计时功能，⽤⼾输⼊⼀个指定的时间，在确认倒计时后开始计时，到指定时间后提醒⽤⼾。 
> 番茄⼯作法简介  
番茄⼯作法是简单易⾏的时间管理⽅法。使⽤番茄⼯作法，选择⼀个待完成的任务，将番茄时间设为
25分钟，专注⼯作，中途不允许做任何与该任务⽆关的事，直到番茄时钟响起，然后进⾏短暂休息
⼀下（5分钟），然后再开始下⼀个番茄。每4个番茄时段多休息⼀会⼉（25分钟）。
要求按照如上的介绍，完成时钟的番茄钟功能。并且，每完成⼀个25分钟的番茄时间，就记录⼀个番
茄数，统计每⽇的番茄数并显⽰在⽇历中。
5. 习惯打卡功能  
为了帮助⽤⼾养成良好的习惯，应⽤应⽀持习惯打卡功能。⽤⼾可以在应⽤中定义和管理⾃⼰的习惯
列表，并设定每个习惯的打卡周期（如每⽇、每周或每⽉）。每个习惯都可以设置提醒，以便在指定
的时间提醒⽤⼾进⾏打卡。
+ 习惯管理：允许⽤⼾添加、编辑和删除⾃定义习惯。每个习惯可以设置名称、描述、打卡周期以及提醒时间。
+ 打卡记录：⽤⼾可以在习惯对应的⽇历上进⾏打卡操作，标记完成习惯。系统应提供视图显⽰⽤⼾的打卡历史，包括连续打卡天数和打卡遗漏的记录。
+ 提醒功能：应⽤应能根据⽤⼾设置的提醒时间⾃动提醒⽤⼾打卡。
+ 习惯分析：提供统计分析功能，帮助⽤⼾了解⾃⼰在各个习惯上的表现，如打卡完成率、最⻓连续打卡记录等。

使用的至少 4 种编程技术：
+ 泛型
+ 枚举
+ 注解
+ 重载
+ 反射
+ Lambda
+ Stream
  
以及至少 2 种设计模式：
+ 静态工厂模式
+ 单例模式

## 依赖
### 项目外部依赖
必需：  
+ [Maven](https://maven.apache.org/download.cgi)  
+ [webview/webview_java](https://github.com/webview/webview_java)  
+ [alibaba/fastjson2](https://github.com/alibaba/fastjson2)
+ [Apache Commons Net](https://commons.apache.org/proper/commons-net/)

### 额外软件包依赖
Windows:  
无  
Linux:
## 构建项目
### Debug构建
`config/DataConfig.java`中的配置为Release配置，可能出现配置文件载入写入失败问题  
```sh
mvn clean package
```

### Release构建
```sh
mvn clean package
mkdir build
cp target/clockcalendar-*-jar-with-dependencies.jar build/clockcalendar.jar
mkdir build/data
cp data/* build/data
```

## 运行项目
### Debug构建
`config/DataConfig.java`中的配置为Release配置，可能出现配置文件载入写入失败问题  
```sh
java -jar target/clockcalendar-版本号-jar-with-dependencies.jar
```

### Release构建
```sh
cd build
java -jar clockcalendar.jar
```

### 构建程序文件结构
```
build/
├── data/               # 数据文件夹（路径可在config/DataConfig.java更改）
│   ├── calendar.json   # 自定义日历文件
│   ├── habits.json     # 习惯数据
│   ├── pomodoro.json   # 番茄钟数据
│   └── todo.json       # 待办数据
└── clockcalendar.jar   # 程序本体
```

在以下环境进行过测试  
+ Windows 11 Professional 22H2 AMD64  
Oracle Java JDK 17.0.11  
Maven 3.9.7  
+ Windows 11 Professional 23H2 AMD64  
Oracle Java JDK 19.0.2  
Maven 3.9.5  

## 项目结构
```
docs/                               # 文档，包含选题PDF原件
data/                               # 程序的数据实例文件
src/
└── main/
    ├── java/
    │   └── szu/
    │       └── dky/
    │           └── clockcalendar/
    │               ├── config/     # 配置类
    │               ├── service/    # 所有服务组件
    │               │   ├── countdown/  # 倒计时与番茄钟模块
    │               │   ├── datetime/   # 时钟日历模块
    │               │   ├── habit/      # 习惯打卡模块
    │               │   ├── router/     # 页面路由模块
    │               │   └── todo/       # 待办模块
    │               ├── util/       # 工具类
    │               └── view/       # 视图类
    └── resources/                  # UI的HTML实现文件
pom.xml                             # Maven构建文件
README.md                           # 本自述文件
```
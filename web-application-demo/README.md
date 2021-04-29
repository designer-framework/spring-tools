# web-application-demo

> 1. 将test-app-jar执行maven install命令打包
> 2. 将test-app-jar项目的jar包在所有./web-start/web/**/jar 目录下放至一份
> 3. 启动web-start项目
> 4. 访问8080端口 +  [./web-start/web/ 目录下的文件夹名字] + '/getClassloader' 即可访问内存隔离的两个项目
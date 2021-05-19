# web-application-demo

> 1. 将test-app-jar执行maven install命令打包
> 2. 将test-app-jar项目的jar包在所有./web-start/web/**/jar 目录下放至一份
> 3. 启动web-start项目
> 4. 访问8080端口 +  [./web-start/web/ 目录下的文件夹名字] + '/getClassloader' 即可访问内存隔离的两个项目
> 5. 完成了一键运行监听在不同端口下的普通Http项目
> 5. 通过构想, 通过类加载器的隔离性, 欲实现一键运行多SpringBoot应用, 不局限于普通Http项目
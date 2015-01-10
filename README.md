ANPRCloud
=========
这个WEBAPP使用了Struts Spring Hibernate集成，实现了ANPR（自动车牌识别）的 RESTful web service ，用来作为软件实训1的大作业。
特性
----------------
*  前端使用dropzone.js 支持批量拖拽文件、文件夹
*  前端使用Foundation框架支持响应式设计
*  使用Struts 的JSON 插件实现了RESTful web service的接口
*  通过Spring调用Opencv的库实现了大部分图像的处理
*  通过Hibernate调用MySql储存
*  图片Base64encode后储存到数据库中

# 部署注意事项

## 数据迁移

* 全库迁移，在 /usr/local/mongo/bin 下运行 mongodump -u -p 会生成 ./dump 目录
* docker cp 将 ./dump 下的文件复制到 container 中。或者放在映射目录下
* docker exec -it container_id sh 进入容器
* docker ./dump 同级目录下运行 mongorestore -u -p 

## 注意事项
* 服务运行后，不要运行 docker-compose down，会删除容器，数据如果未映射至磁盘会导致丢失
* 
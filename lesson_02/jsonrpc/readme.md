HOW TO compile & run  
(java 1.8)

Собрать:  
cd lesson_02/jsonrpc/  
mvn -DskipTests clean package  

Запустить Сервер:  
cd lesson_02/jsonrpc/jsonrpc-server/target/  
java -jar server-0.1.jar  

Запустить демо-клиент:  
cd lesson_02/jsonrpc/zclient/target  
java -jar client-0.1.jar  
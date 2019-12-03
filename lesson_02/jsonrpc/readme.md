HOW TO compile & run  
(java 1.8)

Build:  
cd lesson_02/jsonrpc/  
mvn -DskipTests clean package 

configure postgres: 
manual create 
user shop/shoppassword 
database shop 
schema shop 

Launch server:  
cd lesson_02/jsonrpc/jsonrpc-server/target/  
java -jar server-0.1.jar  

Launch client:  
cd lesson_02/jsonrpc/zclient/target  
java -jar client-0.1.jar  

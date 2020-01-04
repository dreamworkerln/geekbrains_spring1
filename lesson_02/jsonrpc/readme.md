HOW TO compile & run  
(java 1.8)

Build:  
cd lesson_02/jsonrpc/  
mvn -DskipTests clean package 

configure postgres database: 
manual create 
user shop/shoppassword 
database shop 
schema shop
schema oauzz
 

Launch auth-server:  
cd lesson_02/jsonrpc/auth-server/target  
java -jar auth-server-0.1.jar

Launch resource-server:  
cd lesson_02/jsonrpc/resource-server/target 
java -jar resource-server-0.1.jar  

Launch client:  
cd lesson_02/jsonrpc/zclient/target  
java -jar client-0.1.jar



// =====================================================  

Principles

No clientId/client_secret. 
   

1. Go on auth server 
 
curl -u user:password -X POST /oauzz/token

will return only 1 json refresh_token that rotten in 30 min.
      
  
       
2. Then from other confidential device - mobile app with fixed sim phone number, known to auth-server 
go on auth-server (in demo client will be simulated)

curl -u user:password -X POST admin/token/approve --data id=YOU_REFRESH_TOKEN_FROM_STEP_1.id  
 
this will approve previously obtained refresh_token

  
      
3. Go on auth server  

curl -X POST /oauzz/token -H "Authorization: Bearer YOU_REFRESH_TOKEN_FROM_STEP_1"

will get normal 2 tokens
  
access_token  
refresh_token   
  
  
     
4. Go to resource server

curl -X POST /api/... -H "Authorization: Bearer YOU_REFRESH_TOKEN_FROM_STEP_3"   
   

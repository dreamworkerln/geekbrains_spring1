JWT with handmade OAUTH v0.1

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
 
curl -u user:password -X POST /oauzz/token/get
  
will return only 1 json refresh_token that rotten in 30 min.
      
  
       
2. Then from other confidential client approve you refresh_token.  
(For example mobile app with validated sim phone number.  
Phone number is validated by sms-gateway service check.   
Not implemented, demo client is trusted client to server.)  
  
curl -u user:password -X POST /oauzz/token/approve --data id=YOU_REFRESH_TOKEN_FROM_STEP_1.id  
  
May use Bearer access_token instead BasicAuth if already have one(from another (confidential) device)  
This will approve previously obtained refresh_token.  
  
  
  
3. Go on auth server and refresh you refresh_token  
  
curl -X POST /oauzz/token -H "Authorization: Bearer YOU_REFRESH_TOKEN_FROM_STEP_1"  
  
will get normal 2 tokens  
  
access_token  
refresh_token  
  
  
     
4. Go to resource server
  
curl -X POST /api/... -H "Authorization: Bearer YOU_ACCESS_TOKEN_FROM_STEP_3" 
  
  
// ------------------------------------------------------------------------------------  
    
Possible improvements:
Provide blacklist token controller/service on auth-server that allows resource-server
obtain revoked tokens informations.
  
Just api:

curl /oauzz/token/revoked_from --data id=LAST_KNOWN_TO_RESOURCE_SERVER_REVOKED_TOKEN_ID  
  
that return all revoked access_tokens from known to resource server id and to current moment.  
(+ supply push notification from auth-server or use message-broker like rabbit-mq, etc)  
  

This project is a sample of a client application with OAuth2 single
sign on using Spring Cloud (in Java - there is a Groovy sample in the
"demo" directory at https://github.com/spring-cloud-samples/scripts).

You can run the app locally with the "authserver" app from https://github.com/spring-cloud-samples/ (running on localhost:8080/uaa), or against Github as an auth server by using the "github" profile (run it with `-Dspring.profiles.active=github`).

It should also work out of the box on Cloud Foundry if you create a user provided service called "sso" pointing to the UAA, e.g.

```
$ cf create-user-provided-service sso -p '{"userInfoUri":"https://uaa.run.pivotal.io/userinfo", "tokenUri":"https://login.run.pivotal.io/oauth/token", "authorizationUri":"https://login.run.pivotal.io/oauth/authorize", "clientId":"[acme]", "clientSecret":"[secret]"}'
```

where `[client]` and `[secret]` are the credentials of a registered client that is 

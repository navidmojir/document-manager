# document-manager
 
 A simple document manager service for uploading, downloading and searching files through restful web services. It can be useful in a microservice environment where document management is needed.
 
 Developed by java 11 and spring boot with embedded web server and embedded h2 database. So the output jar is self-contained and you just need to run it and everything like web server and database will be run automatically. Database file will be stored with "db" name in the current directory. It can be changed is application.properties file.
 
 Uploaded files are stored in the current directory in the "files" folder. This can be changed in application.properties file by setting "workingDirectory" parameter.
 
 To run the service.
 ```
 java -jar document-manager-0.0.1.jar
 ```


# Web Methods
In the following section web services are introduced. Sample **postman** files are also available at src/test/resources directory.

## upload
```
POST http://localhost:8080/upload
```

There are two services for uploading.

1- multipart/form-data
 This way file can be uploaded as multipart. key for form-data should be "file".

2- application/octet-stream
 This way file can be uploaded in binary format. Content-type headed should be set to application/octet-stream.

## download
```
GET http://localhost:8080/download/{document_id}
```

By calling the get method file can be downloaded.

## search
```
POST http://localhost:8080/search
```

There is a search method for searching files with file name including pagination and sort features.

Sample json for search:
```
{
	"filters": {
		"fileName": "test"
	},
	"paging": {
		"pageNumber": 0,
		"pageSize": 3
	},
	"sorting": {
		"sortField": "fileName",
		"ascending": true
	}
}
```

# Project Dependencies
This project is dependent on another of my projects called "my-spring-boot-commons". 
You can clone it from:
```
https://github.com/nmojir/my-spring-boot-commons.git
````

Or  download compiled jar file from:
```
https://github.com/nmojir/my-spring-boot-commons/releases/download/v0.0.1/my-spring-boot-commons-0.0.1.jar
```
and then install it on maven local repo with the following command:
```
mvn install:install-file -Dfile=my-spring-boot-commons-0.0.1.jar -DgroupId=ir.mojir -DartifactId=my-spring-boot-commons -Dversion=0.0.1 -Dpackaging=jar -DgeneratePom=true
```

# Authentication and authorization using keycloak
The application is integrated with keycloak authorization server to support authentication and authorization. This feature can be enabled by setting the following option to true in application.properties file. Other parameters must also be configured correctly using keycloak project documentation.

```
keycloak.enabled = true
```

Thanks for being here and any comments are appreciated.

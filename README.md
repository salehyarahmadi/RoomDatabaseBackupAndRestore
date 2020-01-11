Room Database Backup And Restore ![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat) [![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
===================
If you are using from room database, you can use from this library for full backup and restore. Also you can encrypt output file with your desired secret key.


**This library is also available at JitPack.io**

[![](https://jitpack.io/v/salehyarahmadi/RoomDatabaseBackupAndRestore.svg)](https://jitpack.io/#salehyarahmadi/RoomDatabaseBackupAndRestore)


`this library is compatible to androidx`



## Setup
The simplest way to use this library is to add the library as dependency to your build.

## Gradle

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.salehyarahmadi:RoomDatabaseBackupAndRestore:v1.0.1'
	}

## Usage

### Backup


    new Backup.Init()
                .database(database)
                .path("path-to-save-backup-file")
                .fileName("filename.txt")
                .secretKey("your-secret-key") //optional
                .onWorkFinishListener(new OnWorkFinishListener() {
                    @Override
                    public void onFinished(boolean success, String message) {
                        // do anything
                    }
                })
                .execute();

### Restore


    new Restore.Init()
                .database(database)
                .backupFilePath("path-to-backup-file/filename.txt")
                .secretKey("your-secret-key") // if your backup file is encrypted, this parameter is required
                .onWorkFinishListener(new OnWorkFinishListener() {
                    @Override
                    public void onFinished(boolean success, String message) {
                        // do anything
                    }
                })
                .execute();


   


        
 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2019 RoomDatabaseBackupAndRestore

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
       

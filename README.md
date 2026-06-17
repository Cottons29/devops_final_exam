```
.
├── bin
│   ├── default
│   ├── generated-sources
│   │   └── annotations
│   ├── generated-test-sources
│   │   └── annotations
│   ├── main
│   │   ├── application.properties
│   │   ├── live
│   │   │   └── cottons
│   │   │       └── lypheng
│   │   │           ├── controller
│   │   │           │   ├── ProfileController.class
│   │   │           │   └── TemplateController.class
│   │   │           ├── dto
│   │   │           │   ├── BatchGenerateRequest.class
│   │   │           │   ├── ProfileCreateRequest.class
│   │   │           │   ├── ProfileResponse.class
│   │   │           │   └── ProfileUpdateRequest.class
│   │   │           ├── exception
│   │   │           │   └── GlobalExceptionHandler.class
│   │   │           ├── LyphengApplication.class
│   │   │           ├── model
│   │   │           │   ├── BarcodeType.class
│   │   │           │   ├── Profile.class
│   │   │           │   ├── Profile$ProfileBuilder.class
│   │   │           │   ├── ProfileType.class
│   │   │           │   ├── Template.class
│   │   │           │   └── Template$TemplateBuilder.class
│   │   │           ├── repository
│   │   │           │   ├── ProfileRepository.class
│   │   │           │   └── TemplateRepository.class
│   │   │           └── service
│   │   │               ├── BarcodeService.class
│   │   │               ├── IdCardPdfService.class
│   │   │               ├── IdCardPreviewService.class
│   │   │               ├── PhotoStorageService.class
│   │   │               ├── ProfileService.class
│   │   │               ├── QrCodeService.class
│   │   │               ├── RegistrationNumberService.class
│   │   │               └── TemplateService.class
│   │   └── templates
│   │       └── idcard-preview.html
│   └── test
│       ├── application-test.properties
│       └── live
│           └── cottons
│               └── lypheng
│                   ├── controller
│                   │   └── ProfileControllerTest.class
│                   ├── LyphengApplicationTests.class
│                   └── service
│                       ├── BarcodeServiceTest.class
│                       ├── ProfileServiceTest.class
│                       ├── QrCodeServiceTest.class
│                       ├── RegistrationNumberServiceTest.class
│                       └── TemplateServiceTest.class
├── build
│   ├── classes
│   │   └── java
│   │       ├── main
│   │       │   └── live
│   │       │       └── cottons
│   │       │           └── lypheng
│   │       │               ├── controller
│   │       │               │   ├── ProfileController.class
│   │       │               │   └── TemplateController.class
│   │       │               ├── dto
│   │       │               │   ├── BatchGenerateRequest.class
│   │       │               │   ├── ProfileCreateRequest.class
│   │       │               │   ├── ProfileResponse.class
│   │       │               │   └── ProfileUpdateRequest.class
│   │       │               ├── exception
│   │       │               │   └── GlobalExceptionHandler.class
│   │       │               ├── LyphengApplication.class
│   │       │               ├── model
│   │       │               │   ├── BarcodeType.class
│   │       │               │   ├── Profile.class
│   │       │               │   ├── Profile$ProfileBuilder.class
│   │       │               │   ├── ProfileType.class
│   │       │               │   ├── Template.class
│   │       │               │   └── Template$TemplateBuilder.class
│   │       │               ├── repository
│   │       │               │   ├── ProfileRepository.class
│   │       │               │   └── TemplateRepository.class
│   │       │               └── service
│   │       │                   ├── BarcodeService.class
│   │       │                   ├── BarcodeService$1.class
│   │       │                   ├── IdCardPdfService.class
│   │       │                   ├── IdCardPreviewService.class
│   │       │                   ├── PhotoStorageService.class
│   │       │                   ├── ProfileService.class
│   │       │                   ├── QrCodeService.class
│   │       │                   ├── RegistrationNumberService.class
│   │       │                   └── TemplateService.class
│   │       └── test
│   ├── generated
│   │   └── sources
│   │       ├── annotationProcessor
│   │       │   └── java
│   │       │       ├── main
│   │       │       └── test
│   │       └── headers
│   │           └── java
│   │               ├── main
│   │               └── test
│   ├── libs
│   │   ├── lypheng-0.0.1-SNAPSHOT-plain.jar
│   │   └── lypheng-0.0.1-SNAPSHOT.jar
│   ├── reports
│   │   └── problems
│   │       └── problems-report.html
│   ├── resolvedMainClassName
│   ├── resources
│   │   └── main
│   │       ├── application.properties
│   │       └── templates
│   │           └── idcard-preview.html
│   └── tmp
│       ├── bootJar
│       │   └── MANIFEST.MF
│       ├── compileJava
│       │   ├── compileTransaction
│       │   │   ├── backup-dir
│       │   │   └── stash-dir
│       │   │       └── LymphengApplication.class.uniqueId0
│       │   └── previous-compilation-data.bin
│       ├── compileTestJava
│       └── jar
│           └── MANIFEST.MF
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── HELP.md
├── MYPLAN.md
├── README.md
├── README.pdf
├── settings.gradle.kts
└── src
    ├── main
    │   ├── java
    │   │   └── live
    │   │       └── cottons
    │   │           └── lypheng
    │   │               ├── controller
    │   │               │   ├── ProfileController.java
    │   │               │   └── TemplateController.java
    │   │               ├── dto
    │   │               │   ├── BatchGenerateRequest.java
    │   │               │   ├── ProfileCreateRequest.java
    │   │               │   ├── ProfileResponse.java
    │   │               │   └── ProfileUpdateRequest.java
    │   │               ├── exception
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── LyphengApplication.java
    │   │               ├── model
    │   │               │   ├── BarcodeType.java
    │   │               │   ├── Profile.java
    │   │               │   ├── ProfileType.java
    │   │               │   └── Template.java
    │   │               ├── repository
    │   │               │   ├── ProfileRepository.java
    │   │               │   └── TemplateRepository.java
    │   │               └── service
    │   │                   ├── BarcodeService.java
    │   │                   ├── IdCardPdfService.java
    │   │                   ├── IdCardPreviewService.java
    │   │                   ├── PhotoStorageService.java
    │   │                   ├── ProfileService.java
    │   │                   ├── QrCodeService.java
    │   │                   ├── RegistrationNumberService.java
    │   │                   └── TemplateService.java
    │   └── resources
    │       ├── application.properties
    │       └── templates
    │           └── idcard-preview.html
    └── test
        ├── java
        │   └── live
        │       └── cottons
        │           └── lypheng
        │               ├── controller
        │               │   └── ProfileControllerTest.java
        │               ├── LyphengApplicationTests.java
        │               └── service
        │                   ├── BarcodeServiceTest.java
        │                   ├── ProfileServiceTest.java
        │                   ├── QrCodeServiceTest.java
        │                   ├── RegistrationNumberServiceTest.java
        │                   └── TemplateServiceTest.java
        └── resources
            └── application-test.properties

```

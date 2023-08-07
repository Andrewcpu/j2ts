# Java to TypeScript API Generator

This utility is a Java to TypeScript API generator. It uses Java Reflections and annotations to scan your Java codebase for RESTful API endpoints and generates corresponding TypeScript functions. This utility is particularly useful when you're working on a project that has both a Java backend and a TypeScript frontend, and you want to ensure that the frontend is always in sync with the backend API.
## Features
- Scans Java classes for RESTful API endpoints.
- Generates TypeScript functions corresponding to the API endpoints.
- Supports GET, POST, PUT, DELETE, and PATCH methods.
- Handles path variables, query parameters, and request headers.
- Supports optional and nullable fields.
- Generates TypeScript types from Java classes.
- Supports Kebab case conversion for TypeScript function names.
## Annotations

This utility uses the following custom annotations:
- `@API`: Used on methods to mark them as API endpoints.
- `@Model`: Used on classes to mark them as models that should be converted to TypeScript types.
- `@NullableField`: Used on fields, parameters, and types to mark them as nullable.
- `@OptionalField`: Used on fields and parameters to mark them as optional.
- `@ParamDescription`: Used on method parameters to provide a description.
- `@ReturnDescription`: Used on methods to provide a description of the return value.
## Usage
1. Annotate your Java classes and methods with the provided annotations.
2. Include the following build plugin in your `pom.xml`:
```xml
    <build>
    <plugins>
        <plugin>
            <groupId>net.andrewcpu</groupId>
            <artifactId>j2ts</artifactId>
            <version>1.1-SNAPSHOT</version>
            <inherited>true</inherited>
            <executions>
                <execution>
                    <phase>process-classes</phase>
                    <goals>
                        <goal>scan</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <mainPackage>net.andrewcpu.example</mainPackage>
            </configuration>
        </plugin>
    </plugins>
</build> 
```
3. Include the following dependency for your annotations:
```xml
<dependency>
    <groupId>net.andrewcpu</groupId>
    <artifactId>j2ts</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```


3. The utility will scan your Java codebase for API endpoints and generate corresponding TypeScript functions in the output directory.
## Example

```java

@RestController
public class AccountController {
    
    @API("Get a user from their userID")
    @GetMapping("/user/{userId}")
    @ReturnDescription("The requested user model.")
    public User getUserById(@PathVariable("userId") @ParamDescription("UserID to search") String userId, 
                            @RequestParam("q") @ParamDescription("Query parameter") String query) {
        return null;
    }
}
```



This will generate the following TypeScript function:

```typescript

/**
 * Get a user from their userID
 *
 * @param {string} userId - UserID to search
 * @param {string} q - Query parameter
 * @returns {backend.IUser} The requested user model.
 */
export function getUserById(userId: string, q: string): Promise<backend.IUser> {
    return request.get(`/user/${userId}`, {
        params: {
            q
        }
    });
}
```


## Dependencies

This utility depends on the following libraries:
- Java Reflections
- Jackson (for JSON serialization/deserialization)
- TypeScript Generator (for generating TypeScript types from Java classes)
## Limitations
- This utility assumes that all API endpoints are annotated with Spring's `@RestController` and `@RequestMapping` annotations.
- The utility currently supports only a subset of Java types. Unsupported types will be converted to `any` in TypeScript.
## Output
The output will be in the target/ directory.
- `types.d.ts`: All of your Java model types converted by typescript-generator
- `controllers/*-api.ts`: Each Rest controller is split into its own TS module
- `api.ts`: All controllers are exported from api.ts
```
target/
└── ts-api/
    ├── controllers/
    │   └── rest-controller-api.ts
    ├── api.ts
    └── types.d.ts
```

## Contributing

Contributions are welcome! Please submit a pull request or create an issue on GitHub.
## License

This utility is released under the MIT License.

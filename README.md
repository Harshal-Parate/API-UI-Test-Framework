# Automation Framework - UI and API Testing

A comprehensive test automation framework built with Java, Selenium, TestNG, and REST Assured for UI and API testing.

## 🚀 Features

- **Hybrid Framework**: Supports both UI (Selenium) and API (REST Assured) testing
- **Parallel Execution**: TestNG parallel execution with configurable thread count
- **Data-Driven Testing**: JSON-based data provider for parameterized tests
- **Selenium Grid Support**: Docker-based Selenium Grid for distributed testing
- **Smart Locator Management**: XML-based locator repository with dynamic resolution
- **Enhanced Reporting**: Allure reports with screenshots, logs, and network traces
- **Retry Mechanism**: Automatic retry for flaky tests
- **DevTools Integration**: Network monitoring and browser console logs
- **Multiple Browser Support**: Chrome, Firefox with headless mode
- **CI/CD Ready**: Jenkins pipeline configuration included

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (for Selenium Grid)
- Allure Commandline (for report generation)

## 🛠️ Tech Stack

- **Java 17**: Programming language
- **Selenium 4.35.0**: UI automation
- **TestNG 7.11.0**: Test framework
- **REST Assured 5.5.6**: API testing
- **Allure 2.20.1**: Test reporting
- **Cucumber 7.31.0**: BDD framework
- **WebDriverManager 5.10.0**: Driver management
- **Logback**: Logging framework
- **Jackson**: JSON processing
- **Apache POI**: Excel data handling

## 📁 Project Structure

```
├── src/
│   ├── main/java/org/example/
│   │   ├── actionmanagement/     # Selenium wrapper with robust interactions
│   │   ├── drivermanager/        # WebDriver factory and management
│   │   ├── listeners/            # TestNG listeners for reporting
│   │   ├── locatormanagement/    # Centralized locator repository
│   │   ├── pages/                # Page Object Model classes
│   │   └── utilities/            # Common utilities (config, logging, waits)
│   └── test/
│       ├── java/org/example/
│       │   ├── dataproviderutils/ # JSON data provider
│       │   └── tests/             # Test classes
│       └── resources/
│           ├── locators.xml       # Locator definitions
│           ├── properties/        # Environment configs
│           ├── testdata/          # Test data files
│           └── logback-test.xml   # Logging configuration
├── docker-compose.yml             # Selenium Grid setup
├── Jenkinsfile                    # CI/CD pipeline
├── testng.xml                     # TestNG suite configuration
└── pom.xml                        # Maven dependencies

```

## 🚦 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd "Automation Framework UI and API"
```

### 2. Install Dependencies

```bash
mvn clean install -DskipTests
```

### 3. Configure Test Environment

Edit `src/test/resources/properties/qa.properties`:

```properties
baseUrl=https://your-application-url.com
browser=chrome
headless=false
execution=local
implicitWait=3
pageLoadTimeout=20
scriptTimeout=10
```

### 4. Run Tests Locally

```bash
# Run all tests
mvn clean test

# Run with specific browser
mvn clean test -Dbrowser=chrome

# Run in headless mode
mvn clean test -Dheadless=true

# Run with custom threads
mvn clean test -Dthreads=4
```

### 5. Run with Selenium Grid

```bash
# Start Selenium Grid
docker-compose up -d

# Wait for Grid to be ready
curl http://localhost:4444/status

# Run tests on Grid
mvn clean test -Dexecution=remote -DgridUrl=http://localhost:4444/wd/hub

# Stop Grid
docker-compose down
```

## 📊 View Reports

### Allure Report

```bash
# Generate and open Allure report
mvn allure:serve

# Or generate static report
mvn allure:report
# Then open: target/site/allure-maven-plugin/index.html
```

### TestNG Report

After test execution, open: `test-output/index.html`

## 🎯 Framework Features

### Smart Locator Management

Locators are defined in `locators.xml`:

```xml
<locator key="USERNAME" value="id:username"/>
<locator key="PASSWORD" value="id:password"/>
<locator key="LOGIN_BUTTON" value="xpath://button[@type='submit']"/>
```

### Robust Element Interactions

The SeleniumWrapper provides:
- Multiple click strategies (normal → JS → Actions)
- Automatic waits and retries
- Screenshot on failure
- Detailed logging

### Data-Driven Testing

```java
@Test(dataProvider = "jsonData", dataProviderClass = JSONDataProvider.class)
@DataProviderConfig(key = "loginCredentials", fields = "username,password")
public void validLoginTest(String username, String password) {
    loginPage.login(username, password);
}
```

### Automatic Retry

Failed tests are automatically retried (configurable):

```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void myTest() {
    // Test logic
}
```

## 🔧 Configuration

### TestNG Configuration (`testng.xml`)

```xml
<suite name="Automation Suite" parallel="tests" thread-count="${threads}">
    <parameter name="env" value="${env}"/>
    <parameter name="browser" value="${browser}"/>
    <!-- Add your test classes -->
</suite>
```

### Maven Properties

Override via command line:

```bash
mvn test -Denv.name=qa -Dbrowser.name=firefox -Dthread.count=3
```

## 🐳 Docker Selenium Grid

The `docker-compose.yml` includes:
- Selenium Hub (4.28.0)
- Chrome Node (up to 3 sessions)
- Firefox Node (up to 3 sessions)

## 🔄 CI/CD Integration

### Jenkins Pipeline

The included `Jenkinsfile` provides:
- Parameterized builds (env, browser, threads)
- Automatic Grid startup/teardown
- Allure report publishing
- Build notifications

## 📝 Writing Tests

### Page Object Example

```java
public class LoginPage extends BasePage {
    public void login(String user, String pass) {
        wrapper.sendKeys(BaseLocators.USERNAME, user);
        wrapper.sendKeys(BaseLocators.PASSWORD, pass);
        wrapper.click(BaseLocators.LOGIN_BUTTON);
    }
}
```

### Test Class Example

```java
@Epic("Authentication")
@Feature("Login Feature")
public class LoginTest extends BaseTest {
    LoginPage loginPage;

    public LoginTest() {
        loginPage = new LoginPage();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void validLoginTest() {
        loginPage.login("testuser", "password123");
    }
}
```

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver not found**: WebDriverManager handles this automatically
2. **Tests timing out**: Adjust timeouts in properties file
3. **Grid connection failed**: Check if Grid is running (`docker ps`)
4. **Port already in use**: Stop existing containers (`docker-compose down`)

### Logs

Check logs at: `logs/test-automation.log`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Contact

For questions or support, please contact the development team.

---

**Happy Testing! 🎉**

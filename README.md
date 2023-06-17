# Tesbo V3 - Configurable Test Automation Framework

Tesbo is an open-source testing framework designed to provide a scalable and efficient way to structure and maintain your automated tests. V3 introduces the new flexible and extensible configuration system.

## Key Features

- Support for various test types (Web, Mobile, API)
- Multi-platform and multi-browser support for web testing
- Device and orientation settings for mobile testing
- Extensive configuration for test setup like retries, timeouts, and execution location
- Flexible data sources handling including JSON, Excel, or database systems
- Compatibility with multiple test frameworks (TestNG, JUnit, Playwright)

## Configuration

Tests are configured using a JSON file with multiple profiles. Each profile defines the settings for a specific type of test (web, mobile, or API) and can be selected at runtime.

The structure of the configuration file can be seen in the `config.json` file in the root directory.

### Test Profile

A profile includes details such as:

- Test Type (`testType`): Can be either "web", "mobile" or "api".
- Test Framework (`testFramework`): The name of the testing framework to be used, e.g., "TestNG", "JUnit", or "Playwright".
- Test Configuration (`testConfiguration`): Contains fields for retries, timeout, and execution location (runLocation).
- Browser or Device: For web testing, the name and version of the browser (and optionally whether to run headless). For mobile testing, the name, platform name/version, and orientation of the device.
- Data Source (`dataSource`): Defines the type of data source and its location or connection details. Types can be "json", "excel", or "database".

### Running Tests

To run tests using a specific profile, you can point Tesbo to use that profile at runtime.

### Folder Structure

```
TesboV3/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── io/
│   │   │   │   ├── tesbo/
│   │   │   │   │   ├── framework/         # Core framework code
│   │   │   │   │   ├── utils/             # Helper classes, utility functions
│   │   │   │   │   ├── ...
│   │   ├── resources/                     # Global resources like configuration files
│   │   │   ├── webConfig.json
│   │   │   ├── mobileConfig.json
│   │   │   ├── apiConfig.json
│   │   │   ├── ...
│
├── test/
│   ├── java/
│   │   ├── io/
│   │   │   ├── tesbo/
│   │   │   │   ├── webTests/              # Web test classes
│   │   │   │   │   ├── pages/             # Page Object classes for web
│   │   │   │   │   ├── actions/           # Action classes for web
│   │   │   │   │   ├── tests/             # Actual test cases for web
│   │   │   │   ├── mobileTests/           # Mobile test classes
│   │   │   │   │   ├── pages/             # Page Object classes for mobile
│   │   │   │   │   ├── screens/           # Action classes for mobile
│   │   │   │   │   ├── tests/             # Actual test cases for mobile
│   │   │   │   ├── apiTests/              # API test classes
│   │   │   │   │   ├── endpoints/         # Endpoint classes for API
│   │   │   │   │   ├── actions/           # Action classes for API
│   │   │   │   │   ├── tests/             # Actual test cases for API
│   ├── resources/                         # Test-specific resources
│   │   ├── datasets/                      # Test data sets (can include json, excel, etc.)
│   │   ├── testng.xml                     # TestNG suite files
│
├── lib/                                   # External libraries or dependencies
│
├── target/                                # Generated files, reports, compiled code, etc.
│
├── .gitignore                             # Specify files to ignore for Git
├── pom.xml                                # Maven dependencies and project configuration
├── README.md                              # Project overview and instructions
└── ...

```

## Contributing

We welcome contributions to Tesbo V3! Please see our [Contributing Guide](CONTRIBUTING.md) for more details.

## License

Tesbo V3 is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for more details.

---
Happy Testing with Tesbo!
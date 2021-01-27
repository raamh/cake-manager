# Steps to build & run the App

1. Install Gradle, configure __GRADLE_HOME__ to your installed Gradle folder and include __/bin__ within your Gradle Installation folder to your System Path.

2. Run any of the below commands in your Terminal to running the App locally:
    1. Run the below command to just run the App. The second command version below includes __--offline__ flag which will help if you are running this command where there is no internet connectivity. The __--offline__ flag tells the gradle to use cached dependencies. 
       You can then access the url __http://localhost:8080__ to get the default home page containing list of cakes in HTML or __http://localhost:8080/cakes__ to get the list of cakes in JSON.
        ```
        $ gradle clean bootRun
        $ gradle --offline clean bootRun
        ```
    2. Run the below command to debug your code with IDE:
       NOTE: Ignore the message _To honour the JVM settings for this build a new JVM will be forked..._ and try to connect to Java Remote Port __5005__ from your IDE. You can use __--offline__ flag in the command like the previous command above.
        ```
        $ gradle clean bootRun --no-daemon -Dorg.gradle.debug=true
        $ gradle --offline clean bootRun --no-daemon -Dorg.gradle.debug=true
        ```
    3. Run the below command to just compile & run your unit tests. You can use __--offline__ flag in the command like the previous command above.
        ```
        $ gradle clean test
        $ gradle --offline clean test
        ```
    4. For debugging an Unit Test, execute the below command and start Remote Debugging on port 5005 on IDE:
        ```
        $ gradle clean test --tests com.waracle.cakemgr.ApplicationTest.contextLoads --no-daemon -Dtest.debug
        ```

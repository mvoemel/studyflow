# Use a base image with OpenJDK
FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Grant execute permission to the Gradle wrapper
RUN chmod +x ./gradlew

RUN ./gradlew distribute

ENV CLASSPATH=app/build/distributions/app/libs/app.jar:app/build/distributions/app/deps/jackson-annotations-2.18.0.jar:app/build/distributions/app/deps/jackson-databind-2.18.0.jar:app/build/distributions/app/deps/jackson-core-2.18.0.jar

# Build the application
#RUN ./gradlew clean build jar --no-daemon


# Expose the port your application will run on
EXPOSE 8080

# Specify the command to run the application
CMD ["java", "-cp", "app/build/distributions/app/libs/app.jar:app/build/distributions/app/deps/jackson-annotations-2.18.0.jar:app/build/distributions/app/deps/jackson-databind-2.18.0.jar:app/build/distributions/app/deps/jackson-core-2.18.0.jar", "ch.zhaw.studyflow.Main"]


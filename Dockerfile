FROM openjdk:21

# JAR 파일을 지정하는 빌드 인수
ARG JAR_FILE=build/libs/*.jar

# JAR 파일 복사
COPY ${JAR_FILE} app.jar

# JVM 시간대 설정 추가
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
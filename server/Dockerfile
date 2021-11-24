FROM gradle:7-jdk17-alpine
COPY . /school_enrollment_server
WORKDIR /school_enrollment_server
RUN gradle build

CMD gradle bootRun
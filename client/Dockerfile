FROM gradle:7-jdk17-alpine
COPY . /school_enrollment_client
WORKDIR /school_enrollment_client
RUN gradle build

CMD gradle bootRun

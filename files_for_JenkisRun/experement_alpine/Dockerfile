FROM alpine
RUN apk update && apk add nano && apk add openjdk17 && apk add git && apk add docker docker-compose
WORKDIR /home
add docker-compose.yaml /home
add test_suite.yaml /home

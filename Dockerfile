FROM bellsoft/liberica-openjdk-alpine:17
RUN apk add curl jq
WORKDIR /home/selenium-docker
ADD /target/docker-resources ./
ADD runner.sh runner.sh
ENTRYPOINT sh runner.sh
#ENTRYPOINT java -Dbrowser=${BROWSER} -Dselenium_grid_enabled=true -Dselenium.grid.hubHost=${HUB_HOST} -cp 'libs/*' org.testng.TestNG test_sutes/${TEST_SUITE}

#FROM мы взяли alpine image с java 17
#RUN apk add curl jq <- add utilities
# ************************************ COMANDS FOR DOCKER EXECUTION *******************************
#--------------------scenario # 1 create dockerfile--------------------------------

# FROM bellsoft/liberica-openjdk-alpine:17
# WORKDIR /home/selenium-docker
# ADD /target/docker-resources ./

# explanation of dockerfile instructions:
# ADD /target/docker-resources ./
# ADD /target/docker-resources < -- то что хотим добавить
# ./ < -- то куда хотим добавить в VM. В нашем случае у нас с помощью  WORKDIR /home/selenium-docker
# уже указана директория поэтому символ './' указывает на текущую директорию т.е. home/selenium-docker

# to build image: use build -t comand

# To create volume mapping and run image:
#docker run -it -v /C/for_experements/docker_experements/DockerFile/vins_guru_framwork/test_output:/home/selenium-docker/test-output delete_me_i_am_for_experement_only

#to run tests in container (pay attention on your private/local ip address)
# java -Dbrowser=chrome -Dselenium_grid_enabled=true -Dselenium.grid.hubHost=192.168.56.1 -cp 'libs/*' org.testng.TestNG test_sutes/vendorPortal.xml

#--------------------scenario # 2 adding ENTRYPOINT to dockerfile-------------------------

# in order to use ENTRYPOINT we can not to hard code the values of variables, we need to use environment va
# riables. We have to difine environment variables that we plan to use.
# 1 BROWSER
# 2 HUB_HOST          - 192.168.56.1
# 3 TEST_SUITE        - test_sutes/vendorPortal.xml

# Dockerfile:
# FROM bellsoft/liberica-openjdk-alpine:17
# WORKDIR /home/selenium-docker
# ADD /target/docker-resources ./
# ENTRYPOINT java -Dbrowser=${BROWSER} -Dselenium_grid_enabled=true -Dselenium.grid.hubHost=${HUB_HOST} -cp 'libs/*' org.testng.TestNG test_sutes/${TEST_SUITE}

# create image via comand: docker build -t=delete_me_i_am_for_experement_only ./

# run the image via camand:
# docker run -e BROWSER=chrome -e HUB_HOST=192.168.56.1 -e TEST_SUITE=vendorPortal.xml delete_me_i_am_for_experement_only

#------------------------------------scneario # 3--------------------------------------

#следущим шагом будет реализация .yaml файла где мы соберём hub+chrome+fireFox+наша image
# сейчас чтобы тесты/image выполнилась надо сначала запустить hub а затем тесты. Т.е. надо иметь 2 файла
# 1).yaml для hub-a 2)для image. Идея в том чтобы держать всё в одном файле, лучше в yaml. Но если просто
# добавить к hub-у наш image (как 3 service)то не заработает так-как hub будет включатся позже чем
# тесты проранятся на нашей image, соответственно так как hub будет не готов то тесты не начем будет
# проводить. Нужна задержка для полной загрузки hub и готовность его к работе. Для этого существует
# runner. добовляем файл runner.sh. в image, меняем ENTRYPOINT и теперь там команда на запуск runner-a.
# Docker будет запускать сервисы согласно установленному depends_on aтрибуту, после запуска они будут
# работать независимо от друг-друга, соответственно сервис с image будет проверять запустился ли hub
# и если он запустился только тогда отдаст команду java на выполнение.

# DockerFile:
# FROM bellsoft/liberica-openjdk-alpine:17
# RUN apk add curl jq            <-- eti utils nyzzni dlia runer-a
# WORKDIR /home/selenium-docker
# ADD /target/docker-resources ./
# ADD runner.sh runnere.sh       <--
# ENTRYPOINT sh runner.sh        <--

# yaml file:
#version: "3"
#services:
#    hub:
#        image: selenium/hub:4.10.0
#        ports:
#        - 4444:4444
#    chrome:
#        image: selenium/node-chrome:4.10
#        shm_size: '2g'
#        depends_on:
#        - hub
#        environment:
#        - SE_EVENT_BUS_HOST=hub # BY specifiing the ports the chrome container can talk to hub container
#        - SE_EVENT_BUS_PUBLISH_PORT=4442     #PORT
#        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
#        - SE_NODE_OVERRIDE_MAX_SESSIONS=true # podklychaem mnogopotochky
#        - SE_NODE_MAX_SESSIONS=4             # ykazivaem skolko thred-ov v 1 syshnasti
#        - SE_VNC_NO_PASSWORD=1               # in order to viw the work flow without password
#    firefox:
#        image: selenium/node-firefox:4.10
#        depends_on:
#        - hub
#        environment:
#        - SE_EVENT_BUS_HOST=hub
#        - SE_EVENT_BUS_PUBLISH_PORT=4442
#        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
#        - SE_NODE_OVERRIDE_MAX_SESSIONS=true
#        - SE_NODE_MAX_SESSIONS=4
#        - SE_VNC_NO_PASSWORD=1
#    framwork:
#        image: frame_work_image
#        depends_on:
#        - hub
#        environment:
#        - BROWSER=firefox
#        - HUB_HOST=192.168.56.1
#        - TEST_SUITE=vendorPortal.xml
#        volumes:
#        - ./output:/home/selenium-docker/test-output

#------------------------------------scneario # 4--------------------------------------
# Следущая задача - запуск сразу двух xml файлов а не одного. Для этого добавим ещо один service в .yaml.
# при этом Dockerfile оставим без изменений (как в 3 сценарии). Также надо переделать структуру папки output
# так как сейчас туда будут записыватся результаты обоих xml файлов, лучше всего зделать output общую ,
# а дальше суб директории для каждого xml файла соответственно.
#version: "3"
 #services:
 #    hub:
 #        image: selenium/hub:4.10.0
 #        ports:
 #        - 4444:4444
 #    chrome:
 #        image: selenium/node-chrome:4.10
 #        shm_size: '2g'
 #        depends_on:
 #        - hub
 #        environment:
 #        - SE_EVENT_BUS_HOST=hub # BY specifiing the ports the chrome container can talk to hub container
 #        - SE_EVENT_BUS_PUBLISH_PORT=4442     #PORT
 #        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
 #        - SE_NODE_OVERRIDE_MAX_SESSIONS=true # podklychaem mnogopotochky
 #        - SE_NODE_MAX_SESSIONS=4             # ykazivaem skolko thred-ov v 1 syshnasti
 #        - SE_VNC_NO_PASSWORD=1               # in order to viw the work flow without password
 #    firefox:
 #        image: selenium/node-firefox:4.10
 #        depends_on:
 #        - hub
 #        environment:
 #        - SE_EVENT_BUS_HOST=hub
 #        - SE_EVENT_BUS_PUBLISH_PORT=4442
 #        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
 #        - SE_NODE_OVERRIDE_MAX_SESSIONS=true
 #        - SE_NODE_MAX_SESSIONS=4
 #        - SE_VNC_NO_PASSWORD=1
 #    vendorPortal:                                                     <--
 #        image: frame_work_image
 #        depends_on:
 #        - hub
 #        environment:
 #        - BROWSER=firefox
 #        - HUB_HOST=192.168.56.1
 #        - TEST_SUITE=vendorPortal.xml
 #        volumes:
 #        - ./output/vendorPortal:/home/selenium-docker/test-output      <--
 #    flight-reservation:                                                <--
 #        image: frame_work_image
 #        depends_on:
 #        - hub
 #        environment:
 #        - BROWSER=chrome                                      <-- dolzzni bit raznimi
 #        - HUB_HOST=192.168.56.1
 #        - TEST_SUITE=flight-reservation.xml
 #        volumes:
 #        - ./output/flight-reservation:/home/selenium-docker/test-output <--

#------------------------------------ scneario # 5 --------------------------------------
# passing new test data for test using volume mapping.

# When we build project with maven we hard coded the test-data for tests (json files)
# The program takes these files and use them as data for running tests, but we can change this
# files/file by volume mapping and give new modified file and programm we take it.
# As experement in vendorPortal application we will change mike.json file and will
# replace it with new mike.json file. In that case we will be expecting test failure because
# the new mike.json file will have mistake/wrong parameter. But the porpose is to demonstrate
# the ability to change data by volume mapping
# See the yaml file:
#
# version: "3"
# services:
#     hub:
#         image: selenium/hub:4.10.0
#         ports:
#         - 4444:4444
#     chrome:
#         image: selenium/node-chrome:4.10
#         shm_size: '2g'
#         depends_on:
#         - hub
#         environment:
#         - SE_EVENT_BUS_HOST=hub # BY specifiing the ports the chrome container can talk to hub container
#         - SE_EVENT_BUS_PUBLISH_PORT=4442     #PORT
#         - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
#         - SE_NODE_OVERRIDE_MAX_SESSIONS=true # podklychaem mnogopotochky
#         - SE_NODE_MAX_SESSIONS=4             # ykazivaem skolko thred-ov v 1 syshnasti
#         - SE_VNC_NO_PASSWORD=1               # in order to viw the work flow without password
#     firefox:
#         image: selenium/node-firefox:4.10
#         depends_on:
#         - hub
#         environment:
#         - SE_EVENT_BUS_HOST=hub
#         - SE_EVENT_BUS_PUBLISH_PORT=4442
#         - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
#         - SE_NODE_OVERRIDE_MAX_SESSIONS=true
#         - SE_NODE_MAX_SESSIONS=4
#         - SE_VNC_NO_PASSWORD=1
#     vendorPortal:
#         image: frame_work_image
#         depends_on:
#         - hub
#         environment:
#         - BROWSER=firefox
#         - HUB_HOST=192.168.56.1
#         - TEST_SUITE=vendorPortal.xml
#         volumes:
#         - ./output/vendorPortal:/home/selenium-docker/test-output
#         - ./experement_test_data/mike.json:/home/selenium-docker/testData_inJSON/vendorPortal/mike.json  <-- magic !!!
#     flight-reservation:
#         image: frame_work_image
#         depends_on:
#         - hub
#         environment:
#         - BROWSER=chrome
#         - HUB_HOST=192.168.56.1
#         - TEST_SUITE=flight-reservation.xml
#         volumes:
#         - ./output/flight-reservation:/home/selenium-docker/test-output
#
##------------------------------------ scneario # 6  --------------------------------------
#In this scenario:
 #We will try to scale(маштабировать) the browsers in run time. The main idea is to create infrastructure (in this case it is browser
 #nodes) whenever you need and scale it as much as you need, and after you finish discart it.
 #For that:
 #1) create yaml file only with hub.
 #We will keep the framwork image separatly in order to control browser scalling in a run time. Because of that in every service
 #that related to browsers there are teg deploy: replica:0.
 #
 #version: "3"
 #services:
 #    hub:
 #        image: selenium/hub:4.10.0
 #    chrome:
 #        image: selenium/node-chrome:4.10
 #        shm_size: '2g'
 #        depends_on:
 #        - hub
 #        deploy:
 #         replicas: 0
 #        environment:
 #        - SE_EVENT_BUS_HOST=hub # BY specifiing the ports the chrome container can talk to hub container
 #        - SE_EVENT_BUS_PUBLISH_PORT=4442     #PORT
 #        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
 #        - SE_NODE_OVERRIDE_MAX_SESSIONS=true # podklychaem mnogopotochky
 #        - SE_NODE_MAX_SESSIONS=4             # ykazivaem skolko thred-ov v 1 syshnasti
 #        - SE_VNC_NO_PASSWORD=1               # in order to viw the work flow without password
 #    firefox:
 #        image: selenium/node-firefox:4.10
 #        depends_on:
 #        - hub
 #        deploy:
 #         replicas: 0
 #        environment:
 #        - SE_EVENT_BUS_HOST=hub
 #        - SE_EVENT_BUS_PUBLISH_PORT=4442
 #        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
 #        - SE_NODE_OVERRIDE_MAX_SESSIONS=true
 #        - SE_NODE_MAX_SESSIONS=4
 #        - SE_VNC_NO_PASSWORD=1
 #
 #2).
 #Our tests we will keep in separate file, and since we alredy have hub, we dont need hub here.
 #In order to be able to pass amaunt of browsers we keep it as environment variables
 #The ather .yaml file:
 #
 #version: "3"
 #services:
 #    vendorPortal:
 #        image: frame_work_image
 #        environment:
 #        - BROWSER=${BROWSER}
 #        - HUB_HOST=192.168.56.1
 #        - TEST_SUITE=vendorPortal.xml
 #        volumes:
 #        - ./output/vendorPortal:/home/selenium-docker/test-output
 #    flight-reservation:
 #        image: frame_work_image
 #        environment:
 #        - BROWSER=${BROWSER}
 #        - HUB_HOST=192.168.56.1
 #        - TEST_SUITE=flight-reservation.xml
 #        volumes:
 #        - ./output/flight-reservation:/home/selenium-docker/test-output
 #
 #So the first insructure:
 #docker-compose -f grid.yaml up --scale chrome=2 -d
 #Explanation:
 #-f grid.yaml <-- since we have few dockerCompose files we tell the docker what specifik file should be сompleted.
 #--scale chrome=2 <-- amaunt of nodes to be present (since we have 2 application we need 2 browsers).
 #-d <--run it in background.(in order) (in a docker file that responsible for grid the ports are removed so we are not able to
 #see the UI in a localhost. If you want to be able to see the UI of the grid use teg ports.)
 #
 #the second insructure:
 #docker-compose up
 #Explanation:
 #Since the second .yaml file contains framwork (image with runner) runner see that grid is presented and execute the java
 #comand wich is presented in it.
 #You should be in the same directory where the desired dockerfile is presented, atherwise you need to provide name of the file
 #with flag -f <name_of_the_file>

#
# If we enter the comand: docker-compose -f grid.yaml up --scale chrome=2 -d
# the docker creates 2 nodes of chrome, but if we want to change it for example to firefox:
# docker-compose -f grid.yaml up --scale firefox=2 -d
# and docker will automaticaly destroy the chrome nodes and instead of it will create firefox, we dont need to delete it manyaly.
#Pay attention if in .yaml file the browser set as chrome and you have fitrefox in grid running then you need to change the
 #OS variable and than call for docker-compose up
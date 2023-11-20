Идея в том чтобы запустить сгенериную Jenkins-ам имедж в первый раз
Взял yaml.file из scenario #1 грид + тесты Поменял только название имедж в сьютах, запускается через docker-compose up

services:
    hub:
        image: selenium/hub:4.10.0
        ports: 
        - 4444:4444
    chrome:
        image: selenium/node-chrome:4.10
        shm_size: '2g'
        depends_on:
        - hub
        environment:
        - SE_EVENT_BUS_HOST=hub
        - SE_EVENT_BUS_PUBLISH_PORT=4442     
        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
        - SE_NODE_OVERRIDE_MAX_SESSIONS=true 
        - SE_NODE_MAX_SESSIONS=4             
        - SE_VNC_NO_PASSWORD=1               
    firefox:
        image: selenium/node-firefox:4.10
        depends_on:
        - hub
        environment:
        - SE_EVENT_BUS_HOST=hub
        - SE_EVENT_BUS_PUBLISH_PORT=4442 
        - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
        - SE_NODE_OVERRIDE_MAX_SESSIONS=true
        - SE_NODE_MAX_SESSIONS=4
        - SE_VNC_NO_PASSWORD=1
    vendorPortal:
        image: dimbas/frame_work
        depends_on:
        - chrome
        environment:      # так как переменные по умолчанию обьявленны в runner-e то здесь можно ограничить или вообще удалить
        - TEST_SUITE=vendorPortal.xml   
        volumes:
        - C:\for_experements\docker_experements\jenkins\jenkinsFile+grid+tests_in_one_yaml_file_6/output/vendorPortal/:/home/docker-resources/test-output
    flight-reservation:
        image: dimbas/frame_work
        depends_on:
        - chrome
        environment:
        - TEST_SUITE=flight-reservation.xml
        volumes:
        - C:\for_experements\docker_experements\jenkins\jenkinsFile+grid+tests_in_one_yaml_file_6/output/flight-reservation/:/home/docker-resources/test-output

    
#ctrl + q = закоментировать выбранные строки .

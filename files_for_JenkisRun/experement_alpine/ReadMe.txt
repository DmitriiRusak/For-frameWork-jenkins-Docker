В Docker-resources лежит проект, я просто скопировал его сюда для того чтобы добавить в Dockerfile когда
строим image. (P.S. уже убрал-он не нужен)
1)Создам dockerfile с openjdk17, git, docker
2)Добавим проект с помощью гит  <-- гит тоже в принципе не надо, проект не используется, используется имедж с докерХаба
3) Поиграем с джава командами:  <-- нет смысла так как нет браузеров

Нужен грид у него есть браузеры, добавлю грид в хост директорию.
Так обломчик случился! Докер есть но докер demon не работает, как я понял нельзя поставить docker на docker,
Но можно запустить какойто готовый котейнер с преустановленной Alpine по этой команде:
docker run -it --name alpine-docker -v "/var/run/docker.sock:/var/run/docker.sock:rw" alpine/ # apk add docker
и внутри этого контейнера добавить докер клиент: apk add docker и мы какбы имеем наш докер который используем на хосте 
в созданном контейнере, о как.

Так всё разобрался, можно зделать имедж из такого dockerFile-a например:

FROM alpine
RUN apk update && apk add nano && apk add openjdk17 && apk add git && apk add docker
WORKDIR /home/project

строим имедж с помщью команды: 
docker build -t=ex-nt_alpine_docker_java17_git . (т.е. имя имеджа- ex-nt_alpine_docker_java17_git)
Теперь не запуская этот контейнер надо сообщить Docker-у какая имедж будет использовать Docker demon, эта же команда и запустит 
эту имедж: 
docker run -it --name alpine-docker -v "/var/run/docker.sock:/var/run/docker.sock:rw" ex-nt_alpine_docker_java17_git <-- обрати внимание 
что в этой команде в конце указывается имя имедж которая может использовать docker engine, здесь это созданная ранее ex-nt_alpine_docker_java17_git.
Теперь мы внутри контейнера и здесь есть всё что мы ранее установили, крутяк не?

Дальше: нам понадобится грид+chrome+firefox, test-suit.yaml, где взять? добавим c помощью ADD. И так dockerfile теперь выглядит вот так

FROM alpine
RUN apk update && apk add nano && apk add openjdk17 && apk add git && apk add docker docker-compose
WORKDIR /home/project
add docker-compose.yaml /home
add test_suite.yaml /home

Незабудь переделать имедж чтобы иметь изменения.
docker-compose up -d                  <-- start grid
docker-compose -f test_suite.yaml up  <-- start test-suits
По факту там конечно же будут другие порты, поэтому тесты не пройдут. Посмотреть действующий порт ip a, внеси эти изменения в test suits
127.0.0.1
172.17.0.2
192.168.16.2
Порты то я подставил но ничего не работает, в смысле грид с браузерами работает а тесты просто наченаются и всё, просто стоят какбудто зависли.
Самое Очевидное это неправильно указанн порт для selenium grid в test_suites. УУУВВАУУ. Да неправильно были указаны порты. и те порты которые 
можно посмотреть с помощью стандартных команд типа(ip a, ifconfig) не правильные. Docker каждому контейнеру даёт что - то типа описания там указа-
нно много чего в том числе и порты, команда: 
docker network inspect [OPTIONS] NETWORK [NETWORK...] <-- т.е. надо как-то узнать id network того контейнера который хочеш просмотреть, я вводил:
docker network inspect bdc09597ee64af0729cca1fb1c0dded218de70ae703df3e24ed7d295d2546aac
Выдаёт JSON format но там находиш по имени нужный контейнер и смотриш его id. Класс!!! О чуть не забыл, смотреть надо на машине хоста.
Выход есть, надо смотреть...
ОГО можно посмотреть и так 
docker inspect id-контейнера <-- там бля куча конечно всего но там есть и networkId:.... и IPAdress:....
А id контейнера посмотреть docker ps
Так по факту: поставил имедж с алпайн + nano (редактор)









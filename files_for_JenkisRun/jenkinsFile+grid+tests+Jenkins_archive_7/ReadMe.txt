 
Надо отдельно создать грид с браузерами в одном yaml.fil-e
Сoздать тест сьюты в отдельном yaml.file. Здесь можно убрать тег: depends on,так как теперь грид раздельно то 
смысла в этом тэге нет.
Фишка в этом подходе - это сделанный отдельно .env file, чтобы применить переменные прописанные в нём в гриде используем:
env_file: .env    <-- вроде бы можно давать имя этим файлам (если их несколько) а затем подставлять имена файлов которые
требуются, да проверил так и есть.
отдельно jenkinsfile

Имея всё это раздельно, запустим сначала грид: docker-compose -f grid.yaml up -d
убедимся что грид работает и всё ок
Запустим тесты: docker-compose -f test_suite.yaml up
тесты выполнятся и завершатся. теперь можно закрыть грид
docker-compose -f grid.yaml down
docker-compose test_suite.yaml down

Соответственно надо изменить jenkinsfile чтобы в нём был такой же порядок docker 
инструкций как описано чуть выше. 
Для того чтобы заархивировать результаты выполнения нашей framework используются последние
2 строчки. (смотри файл ниже, секцию post).
pipeline{
	
	agent any
	
	stages{
		
		stage('Start grid'){
			steps{
				bat "docker-compose -f grid.yaml up -d"
			}
		}
		
		stage('Run tests'){
			steps{
				bat "docker-compose -f test_suite.yaml up"			
			}
		}
	}

	post {
		always {
			bat "docker-compose -f grid.yaml down"
			bat "docker-compose -f test_suite.yaml down"
			archiveArtifacts artifacts: 'output/flight-reservation/emailable-report.html', followSymlinks: false
			archiveArtifacts artifacts: 'output/vendorPortal/emailable-report.html', followSymlinks: false
		}
	
	}
}
Если удалить директорию output на локальном компе из C:\for_experements\docker_experements\jenkins\jenkinsFile+grid+tests+Jenkins_archive_7
, где распологается у нас локально этот пример, выполнить джоб, то папка волум маппинга (output)не создастся так как
дженкинс всё запишет в директорию workspace того нода кто делал джоб. Ну да всё верно джоб же не делал наш локальный 
терминал/комп.
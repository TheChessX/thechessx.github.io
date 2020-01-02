# thechessx.github.io

Website for ChessX, a chess engine with breakdown analysis

Install maven 3.6.3

Add maven to path (.bash_profile add export PATH=$PATH: maven-path )

Jdk 13

webapp runner: https://github.com/heroku/webapp-runner clone and build

install heroku cli

heroku

heroku plugins:install java

heroku war:deploy IdeaProjects/ChessX/out/artifacts/WebChess_war/WebChess_war.war --app thechessx

To run locally:

java -jar webapp-runner/assembly/target/webapp-runner.jar IdeaProjects/ChessX/out/artifacts/WebChess_war/WebChess_war.war

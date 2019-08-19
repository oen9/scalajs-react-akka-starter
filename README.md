# scalajs-react-akka-starter
[![Build Status](https://travis-ci.org/oen9/scalajs-react-akka-starter.svg?branch=master)](https://travis-ci.org/oen9/scalajs-react-akka-starter)

# Libs

## backend
1. scala
1. akka-http

## frontend
1. scalajs
1. reactjs
1. diode
1. bootstrap

# DEV

## js
`fastOptJS::webpack`\
`~fastOptJS`\
http://localhost:12345/js/target/scala-2.12/classes/index-dev.html

## server
`reStart`\
http://localhost:8080/

## js + server (dev conf)
Run server normally `reStart`.\
Connect your js api to http://localhost:8080 (e.g. change some baseUrl in js project).\
Run js: `fastOptJS::webpack` and `fastOptJS`.\
Open http://localhost:12345/js/target/scala-2.12/classes/index-dev.html in browser.\
When server changed run `reStart`.\
When js changed run `fastOptJS`.

## hints
Remember to run `fastOptJS::webpack` after e.g. `npmDependencies` changes.

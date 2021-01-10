[![Build Actions Status](https://github.com/pisalore/attsw-exam/workflows/build/badge.svg)](https://github.com/pisalore/attsw-exam/actions)
[![Coverage Status](https://coveralls.io/repos/github/pisalore/attsw-exam/badge.svg?branch=master)](https://coveralls.io/github/pisalore/attsw-exam?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=alert_status)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=security_rating)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=bugs)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pisalore_attsw-exam&metric=code_smells)](https://sonarcloud.io/dashboard?id=pisalore_attsw-exam)
## Museum App
###### Advanced Tool and Techniques for Software Development exam project at UNIFI

This is repository contains a very simple application developed in **Java11**, using a **PostgreSQL** database and **Swing** for GUI implementation. The main goal developing this application was to use tools and techniques for a better coding experience, with the aim to enhance code quality and its maintainability. Such tools are [Coveralls](https://coveralls.io/), [SonarCloud](https://sonarcloud.io/) and [Docker](https://www.docker.com/), all wired together using CI/CD provided by GitHub with [GitHub Actions](https://github.com/features/actions) workflows. More details are available in the full report [final-report_ATTSW.pdf](https://github.com/pisalore/attsw-exam/blob/master/final-report_ATTSW.pdf).

### Getting Started
**NB: tests and app have been executed with Windows 10 and Ubuntu 20.04.**

Assuming that you have already installed Docker, Docker-compose (both included in [Docker Desktop](https://www.docker.com/products/docker-desktop) if on Windows 10) and Java LTS 8 or 11 (Maven is not mandaory since you can use the maven wrapper provided in this repository) you can locally analyze the app with different configurations, build and run it with docker-compose:
1. Clone this repo:
```console
git clone https://github.com/pisalore/attsw-exam.git
```
2. Navigate inside the root project directory:
```console
cd attsw-exam
```
3. **Run Maven build** (with the Wrapper or your Maven installation) considering the following profiles:
    * jacoco: run jacoco analysis (code coverage).
    * pit: run mutation testing with [PIT](http://pitest.org/)
    * build-app: build Museum Manager App in order to generate jars and the relative Docker Image to be used with docker-compose
    
     In the following example, we build the app and run tests (use **mvnw.cmd** if you are using Windows instead Linux):
     ```console
     ./mvnw -f museum-manager/museum-manager-aggregator/pom.xml clean verify -Pbuild-app
     ```
4. **Run the application**: once you have build the app and generated its jars, you can decide to run the app:
   * exploiting **docker-compose** (the following command will orchestrate one PostgreSQL container with a MuseumManagerapp container):
   ```console
   cd attsw-exam
   docker-compose up
   ```
   * creating your database in PostgreSQL and passing your credentials from Command Line:
   ```console
   cd museum-manager/museum-manager-app
   java -jar ./target/museum-manager-app-*-jar-with-dependencies.jar --database-url=<JDBC_URL> --database-user=<DB_USER> --database-password=<DB_PASSWORD> 
   ```
#### Docker-compose in Windows
For docker compose usage in Windows, it is necessary to install an X Server. I have used [VcXsrv](https://sourceforge.net/projects/vcxsrv/), following the instructions below:
1. Install VcXsrv (for example via [Chocolatey](https://chocolatey.org/) Windows package manager:
```console
choco install vcxsrv
```
2. Run XLaunch from Start, using the standard configurations in Wizard. In **Extra Settings**, check "Disable access control".
3. Export the display variable, using your machine IP address:
```console
set DISPLAY=<YOUR_IP>:0.0
```
4. Finally, run **docker-compose** from project root:
```console
cd attsw-exam
docker-compose up
```
An useful guide can be found here: [Docker GUI App on Windows Host](https://dev.to/darksmile92/run-gui-app-in-linux-docker-container-on-windows-host-4kde) .

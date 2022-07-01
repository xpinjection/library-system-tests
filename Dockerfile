# Execute following commands on the cached MVN repository

# 1. Compile project with dependencies download:
# mvn -U clean compile test-compile
# 2. Copy dependencies to local repository dir:
# mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=true -Dmdep.copyPom=true -DoutputDirectory=./repository
# 3. Download remaining dependencies for test execution:
# mvn -DforkMode=never -Dmaven.repo.local=./repository surefire:test -Dtest=BookRegistryTest
# 4. Verify that offline mode is ready with local repository dir:
# mvn -o -DforkMode=never -Dmaven.repo.local=./repository surefire:test -Dtest=BookRegistryTest

FROM maven:3.6.3-openjdk-17-slim
ENV DTEST=""
RUN rm /usr/share/maven/conf/settings.xml
COPY . /tmp/
WORKDIR /tmp/
RUN mvn -o -DforkMode=never -Dmaven.repo.local=./repository surefire:test -Dtest=BookRegistryTest
CMD ["./entrypoint.sh"]
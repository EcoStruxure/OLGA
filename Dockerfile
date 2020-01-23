FROM alpine/git as clone
ARG url
ARG branch
WORKDIR /app
RUN git clone -b ${branch} --single-branch ${url}

FROM maven:3.5-jdk-8-alpine as build
ARG project
ARG subprojects
WORKDIR /app
COPY --from=clone /app/${project} /app
WORKDIR /app/${project}
RUN mvn --projects ${subprojects} clean install -DskipTests

FROM maven:3.5-jdk-8-alpine
ARG project
ARG artifactid
ARG version
ENV artifact ${artifactid}-${version}-with-dependencies.jar
ENV M2_HOME ${MAVEN_HOME}
ARG REPO=mcr.microsoft.com/dotnet/core/runtime-deps

# Disable the invariant mode (set in base image)
RUN apk add --no-cache icu-libs openssl-dev

ENV DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=false \
    LC_ALL=en_US.UTF-8 \
    LANG=en_US.UTF-8

# Install .NET Core SDK
ENV DOTNET_SDK_VERSION 2.1.607

RUN wget -O dotnet.tar.gz https://dotnetcli.azureedge.net/dotnet/Sdk/$DOTNET_SDK_VERSION/dotnet-sdk-$DOTNET_SDK_VERSION-linux-musl-x64.tar.gz \
    && dotnet_sha512='61caf6602b8a2aa89769b3e91ddaec963d8ab9f802cd7f6c6da4f02426358712bc2bb0930e7ee3a81d75c7607039543b554cb8ed50e45610655f9e91ed0f2f17' \
    && echo "$dotnet_sha512  dotnet.tar.gz" | sha512sum -c - \
    && mkdir -p /usr/share/dotnet \
    && tar -C /usr/share/dotnet -xzf dotnet.tar.gz \
    && ln -s /usr/share/dotnet/dotnet /usr/bin/dotnet \
    && rm dotnet.tar.gz

# Enable correct mode for dotnet watch (only mode supported in a container)
ENV DOTNET_USE_POLLING_FILE_WATCHER=true \ 
    # Skip extraction of XML docs - generally not useful within an image/container - helps performance
    NUGET_XMLDOC_MODE=skip

# Trigger first run experience by running arbitrary cmd to populate local package cache
RUN dotnet help
WORKDIR /app
COPY --from=build /app/${project}/${artifactid}/target/${artifact} /app
EXPOSE 9090
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar ${artifact}"]

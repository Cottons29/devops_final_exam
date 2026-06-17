FROM eclipse-temurin:25-jdk

# Install NGINX and OpenSSH server
RUN apt-get update && \
    apt-get install -y nginx openssh-server && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Configure SSH
RUN mkdir -p /var/run/sshd && \
    echo 'root:root' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files first (for caching)
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Copy source code
COPY src/ src/

# Build the application
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Copy NGINX config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Create uploads directory
RUN mkdir -p /app/uploads/photos

# Create startup script
RUN echo '#!/bin/bash\n\
service ssh start\n\
nginx -g "daemon off;" &\n\
java -jar /app/build/libs/*.jar --server.port=8081\n'\
> /start.sh && chmod +x /start.sh

EXPOSE 8443 2222

CMD ["/start.sh"]

# Multi-stage Dockerfile for XYZ Bank Selenium tests to keep final image lean
#
# Stage 1 (build): Maven + JDK 21 — compiles project and downloads dependencies
# Stage 2 (test):  Chrome + ChromeDriver + JDK 21 — runs tests headlessly

# STAGE 1: Maven dependency resolution and compilation
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first — Docker layer caching: if pom.xml hasn't changed,
# the dependency download layer is reused (much faster rebuilds).
COPY pom.xml .
RUN mvn dependency:go-offline -B  # download all deps into local Maven cache

# Copy source after deps — source changes don't invalidate the deps layer
COPY src ./src

# Compile without running tests in this stage
RUN mvn compile test-compile -B

# STAGE 2: Test runner with Chrome installed
FROM eclipse-temurin:21-jdk AS test-runner

# Install Chrome and dependencies
# Using Google's official apt repository for stable Chrome builds
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libcups2 \
    libdrm2 \
    libgbm1 \
    libgtk-3-0 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    xdg-utils \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
       > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Install Maven (needed to run tests in this stage)
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy compiled project from builder stage (dependencies + classes)
COPY --from=builder /root/.m2 /root/.m2
COPY --from=builder /app /app

# Run tests headlessly — -Dheadless=true activates the headless Chrome profile
# in DriverFactory.createChromeDriver()
CMD ["mvn", "test", "-Dheadless=true", "-B"]
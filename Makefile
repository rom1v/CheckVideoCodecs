.PHONY: build push run

ADB ?= adb
GRADLE ?= ./gradlew
TARGET ?= cvc.jar

build:
	$(GRADLE) assembleDebug

push: build
	adb push app/build/outputs/apk/debug/app-debug.apk /data/local/tmp/$(TARGET)

run: push
	./run

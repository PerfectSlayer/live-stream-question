# Live Stream Question

This project is a Java application that fetch YouTube live and Twitch chat comments to display some in your live stream.

## Features

* Display chat comments
* Pin a comment as a question
* Promote a question to the live
* Demote the live question
* Unpin a question

## Usage

The moderation team can select questions from chat and promote them to live stream:

![ts](screenshot.png)

In the same time, the overlay will update to display the promoted question:

![ts](overlay.png)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `youtube-live-question-1.0.0-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/youtube-live-question-1.0.0-runner.jar`.

## Setting Up Twitch

### Getting Credentials

To get the chat comments, this application needs to be registered as a Twitch application [from the Twitch developers site](https://dev.twitch.tv/console/apps) to a get *client id* and a *client secret*.

It also needs an *access token* (without specific permission other than the default public access) to read the user details (display name and profile picture URL). Access token can be generated from [Twitch Token Generator](https://twitchtokengenerator.com) (use custom scope token and remove all scopes).

### Configuring Application

The Twitch configuration can be applied using the following environment variables:

```bash
TWITCH_CHAT_ENABLED=true
TWITCH_CHANNEL=<<channel_name>>
TWITCH_CLIENT_ID=<<client_id>>
TWITCH_CLIENT_SECRET=<<client_secret>>
TWITCH_ACCESS_TOKEN=<<access_token>>
```

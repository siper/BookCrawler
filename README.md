
[![Badge License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://github.com/siper/BookCrawler/blob/dev/LICENSE)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://hub.docker.com/repository/docker/stersh/bookcrawler-dev/general "Docker hub")

## Overview

Bookcrawler - selfhosted tool for monitoring your library on book publishing platforms (like author.today), and deliver updates with Dropbox, telegram and etc.

## Usage

Serve it with Docker:

```Docker
docker run -d \
--name=bookcrawler \
-e PUID=1000 \
-e PGID=1000 \
-e TZ=Etc/UTC \
-v /path/to/data:/config \
--restart unless-stopped \
stersh/bookcrawler-dev:latest
```

Or use docker compose:

```Docker-compose
version: '3'
services:
  bookcrawler:
    image: stersh/bookcrawler-dev:latest
    container_name: bookcrawler
    restart: unless-stopped
    volumes:
      - /path/to/data:/config
```

## Configuration

Bookcrawler requires your [author.today](https://author.today/) access token to work. You can create it with [this](https://author.today/account/bearer-token) link.
After that, you should create `config.properties` file in `/path/to/data` folder, which your set for docker container.
Example configuration file:

```properties
logging.network=NONE                                   # network layer log level, may be: ALL, HEADERS, BODY, INFO, NONE (default: NONE)
logging.app=INFO                                       # app log level, may be: OFF, ERROR, WARN, INFO, DEBUG, TRACE, ALL (default: INFO)
logging.db=true                                        # log database transactions (default: false)
at.accessToken=<author.today token here>               # author.today access token
at.libraryCheckPeriod=10                               # library check interval in minutes (default: 10)
telegram.botToken=<your telegram token here>           # telegram bot token (use Bot Father to receive it)
telegram.chatId=<telegram chatId>                      # telegram chat id (use chat id bot to receive it)
telegram.uploadBookUpdates=true                        # send book updates to telegram bot (default: false)
dropbox.storeFolder=/Applications/Dropbox PocketBook   # Dropbox book store folder (for pocket book for example)
dropbox.token=<dropbox app access token>               # Dropbox app access token, should have write metadata and write files scopes
local.storeFolder=/path/to/library                     # Local book storage path, if not set, books not been stored locally
```

## License

```
MIT License

Copyright (c) 2023 Kirill Zhukov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

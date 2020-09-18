# Steps to install kafkacat

Kafkacat is an easy to use commandline tool to produce and consume Kafka messages.
See https://github.com/edenhill/kafkacat

## Installation

### Option 1: apt-get / homebrew

`apt-get install kafkacat`

`brew install kafkacat`

### Option 2: just download
Because latest version is not in apt yet, you can download it manually.

```bash
$ export KAFKACAT_VERSION=1.6.0

$ KAFKACATTEMP="$(mktemp -d)" \
    && cd "$KAFKACATTEMP" \
    && wget -q "https://github.com/edenhill/kafkacat/archive/$KAFKACAT_VERSION.tar.gz" \
    && tar --strip-components=1 -xzf "$KAFKACAT_VERSION.tar.gz" \
    && ./bootstrap.sh \
    && mv kafkacat /usr/local/bin \
    && cd - \
    && rm -r "$KAFKACATTEMP"
```
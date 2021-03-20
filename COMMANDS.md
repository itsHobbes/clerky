# Commands
## help

show all commands or detailed help of one command
* **Cooldown:** N/A
* **Required Permission:** N/A
* **Required Role:** N/A
#### Arguments
* `-p`, `--page` : select a specific page to showcase

#### Usage
## setup

Setup voice category
* **Cooldown:** 10 seconds per user
* **Required Permission:** BAN_MEMBERS
* **Required Role:** N/A
#### Arguments
* `-c`, `--category` : The name of the channel category to hold voice channels
* `-n`, `--channels` : Max channels allowed per channel. (Default: 10)
* `-h`, `--help` : show usage of a particular command
* `-u`, `--users` : Max users allowed per channel. (Default: 3)
* `-null`, `--channel` : The name created channels should use. (Default: Study Room)

#### Usage
* -c "Study Rooms" -u 3 -n 7 --channel "Study Room" -> Will setup the room management with a maximum of 7 channels with 3 users per channel

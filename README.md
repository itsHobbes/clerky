# clerky

- [Add me to your server!](https://discord.com/oauth2/authorize?client_id=821516748800917534&permissions=2415987728&scope=bot)

Once added, you only need to run the setup command once.

## Commands

### Add voice groups
Format: `/clerky-addvoicegroup categoryname channelname maxusers maxchannels adjustablesize`

Example: `/clerky-addvoicegroup Studying "Study Group" 4 10 false` - This will make a "Study Group" voice channel in the existing "Studying" category. Each voice channel will have a maximum of 4 users. A maximum of 10 voice channels will be created.

### List voice groups
Format: `/clerky-listvoicegroups`
This will list all voice groups for your server.

### Remove voice group
Format: `/clerky-removevoicegroups id`

Example: `/clerky-removevoicegroups 12353839` - This will remove the voice group with the ID `12353839`. IDs can be found vice the `listvoicegroups` command.

### Alter voice channel size
Format: `/clerky-size size`

Example: `/clerky-size 6` - Increase or decrease the user limit of the voice channel to 6. This command **must** be run by a user connected to that voice channel, and must be sent in the voice-text channel of that channel. This change is only made to the specific voice channels and does not affect others within the group.

**Note:** When clerky creates a voice channel, it will attempt to sync the voice channel permissions with the category permissions. If clerky does not have a specific permission (e.g video), it will not be able to sync that permission.

## Permsisions Required

• **Manage Channels** - For creating/removing voice channels

• **Manage Roles** - Required because Clerky syncs channel permissions to the parent category

• **View Channels** - Channel visibility

• **Send Messages** - Sending command response messages

• **Read Message history** - Reading command messages

# clerky

- [Add me to your server!](https://discord.com/oauth2/authorize?client_id=821516748800917534&permissions=2415987728&scope=bot)

Once added, you only need to run the setup command once.

## Commands

### Add voice groups
Format: `/clerky-addvoicegroup categoryname channelname maxusers maxchannels`
Example: `/clerky-addvoicegroup Studying "Study Group" 4 10` - This will make a "Study Group" voice channel in the existing "Studying" category. Each voice channel will have a maximum of 4 users. A maximum of 10 voice channels will be created.

### List voice groups
Format: `/clerky-listvoicegroups`
This will list all voice groups for your server.

### Remove voice group
Format: `/clerky-removevoicegroups id`
Example: `/clerky-removevoicegroups 12353839` - This will remove the voice group with the ID `12353839`. IDs can be found vice the `listvoicegroups` command.

Note: When clerky creates a voice channel, it will attempt to sync the voice channel permissions with the category permissions. If clerky does not have a specific permission (e.g video), it will not be able to sync that permission.

## Permsisions Required

• **Manage Channels** - For creating/removing voice channels

• **Manage Roles** - Required because Clerky syncs channel permissions to the parent category

• **View Channels** - Channel visibility

• **Send Messages** - Sending command response messages

• **Read Message history** - Reading command messages

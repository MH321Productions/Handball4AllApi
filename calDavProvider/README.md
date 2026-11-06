# CalDAV Provider

[![Docker Image Version](https://img.shields.io/docker/v/321productions/h4a-caldav?label=Docker%20image)
](https://hub.docker.com/r/321productions/h4a-caldav)

This program queries the Handball4All API and feeds the data into a
CalDAV server.

## Environment Variables

| Variable            | Description                                                                |
|---------------------|----------------------------------------------------------------------------|
| `H4A_CAL_PROVIDER`  | The CalDAV Server                                                          |
| `H4A_CAL_USER`      | The username                                                               |
| `H4A_CAL_PASSWORD`  | The password (can be an App password for Nextcloud)                        |
| `H4A_TEAMS_FILE`   | The absolute path to the calendar creation JSON file (see the [schema](schema.json) and the example for more details)|

## Calendar creation JSON file

This file contains the H4All IDs for each calendar,
as well as the calendar name to write into.
See the [schema](https://github.com/MH321Productions/Handball4AllApi/blob/main/calDavProvider/schema.json) for more details.

> [!IMPORTANT]
>
> - The calendar has to exist, it can't be created. The program will fail if the calendar doesn't exist or can't be written to
> - The program clears the whole calendar and re-adds the events. **Only use the specified calendars for the program to use**

This is an example file:

```json
{
  "$schema": "https://raw.githubusercontent.com/MH321Productions/Handball4AllApi/refs/heads/main/calDavProvider/schema.json",
  "calendars": [
    {
      "name": "Herren 1", //The calendar name in the CalDAV server
      "teams": [
        {
          "orgId": 216, //The organization ID (216 for the BWHV) 
          "leagueId": 123456, //The league ID (the lId parameter in the Browser
          "teamId": 1234567 //The team ID (the tId parameter in the Browser)
        }
      ]
    },
    {
      "name": "wE",
      "teams": [
        {
          "orgId": 216,
          "leagueId": 234567,
          "teamId": 2345678
        },
        { //Add a second team to the calendar (e.g. for the "Koordinationsübungen")
          "orgId": 216,
          "leagueId": 345678,
          "teamId": 3456789,
          "suffix": "(Koordination)" //Set a suffix to the event title
        }
      ]
    }
  ],
  "prefixes": ["HG Hugendubel", "HG Hug"] //The H4A prefixes to filter (In the league view, some team names are shortened. Use those shortened names as prefixes)
}
```
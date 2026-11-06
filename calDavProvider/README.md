# CalDAV Provider

This program queries the Handball4All API and feeds the data into a
CalDAV server.

## Environment Variables

| Variable            | Description                                                                |
|---------------------|----------------------------------------------------------------------------|
| `H4A_CAL_PROVIDER`  | The CalDAV Server                                                          |
| `H4A_CAL_USER`      | The username                                                               |
| `H4A_CAL_PASSWORD`  | The password (can be an App password for Nextcloud)                        |
| `H4A_TEAMS_FILE`   | The absolute path to the calendar creation JSON file (see the [schema](schema.json) and the example for more details)|

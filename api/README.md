# Handball4All API

This module implements a client for the Handball4All API using coroutines.

> [!IMPORTANT]
> Since all queries are suspended functions, they need to be executed in a `CoroutineScope`
> like `runBlocking` or a worker thread.

# Specification

Again, huge thanks to [@PerDaw](https://github.com/PerDaw) for providing the specification
[here](https://github.com/PerDaw/Handball4AllAPI).

Base URL: https://spo.handball4all.de/service/if_g_json.php

## Commands

| Command | Description                                 |
|---------|---------------------------------------------|
| pcu     | Vereinsseite (ClubDetailControl)            |
| pgy     | Hallenspielplan (GymnasiumMatchesControl)   |
| ps      | Ligaseite (LeagueDetailControl)             |
| cs      | Vereinssuche (ClubSearch)                   |
| gs      | Hallensuche (GymnasiumSearch)               |
| gi      | Hallen-Detailseite (GymnasiumDetailControl) |
| po      | Bereichs-Spielplan (ScheduleDetailControl)  |
| ical    | Spieltermin-Export (iCal Format)            |

## Parameters

| Command | DataType | Value                    | Description                       |
|---------|----------|--------------------------|-----------------------------------|
| debug   | number   | 1 \|\| 0                 | Replaces JSON with custom format  |
| cmd     | string   | command type (see above) | Defines which model to use        |
| og      | number   | Varies > 0               | OrganisationID (VerbandsID)       |
| o       | number   | Varies > 0               | RegionID (RegionsID)              |
| p       | number   | Varies > 0               | PeriodID (SaisonID)               |
| do      | string   | yyyy-mm-dd               | Date (Datum)                      |
| g       | number   | Varies > 0               | GymID (SporthallenID)             |
| c       | number   | Varies > 0               | ClubID (VereinsID)                |
| cl      | number   | Varies > 0               | LeagueID (LigaID)                 |
| ca      | number   | 1 \|\| 0                 | ShowAllGames (AlleSpieleAusgeben) |
| ct      | number   | Varies > 0               | TeamID (MannschaftsID)            |

# Usage

All relevant methods are contained in the singleton
[Handball4All](src/main/kotlin/com/github/mh321Productions/handball4AllApi/api/Handball4All.kt).
Since no authentication is required, all methods are static and can be called without any configuration:

```kotlin
suspend fun main() {
    val league = Handball4All.getLeagueInfo(216, 164116)
}
```

```kotlin
fun main() {
    runBlocking {
        val league = Handball4All.getLeagueInfo(216, 164116)
    }
}
```
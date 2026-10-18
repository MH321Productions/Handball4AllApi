package com.github.mh321Productions.handball4AllApi.api.data.exceptions

import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Game

class NoGameReportException(val game: Game) : Exception("No report available for game ${game.id}")
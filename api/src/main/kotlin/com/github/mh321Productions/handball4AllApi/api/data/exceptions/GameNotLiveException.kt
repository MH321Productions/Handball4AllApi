package com.github.mh321Productions.handball4AllApi.api.data.exceptions

import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Game

class GameNotLiveException(val game: Game) : Exception("The game ${game.id} is not live (yet)")
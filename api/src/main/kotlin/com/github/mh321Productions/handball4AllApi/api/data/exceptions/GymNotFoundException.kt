package com.github.mh321Productions.handball4AllApi.api.data.exceptions

class GymNotFoundException(val orgId: Int, val gymId: Int): Exception("No Gym found with ID $gymId in organization $orgId")